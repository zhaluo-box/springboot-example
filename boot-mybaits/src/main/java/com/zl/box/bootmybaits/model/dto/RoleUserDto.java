package com.zl.box.bootmybaits.model.dto;

import com.zl.box.bootmybaits.model.Role;
import com.zl.box.bootmybaits.model.User;
import lombok.Data;

import java.util.List;

@Data
public class RoleUserDto extends Role {

    private List<User> users;

}
