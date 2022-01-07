package com.example.boot.base.common.utils;

import com.example.boot.base.common.serializable.SerializableBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializationUtils {

    public static byte[] serialize(Object object) {
        SerializableBean serializableBean = new SerializableBean(object);
        return JsonUtil.toJSON(serializableBean).getBytes(StandardCharsets.UTF_8);
    }

    public static Object deserialize(byte[] bytes) {
        return JsonUtil.toObject(new String(bytes, StandardCharsets.UTF_8), SerializableBean.class).getTargetBean();
    }
}
