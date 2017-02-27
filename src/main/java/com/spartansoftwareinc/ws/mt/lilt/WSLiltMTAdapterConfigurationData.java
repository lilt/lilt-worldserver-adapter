package com.spartansoftwareinc.ws.mt.lilt;

import java.util.LinkedHashSet;
import java.util.Set;

import com.idiominc.wssdk.component.mt.WSMTConfigurationData;
import com.spartansoftwareinc.lilt.api.Memory;

public class WSLiltMTAdapterConfigurationData extends WSMTConfigurationData {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MATCH_SCORE = 95;

    private String apiKey;
    private LinkedHashSet<Memory> memoryIds = new LinkedHashSet<>();
    private int matchScore = DEFAULT_MATCH_SCORE;

    public WSLiltMTAdapterConfigurationData() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Set<Memory> getMemories() {
        return memoryIds;
    }

    public void setMemories(Set<Memory> memoryIds) {
        this.memoryIds = new LinkedHashSet<>(memoryIds);
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }
}
