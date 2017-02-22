package com.spartansoftwareinc.lilt.api;

import org.json.simple.JSONObject;
import static com.spartansoftwareinc.lilt.api.JSONUtil.*;

public class Memory {
    public final long id;
    public final String srcLang;
    public final String tgtLang;
    public final String name;
    public final long nexamples;

    public Memory(long id, String srcLang, String tgtLang, String name, long nexamples) {
        this.id = id;
        this.srcLang = srcLang;
        this.tgtLang = tgtLang;
        this.name = name;
        this.nexamples = nexamples;
    }

    public boolean supportsLanguagePair(String srcLang, String tgtLang) {
        return srcLang.equals(this.srcLang) && tgtLang.equals(this.tgtLang);
    }

    static Memory fromJSON(JSONObject json) {
        Long id = requireLong(json, "memory_id");
        String srcLang = requireString(json, "srclang");
        String tgtLang = requireString(json, "tgtlang");
        String name = requireString(json, "name");
        Long nexamples = requireLong(json, "nexamples");
        return new Memory(id, srcLang, tgtLang, name, nexamples);
    }
}
