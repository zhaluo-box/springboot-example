package com.example.boot.base.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassUtil {

    public static List<Class<?>> getAllClassInJarFile(JarFile jarFile, ClassLoader classLoader) {
        return jarFile.stream()
                      .map(ZipEntry::toString)
                      .filter(z -> z.endsWith(".class"))
                      .map(z -> StringUtils.substringBefore(z, "."))
                      .map(z -> StringUtils.replace(z, "/", "."))
                      .map(c -> {
                          try {
                              return Class.forName(c, true, classLoader);
                          } catch (ClassNotFoundException e) {
                              log.error("从流程中加载类出错:", e);
                              throw new RuntimeException(e);
                          }
                      })
                      .collect(Collectors.toList());
    }

}
