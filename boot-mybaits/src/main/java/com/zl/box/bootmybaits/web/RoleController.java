package com.zl.box.bootmybaits.web;

import com.zl.box.bootmybaits.mapper.RoleMapper;
import com.zl.box.bootmybaits.mapper.UserMapper;
import com.zl.box.bootmybaits.model.Role;
import com.zl.box.bootmybaits.model.dto.RoleUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    public List<RoleUserDto> findAll() {
        return mapper.findAll();
    }

    @GetMapping("/{uid}")
    public List<Role> findRoleByUid(@PathVariable int uid) {
        final List<Role> roles = userMapper.selectRoles(uid);
        System.out.println(roles.get(0).getRoleName());
        return roles;
    }

    @GetMapping("/find/{uid}")
    public List<Role> findRoleByUid2(@PathVariable int uid) {
        final List<Role> roles = mapper.findRoleByUid(uid);
        System.out.println(roles.get(0).getRoleName());
        return roles;
    }

}
