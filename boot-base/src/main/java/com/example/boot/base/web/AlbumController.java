package com.example.boot.base.web;

import com.example.boot.base.entity.jpa.PhotoAlbum;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MingZhe on 2021/10/15 17:17:50
 *
 * @author wmz
 */
@RestController
@RequestMapping("/albums/")
public class AlbumController {

    @PostMapping
    public PhotoAlbum create(@RequestBody PhotoAlbum photoalbum) {

        return photoalbum;
    }

    @PutMapping
    public void update(@RequestBody PhotoAlbum photoalbum) {

    }

    @DeleteMapping
    public void delete(@RequestBody PhotoAlbum photoalbum) {

    }
}
