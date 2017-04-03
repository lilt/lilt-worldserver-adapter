package com.spartansoftwareinc.lilt.api;

import org.json.simple.JSONObject;

public class UpdateMemoryRequest {
    private final long memoryId;
    private final String source;
    private final String target;

    public UpdateMemoryRequest(long memoryId, String source, String target) {
        this.memoryId = memoryId;
        this.source = source;
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("memory_id", memoryId);
        json.put("source", source);
        json.put("target", target);
        return json;
    }
}
