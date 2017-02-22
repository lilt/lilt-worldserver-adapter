package com.spartansoftwareinc.ws.mt.lilt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.idiominc.wssdk.WSContext;
import com.idiominc.wssdk.WSRuntimeException;
import com.idiominc.wssdk.component.WSComponentConfigurationData;
import com.idiominc.wssdk.component.WSComponentConfigurationUI;
import com.spartansoftwareinc.lilt.api.LiltAPI;
import com.spartansoftwareinc.lilt.api.LiltAPIFactory;
import com.spartansoftwareinc.lilt.api.LiltAPIImpl;
import com.spartansoftwareinc.ws.okapi.base.ui.UITable;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

public class WSLiltMTAdapterConfigurationUI extends WSComponentConfigurationUI {
    public static final Logger LOG = Logger.getLogger(WSLiltMTAdapterConfigurationUI.class);

    private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    private static final String ERROR_MESSAGE = "Error: Please enter valid values for ";

    private static final String LABEL_API_KEY = "API Key";
    private static final String LABEL_MEMORIES = "Memories";
    private static final String LABEL_MATCH_SCORE = "MT Match Score";
    private static final String LABEL_INSTRUCTIONS = "Instructions";

    private static final String API_KEY = "apiKey";
    private static final String MATCH_SCORE = "matchScore";

    private static final String INSTRUCTIONS_TEXT =
            "Enter your Lilt API key below and click \"Refresh Memories\" to load the list of available memories." + 
            "<br/>Then select the memories you wish to use for this configuration.";

    private LiltAPIFactory apiFactory;

    public WSLiltMTAdapterConfigurationUI() {
        apiFactory = new LiltAPIFactory() {
            @Override
            public LiltAPI create(String apiKey) {
                    CloseableHttpClient client = HttpClientBuilder.create()
                            .setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectTimeout(20*1000)
                                .setSocketTimeout(20*1000)
                                .build())
                            .build();
                    return new LiltAPIImpl(client, apiKey);
                }
        };
    }

    @Override
    public String render(WSContext context, HttpServletRequest request, WSComponentConfigurationData config) {
        WSLiltMTAdapterConfigurationData configData = config != null ?
                ((WSLiltMTAdapterConfigurationData) config) : new WSLiltMTAdapterConfigurationData();

        StringBuilder sb = new StringBuilder();
        String apiKey = configData.getApiKey() == null ? "" : configData.getApiKey();

        sb.append("<script language=\"JavaScript\" src=\"js/jquery/jquery.js\"></script>"); // oops
        final String error = (String)request.getAttribute(ERROR_MESSAGE_ATTRIBUTE);
        if (error != null) {
            sb.append("<p style=\"color: red;\">");
            sb.append(UIUtil.escapeHtml(error));
            sb.append("</p>");
        }
        List<UIMultiSelect.OptionValue> options = new MemoryOptionBuilder(apiFactory). 
                        buildMemoryOptionsList(configData.getApiKey(), configData.getMemoryIds());
        UITable table = new UITable()
                .add(new UIStaticText(LABEL_INSTRUCTIONS, INSTRUCTIONS_TEXT))
                .add(new UITextField(LABEL_API_KEY, API_KEY, apiKey, getLoadMemoryButton()))
                .add(new UIMultiSelect(LABEL_MEMORIES, options))
                .add(new UINumericInput(LABEL_MATCH_SCORE, MATCH_SCORE, 0, 100, configData.getMatchScore()));
        sb.append(table.render());
        insertResource(sb, "/com/spartansoftwareincws/mt/lilt/loadMemoryButton.html.template");
        return sb.toString();
    }

    private String getLoadMemoryButton() {
        return "<input id=\"loadMemoryButton\" name=\"load_memory\" value=\"Refresh Memories\" type=\"button\">";
    }

    private void insertResource(StringBuilder sb, String resourceName) {
        try {
            sb.append(UIUtil.loadResourceAsString(resourceName));
        }
        catch (IOException e) {
            throw new WSRuntimeException(e);
        }
    }

    @Override
    public WSComponentConfigurationData save(WSContext context, HttpServletRequest request,
            WSComponentConfigurationData config) {
        WSLiltMTAdapterConfigurationData configData = config == null || !(config instanceof WSLiltMTAdapterConfigurationData)
                ? new WSLiltMTAdapterConfigurationData()
                : ((WSLiltMTAdapterConfigurationData) config);

        final String apiKey = request.getParameter(API_KEY);
        String[] ids = request.getParameterValues("memoryList[]");
        if (ids == null) {
            ids = new String[0];
        }
        Set<String> memoryIds = new LinkedHashSet<String>(Arrays.asList(ids));
        String errors = null;

        if (apiKey == null || apiKey.length() < 1) {
            errors = addError(LABEL_API_KEY, errors);
        }
        List<String> invalidMemoryIds = new ArrayList<>();
        for (String id : memoryIds) {
            if (!isLong(id)) {
                invalidMemoryIds.add(id);
            }
        }
        if (invalidMemoryIds.size() > 0) {
            errors = addError(LABEL_MEMORIES, errors);
        }

        int matchScore = getMatchScoreParameter(request);
        if (matchScore < 0 || matchScore > 100) {
            errors = addError(LABEL_MATCH_SCORE, errors);
        }

        if (errors != null) {
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, ERROR_MESSAGE + errors);
            throw new IllegalArgumentException();
        }

        configData.setApiKey(apiKey);
        configData.setMemoryIds(memoryIds);
        configData.setMatchScore(matchScore);
        return configData;
    }

    private String addError(String field, String invalidFields) {
        return invalidFields == null ? field : invalidFields + ", " + field;
    }

    private boolean isLong(String s) {
        try {
            if (Long.valueOf(s) != null) {
                return true;
            }
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    private int getMatchScoreParameter(HttpServletRequest request) {
        try {
            return Integer.valueOf(request.getParameter(MATCH_SCORE));
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}
