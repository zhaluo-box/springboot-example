package com.zl.box.bootmybaits.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.box.bootmybaits.common.BaseQuery;
import com.zl.box.bootmybaits.mapper.UserMapper;
import com.zl.box.bootmybaits.model.User;
import com.zl.box.bootmybaits.model.dto.UserAccountDto;
import com.zl.box.bootmybaits.model.dto.UserRoleDto;
import com.zl.box.bootmybaits.model.vo.QueryVo;
import com.zl.box.bootmybaits.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户
     *
     * @param id id编号
     * @return
     */
    @ApiOperation(value = "获取用户")
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    /**
     * 查询所有用户.
     *
     * @return
     */
    @ApiOperation(value = "查询用户")
    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * 保存操作.
     * -- 保存测试参见 --测试类com.zl.box.bootmybaits.web.UserControllerTest  --Method
     *
     * @param users
     * @return
     */
    @ApiOperation(value = "新增用户")
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody List<User> users) {
        return ResponseEntity.ok(userService.save(users));
    }

    /**
     * 根据性别进行分页查询.
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "根据性别查询用户")
    @PostMapping("/by/sex")
    public ResponseEntity<PageInfo<User>> findUserBySex(@RequestBody BaseQuery<String> params) {
        return ResponseEntity.ok(userService.findUserBySex(params));
    }

    /**
     * 更新用户的操作
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PutMapping("/put")
    public ResponseEntity<String> updateUser1(@RequestBody User user) {
        final int i = userMapper.updateByPrimaryKeySelective(user);
        if (i > 0) {
            return ResponseEntity.ok("更新成功");
        }
        return ResponseEntity.ok("更新失败");
    }

    /**
     * 更新用户的操作
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PatchMapping("/patch")
    public ResponseEntity<String> updateUser2(@RequestBody User user) {
        final int i = userMapper.updateUserByKey(user);
        if (i > 0) {
            return ResponseEntity.ok("更新成功");
        }
        return ResponseEntity.ok("更新失败");
    }

    /**
     * 删除用户.
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserByKey(@RequestBody User user) {
        final int i = userMapper.deleteUserByKey(user.getId());
        if (i > 0) {
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.ok("删除失败");
    }

    /**
     * 模糊查询
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "模糊查询用户")
    @GetMapping("/like/username")
    public ResponseEntity<PageInfo<User>> findUserLikeUsername(@RequestBody BaseQuery<String> username) {
        return ResponseEntity.ok(userService.selectUserLikeUsername(username));
    }

    /**
     * 查询总用户数
     *
     * @return
     */
    @ApiOperation(value = "获取用户总数")
    @GetMapping("/find/total")
    public ResponseEntity<Integer> findTotal() {
        return ResponseEntity.ok(userMapper.findTotal());
    }

    /**
     * paramterType 传递自定义的VO
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "vo查询用户")
    @PostMapping("/select")
    public ResponseEntity<PageInfo<User>> findByUser(@RequestBody BaseQuery<QueryVo> query) {
        PageHelper.startPage(query.getPager().getPageIndex(), query.getPager().getPageSize());
        List<User> users = userMapper.selectByUserVo(query.getEntity());
        return ResponseEntity.ok(new PageInfo<>(users));
    }

    /**
     * paramterType 传递自定义的VO
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "vo查询用户")
    @PostMapping("/select2")
    public ResponseEntity<PageInfo<User>> findByUser2(@RequestBody BaseQuery<User> query) {
        PageHelper.startPage(query.getPager().getPageIndex(), query.getPager().getPageSize());
        List<User> users = userMapper.selectByUserVo2(query);
        return ResponseEntity.ok(new PageInfo<>(users));
    }

    /**
     * 需求：
     * 查询所有用户信息及用户关联的账户信息。
     * 分析：
     * 用户信息和他的账户信息为一对多关系，并且查询过程中如果用户没有账户信息，此时也要将用户信息
     * 查询出来，我们想到了左外连接查询比较合适
     */
    @GetMapping("/account")
    public List<UserAccountDto> findUserAndAccount() {
        return userMapper.selectUserAndAccount();
    }


    /**
     * 需求：
     * 查询账户(Account)信息并且关联查询用户(User)信息。如果先查询账户(Account)信息即可满足要
     * 求，当我们需要查询用户(User)信息时再查询用户(User)信息。把对用户(User)信息的按需去查询就是延迟加
     * 载。
     * 反之 先查用户信息,当需要查询account信息的时候再去查找account 信息
     */
    @GetMapping("/lazy/loading")
    public List<UserRoleDto> findUserLazyLoadingRoles() {
        final List<UserRoleDto> users = userMapper.findUserLazyLoadingRole();
        users.forEach(
                p ->
                        System.out.println(p.toString())

        );
        return users;
    }

    /**
     *
     */
    @GetMapping("/export/excel")
    public void exportExecl(HttpServletRequest request, HttpServletResponse response){
        userService.exportExcelByTemplate(request,response);
    }
}
