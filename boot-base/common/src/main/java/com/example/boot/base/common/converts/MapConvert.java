package com.example.boot.base.common.converts;

import com.example.boot.base.common.utils.JsonUtil;

import javax.persistence.AttributeConverter;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MapConvert implements AttributeConverter<Map<String, String>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        return JsonUtil.toJSON(attribute);
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        return JsonUtil.toObject(dbData, Map.class);
    }
}
