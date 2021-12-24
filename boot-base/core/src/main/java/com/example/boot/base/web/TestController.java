package com.example.boot.base.web;

import com.example.boot.base.common.entity.jpa.User;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/tests/")
public class TestController {

    @GetMapping
    public void testListParamForGetRequest(@RequestParam List<User> users) {
        users.forEach(System.out::println);
    }

    @PostMapping
    public Page<User> testPageableBody(@RequestBody User request, Pageable pageable) {
        System.out.println(request);
        System.out.println(pageable);
        var page = (Pageable) PageRequest.of(1, 2, Sort.by("name"));
        System.out.println(page);
        //        return page;
        return new PageImpl<>(List.of(new User()), page, 10);
    }

}
