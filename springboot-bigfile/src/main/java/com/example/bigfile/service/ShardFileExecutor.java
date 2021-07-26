package com.example.bigfile.service;

import com.example.bigfile.entity.FileShard;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ShardFileExecutor implements Runnable {

    //默认的分片大小
    int byteSize;

    //分片的文件名称
    String partFileName;

    //源文件
    File originFile;

    //开始读取的位置
    long startPos;

    //写入文件的路径
    String writerFileDir;

    public ShardFileExecutor(FileShard fileShard, int subSize, File originFile, String writerFileDir) {
        this.startPos = (fileShard.getSortId() - 1) * subSize;
        this.byteSize = fileShard.getSize();
        this.partFileName = fileShard.getName();
        this.originFile = originFile;
        this.writerFileDir = writerFileDir;
    }


    public void run() {
        RandomAccessFile raf;
        OutputStream os;
        try {
            raf = new RandomAccessFile(originFile, "r");
            byte[] b = new byte[byteSize];
            raf.seek(startPos);// 移动指针到每“段”开头
            int s = raf.read(b);
            os = new FileOutputStream(writerFileDir + partFileName);
            os.write(b, 0, s);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.info("分片处理失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}