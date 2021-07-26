package com.example.filetranswriter.listener;

import com.example.filetranswriter.entity.FileImport;
import com.example.filetranswriter.entity.FileShard;
import com.example.filetranswriter.event.MergeFileEvent;
import com.example.filetranswriter.service.FileImportService;
import com.example.filetranswriter.service.FileShardService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MergeFileListener implements ApplicationListener<MergeFileEvent> {

    @Autowired
    private FileImportService importService;

    @Autowired
    private FileShardService shardService;

    @Value("${shard.bigfile}")
    private String outPath;

    @SneakyThrows
    @Override
    public void onApplicationEvent(MergeFileEvent mergeFileEvent) {
        String fileId = mergeFileEvent.getFileId();
        Integer shardsCount = shardService.CountByfileId(fileId);
        Optional<FileImport> optional = importService.findById(fileId);
        FileImport fileImport = optional.get();
        int shardNum = fileImport.getShardNum();
        if (shardNum != shardsCount) {
            return;
        }
        // 开始合并文件
        List<FileShard> shards = shardService.findShardsByFileId(fileId);
        File file = new File(outPath + fileImport.getFilename());
        RandomAccessFile ww = new RandomAccessFile(file, "rw");
        shards.stream().forEach(f -> {
            try {
                RandomAccessFile rw = new RandomAccessFile(f.getShardPath(), "rw");
                long length = rw.length();
                byte[] bytes = new byte[(int) length];
                rw.read(bytes);
                int pos = (f.getShardSequence() - 1) * 2097152;
                ww.seek(pos);
                ww.write(bytes);
                rw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ww.close();
    }
}


