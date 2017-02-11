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
}
