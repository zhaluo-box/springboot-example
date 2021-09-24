package com.example.integration.core;

import com.example.integration.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users/")
public class UserController {

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
    public Map<String, Object> update(@RequestBody User user) {
        log.debug(user.toString());
        return Map.of("name", "zhangsan");
    }

    @GetMapping("{name}")
    public Map<String, Object> get(@PathVariable String name) {
        log.debug("接受到的路径参数 name : {}", name);
        return Map.of("name", name);
    }

    @PostMapping("files/actions/upload/")
    public String upload(MultipartFile upload) throws IOException {
        String filename = upload.getOriginalFilename();
        upload.transferTo(new File("C:\\testDir\\" + filename));
        return "success";
    }
}
