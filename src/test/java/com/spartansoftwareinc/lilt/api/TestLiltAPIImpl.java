package com.spartansoftwareinc.lilt.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestLiltAPIImpl {

    @Mock
    HttpClient client;

    @Mock
    HttpResponse response;

    @Mock
    HttpEntity entity;

    @Mock
    StatusLine status;

    @Test
    public void testGetMemory() throws Exception {
        String json =
                "{\"id\":1234,\"srclang\":\"en\",\"trglang\":\"fr\",\"name\":\"Test Memory\",\"num_segments\":0}";
        setupResponse(json);

        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        Memory mem = api.getMemory(1234);
        assertEquals(1234, mem.id);
        assertEquals("en", mem.srcLang);
        assertEquals("fr", mem.trgLang);
        assertEquals("Test Memory", mem.name);
        assertEquals(0, mem.numSegments);
    }

    @Test
    public void testGetAllMemories() throws Exception {
        String json =
                "[{\"id\":1234,\"srclang\":\"en\",\"trglang\":\"fr\",\"name\":\"Test Memory\",\"num_segments\":0}]";
        setupResponse(json);
        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        List<Memory> memories = api.getAllMemories();
        assertEquals(1, memories.size());
        Memory mem = memories.get(0);
        assertEquals(1234, mem.id);
        assertEquals("en", mem.srcLang);
        assertEquals("fr", mem.trgLang);
        assertEquals("Test Memory", mem.name);
        assertEquals(0, mem.numSegments);
    }

    @Test
    public void testParseSimpleTranslation() throws Exception {
        String json = "[\"Bonjour\",\"Hello\",\"Bonjour,\"]";
        setupResponse(json);
        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        List<String> results = api.getSimpleTranslation(1234, "Hello", 3);
        assertEquals(3, results.size());
        assertEquals("Bonjour", results.get(0));
        assertEquals("Hello", results.get(1));
        assertEquals("Bonjour,", results.get(2));
    }

    @Test
    public void testParseRichTranslation() throws Exception {
        String json = "{ \"untokenizedSource\": \"Hello\", \"tokenizedSource\": \"Hello\", \"sourceDelimiters\": " +
                "[ \"\", \"\" ], \"translation\": [ { \"target\": \"Bonjour\", \"align\": \"0-0\", \"score\": " +
                "9.2579074e-05, \"targetDelimiters\": [ \"\", \"\" ], \"isTMMatch\": false, \"provenance\": \"0\", " +
                "\"sourceContextHash\": 0 } ], \"procTime\": 18 }";
        setupResponse(json);
        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        List<Translation> results = api.getRichTranslation(1234, "Hello", 1);
        assertEquals(1, results.size());
        assertEquals("Bonjour", results.get(0).target);
        assertFalse(results.get(0).isTMMatch);
    }

    private void setupResponse(String rawJson) throws Exception {
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(rawJson.getBytes()));
        when(response.getStatusLine()).thenReturn(status);
        when(status.getStatusCode()).thenReturn(200);
    }
}
