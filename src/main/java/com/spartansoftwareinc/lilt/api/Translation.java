package com.spartansoftwareinc.lilt.api;

import static com.spartansoftwareinc.lilt.api.JSONUtil.*;

import org.json.simple.JSONObject;

/**
 * Represents rich translation data from Lilt.
 *
 * Note: this is not a complete mapping of all the fields returned by the API.
 */
public class Translation {
    public final String target;
    public final boolean isTMMatch;
    public final double score;

    Translation(String target, double score, boolean isTMMatch) {
        this.target = target;
        this.isTMMatch = isTMMatch;
        this.score = score;
    }

    /**
     * This method expects to be passed one of the inner objects from the 'translation'
     * array inside a rich results object.
     */
    static Translation fromJSON(JSONObject tInfo) {
        String target = requireString(tInfo, "target");
        double rawScore = requireDouble(tInfo, "score");
        boolean isMatch = requireBoolean(tInfo, "isTMMatch");
        return new Translation(target, rawScore, isMatch);
    }
}
