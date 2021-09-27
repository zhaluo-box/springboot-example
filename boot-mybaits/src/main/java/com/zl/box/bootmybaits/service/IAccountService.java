package com.zl.box.bootmybaits.service;

import com.github.pagehelper.PageInfo;
import com.zl.box.bootmybaits.model.dto.AccountUserDto;

import java.util.List;

public interface IAccountService {

    List<AccountUserDto> findAll();
}
