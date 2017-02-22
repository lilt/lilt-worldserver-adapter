package com.spartansoftwareinc.ws.mt.lilt;

import com.spartansoftwareinc.ws.okapi.base.ui.UIElement;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

//TODO: refactor with base
// - custom HTML
// - factor out the hardcoded width
public class UITextField implements UIElement {
    private String label, inputName, value, customHtml;

    public UITextField(String label, String inputName, String value) {
        this(label, inputName, value, null);
    }

    public UITextField(String label, String inputName, String value, String customHtml) {
        this.label = label;
        this.inputName = inputName;
        this.value = value != null ? value : "";
        this.customHtml = customHtml;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td class=\"prop_table_left_side\">" + UIUtil.escapeHtml(label) + ":</td>");
        sb.append("<td class=\"prop_table_right_side\">");
        sb.append("<input size=\"30\" type=\"text\" name=\"" + UIUtil.escapeHtml(inputName) +
                  "\" value=\"" + UIUtil.escapeHtml(value) + "\"/>");
        if (customHtml != null) {
            sb.append(customHtml);
        }
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }

}
