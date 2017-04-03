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
import com.idiominc.wssdk.component.mt.WSMTSegmentTranslation;
import com.idiominc.wssdk.linguistic.WSLanguage;
import com.idiominc.wssdk.linguistic.WSLanguagePair;
import com.idiominc.wssdk.linguistic.WSLinguisticManager;
import com.idiominc.wssdk.mt.WSMTAdapterRuntimeException;
import com.idiominc.wssdk.mt.WSMTResult;
import com.spartansoftwareinc.lilt.Version;
import com.spartansoftwareinc.lilt.api.LiltAPI;
import com.spartansoftwareinc.lilt.api.LiltAPIImpl;
import com.spartansoftwareinc.lilt.api.Memory;

public class WSLiltMTAdapter extends WSMTAdapterComponent {
    public static final Logger LOG = Logger.getLogger(WSLiltMTAdapter.class);

    private WSLiltMTAdapterConfigurationData configurationData;
    private LiltAPI api;

    @Override
    public String getDescription() {
        return "MT Adapter for Lilt";
    }

    @Override
    public WSVersion getMinimumWorldServerVersion() {
        return new WSVersion(9, 0, 0);
    }

    @Override
    public String getName() {
        return "Lilt MT Adapter";
    }

    @Override
    public String getVersion() {
        return Version.BANNER;
    }

    @Override
    public WSLanguagePair[] getSupportedLanguagePairs(WSContext context) {
        Set<Memory> memories = new HashSet<>(getConfiguration().getMemories());
        WSLinguisticManager lingManager = context.getLinguisticManager();
        List<WSLanguagePair> pairs = new ArrayList<>();
        for (Memory mem : memories) {
            WSLanguage src = getWSLanguageForLanguageCode(lingManager, mem.srcLang);
            WSLanguage tgt = getWSLanguageForLanguageCode(lingManager, mem.trgLang);
            if (src == null || tgt == null) {
                LOG.warn("Could not map language pair " + mem.srcLang + " --> " + mem.trgLang +
                         " for Lilt memory " + mem.id + " to WorldServer");
                continue;
            }
            pairs.add(new WSLanguagePair(src, tgt));
        }
        return pairs.toArray(new WSLanguagePair[pairs.size()]);
    }

    @Override
    public boolean updatable() {
        return true;
    }

    @Override
    public void translate(WSContext context, WSMTRequest[] mtRequests, WSLanguage srcLang, WSLanguage tgtLang) {
        try {
            // Find a memory for these languages
            Memory mem = findMemoryForLanguagePair(srcLang, tgtLang);
            LOG.info("Using Lilt memory " + mem.id + " to translate " + srcLang.getName() + " --> " + tgtLang.getName());
            for (WSMTRequest request : mtRequests) {
                handleRequest(mem, request);
            }
        }
        catch (IOException e) {
            throw new WSMTAdapterRuntimeException("Failed to translate using Lilt MT", e);
        }
    }

    @Override
    public boolean updateMT(WSContext context, WSMTSegmentTranslation[] translations, WSLanguage srcLang,
            WSLanguage tgtLang) {
        try {
            Memory mem = findMemoryForLanguagePair(srcLang, tgtLang);
            LOG.info("Using Lilt memory " + mem.id + " to update " + srcLang.getName() + " --> " + tgtLang.getName());
            int success = 0;
            for (WSMTSegmentTranslation translation : translations) {
                if (getLiltAPI().updateTranslation(mem.id, translation.getSource(), translation.getTarget())) {
                    success++;
                }
            }
            if (success < translations.length) {
                LOG.warn("Updating memory " + mem.id + ", only " + success + " of " +
                         translations.length + " translations updated");
            }
            return true;
        }
        catch (IOException e) {
            throw new WSMTAdapterRuntimeException("Failed to update Lilt MT", e);
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

    protected Memory findMemoryForLanguagePair(WSLanguage srcWSLang, WSLanguage tgtWSLang) throws IOException {
        Set<Memory> memories = getConfiguration().getMemories(); 
        String srcLang = srcWSLang.getLocale().getLanguage();
        String tgtLang = tgtWSLang.getLocale().getLanguage();
        for (Memory mem : memories) {
            if (mem.supportsLanguagePair(srcLang, tgtLang)) {
                return mem;
            }
        }
        throw new WSMTAdapterRuntimeException("Could not find a configured Lilt memory for language pair " +
                srcWSLang.getName() + " -> " + tgtWSLang.getName());
    }

    protected void handleRequest(Memory mem, WSMTRequest request) throws IOException {
        List<String> response = getLiltAPI().getSimpleTranslation(mem.id, request.getSource(), 1);
        WSMTResult[] results = new WSMTResult[1];
        results[0] = new WSMTResult(request.getSource(), response.get(0), getConfiguration().getMatchScore());
        request.setResults(results);
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
