package com.zl.box.bootmybaits.model.dto;

import com.zl.box.bootmybaits.model.Account;
import com.zl.box.bootmybaits.model.User;
import lombok.Data;

@Data
public class AccountUserDto2  extends Account {
    private User user;
}
