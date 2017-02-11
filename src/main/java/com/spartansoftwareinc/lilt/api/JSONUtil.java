package com.spartansoftwareinc.lilt.api;

import org.json.simple.JSONObject;

class JSONUtil {

    static Long requireLong(JSONObject json, String key) {
        return (Long)require(json, key);
    }

    static String requireString(JSONObject json, String key) {
        return (String)require(json, key);
    }

    private static Object require(JSONObject json, String key) {
        Object o = json.get(key);
        if (o == null) {
            throw new RuntimeException("Missing required field " + key);
        }
        return o;
    }
}
