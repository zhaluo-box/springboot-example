package com.example.bigfile.service;

import com.example.bigfile.common.OutboundAdapter;
import com.example.bigfile.entity.FileInfo;
import com.example.bigfile.entity.FileShard;
import com.example.bigfile.utils.MD5Util;
import com.example.bigfile.view.FileInfoView;

import com.example.bigfile.view.FileShardView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fenng 2021-05-25 11:10
 */
@Slf4j
@Service
public class DefaultOutboundAdapter implements OutboundAdapter {

    @Autowired
    private FileInfoView fileInfoView;

    @Autowired
    private FileShardView fileShardView;

    @Value("${bc.file.sub.size}")
    private int subSize;

    @Value("${bc.file.out.path}")
    private String writeDir;

    @SneakyThrows
    @Override
    public void build(FileInfo fileInfo) {
        //查找文件分片信息
        Set<FileShard> fileShards = fileShardView.findByFileId(fileInfo.getId());
        //初始化线程池
        int size = fileShards.size();
        var threadPool = new ThreadPoolExecutor(size, size * 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(size * 2));

        //开始分片处理
        fileShards.stream().filter(fileShard -> !fileShard.isProcessed()).forEach(fileShard -> {
            threadPool.execute(new ShardFileExecutor(fileShard, subSize, new File(fileInfo.getFilePath()), writeDir));
            while (true) {
                if (fileShard.getSize() == new File(writeDir + fileShard.getName()).length()) {
                    String shardMD5 = MD5Util.generateMD5(new File(writeDir + fileShard.getName()));
                    log.debug("shardMD5:{}", shardMD5);
                    if (shardMD5.equals(fileShard.getContextMD5())) {
                        fileShard.setProcessed(true);
                        fileShardView.save(fileShard);
                    }
                    break;
                }
            }
        });

        boolean allComplete = fileShards.stream().allMatch(FileShard::isProcessed);
        if (allComplete && !fileInfo.isProcessed()) {
            log.debug("开始合并文件");

            //合并文件
            String newFilePath = writeDir + "new_" + fileInfo.getFileName();
            fileShards.forEach(fileShard -> threadPool.execute(new MergeFileExecutor(fileShard, subSize, writeDir, newFilePath)));

            while (true) {
                //校验写入的文件是否成功
                if (fileInfo.getTotal() == new File(newFilePath).length()) {
                    log.debug("源文件MD5：{}", fileInfo.getContextMD5());
                    String newFileMD5 = MD5Util.generateMD5(new File(newFilePath));
                    log.debug("新文件MD5：{}", newFileMD5);
                    if (fileInfo.getContextMD5().equals(newFileMD5)) {
                        //修改文件的状态
                        fileInfo.setProcessed(true);
                        fileInfoView.save(fileInfo);
                    }
                    break;
                }
            }
        }

    }

}
