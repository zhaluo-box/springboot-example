package com.example.bigfile.service;


import com.example.bigfile.common.InboundAdapter;
import com.example.bigfile.entity.FileInfo;
import com.example.bigfile.entity.FileShard;
import com.example.bigfile.utils.FileUtil;
import com.example.bigfile.utils.MD5Util;
import com.example.bigfile.view.FileInfoView;
import com.example.bigfile.view.FileShardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DefaultInboundAdapter implements InboundAdapter {

    @Autowired
    private FileInfoView fileInfoView;

    @Autowired
    private FileShardView fileShardView;

    @Value("${bc.file.sub.size}")
    private int subSize;

    @Override
    public void build(File file) {
        //查找文件是否已经在数据库中存在
        Optional<FileInfo> optional = fileInfoView.findByPathAndLength(file.getPath(), file.length());

        //不存在则先处理基础信息
        FileInfo fileInfo = optional.orElse(handleFileInfo(file));

        //查找文件的分片信息是否存在
        Set<FileShard> fileShards = fileShardView.findByFileId(fileInfo.getId());

        //不存在则处理分片信息
        long shardSizeTotal = fileShards.stream().mapToLong(FileShard::getSize).sum();
        if (fileShards.isEmpty() || shardSizeTotal != file.length()) {
            fileShards = handleFileShardInfo(file);
        }

        //保存到数据库
        fileInfoView.save(fileInfo);
        fileShards.stream().peek(fileShard -> fileShard.setFileId(fileInfo.getId())).forEach(fileShard -> fileShardView.save(fileShard));
    }

    /**
     * @param file 原始文件
     * @return 文件基础信息
     */
    private FileInfo handleFileInfo(File file) {
        FileInfo fileInfo = new FileInfo();
        //保存文件在基本信息
        String path = file.getPath();
        String suffix = path.substring(path.lastIndexOf("."));
        String id = MD5Util.generateMD5(path + file.length());
        String MD5Context = MD5Util.generateMD5(file);
        fileInfo.setFileName(file.getName()).setId(id).setTotal(file.length()).setFilePath(file.getPath()).setSuffix(suffix).setContextMD5(MD5Context);
        return fileInfo;
    }

    /**
     * 处理文件分片信息
     *
     * @param file 原始文件
     * @return 拆分后的文件信息列表
     */
    private Set<FileShard> handleFileShardInfo(File file) {
        var sets = new HashSet<FileShard>();
        //向下取整
        long count = (long) Math.floor(file.length() / (double) subSize);
        //再取余数
        int surplus = (int) (file.length() % subSize);
        if (surplus > 0) {
            count++;
        }
        int countLen = (count + "").length();

        for (int i = 0; i < count; i++) {
            FileShard fileShard = new FileShard();
            fileShard.setName(file.getName() + "." + FileUtil.leftPad((i + 1) + "", countLen, '0') + ".part");
            fileShard.setId(MD5Util.generateMD5(fileShard.getName()));
            fileShard.setSortId(i + 1);
            fileShard.setSize(subSize);
            if (i == count - 1) {
                fileShard.setSize(surplus);
            }
            fileShard.setContextMD5(MD5Util.generateMD5(file, (long) i * subSize, fileShard.getSize()));
            sets.add(fileShard);
        }
        return sets;
    }

}
