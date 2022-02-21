package com.example.boot.simple.framework.controller;

import com.example.boot.simple.framework.common.annotation.ApiGroup;
import com.example.boot.simple.framework.common.annotation.ApiResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色资源控制层
 * Created  on 2022/2/21 21:21:46
 *
 * @author zl
 */
@ApiGroup(name = "system:role")
@RestController
@RequestMapping("/roles/")
public class RoleController {

    /**
     * 查询所有角色
     *
     * @param keyword 关键字
     * @return 所有角色
     */
    @GetMapping
    @ApiResource(name = "列出所有角色", value = "list")
    public List<Object> list(@RequestParam String keyword) {

        return null;
    }

}
