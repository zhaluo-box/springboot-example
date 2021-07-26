package com.example.filetransreader.web;

import com.example.filetransreader.entity.FileImport;
import com.example.filetransreader.service.FileImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file/import")
public class FileImportController {

    @Autowired
    private FileImportService service;


    /**
     * 文件扫描接口.
     *
     * @param filePath 文件扫描路径
     * @return
     */
    @PostMapping("/scan")
    public ResponseEntity<List<FileImport>> scanFile(String filePath) {
        List<FileImport> list = service.scanFilePath(filePath);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/hook")
    public ResponseEntity<FileImport> get(String id) {
        return ResponseEntity.ok(service.findById(id));
    }

}
