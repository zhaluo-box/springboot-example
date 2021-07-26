package com.example.filetransreader.dao;

import com.example.filetransreader.entity.FileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface FileImportRepository extends JpaRepository<FileImport, String> {


    /**
     * 根据文件路径查询已被处理的文件集合
     *
     * @param path 文件的绝对路径
     * @return
     */
    @Query(value = "select * from File_Import t where t.source_path = ?1  and t.is_proccesed = 'Y' ", nativeQuery = true)
    List<FileImport> findBySourcePath(String path);

    @Modifying
    @Transactional
    @Query(value = "update File_Import t set t.is_processed = 'Y' where t.id = ?1", nativeQuery = true)
    int updateIsProcessedById(String id);
}
