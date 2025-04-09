package com.example.myinvoice.util;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.teaopenapi.models.Config;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

public class AliClient {

    public static Client createClient() throws Exception {
        // 加载配置文件
        Properties props = loadConfigProperties();

        // 读取配置项
        String accessKeyId = getPropertyOrThrow(props, "aliyun.accessKeyId");
        String accessKeySecret = getPropertyOrThrow(props, "aliyun.accessKeySecret");
        String endpoint = getPropertyOrThrow(props, "aliyun.endpoint");

        // 创建配置对象
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;

        return new Client(config);
    }

    private static Properties loadConfigProperties() throws Exception {
        try (InputStream inputStream = AliClient.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("配置文件 config.properties 未找到");
            }
            Properties props = new Properties();
            props.load(inputStream);
            return props;
        }
    }

    private static String getPropertyOrThrow(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("配置项缺失: " + key);
        }
        return value.trim();
    }
}