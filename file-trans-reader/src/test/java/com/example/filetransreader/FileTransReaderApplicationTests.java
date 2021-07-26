package com.example.filetransreader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest
class FileTransReaderApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testResource() throws FileNotFoundException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "test-dir" + File.separator + "aa.txt");
        if (file.exists()) {
            System.out.println(file.getAbsolutePath());
        }
    }

}
