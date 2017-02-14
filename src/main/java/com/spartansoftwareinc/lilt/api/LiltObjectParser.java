package com.spartansoftwareinc.lilt.api;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class LiltObjectParser {
    private JSONParser parser = new JSONParser();

    public Memory parseMemory(String rawJson) throws ParseException {
        JSONObject json = (JSONObject)parser.parse(rawJson);
        return Memory.fromJSON(json);
    }

    public List<Memory> parseMemories(String rawJson) throws ParseException {
        JSONArray array = (JSONArray)parser.parse(rawJson);
        List<Memory> memories = new ArrayList<>();
        for (Object o : array) {
            memories.add(Memory.fromJSON(((JSONObject)o)));
        }
        return memories;
    }

    public List<String> parseSimpleTranslation(String rawJson) throws ParseException {
        JSONArray array = (JSONArray)parser.parse(rawJson);
        List<String> results = new ArrayList<>();
        for (Object o : array) {
            results.add((String)o);
        }
        return results;
    }

    public List<Translation> parseRichTranslation(String rawJson) throws ParseException {
        /**
         * Rich translation results have an outer container that includes source
         * (tokenized/untokenized/delimiters) info, plus an inner 'translation' object that
         * includes the results.
         */
        JSONObject json = (JSONObject)parser.parse(rawJson);
        JSONArray tInfo = JSONUtil.requireArray(json, "translation");
        List<Translation> results = new ArrayList<>();
        for (Object o : tInfo) {
            results.add(Translation.fromJSON(((JSONObject)o)));
        }
        return results;
    }
}
