package com.example.boot.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主要测试 参数绑定中 基础类型与包装类的区别
 * Created  on 2022/4/2 10:10:59
 *
 * @author wmz
 */
@RestController
@RequestMapping("/param-test/")
public class ParamTestController {

    /**
     * 测试不传参数 null 的处理
     * 测试参数是基础类型 并且不添加@requestParam 注解
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/primitive-type-test/")
    public String primitiveTypeTest(int param) {
        return "result : " + param;
    }

    /**
     * 测试不传参数
     * 测试参数是包装类型 并且不添加@requestParam 注解
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/wrapper-type-test/")
    public String wrapperTypeTest(Integer param) {
        return "result : " + param;
    }

    /**
     * 测试不传参数 null 的处理
     * 布尔类型
     * 测试参数是基础类型 并且不添加@requestParam 注解
     * 结果： status 为 false
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/primitive-type-for-boolean-test/")
    public String primitiveTypeForBooleanTest(boolean status) {
        return "result :" + status;
    }

    /**
     * 测试不传参数 null 的处理
     * 布尔类型
     * 测试参数是包装类型  不添加添加@requestParam 注解
     * 结果 参数 status 为null
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/wrapper-type-for-boolean-test/")
    public String wrapperTypeForBooleanTest(Boolean status) {
        return "result :" + status;
    }

    /**
     * 测试不传参数 null 的处理
     * 测试参数是基础类型 添加@requestParam 注解
     * 依旧会抛出异常，只有前端穿默认值 过来 自己校验过过滤，否则会抛出异常，因为没有传参 MVC是按照 null 处理的，依旧走的handlerNullValue
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/primitive-type-has-annotation-test/")
    public String primitiveTypeHasAnnotationTest(@RequestParam(required = false) int param) {
        return "result : " + param;
    }

    /**
     * 测试不传参数
     * 测试参数是包装类型 并且不添加@requestParam 注解
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/wrapper-type-has-annotation-test/")
    public String wrapperTypeHasAnnotationTest(@RequestParam(required = false) Integer param) {
        return "result : " + param;
    }

    /**
     * 测试不传参数 null 的处理
     * 要求必传
     * 测试参数是基础类型 添加@requestParam 注解
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/primitive-type-is-required-test/")
    public String primitiveTypeIsRequiredTest(@RequestParam int param) {
        return "result : " + param;
    }

    /**
     * 测试不传参数
     * 要求必传
     * 测试参数是包装类型 并且不添加@requestParam 注解
     *
     * @see org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver#handleNullValue(String, Object, Class)
     */
    @GetMapping("no-request-param-annotation/actions/wrapper-type-is-required-test/")
    public String wrapperTypeIsRequiredTest(@RequestParam Integer param) {
        return "result : " + param;
    }

}
