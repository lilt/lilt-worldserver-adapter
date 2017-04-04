package com.spartansoftwareinc.ws.mt.okapi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MTRequestConverterTest {
    private MTRequestConverter converter = new MTRequestConverter();

    @Test
    public void testConverter() {
        String source = "I'm a {1}reader{2}. ";
        assertEquals("I'm a <span ws_id=\"1\"/>reader<span ws_id=\"2\"/>. ", converter.addCodeMarkup(source));

        String target2 = "Ich bin ein <span ws_id=\"1\"/>Leser<span ws_id=\"2\"/>.";
        assertEquals("Ich bin ein {1}Leser{2}.", converter.removeCodeMarkup(target2));
    }

}
