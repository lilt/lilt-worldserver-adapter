package com.spartansoftwareinc.ws.mt.lilt;

import java.io.IOException;

import com.spartansoftwareinc.ws.okapi.base.ui.UIElement;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

// TODO: refactor into base
public class UINumericInput implements UIElement {
    private final String label, inputName;
    private final int min, max, value;

    public UINumericInput(String label, String inputName, int min, int max, int value) {
        this.label = label;
        this.inputName = inputName;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    @Override
    public String render() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td class=\"prop_table_left_side\">" + UIUtil.escapeHtml(label) + ":</td>");
        sb.append("<td class=\"prop_table_right_side\">");
        sb.append("<input type=\"number\" min=\"");
        sb.append(min);
        sb.append("\" max=\"");
        sb.append(max);
        sb.append("\" name=\"");
        sb.append(inputName);
        sb.append("\" value=\"");
        sb.append(value);
        sb.append("\"/>");
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }

}
