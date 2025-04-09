package com.example.myinvoice.util.JsonTypeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Collections;
import java.util.Map;

public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {

    // 1. 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(JsonMapTypeHandler.class);

    // 2. 线程安全的JSON解析器
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 3. 序列化：Java对象 -> 数据库字段
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Map<String, Object> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            // 将Map序列化为JSON字符串
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json); // 直接存储为字符串
        } catch (JsonProcessingException e) {
            logger.error("JSON序列化失败", e);
            throw new SQLException("JSON序列化失败", e);
        }
    }

    // 4. 反序列化：数据库字段 -> Java对象
    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        // 优先尝试读取字符串
        String json = rs.getString(columnName);
        if (json == null) {
            // 如果字符串为null，尝试读取二进制数据（兼容BLOB）
            byte[] bytes = rs.getBytes(columnName);
            if (bytes != null && bytes.length > 0) {
                json = new String(bytes, StandardCharsets.UTF_8);
            }
        }
        return parseJson(json);
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String json = rs.getString(columnIndex);
        if (json == null) {
            byte[] bytes = rs.getBytes(columnIndex);
            if (bytes != null && bytes.length > 0) {
                json = new String(bytes, StandardCharsets.UTF_8);
            }
        }
        return parseJson(json);
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String json = cs.getString(columnIndex);
        if (json == null) {
            byte[] bytes = cs.getBytes(columnIndex);
            if (bytes != null && bytes.length > 0) {
                json = new String(bytes, StandardCharsets.UTF_8);
            }
        }
        return parseJson(json);
    }

    // 5. 核心解析逻辑
    private Map<String, Object> parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyMap(); // 返回空Map避免NullPointerException
        }
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (IOException e) {
            logger.error("JSON解析失败，原始数据: {}", json, e);
            return Collections.emptyMap(); // 解析失败时返回空Map
        }
    }
}