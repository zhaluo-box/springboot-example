package com.example.bigfile.repository;


import com.example.bigfile.entity.FileShard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FileShardRepository extends CrudRepository<FileShard, String> {
    Set<FileShard> findByFileId(String fileId);
}
