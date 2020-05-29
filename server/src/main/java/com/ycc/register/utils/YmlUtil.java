package com.ycc.register.utils;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class YmlUtil {

    /**
     * key:文件名索引
     * value:配置文件内容
     */
    private static Map<String, LinkedHashMap> pro = new HashMap<>();

    /**
     * string:当前线程需要查询的文件名
     */
    private static ThreadLocal<String> nowFileName = new ThreadLocal<>();

    /**
     * 加载配置文件
     *
     * @param fileName
     */
    public static void loadYml(String fileName) {
        nowFileName.set(fileName);
        if (!pro.containsKey(fileName)) {
            pro.put(fileName, new Yaml().loadAs(YmlUtil.class.getResourceAsStream("/" + fileName), LinkedHashMap.class));
        }
    }

    public static void loadYml(){
        loadYml("app.yml");
    }

    public static Object getValue(String key) {
        // 首先将key进行拆分
        String[] keys = key.split("[.]");

        // 将配置文件进行复制
        Map ymlInfo = (Map) pro.get(nowFileName.get()).clone();
        for (int i = 0; i < keys.length; i++) {
            Object value = ymlInfo.get(keys[i]);
            if (i < keys.length - 1) {
                ymlInfo = (Map) value;
            } else if (value == null) {
                throw new RuntimeException("key不存在");
            } else {
                nowFileName.remove();
                return value;
            }
        }
        throw new RuntimeException("解析YML文件出错...");
    }

    public static Object getValue(String fileName, String key) {
        // 首先加载配置文件
        loadYml(fileName);
        return getValue(key);
    }



    public static void main(String[] args) {
        System.out.println((getValue("app.yml", "server.port")));
    }
}
