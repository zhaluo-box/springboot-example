package com.example.bigfile.view;


import com.example.bigfile.entity.FileShard;
import com.example.bigfile.repository.FileShardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FileShardView {

    @Autowired
    private FileShardRepository fileShardRepository;

    public void save(FileShard fileShard) {
        fileShardRepository.save(fileShard);
    }

    public Set<FileShard> findByFileId(String fileId) {
        return fileShardRepository.findByFileId(fileId);
    }
}
