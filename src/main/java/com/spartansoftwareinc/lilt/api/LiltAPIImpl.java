package com.spartansoftwareinc.lilt.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class LiltAPIImpl implements LiltAPI {
    static final Logger LOG = Logger.getLogger(LiltAPIImpl.class);
    static final String API_BASE = "https://lilt.com/1";

    private HttpClient client;
    private String apiKey;
    private LiltObjectParser parser = new LiltObjectParser();

    
    public LiltAPIImpl(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    @Override
    public Memory getMemory(long memoryId) throws IOException {
        try {
            HttpUriRequest request = get("/mem")
                    .addParameter("memory_id", String.valueOf(memoryId))
                    .build();
            String raw = getRawJSONResponse(request);
            return raw != null ? parser.parseMemory(raw) : null;
        }
        catch (ParseException e) {
            // XXX what is the right thing to do here?
            throw new RuntimeException(e);
        }
    }

    protected String getRawJSONResponse(HttpUriRequest request) throws IOException {
        HttpResponse response = execute(request);
        StatusLine status = response.getStatusLine();
        try (InputStream is = response.getEntity().getContent()) {
            // Read the stream immediately so that it is consumed even in error cases, in
            // order to prevent leaks
            String rawJson = streamUtf8AsString(is);
            if (status.getStatusCode() == 200) {
                return rawJson;
            }
            logResponseError("Request error", request, status);
        }
        return null;
    }

    private void logResponseError(String message, HttpUriRequest request, StatusLine status) {
        LOG.error(message + ": " + status.getStatusCode() + ": " + status.getReasonPhrase() +
                ", for query " + request.toString());
    }

    @Override
    public List<Memory> getAllMemories() throws IOException {
        try {
            String raw = getRawJSONResponse(get("/mem").build());
            return raw != null ? parser.parseMemories(raw) : null;
        }
        catch (ParseException e) {
            // XXX what is the right thing to do here?
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSimpleTranslation(long memoryId, String source, int count) throws IOException {
        try {
            HttpUriRequest request = get("/tr")
                    .addParameter("memory_id", String.valueOf(memoryId))
                    .addParameter("source", source)
                    .addParameter("n", String.valueOf(count))
                    .build();
            String raw = getRawJSONResponse(request);
            return raw != null ? parser.parseSimpleTranslation(raw) : null;
        }
        catch (ParseException e) {
            // XXX what is the right thing to do here?
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Translation> getRichTranslation(long memoryId, String source, int count) throws IOException {
        try {
            HttpUriRequest request = get("/tr")
                    .addParameter("memory_id", String.valueOf(memoryId))
                    .addParameter("source", source)
                    .addParameter("n", String.valueOf(count))
                    .addParameter("rich", String.valueOf(true))
                    .build();
            String raw = getRawJSONResponse(request);
            return raw != null ? parser.parseRichTranslation(raw) : null;
        }
        catch (ParseException e) {
            // XXX what is the right thing to do here?
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTranslation(long memoryId, String source, String target) throws IOException {
        JSONObject json = new UpdateMemoryRequest(memoryId, source, target).toJSON();
        StringWriter sw = new StringWriter();
        json.writeJSONString(sw);
        LOG.warn("Updating Lilt: " + sw.toString());
        HttpUriRequest request = post("/mem")
                .setEntity(new StringEntity(sw.toString(), StandardCharsets.UTF_8))
                .build();
        String raw = getRawJSONResponse(request);
        LOG.warn("Updat response: " + raw);
    }

    protected RequestBuilder get(String endpoint) {
        return RequestBuilder.get(API_BASE + endpoint).addParameter("apikey", apiKey);
    }

    protected RequestBuilder post(String endpoint) {
        return RequestBuilder.post(API_BASE + endpoint).addParameter("apikey", apiKey);
    }

    protected HttpResponse execute(HttpUriRequest request) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(request.toString());
        }
        HttpResponse response = client.execute(request);
        if (LOG.isDebugEnabled()) {
            LOG.debug(response.toString());
        }
        return response;
    }

    protected String streamUtf8AsString(InputStream is) throws IOException {
        Scanner s = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
