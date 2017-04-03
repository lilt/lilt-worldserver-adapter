package com.spartansoftwareinc.lilt.api;

import static com.spartansoftwareinc.lilt.api.JSONUtil.requireLong;
import static com.spartansoftwareinc.lilt.api.JSONUtil.requireString;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class Memory implements Serializable {
    private static final long serialVersionUID = 1L;

    public final long id;
    public final String srcLang;
    public final String trgLang;
    public final String name;
    public final long numSegments;

    public Memory(long id, String srcLang, String trgLang, String name, long numSegments) {
        this.id = id;
        this.srcLang = srcLang;
        this.trgLang = trgLang;
        this.name = name;
        this.numSegments = numSegments;
    }

    public boolean supportsLanguagePair(String srcLang, String trgLang) {
        return srcLang.equals(this.srcLang) && trgLang.equals(this.trgLang);
    }

    static Memory fromJSON(JSONObject json) {
        Long id = requireLong(json, "id");
        String srcLang = requireString(json, "srclang");
        String trgLang = requireString(json, "trglang");
        String name = requireString(json, "name");
        Long numSegments = requireLong(json, "num_segments");
        return new Memory(id, srcLang, trgLang, name, numSegments);
    }
}
