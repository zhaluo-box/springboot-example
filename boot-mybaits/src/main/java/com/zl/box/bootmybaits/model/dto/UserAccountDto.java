package com.zl.box.bootmybaits.model.dto;

import com.zl.box.bootmybaits.model.Account;
import com.zl.box.bootmybaits.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserAccountDto extends User implements Serializable {
    private static final long serialVersionUID = 8047589417381196282L;
    private List<Account> accounts;
}
