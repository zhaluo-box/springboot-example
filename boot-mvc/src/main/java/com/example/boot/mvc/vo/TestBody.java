package com.example.boot.mvc.vo;

import lombok.Data;
import lombok.ToString;

/**
 * body 参数测试
 * Created  on 2022/4/2 13:13:32
 *
 * @author zl
 */
@Data
@ToString
public class TestBody {

    private String name;

    private int age;

    private boolean status;

    private Boolean flag;

}
