package com.example.filetransreader.service;

import com.example.filetransreader.dao.FileShardRepository;
import com.example.filetransreader.entity.FileShard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileShardService {


    @Autowired
    private FileShardRepository repository;

    @Transactional
    public FileShard save(FileShard shard) {
        return repository.save(shard);
    }

    @Transactional
    public List<FileShard> saveAll(List<FileShard> shards) {
        return repository.saveAll(shards);
    }
}
