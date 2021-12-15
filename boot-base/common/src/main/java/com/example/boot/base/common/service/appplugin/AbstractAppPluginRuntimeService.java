package com.example.boot.base.common.service.appplugin;

import com.example.boot.base.common.constants.Constant;
import com.example.boot.base.common.dto.PluginInfo;
import com.example.boot.base.common.entity.jpa.AppPlugin;
import com.example.boot.base.common.service.AbstractRuntimeService;
import com.example.boot.base.common.utils.ClassUtil;
import com.example.boot.base.common.utils.JsonUtil;
import com.example.boot.base.common.view.AppPluginView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

/**
 *
 */
@Slf4j
public class AbstractAppPluginRuntimeService {

    private static final String INFO_JSON_FILE_NAME = "info.json";

    private static final String CONFIG_JSON_FILE_NAME = "config.json";

    private static final String VARIABLES_BEAN_NAME = "variables";

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected AppPluginView appPluginView;

    private static final List<ApplicationContextInfo> APPLICATION_CONTEXT_INFO_LIST = new CopyOnWriteArrayList<>();

    /**
     * 加载插件
     */
    public void install(String pluginId) {

        if (isRunning(pluginId)) {
            log.warn("插件正在运行!");
            return;
        }
        var appPlugin = appPluginView.findOne(pluginId);

        var classLoader = getClassLoader(pluginId);
        var context = getContext(classLoader, appPlugin);

        context.refresh();

        var beansOfType = context.getBeansOfType(AbstractRuntimeService.class);
        if (beansOfType.size() != 1) {
            context.close();
            throw new RuntimeException("期望有一个AbstractRuntimeService 的Bean, 实际由" + beansOfType.size() + "个!");
        }

        var applicationContextInfo = new ApplicationContextInfo(pluginId).setApplicationContext(context).setClassLoader(classLoader);
        APPLICATION_CONTEXT_INFO_LIST.add(applicationContextInfo);
    }

    /**
     * 卸载插件
     */
    public void uninstall(String pluginId) {

        var applicationContextInfo = APPLICATION_CONTEXT_INFO_LIST.remove(APPLICATION_CONTEXT_INFO_LIST.indexOf(new ApplicationContextInfo(pluginId)));
        var context = applicationContextInfo.getApplicationContext();
        var classLoader = applicationContextInfo.getClassLoader();
        var taskScheduler = applicationContextInfo.getTaskScheduler();

        if (context != null) {
            context.close();
        }
        if (classLoader != null) {
            try {
                classLoader.close();
            } catch (IOException e) {
                log.warn("关闭插件应用：{} 的ClassLoader出错。", pluginId);
                throw new RuntimeException("classloader 关闭异常!", e);
            }
        }
        if (taskScheduler != null) {
            taskScheduler.shutdown();
        }

    }

    /**
     * 单次运行
     */
    public void run(String pluginId) {
        var applicationContextInfo = APPLICATION_CONTEXT_INFO_LIST.get(APPLICATION_CONTEXT_INFO_LIST.indexOf(new ApplicationContextInfo(pluginId)));
        var beans = applicationContextInfo.getApplicationContext().getBean(AbstractRuntimeService.class);
        beans.run();
    }

    /**
     * 是否运行
     */
    public boolean isRunning(String pluginId) {
        var applicationContextInfo = new ApplicationContextInfo(pluginId);
        return APPLICATION_CONTEXT_INFO_LIST.contains(applicationContextInfo);
    }

    protected final String getContentOfFile(JarFile jarFile, String filePath, boolean noFile) {

        var infoFile = jarFile.getEntry(filePath);

        if (noFile && infoFile == null) {
            throw new RuntimeException("从Jar包中未找到文件：" + filePath);
        } else if (infoFile == null) {
            return "";
        }

        InputStream inputStream = null;
        String info;
        try {
            inputStream = jarFile.getInputStream(infoFile);
            info = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("从Jar包中，读取文件：" + filePath + "内容出错。");
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        if (log.isDebugEnabled()) {
            log.debug("文件内容:\n{}", info);
        }
        return info;
    }

    protected final PluginInfo getAppInfo(JarFile jarFile) {
        final var filePathInTiMeta = getFilePathInTiMeta(INFO_JSON_FILE_NAME);
        var info = getContentOfFile(jarFile, filePathInTiMeta, true);
        Assert.hasLength(info, "描述信息为空。请补充：" + filePathInTiMeta + "的内容。");
        return JsonUtil.toObject(info, PluginInfo.class);
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, Object> getConfigInfo(JarFile jarFile) {

        final var filePathInTiMeta = getFilePathInTiMeta(CONFIG_JSON_FILE_NAME);
        var config = getContentOfFile(jarFile, filePathInTiMeta, false);
        if (!StringUtils.hasLength(config)) {
            log.info("没有配置项。");
            return new HashMap<>();
        }
        return (Map<String, Object>) JsonUtil.toObject(config, Map.class);
    }

    protected void test(JarFile jarFile) {
        // 测试jar
        AnnotationConfigApplicationContext context = null;

        try {
            var classloader = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + jarFile.getName() + "!/") }, this.getClass().getClassLoader());
            context = new AnnotationConfigApplicationContext();
            context.setClassLoader(classloader);
            context.setParent(applicationContext);

            var classList = ClassUtil.getAllClassInJarFile(jarFile, classloader);
            registerAnnotatedBeans(classList, context);
            context.getBeanFactory().registerSingleton(VARIABLES_BEAN_NAME, new HashMap<String, String>());

            context.refresh();

            var beansOfType = context.getBeansOfType(AbstractRuntimeService.class);
            if (beansOfType.size() != 1) {
                context.close();
                throw new RuntimeException("期望有一个AbstractRuntimeService 的Bean, 实际由" + beansOfType.size() + "个!");
            }

        } catch (MalformedURLException e) {
            log.error("测试jar 出错!");
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                if (context != null) {
                    context.close();
                }
            } catch (Exception e) {
                log.error("jarFile close error ", e);
            }
        }
    }

    private String getFilePathInTiMeta(String fileName) {
        return "META-INF/" + fileName;
    }

    private void registerAnnotatedBeans(List<Class<?>> classes, AnnotationConfigApplicationContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("只支持@Component,@Service,@Configuration的Spring注解。");
        }
        //注册@Component,@Service...,@Configuration
        classes.stream()
               .filter(c -> c.isAnnotationPresent(Component.class) || c.isAnnotationPresent(Service.class) || c.isAnnotationPresent(Configuration.class))
               .forEach(ctx::register);

    }

    private URLClassLoader getClassLoader(String pluginId) {
        try {
            return URLClassLoader.newInstance(new URL[] { new URL("jar:" + Constant.PROTOCOL + ":" + pluginId + "!/") }, this.getClass().getClassLoader());
        } catch (MalformedURLException e) {
            log.error("取插件应用：" + pluginId + "的ClassLoader出错。");
            throw new RuntimeException(e);
        }
    }

    private AnnotationConfigApplicationContext getContext(URLClassLoader classLoader, AppPlugin application) {
        var context = new AnnotationConfigApplicationContext();
        context.setClassLoader(classLoader);
        context.setParent(applicationContext);

        //注册键值对配置
        var config = application.getConfig();
        context.getBeanFactory().registerSingleton(VARIABLES_BEAN_NAME, JsonUtil.toObject(config, Map.class));

        //注册Spring注解的Bean
        registerAnnotationBeans(context, classLoader, application);

        return context;
    }

    private void registerAnnotationBeans(AnnotationConfigApplicationContext applicationContext, ClassLoader classLoader, AppPlugin syncTaskApplication) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        JarFile jarFile = null;
        File appFile = null;
        try {
            appFile = File.createTempFile("test-", ".tmp");
            outputStream = new FileOutputStream(appFile);
            inputStream = new ByteArrayInputStream(syncTaskApplication.getJarContent());

            inputStream.transferTo(outputStream);
            jarFile = new JarFile(appFile);
            var allClass = ClassUtil.getAllClassInJarFile(jarFile, classLoader);
            registerAnnotatedBeans(allClass, applicationContext);
        } catch (Exception e) {
            log.info("注册应用：{}的Spring注解Bean时，出错：{}", syncTaskApplication, e.getMessage());
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(jarFile);
            FileUtils.deleteQuietly(appFile);
        }

    }
}
