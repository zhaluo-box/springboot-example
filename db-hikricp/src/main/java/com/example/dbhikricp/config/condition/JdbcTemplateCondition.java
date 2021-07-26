package com.example.dbhikricp.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

@Slf4j
public class JdbcTemplateCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();

        ClassLoader classLoader = conditionContext.getClassLoader();
        Environment environment = conditionContext.getEnvironment();
        String port = environment.getProperty("server.port");
        log.error("----------JdbcTemplateCondition----------------");
        log.error(" 端口号为: {}", port);

        BeanDefinitionRegistry registry = conditionContext.getRegistry();
        System.out.println(registry.getBeanDefinition("dataSource"));
        Arrays.stream(registry.getBeanDefinitionNames()).forEach(System.out::println);
        ResourceLoader resourceLoader = conditionContext.getResourceLoader();
        log.info("------------------" + resourceLoader.getClass());
        log.error("条件注入 注解相关! ");
        annotatedTypeMetadata.getAnnotations().stream().forEach(System.out::println);

        return true;
    }
}
