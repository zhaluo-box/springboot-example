package com.example.contextlearn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

@SpringBootTest
class ContextLearnApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testResource() throws FileNotFoundException {
        String path =ResourceUtils.CLASSPATH_URL_PREFIX+"test-dir"+File.separator+"aa.txt";
        File file = ResourceUtils.getFile(path);
        if (file.exists()) {
            System.out.println("file.exists");
            System.out.println(file.getAbsolutePath());
        }
        ClassLoader classLoader = ContextLearnApplicationTests.class.getClassLoader();
        System.out.println(classLoader.getName());
        System.out.println(classLoader.getDefinedPackages());
     //   Arrays.stream(classLoader.getDefinedPackages()).forEach(System.out::println);
        System.out.println(classLoader.getResource("aa.txt").getPath());
    }

}
