package com.example.filetransreader.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author fenng 2021-05-27 9:55
 */
@Slf4j
@NoArgsConstructor
public final class CrcUtils {

    /**
     * 采用BufferedInputStream的方式加载文件
     */
    public static long checksumBufferedInputStream(String filepath) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
        CRC32 crc = new CRC32();
        byte[] bytes = new byte[1024];
        int cnt;
        while ((cnt = inputStream.read(bytes)) != -1) {
            crc.update(bytes, 0, cnt);
        }
        inputStream.close();
        return crc.getValue();
    }

    /**
     * 使用CheckedInputStream计算CRC
     */
    public static Long getCRC32(String filepath) throws IOException {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = new FileInputStream(new File(filepath));
        CheckedInputStream checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
        while (checkedinputstream.read() != -1) {
        }
        checkedinputstream.close();
        return crc32.getValue();
    }

    public static void main(String[] args) throws IOException {
        long beginTime = System.currentTimeMillis();
        var path = "E:\\temp\\break\\inbound\\cn_visio_professional_2016_x86_x64_dvd_6970929.iso";
        long md5 = checksumBufferedInputStream(path);
        //        String md5 = generateMD5(file, 104857600 * 18, 71680000);
        long endTime = System.currentTimeMillis();
        log.info("\nMD5:" + md5 + "耗时:" + ((endTime - beginTime) / 1000) + "s");
    }

}
