package com.example.filetranswriter.dao;


import com.example.filetranswriter.entity.FileShard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileShardRepository extends JpaRepository<FileShard, String> {

    Integer countByfileId(String fileId);

    List<FileShard> findAllByFileId(String fileId);

    @Query(value = "select * from file_shard t where t.file_id = ?1 order by t.shard_sequence", nativeQuery = true)
    List<FileShard> findAllByFileIdAndSortShardSequence(String fileId);
}
