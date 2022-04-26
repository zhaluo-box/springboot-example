package com.example.boot.mvc.controller;

import com.example.boot.mvc.vo.TestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/4/2 12:12:43
 *
 * @author zl
 */
@RestController
@RequestMapping("/body-param-tests/")
public class BodyParamTestController {

    /**
     * 测试body体里面的参数为基础类型
     * {
     * "name": "zhangsan"
     * }
     * age =0,
     * status = false;
     * flag = null
     */
    @PostMapping("primitives/actions/null-able-test/")
    public String primitiveBodyTest(@RequestBody TestBody testBody) {
        return testBody.toString();
    }

}
