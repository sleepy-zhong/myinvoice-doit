package com.example.myinvoice.util.JsonTypeHandler;

import com.example.myinvoice.util.JsonTypeHandler.GenericJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class JsonListMapTypeHandler extends GenericJsonTypeHandler<List<Map<String, Object>>> {
    public JsonListMapTypeHandler() {
        super(new TypeReference<List<Map<String, Object>>>() {});
    }
}
