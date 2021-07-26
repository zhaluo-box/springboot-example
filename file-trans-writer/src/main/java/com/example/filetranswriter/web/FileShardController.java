package com.example.filetranswriter.web;

import com.example.filetranswriter.entity.FileShard;
import com.example.filetranswriter.service.FileShardService;
import com.example.filetranswriter.vo.FileShardTransVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shard")
public class FileShardController {

    // 分片信息查询接口 ByFileID

    //

    // 分片数据接收接口
    @Autowired
    private FileShardService service;

    @PostMapping("/trans")
    public ResponseEntity<FileShard> receiveShard(@RequestBody FileShardTransVO vo) {

        FileShard shard = service.handleShard(vo);

        return ResponseEntity.ok(shard);
    }
}
