package com.example.boot.mybatis.plus.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot.mybatis.plus.entiry.User;
import com.example.boot.mybatis.plus.mapper.UserMapper;
import com.example.boot.mybatis.plus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

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
     * @see com.baomidou.mybatisplus.core.mapper.BaseMapper#selectList(Wrapper) wrapper 可以为null;
     * @see Wrapper
     */
    @GetMapping("actions/list/")
    public List<User> list() {
        //TODO  使用wrapper
        return userMapper.selectList(null);
    }

    @PostMapping
    public void insertUser(@RequestBody User user) {
        userService.insert(user);
    }

    /**
     * 根据ID列表 批量删除
     */

    @DeleteMapping
    public ResponseEntity<Void> batchDelete(@RequestParam List<Long> ids) {
        Assert.notNull(ids, "参数ids 为null");
        Assert.isTrue(ids.size() > 0, "ids 数量为" + ids.size());
        userService.batchDelete(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("actions/batch-delete-ids-str/")
    public ResponseEntity<Void> batchDeleteIdsStr(@RequestParam List<Long> ids) {
        Assert.notNull(ids, "参数ids 为null");
        Assert.isTrue(ids.size() > 0, "ids 数量为" + ids.size());
        String idsStr = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        userService.batchDelete(idsStr);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
