package com.zl.box.bootmybaits.service.impl;

import com.zl.box.bootmybaits.mapper.AccountMapper;
import com.zl.box.bootmybaits.model.dto.AccountUserDto;
import com.zl.box.bootmybaits.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<AccountUserDto> findAll() {
        return accountMapper.findAll2();
    }
}
