package com.zl.box.bootmybaits.mapper;

import com.zl.box.bootmybaits.common.BaseMapper;
import com.zl.box.bootmybaits.model.Role;
import com.zl.box.bootmybaits.model.dto.RoleUserDto;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    List<RoleUserDto> findAll();

    List<Role> findRoleByUid(int uid);
}
