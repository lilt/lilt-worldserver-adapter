package com.spartansoftwareinc.ws.mt.lilt;

import java.util.ArrayList;
import java.util.List;

import com.idiominc.wssdk.component.mt.WSMTConfigurationData;

public class WSLiltMTAdapterConfigurationData extends WSMTConfigurationData {
    private static final long serialVersionUID = 1L;

    private String apiKey;
    private List<String> memoryIds = new ArrayList<>();

    public WSLiltMTAdapterConfigurationData() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getMemoryIds() {
        return memoryIds;
    }

    public void setMemoryIds(List<String> memoryIds) {
        this.memoryIds = memoryIds;
    }
}
