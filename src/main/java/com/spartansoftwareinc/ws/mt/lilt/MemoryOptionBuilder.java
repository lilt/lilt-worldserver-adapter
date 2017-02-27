package com.spartansoftwareinc.ws.mt.lilt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.spartansoftwareinc.lilt.api.Memory;

class MemoryOptionBuilder {
    public static final Logger LOG = Logger.getLogger(MemoryOptionBuilder.class);

    MemoryOptionBuilder() {
    }

    public List<UIMultiSelect.OptionValue> buildMemoryOptionsList(Set<Memory> memories, List<Memory> liveMemories) {
        List<UIMultiSelect.OptionValue> options = new ArrayList<>();
        if (memories.size() > 0 && liveMemories.size() > 0) {
            // Filter out any memories that have been removed on the server side
            for (Memory m : memories) {
                Memory mem = removeMemoryById(liveMemories, m.id);
                String s = String.valueOf(m.id);
                if (mem != null) {
                    String label = mem.name + " (" + mem.srcLang + " -> " + mem.tgtLang + ")";
                    options.add(new UIMultiSelect.OptionValue(s, label, true));
                }
                else {
                    LOG.warn("Unable to find memory with id " + s);
                }
            }
            // Add any unselected ones
            for (Memory mem : liveMemories) {
                String label = mem.name + " (" + mem.srcLang + " -> " + mem.tgtLang + ")";
                options.add(new UIMultiSelect.OptionValue(String.valueOf(mem.id), label, false));
            }
        }
        return options;
    }


    private Memory removeMemoryById(List<Memory> memories, long id) {
        for (int i = 0; i < memories.size(); i++) {
            Memory mem = memories.get(i);
            if (mem.id == id) {
                return memories.remove(i);
            }
        }
        return null;
    }
}
