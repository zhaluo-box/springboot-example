package com.example.bigfile.service;

import com.example.bigfile.entity.FileShard;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author fenng 2021-05-24 15:04
 */
@ToString
@Slf4j
public class MergeFileExecutor implements Runnable {

    //开始读取的位置
    long startPos;

    //分片文件的名称
    String partFileName;

    //写入文件的路径
    String writerFileDir;

    //合并文件的地址
    String newFilePath;

    public MergeFileExecutor(FileShard fileShard, int subSize, String writerFileDir, String newFilePath) {
        this.startPos = (fileShard.getSortId() - 1) * subSize;
        this.partFileName = fileShard.getName();
        this.writerFileDir = writerFileDir;
        this.newFilePath = newFilePath;
    }

    public void run() {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(newFilePath, "rw");
            raf.seek(startPos);
            FileInputStream fs = new FileInputStream(writerFileDir + partFileName);
            byte[] b = new byte[fs.available()];
            int read = fs.read(b);
            fs.close();
            raf.write(b, 0, read);
            raf.close();
        } catch (IOException e) {
            log.info("合并文件失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
