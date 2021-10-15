package com.example.boot.base.web;

import com.example.boot.base.entity.jpa.PhotoItem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MingZhe on 2021/10/15 17:17:45
 *
 * @author wmz
 */
@RestController
@RequestMapping("/photos/")
public class PhotoController {

    @PostMapping
    public void upload() {

    }

    @DeleteMapping()
    public void batchDelete(@RequestBody List<String> ids) {

    }

    @GetMapping
    public List<PhotoItem> list(String albumId) {
        return new ArrayList<>();
    }

}
