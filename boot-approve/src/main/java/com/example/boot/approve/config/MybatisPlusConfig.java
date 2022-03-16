package com.example.boot.approve.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created  on 2022/3/11 11:11:34
 *
 * @author zl
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * TODO 优化  批量执行 重点关心事务
     * mybatis-spring 提供的批量操作, 配置了这个会影响mybatis_plus 插入返回主键， sqlSession 无法自动提交
     */
    //    @Bean
    //    public SqlSessionTemplate sqlSessionBatchTemplate(SqlSessionFactory sqlSessionFactory) {
    //        log.info("创建批量操作SqlSessionTemplate");
    //        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    //    }

    //    @Bean
    //    public EasySqlInjector easySqlInjector() {
    //        return new EasySqlInjector();
    //    }
}
