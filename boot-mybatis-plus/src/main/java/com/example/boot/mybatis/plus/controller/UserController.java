package com.example.boot.mybatis.plus.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot.mybatis.plus.entiry.User;
import com.example.boot.mybatis.plus.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created  on 2022/2/10 22:22:16
 *
 * @author zl
 */
@RestController
@RequestMapping("/users/")
public class UserController {

    //TODO 这里直接使用 mapper 省去service层 方便测试

    @Autowired
    private UserMapper userMapper;

    /**
     * 测试BaseMapper 自带的CRUD
     */

    /**
     * 测试查询一个
     */
    @GetMapping
    public User get(@RequestParam(required = false) Integer id) {
        return userMapper.selectById(id == null ? 1 : id);
    }

    /**
     * 查询列表多个
     *
     * @return
     * @see com.baomidou.mybatisplus.core.mapper.BaseMapper#selectList(Wrapper) wrapper 可以为null;
     * @see Wrapper
     */
    @GetMapping("actions/list/")
    public List<User> list() {
        //TODO  使用wrapper
        return userMapper.selectList(null);
    }

}
