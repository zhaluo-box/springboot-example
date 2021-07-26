package com.example.bigfile.view;


import com.example.bigfile.entity.FileInfo;
import com.example.bigfile.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FileInfoView {
    @Autowired
    private FileInfoRepository fileInfoRepository;

    public void save(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }

    public List<FileInfo> findAll() {
        return (List<FileInfo>) fileInfoRepository.findAll();
    }

    public Optional<FileInfo> findByPathAndLength(String path, long length) {
        return fileInfoRepository.findByFilePathAndTotal(path, length);
    }
}
