package com.zl.box.bootmybaits.web;

import com.zl.box.bootmybaits.mapper.AccountMapper;
import com.zl.box.bootmybaits.model.Account;
import com.zl.box.bootmybaits.model.dto.AccountUserDto;
import com.zl.box.bootmybaits.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/acc")
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IAccountService accountService;

    /**
     * 一对一演示1
     *
     *      javaType
     * @return
     */
    @GetMapping("/findAll1")
    public ResponseEntity<List<AccountUserDto>> findAll(){

        final List<AccountUserDto> info = accountService.findAll();
        return ResponseEntity.ok(info);
    }

    /**
     * 一对一演示2
     *      ResultMap
     * @return
     */
    @GetMapping("/findAll2")
    public ResponseEntity<List<Account>> findAll2(){
        return ResponseEntity.ok(accountMapper.findAll3());
    }

    /**
     * 通用mapper 自带的findALl
     *
     */
    @GetMapping("/findAll3")
    public List<Account>  findAll3(){
        return accountMapper.selectAll();
    }
}
