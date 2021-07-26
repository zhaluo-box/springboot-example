package com.example.filetranswriter.service;


import com.example.filetranswriter.dao.FileImportRepository;
import com.example.filetranswriter.entity.FileImport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class FileImportService {

    @Autowired
    private FileImportRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${}")
    private String url;


    public Optional<FileImport> findById(String fileId) {
        return repository.findById(fileId);
    }


    public FileImport hook(String fileId) {
        FileImport body = restTemplate.postForEntity("", fileId, FileImport.class).getBody();
        Assert.notNull(body, "从reader 获取数据异常, id : " + fileId);
        return repository.save(body);
    }
}
