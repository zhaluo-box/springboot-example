package com.example.filetransreader.dao;

import com.example.filetransreader.entity.FileShard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileShardRepository extends JpaRepository<FileShard, String> {


}
