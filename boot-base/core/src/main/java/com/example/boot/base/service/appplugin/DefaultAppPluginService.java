package com.example.boot.base.service.appplugin;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import com.example.boot.base.common.service.appplugin.AbstractAppPluginRuntimeService;
import com.example.boot.base.common.service.appplugin.AppPluginService;
import com.example.boot.base.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.jar.JarFile;

/**
 *
 */
@Slf4j
@Service
public class DefaultAppPluginService extends AbstractAppPluginRuntimeService implements AppPluginService {

    @Override
    @Transactional
    public void create(byte[] bytes) {
        var jarFile = getJarFile(bytes);

        try {

            test(jarFile);

            var appInfo = super.getAppInfo(jarFile);
            var configInfo = super.getConfigInfo(jarFile);
            var app = new AppPlugin().setConfig(JsonUtil.toJSON(configInfo))
                                     .setName(appInfo.getName())
                                     .setDescription(appInfo.getDescription())
                                     .setVersion(appInfo.getVersion())
                                     .setJarContent(bytes)
                                     .setIdentifier(UUID.randomUUID().toString());
            appPluginView.save(app);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                jarFile.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    private JarFile getJarFile(byte[] bytes) {

        try {
            var tempFile = File.createTempFile("app", UUID.randomUUID() + "jar");
            Files.write(tempFile.toPath(), bytes);
            return new JarFile(tempFile);
        } catch (IOException e) {
            log.error("转JarFile error!");
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void update(AppPlugin appPlugin) {
        appPluginView.save(appPlugin);
    }

    @Override
    public void delete(String id) {
        appPluginView.deleteById(id);
    }

    @Override
    public Page<AppPlugin> findAll(String keyword, Pageable pageable) {
        return appPluginView.findAll(keyword, pageable);
    }

    @Override
    public void install(String id) {
        super.install(id);
    }

    @Override
    public void uninstall(String id) {
        super.uninstall(id);
    }

}
