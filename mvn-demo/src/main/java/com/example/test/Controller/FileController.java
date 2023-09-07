package com.example.test.Controller;

import com.example.test.PDFUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created  on 2023/8/14 15:15:54
 *
 * @author wmz
 */
@RestController
public class FileController {

    @GetMapping("add")
    public ResponseEntity<String> addWatermark() {

        try {
            FileInputStream fileInputStream = new FileInputStream("/home/temp/1.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream("/home/temp/" + UUID.randomUUID() + "-back.pdf");
            PDFUtil.addWaterMark(fileInputStream, fileOutputStream, "mvn-demo 测试水印 111");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("成功");
    }
}
