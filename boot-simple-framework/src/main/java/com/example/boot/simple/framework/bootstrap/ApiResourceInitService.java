package com.example.boot.simple.framework.bootstrap;

import com.example.boot.simple.framework.common.annotation.ApiGroup;
import com.example.boot.simple.framework.common.annotation.ApiResource;
import com.example.boot.simple.framework.common.bootstrap.BootstrapService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created  on 2022/2/21 22:22:28
 *
 * @author zl
 */
@Slf4j
@Component
public class ApiResourceInitService implements BootstrapService {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    /**
     * 不需要检查扫描的Controller： 例如登录接口等
     */
    private static final List<String> EXCLUDE_CLASS_LIST = List.of();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("API接口扫描 开始！");
        List<Method> methodList = handlerMapping.getHandlerMethods().values().stream().map(HandlerMethod::getMethod).collect(Collectors.toList());

        // 检查所有handlerMethod 的声明类是否都标记了 ApiGroup

        // 检查所有HandlerMethod 是否都标记了ApiResource

        // 更新数据库 通过redisTemplate 管道将所有资源缓存起来，

        methodList.stream().filter(m -> !EXCLUDE_CLASS_LIST.contains(m.getDeclaringClass().getSimpleName())).forEach(m -> {
            Assert.isTrue(m.getDeclaringClass().isAnnotationPresent(ApiGroup.class), m.getDeclaringClass() + "的类缺少Api注释");
            Assert.isTrue(m.isAnnotationPresent(ApiResource.class), MethodUtil.getMethodDescription(m) + "的方法缺少ApiResource注释");
        });

        log.info("API接口扫描 结束！");
    }

    @Override
    public int getOrder() {
        return Order.API_RESOURCE.ordinal();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class MethodUtil {

        public static String getMethodDescription(Method method) {
            var args = Stream.of(method.getParameters()).map(p -> p.getType().getName()).collect(Collectors.joining(","));
            return String.format("%s.%s(%s)", method.getDeclaringClass().getName(), method.getName(), args);
        }
    }
}
