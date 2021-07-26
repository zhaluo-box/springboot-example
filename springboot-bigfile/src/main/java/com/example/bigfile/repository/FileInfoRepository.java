package com.example.bigfile.repository;

import com.example.bigfile.entity.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, String> {

    Optional<FileInfo> findByFilePathAndTotal(String path, long length);
}
