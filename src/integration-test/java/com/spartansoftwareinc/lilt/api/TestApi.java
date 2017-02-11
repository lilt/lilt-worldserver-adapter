package com.spartansoftwareinc.lilt.api;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;

public class TestApi {
    private LiltAPI api;

    public static String API_KEY = ""; // XXX Fill this in

    @Before
    public void setup() {
        CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(20*1000)
                        .setSocketTimeout(20*1000)
                        .build())
                    .build();
        api = new LiltAPIImpl(client, API_KEY);
    }

    @Test
    public void getGetMemory() throws Exception {
        Memory mem = api.getMemory(9068);
        assertEquals(9068, mem.id);
        assertEquals("en", mem.srcLang);
        assertEquals("fr", mem.tgtLang);
        assertEquals("Test Memory", mem.name);
        assertEquals(0, mem.nexamples);
        assertNull(api.getMemory(10));
    }

    @Test
    public void testGetAllMemories() throws Exception {
        List<Memory> memories = api.getAllMemories();
        assertEquals(1, memories.size());
        Memory mem = memories.get(0);
        assertEquals(9068, mem.id);
        assertEquals("en", mem.srcLang);
        assertEquals("fr", mem.tgtLang);
        assertEquals("Test Memory", mem.name);
        assertEquals(0, mem.nexamples);
    }

    @Test
    public void testGetSimpleTranslation() throws Exception {
        List<String> results = api.getSimpleTranslation(9068, "Hello world", 1);
        assertEquals(1, results.size());
        assertEquals("Bonjour le monde", results.get(0));
    }
}
