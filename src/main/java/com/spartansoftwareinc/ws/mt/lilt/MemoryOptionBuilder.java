package com.spartansoftwareinc.ws.mt.lilt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.idiominc.wssdk.WSRuntimeException;
import com.spartansoftwareinc.lilt.api.LiltAPIFactory;
import com.spartansoftwareinc.lilt.api.Memory;

class MemoryOptionBuilder {
    public static final Logger LOG = Logger.getLogger(MemoryOptionBuilder.class);

    private LiltAPIFactory apiFactory;

    MemoryOptionBuilder(LiltAPIFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    public List<UIMultiSelect.OptionValue> buildMemoryOptionsList(String apiKey, Set<String> memoryIds) {
        List<UIMultiSelect.OptionValue> options = new ArrayList<>();
        if (memoryIds.size() > 0 && !"".equals(apiKey)) {
            List<Memory> memories = getMemories(apiKey);
            for (String s : memoryIds) {
                Memory mem = removeMemoryById(memories, s);
                if (mem != null) {
                    String label = mem.name + " (" + mem.srcLang + " -> " + mem.tgtLang + ")";
                    options.add(new UIMultiSelect.OptionValue(s, label, true));
                }
                else {
                    LOG.warn("Unable to find memory with id " + s + " for apiKey " + apiKey);
                }
            }
            // Add any unselected ones
            for (Memory mem : memories) {
                String label = mem.name + " (" + mem.srcLang + " -> " + mem.tgtLang + ")";
                options.add(new UIMultiSelect.OptionValue(String.valueOf(mem.id), label, false));
            }
        }
        return options;
    }

    private List<Memory> getMemories(String apiKey) {
        try {
            LOG.warn("apiFactory " + apiFactory);
            LOG.warn("apiKey: " + apiKey);
            LOG.warn("api " + apiFactory.create(apiKey));
            return apiFactory.create(apiKey).getAllMemories();
        }
        catch (Exception e) {
            throw new WSRuntimeException(e);
        }
    }

    private Memory removeMemoryById(List<Memory> memories, String id) {
        for (int i = 0; i < memories.size(); i++) {
            Memory mem = memories.get(i);
            if (String.valueOf(mem.id).equals(id)) {
                return memories.remove(i);
            }
        }
        return null;
    }
}
