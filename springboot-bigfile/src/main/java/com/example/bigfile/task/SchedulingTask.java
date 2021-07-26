package com.example.bigfile.task;


import com.example.bigfile.common.InboundAdapter;
import com.example.bigfile.common.OutboundAdapter;
import com.example.bigfile.view.FileInfoView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Slf4j
@Component
public class SchedulingTask {

    @Autowired
    private InboundAdapter inboundAdapter;

    @Autowired
    private OutboundAdapter outboundAdapter;

    @Autowired
    private FileInfoView fileInfoView;

    @Value("${bc.file.in.path}")
    private String dirPath;

    //每十分钟扫描一次文件夹内在文件
    @Scheduled(cron = "0 0/2 * * * ?")
    public void FileTask() {
        //获取当前文件夹下的所有文件
        File[] files = new File(dirPath).listFiles();
        if (files == null) {
            log.error("当前文件夹下暂无文件：{}", dirPath);
            return;
        }
        Arrays.stream(files).parallel().forEach(file -> inboundAdapter.build(file));
    }

    //每二十分钟扫描一次数据库
    @Scheduled(cron = "0 0/5 * * * ?")
    public void DataTask() {
        //获取数据库中等待处理在文件信息
        fileInfoView.findAll().parallelStream().filter(fileInfo -> !fileInfo.isProcessed()).forEach(fileInfo -> outboundAdapter.build(fileInfo));
    }
}
