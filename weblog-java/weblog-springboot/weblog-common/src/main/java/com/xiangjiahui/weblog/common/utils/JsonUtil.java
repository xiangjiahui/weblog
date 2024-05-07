package com.xiangjiahui.weblog.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * @date 2024-04-19 20:21
 * @author xiangjiahui
 * @description json工具类
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper instance = new ObjectMapper();

    static {
        // 忽略json字符串中不识别的属性,忽略未知属性
        instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 忽略空bean转json的错误,忽略空的Java Bean属性
        instance.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        // 支持java8时间
        instance.registerModules(new JavaTimeModule());
    }

    public static String toJsonString(Object obj) {
        try {
            return instance.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化失败, {}", e.getMessage());
            return obj.toString();
        }
//        return JSON.toJSONString(obj);
    }

    public static Object toObject(String json, Class<?> clazz) {
        try {
            return instance.convertValue(json, clazz);
        }catch (IllegalArgumentException e){
            log.error("json反序列化失败", e);
            return null;
        }
    }
}
