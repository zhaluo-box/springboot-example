package com.zl.box.bootmybaits.mapper;

import com.zl.box.bootmybaits.common.BaseMapper;
import com.zl.box.bootmybaits.model.Account;
import com.zl.box.bootmybaits.model.dto.AccountUserDto;

import java.util.List;

public interface AccountMapper extends BaseMapper<Account> {

    List<AccountUserDto> findAll2();

    List<Account> findAll1();
    List<Account> findAll3();
}
