package com.zl.box.bootmybaits.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zl.box.bootmybaits.model.Role;
import com.zl.box.bootmybaits.model.User;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserRoleDto extends User implements Serializable {
    private static final long serialVersionUID = -7212016626758473435L;
    private List<Role> roles;
}
