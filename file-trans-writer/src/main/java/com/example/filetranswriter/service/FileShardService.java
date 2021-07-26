package com.example.filetranswriter.service;


import com.example.filetranswriter.dao.FileShardRepository;
import com.example.filetranswriter.entity.FileImport;
import com.example.filetranswriter.entity.FileShard;
import com.example.filetranswriter.publish.MergePublish;
import com.example.filetranswriter.vo.FileShardTransVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class FileShardService {


    @Autowired
    private FileShardRepository repository;

    @Autowired
    private FileImportService fileImportService;

    @Transactional
    public FileShard save(FileShard shard) {
        return repository.save(shard);
    }

    @Transactional
    public List<FileShard> saveAll(List<FileShard> shards) {
        return repository.saveAll(shards);
    }

    @Value("${shard.temp-path}")
    private String tempDir;

    @Autowired
    private MergePublish publish;

    public FileShard handleShard(FileShardTransVO vo) {
        String fileId = vo.getFileId();
        Optional<FileImport> fileImport = fileImportService.findById(fileId);

        // TODO 待实现
        fileImport.orElse(fileImportService.hook(fileId));

        FileShard shard = vo.getShard();
        File shardFile = vo.getShardFile();

        // 分片文件写入本地
        String fileName = shardFile.getName();
        shard.setShardPath(tempDir + fileName);
        repository.save(shard);
        // 事件处理 文件合并
        publish.publish(fileId);
        return shard;
    }


    /**
     * 根据源文件ID 统计分片数量.
     *
     * @param fileId
     * @return
     */
    public Integer CountByfileId(String fileId) {
        return repository.countByfileId(fileId);
    }


    public List<FileShard> findShardsByFileId(String fileId) {
        return repository.findAllByFileIdAndSortShardSequence(fileId);
    }
}
