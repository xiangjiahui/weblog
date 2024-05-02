package com.xiangjiahui.weblog.common.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @date 2024-04-19 20:21
 * @author xiangjiahui
 * @description json工具类
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper instance = new ObjectMapper();

    public static String toJsonString(Object obj) {
        try {
            return instance.writeValueAsString(obj);

        } catch (JsonProcessingException e) {
            log.error("json序列化失败", e);
            return null;
        }
//        return JSON.toJSONString(obj);
    }

    public static Object toObject(String json, Class<?> clazz) {
//        try {
//            return instance.convertValue(json, clazz);
//        }catch (IllegalArgumentException e){
//            log.error("json反序列化失败", e);
//            return null;
//        }
        return JSON.parseObject(json,clazz);
    }
}
