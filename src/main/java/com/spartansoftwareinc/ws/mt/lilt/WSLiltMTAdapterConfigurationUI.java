package com.spartansoftwareinc.ws.mt.lilt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.idiominc.wssdk.WSContext;
import com.idiominc.wssdk.component.WSComponentConfigurationData;
import com.idiominc.wssdk.component.WSComponentConfigurationUI;
import com.spartansoftwareinc.ws.okapi.base.ui.UIMultiValueInput;
import com.spartansoftwareinc.ws.okapi.base.ui.UITable;
import com.spartansoftwareinc.ws.okapi.base.ui.UITextField;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

public class WSLiltMTAdapterConfigurationUI extends WSComponentConfigurationUI {

    private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    private static final String ERROR_MESSAGE = "Error: Please enter valid values for ";

    private static final String LABEL_API_KEY = "API Key";
    private static final String LABEL_MEMORY_IDS = "Memory IDs";
    private static final String LABEL_MATCH_SCORE = "MT Match Score";

    private static final String API_KEY = "apiKey";
    private static final String MEMORY_IDS = "memoryIds";
    private static final String MATCH_SCORE = "matchScore";

    @Override
    public String render(WSContext context, HttpServletRequest request, WSComponentConfigurationData config) {
        WSLiltMTAdapterConfigurationData configData = config != null ?
                ((WSLiltMTAdapterConfigurationData) config) : new WSLiltMTAdapterConfigurationData();

        StringBuilder sb = new StringBuilder();
        String apiKey = configData.getApiKey() == null ? "" : configData.getApiKey();

        final String error = (String)request.getAttribute(ERROR_MESSAGE_ATTRIBUTE);
        if (error != null) {
            sb.append("<p style=\"color: red;\">");
            sb.append(UIUtil.escapeHtml(error));
            sb.append("</p>");
        }
        Set<String> memoryIds = configData.getMemoryIds();
        UITable table = new UITable()
                .add(new UITextField(LABEL_API_KEY, API_KEY, apiKey))
                .add(new UIMultiValueInput(LABEL_MEMORY_IDS, MEMORY_IDS, memoryIds, memoryIds))
                .add(new UINumericInput(LABEL_MATCH_SCORE, MATCH_SCORE, 0, 100, configData.getMatchScore()));
        sb.append(table.render());
        return sb.toString();
    }

    @Override
    public WSComponentConfigurationData save(WSContext context, HttpServletRequest request,
            WSComponentConfigurationData config) {
        WSLiltMTAdapterConfigurationData configData = config == null || !(config instanceof WSLiltMTAdapterConfigurationData)
                ? new WSLiltMTAdapterConfigurationData()
                : ((WSLiltMTAdapterConfigurationData) config);

        final String apiKey = request.getParameter(API_KEY);
        Set<String> memoryIds = new HashSet<>(UIUtil.getOptionValues(request, "memoryIds_keys_res"));

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
            errors = addError(LABEL_MEMORY_IDS, errors);
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
