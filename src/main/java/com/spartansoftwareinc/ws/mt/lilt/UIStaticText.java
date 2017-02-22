package com.spartansoftwareinc.ws.mt.lilt;

import java.io.IOException;

import com.spartansoftwareinc.ws.okapi.base.ui.UIElement;
import com.spartansoftwareinc.ws.okapi.base.ui.UIUtil;

public class UIStaticText implements UIElement {
    private String label, text;

    public UIStaticText(String label, String text) {
        this.label = label;
        this.text = text;
    }

    @Override
    public String render() throws IOException {
      StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td class=\"prop_table_left_side\">")
          .append(UIUtil.escapeHtml(label))
          .append(":</td>");
        sb.append("<td class=\"prop_table_right_side\">");
        sb.append(text);
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }
}
