package com.example.boot.base.common.utils;

import com.csvreader.CsvWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CsvUtil {

    public static void write(OutputStream outputStream, String[] headers, List<String[]> content) {

        var csvWriter = new CsvWriter(outputStream, ',', StandardCharsets.UTF_8);

        try {
            // 写入头信息
            csvWriter.writeRecord(headers);
            // 写入内容
            for (String[] strings : content) {
                csvWriter.writeRecord(strings);
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV 文件写入失败" + e.getMessage(), e);
        } finally {
            // csvWriter close() 方法会关闭构造入参中的流
            csvWriter.close();
        }

    }

}
