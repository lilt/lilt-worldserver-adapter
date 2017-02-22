package com.spartansoftwareinc.ws.mt.lilt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.spartansoftwareinc.ws.okapi.base.ui.UIElement;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

public class UIMultiSelect implements UIElement {
    public static final Logger LOG = Logger.getLogger(UIMultiSelect.class);

    private Collection<OptionValue> options = new ArrayList<>();
    private String label;

    public static class OptionValue {
        final String id, label;
        final boolean selected;
        public OptionValue(String id, String label, boolean selected) {
            this.id = id;
            this.label = label;
            this.selected = selected;
        }
    }

    public UIMultiSelect(String label, Collection<OptionValue> options) {
        this.label = label;
        this.options = options;
    }

    @Override
    public String render() throws IOException {
        String s = UIUtil.loadResourceAsString("/com/spartansoftwareincws/mt/lilt/memoryList.html.template");
        s = s.replaceAll("\\$\\{label\\}", UIUtil.escapeHtml(label));
        return String.format(s, getOptionValues());
    }

    private String getOptionValues() {
        StringBuilder sb = new StringBuilder(" ");
        for (OptionValue option : options) {
            sb.append("<option value=\"")
              .append(option.id)
              .append("\"");
            if (option.selected) {
              sb.append(" selected");
            }
            sb.append(">")
              .append(UIUtil.escapeHtml(option.label))
              .append("</option>");
        }
        return sb.toString();
    }
}
