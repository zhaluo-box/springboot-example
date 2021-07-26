package com.example.bigfile.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 小小 on 2018/6/4 11:30
 */
@Slf4j
public final class MD5Util {

    private static final char[] HEX_CODE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest digest = null;

    private static MessageDigest getDigest() {
        if (digest == null) {
            synchronized (MD5Util.class) {
                if (digest == null) {
                    try {
                        digest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        log.error("初始化MD5算法失败:{}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return digest;
    }

    public static String generateMD5(String param) {
        return generateMD5(param.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateMD5(byte[] bytes) {
        return toHexString(getDigest().digest(bytes));
    }

    /**
     * 计算文件 MD5
     *
     * @param file 原始文件
     * @return 返回文件的md5字符串
     */
    public static String generateMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                getDigest().update(buf, 0, len);
            }
            return toHexString(getDigest().digest());
        } catch (IOException e) {
            log.error("生成MD5失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算文件中某一段的大小 MD5
     *
     * @param file     原始文件
     * @param startPos 从哪里开始读取
     * @param readSize 读取分段的大小
     * @return 返回文件的md5字符串
     */
    public static String generateMD5(File file, long startPos, int readSize) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(startPos);
            byte[] buf = new byte[readSize];
            raf.read(buf);
            return generateMD5(buf);
        } catch (IOException e) {
            log.error("生成MD5失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        File file = new File("E:\\temp\\break\\inbound\\cn_visio_professional_2016_x86_x64_dvd_6970929.iso");
        String md5 = generateMD5(file);
        //        String md5 = generateMD5(file, 104857600 * 18, 71680000);
        long endTime = System.currentTimeMillis();
        log.info("\nMD5:" + md5 + "耗时:" + ((endTime - beginTime) / 1000) + "s");
    }
}
