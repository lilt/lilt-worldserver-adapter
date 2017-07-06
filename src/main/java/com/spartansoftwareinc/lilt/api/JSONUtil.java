package com.spartansoftwareinc.lilt.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
        // if we get a long, cast to double
        Object obj = require(json, key);
        return obj instanceof Long ? ((Long)obj).doubleValue() : (Double)obj;
    }

    private static Object require(JSONObject json, String key) {
        Object o = json.get(key);
        if (o == null) {
            throw new RuntimeException("Missing required field " + key);
        }
        return o;
    }

    static String streamUtf8AsString(InputStream is) throws IOException {
        Scanner s = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
