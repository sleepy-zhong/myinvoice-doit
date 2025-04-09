package com.example.myinvoice.util.JsonTypeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Collections;

/**
 * 通用 JSON TypeHandler，支持 Map<String, Object> 和 List<Map<String, Object>>
 */
public abstract  class GenericJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(GenericJsonTypeHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TypeReference<T> typeReference;

    public GenericJsonTypeHandler(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (JsonProcessingException e) {
            logger.error("JSON 序列化失败", e);
            throw new SQLException("JSON 序列化失败", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private T parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            if (typeReference.getType().getTypeName().contains("List")) {
                return (T) Collections.emptyList();
            } else {
                return (T) Collections.emptyMap();
            }
        }

        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error("JSON 解析失败，原始数据: {}", json, e);
            if (typeReference.getType().getTypeName().contains("List")) {
                return (T) Collections.emptyList();
            } else {
                return (T) Collections.emptyMap();
            }
        }
    }
}
