package com.example.filetransreader.service;

import com.example.filetransreader.dao.FileImportRepository;
import com.example.filetransreader.entity.FileImport;
import com.example.filetransreader.entity.FileShard;
import com.example.filetransreader.utils.CrcUtils;
import com.example.filetransreader.utils.FileUtil;
import com.example.filetransreader.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class FileImportService {

    @Autowired
    private FileImportRepository fileImportRepository;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 扫描目录下的最新文件
     *
     * @param filePath
     * @return
     */
    public List<FileImport> scanFilePath(String filePath) {

        File file = new File(filePath);
        File[] files = file.listFiles(f -> {
            // 如果文件已经被处理 就过滤
            List<FileImport> list = fileImportRepository.findBySourcePath(f.getAbsolutePath());
            return null == list || list.size() == 0;
        });
        var list = new ArrayList<FileImport>();
        Arrays.asList(files).stream().forEach(f -> {
            // 处理基本信息数据落库
            String name = f.getName();
            String sourcePath = file.getAbsolutePath();
            long length = file.length();
            String id = MD5Util.generateMD5(sourcePath);
//            String uuid = UUID.fromString(sourcePath).toString();
            FileImport fileImport = new FileImport();
            fileImport.setId(id);
            fileImport.setFilename(name);
            fileImport.setFileSize(length);
            fileImport.setSourcePath(sourcePath);
            fileImport.setCreateTime(new Date());
            // 默认分片
            fileImport.setShardFlag("Y");
            // 计算CRC值, 采用发布订阅 异步的方式计算保存;
//            fileImport.setKey("");
            // 文件基本信息 两端数据同步
            // 开始文件分片 分片文件保存在本地
            List<FileShard> shards = null;
            try {
                shards = this.shardHandler(fileImport);
            } catch (IOException e) {
                log.error("分片异常 : {}", e.getMessage());
                e.printStackTrace();
            }
            // 开始分片传输
            this.sendShards(shards);
            // 更新已处理标识与分片数量.
            fileImportRepository.updateIsProcessedById(id);
            list.add(fileImport);
        });
        return list;
    }

    @Value("${trans.url}")
    private String url;

    private void sendShards(List<FileShard> shards) {

        shards.stream().forEach(shard -> {
                    // 根据shards 查询本地分片信息存储路径
                    String shardPath = shard.getShardPath();
                    //TODO 未完成
                    restTemplate.postForEntity(url, "", null);

                }
        );

    }

    @Value("${shard.temp-path}")
    private String shardDir;

    /**
     * 分片大小
     */
    @Value("${shard.sub-size}")
    private int subSize;


    @Autowired
    private FileShardService fileShardService;

    /**
     * 分片处理器.
     *
     * @param fileImport 文件导入基本信息
     * @return 处理是否完成标识.
     */
    private List<FileShard> shardHandler(FileImport fileImport) throws IOException {
        File file = new File(fileImport.getSourcePath());
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        long length = file.length();
        long count = length / subSize;
        long po = length % subSize;
        if (po > 0) {
            count++;
        }
        int countLen = (count + "").length();
        String name = file.getName();
        List<FileShard> shards = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            int startPos = (i - 1) * subSize;
            byte[] b = new byte[subSize];

            if (po > 0 && i == count) {
                b = new byte[(int) po];
            }
            rw.seek(startPos);
            rw.read(b);
            String shardName = name + FileUtil.leftPad((i) + "", countLen, '0') + ".part";
            String shardPath = shardDir + shardName;
            writeShard(b, shardPath);

            FileShard fileShard = new FileShard();
            fileShard.setId(MD5Util.generateMD5(shardPath));
            fileShard.setCreateTime(new Date());
            fileShard.setFileId(fileImport.getId());
            fileShard.setShardSequence(i);
            fileShard.setKey(CrcUtils.getCRC32(shardPath));
            fileShard.setIsDeleted("N");
            fileShard.setShardPath(shardPath);
            fileShard.setShardName(shardName);
            fileShard.setShardSize(b.length);
            shards.add(fileShard);

        }
        rw.close();
        return fileShardService.saveAll(shards);
    }

    /**
     * 分片数据写入.
     *
     * @param b
     * @param shardPath
     * @throws IOException
     */
    private void writeShard(byte[] b, String shardPath) throws IOException {
        FileOutputStream fs = new FileOutputStream(shardPath);
        fs.write(b);
        fs.flush();
        fs.close();
    }

    public FileImport findById(String id) {
        Optional<FileImport> anImport = fileImportRepository.findById(id);
        return anImport.orElse(null);
    }
}
