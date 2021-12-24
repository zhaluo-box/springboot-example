package com.example.boot.base.web;

import com.example.boot.base.common.entity.jpa.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户测试
 */
@Slf4j
@RestController
@RequestMapping("/tests/")
public class TestController {

    @Autowired
    private HandlerMapping handlerMapping;

    @GetMapping
    public void testListParamForGetRequest(@RequestParam List<User> users) {
        users.forEach(System.out::println);
    }

    @PostMapping
    public Page<User> testPageableBody(@RequestBody User user, Pageable pageable, HttpServletRequest request) throws Exception {

        var handler = handlerMapping.getHandler(request);
        System.out.println("===============================================");
        System.out.println("HANDLE" + handler.getHandler());
        System.out.println("INTERCEPTOR" + handler.getInterceptorList());
        System.out.println("S" + handler.getInterceptors());
        System.out.println("===============================================");

        var users = new ArrayList<User>(10);
        generateUsers(users);
        return new PageImpl<>(users, pageable, 10);
    }

    private void generateUsers(List<User> users) {
        for (int i = 0; i < 10; i++) {
            users.add(new User().setAge(i).setUsername("xxx-" + i));
        }
    }

}
