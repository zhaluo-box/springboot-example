package com.example.boot.approve.common.mybaitsplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Created  on 2022/3/11 15:15:23
 *
 * @author zl
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 不支持主键自增， 回报异常，mybatis plus 开发的作者在源码中就写了，他也不知道为什么
     */
    int insertBatchSomeColumn(List<T> entityList);
}
