package com.example.boot.approve.common.mybaitsplus;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 不加MappedType与MappedJdbcTypes 注解也可以正确映射，但是添加 自定义typeHandler的地方，需要在@TableName 的属性 autoResult=true
 * Created  on 2022/3/14 15:15:01
 *
 * @author zl
 */
public class ListToStringTypeHandler extends AbstractJsonTypeHandler<List<Long>> {
    @Override
    public List<Long> parse(String json) {
        List<Long> list = Collections.emptyList();
        if (!StringUtils.hasLength(json)) {
            return list;
        }
        String[] split = json.split(",");
        return Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public String toJson(List<Long> obj) {
        return obj.stream().map(Object::toString).collect(Collectors.joining(","));
    }

}
