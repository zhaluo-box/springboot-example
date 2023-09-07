package com.example.boot.base.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.SimpleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象没有属性时，也要序列化
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 将Json转换指定类名的Java对像
     *
     * @param json      json字符串
     * @param className 类名
     * @return 指定类名的对像
     */
    public static Object toObject(String json, String className) {
        try {
            SimpleType javaType = SimpleType.constructUnsafe(Class.forName(className));
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("将字符串：{}转为对象：{}时出错。", json, className);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json转换指定类名的Java对像
     *
     * @param json      json字符串
     * @param className 类名
     * @return 指定类名的对像
     */
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("将字符串：{}转为对象：{}时出错。", json, typeReference);
            throw new RuntimeException(e);
        }
    }


    /**
     * 将Json转换指定类名的Java对像
     *
     * @param json      json字符串
     * @param type 类名
     * @return 指定类名的对像
     */
    public static <T> T toObject(String json, Type type) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(type));
        } catch (Exception e) {
            log.error("将字符串：{}转为对象：{}时出错。", json, type);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Json转换为指定类的Java对像
     *
     * @param json  json字符串
     * @param clazz 类
     * @param <T>   指定类
     * @return 指定类的对像
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        Assert.hasLength(json, "要转对象的JSON字符串不能为空。");
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("将字符串：{}转为对象：{}时出错。", json, clazz);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Java对像转换为Json
     *
     * @param object Java对像
     * @return json
     */
    public static String toJSON(Object object) {
        Assert.notNull(object, "要转JSON的对像不能为空。");

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("将对象：{}转为JSON字符串时出错。", object);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json转换为Java对像列表
     *
     * @param json         json
     * @param elementClass 列表中的java类型
     * @param <T>          java类型
     * @return java对像列表
     */
    public static <T> List<T> toList(String json, Class<T> elementClass) {
        CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, elementClass);
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.error("将JSON:{}转为List<{}>时出错。", json, elementClass);
            throw new RuntimeException(e);
        }
    }

    public static <T> Set<T> toSet(String json, Class<T> elementClass) {
        CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(HashSet.class, elementClass);
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.error("将JSON:{}转为Set<{}>时出错。", json, elementClass);
            throw new RuntimeException(e);
        }
    }




}

