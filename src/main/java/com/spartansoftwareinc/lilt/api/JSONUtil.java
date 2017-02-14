package com.spartansoftwareinc.lilt.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class JSONUtil {

    static Long requireLong(JSONObject json, String key) {
        return (Long)require(json, key);
    }

    static String requireString(JSONObject json, String key) {
        return (String)require(json, key);
    }

    static JSONObject requireObject(JSONObject json, String key) {
        return (JSONObject)require(json, key);
    }

    static JSONArray requireArray(JSONObject json, String key) {
        return (JSONArray)require(json, key);
    }

    static Boolean requireBoolean(JSONObject json, String key) {
        return (Boolean)require(json, key);
    }

    static Double requireDouble(JSONObject json, String key) {
        return (Double)require(json, key);
    }

    private static Object require(JSONObject json, String key) {
        Object o = json.get(key);
        if (o == null) {
            throw new RuntimeException("Missing required field " + key);
        }
        return o;
    }
}
