package com.spartansoftwareinc.ws.mt.lilt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.idiominc.wssdk.WSContext;
import com.idiominc.wssdk.WSVersion;
import com.idiominc.wssdk.component.WSComponentConfiguration;
import com.idiominc.wssdk.component.WSComponentConfigurationUI;
import com.idiominc.wssdk.component.mt.WSMTAdapterComponent;
import com.idiominc.wssdk.component.mt.WSMTRequest;
import com.idiominc.wssdk.linguistic.WSLanguage;
import com.idiominc.wssdk.linguistic.WSLanguagePair;
import com.idiominc.wssdk.linguistic.WSLinguisticManager;
import com.spartansoftwareinc.lilt.api.LiltAPI;
import com.spartansoftwareinc.lilt.api.LiltAPIImpl;
import com.spartansoftwareinc.lilt.api.Memory;

public class WSLiltMTAdapter extends WSMTAdapterComponent {
    public static final Logger LOG = Logger.getLogger(WSLiltMTAdapter.class);

    private WSLiltMTAdapterConfigurationData configurationData;
    private LiltAPI api;

    public String getDescription() {
        return "MT Adapter for Lilt";
    }

    public WSVersion getMinimumWorldServerVersion() {
        return new WSVersion(9, 0, 0);
    }

    public String getName() {
        return "Lilt MT Adapter";
    }

    public String getVersion() {
        return "1.0"; // TODO
    }

    @Override
    public WSLanguagePair[] getSupportedLanguagePairs(WSContext context) {
        try {
            Set<String> memoryIds = new HashSet<>(getConfiguration().getMemoryIds());
            List<Memory> memories = getLiltAPI().getAllMemories();
            WSLinguisticManager lingManager = context.getLinguisticManager();
            List<WSLanguagePair> pairs = new ArrayList<>();
            for (Memory mem : memories) {
                if (memoryIds.contains(String.valueOf(mem.id))) {
                    WSLanguage src = getWSLanguageForLanguageCode(lingManager, mem.srcLang);
                    WSLanguage tgt = getWSLanguageForLanguageCode(lingManager, mem.tgtLang);
                    if (src == null || tgt == null) {
                        LOG.warn("Could not map language pair " + mem.srcLang + " --> " + mem.tgtLang +
                                 " for Lilt memory " + mem.id + " to WorldServer");
                        continue;
                    }
                    pairs.add(new WSLanguagePair(src, tgt));
                }
            }
            return pairs.toArray(new WSLanguagePair[pairs.size()]);
        }
        catch (IOException e) {
            LOG.error("Could not obtain available memories from Lilt", e);
            return new WSLanguagePair[0];
        }
    }

    @Override
    public WSComponentConfigurationUI getConfigurationUI() {
        return new WSLiltMTAdapterConfigurationUI();
    }

    @Override
    public boolean supportsPlaceholders() {
        return false; // XXX for now
    }

    protected WSLanguage getWSLanguageForLanguageCode(WSLinguisticManager lingManager, String languageCode) {
        // XXX I may need to selectively remap certain codes here to account for differences
        // between java.util.Locale and the codes Lilt uses
        return lingManager.getLanguage(new Locale(languageCode));
    }

    @Override
    public void translate(WSContext context, WSMTRequest[] mtRequests, WSLanguage srcLang, WSLanguage trgLang) {
        // TODO Auto-generated method stub
        
    }

    protected LiltAPI getLiltAPI() {
        if (api == null) {
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(20*1000)
                        .setSocketTimeout(20*1000)
                        .build())
                    .build();
            api = new LiltAPIImpl(client, getConfiguration().getApiKey());
        }
        return api;
    }

    protected WSLiltMTAdapterConfigurationData getConfiguration() {
        if (configurationData == null) {
            WSComponentConfiguration configuration = getCurrentConfiguration();
            configurationData = configuration != null
                    ? ((WSLiltMTAdapterConfigurationData) configuration.getConfigurationData())
                    : new WSLiltMTAdapterConfigurationData();
        }

        return configurationData;
    }
}