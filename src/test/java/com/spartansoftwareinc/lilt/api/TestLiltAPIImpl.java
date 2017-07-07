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
    public void testParseRichTranslationWithDetokenization() throws Exception {
        String json = JSONUtil.streamUtf8AsString(getClass().getResourceAsStream("/detokenize.json"));
        setupResponse(json);
        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        String src = "I went to the doctor's office";
        List<Translation> results = api.getRichTranslation(1234, src, 1);
        assertEquals(1, results.size());
        assertEquals("I went to the doctor's office", results.get(0).target);
        assertFalse(results.get(0).isTMMatch);
    }

    @Test
    public void testParseRichTranslationWithDetokenization2() throws Exception {
        String json = "{\"untokenizedSource\":\"Let's go to Mike's house\",\"tokenizedSource\":\"Let 's go to " +
                "Mike 's house\",\"sourceDelimiters\":[\"\",\"\",\" \",\" \",\" \",\"\",\" \",\"\"]," +
                "\"translation\":[{\"score\":1.8206063e-17,\"align\":\"0-0 2-0 3-1 4-2 5-3 6-4\"," +
                "\"targetDelimiters\":[\"\",\" \",\" \",\" \",\" \",\"\"],\"targetWords\":[\"Allons\",\"à\"," +
                "\"Mike\",\"de\",\"maison\"],\"target\":\"Allons à Mike de maison\",\"targetWithTags\":\"Allons " +
                "à Mike de maison\",\"isTMMatch\":false,\"provenance\":\"0 0 0 0 0\"}]}";
        setupResponse(json);
        LiltAPI api = new LiltAPIImpl(client, "abcdef");
        String src = "Let's go to Mike's house";
        List<Translation> results = api.getRichTranslation(1234, src, 1);
        assertEquals(1, results.size());
        assertEquals("Allons à Mike de maison", results.get(0).target);
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
