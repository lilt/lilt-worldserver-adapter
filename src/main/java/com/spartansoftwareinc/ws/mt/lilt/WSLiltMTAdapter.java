package com.spartansoftwareinc.ws.mt.lilt;

import com.idiominc.wssdk.WSContext;
import com.idiominc.wssdk.WSVersion;
import com.idiominc.wssdk.component.mt.WSMTAdapterComponent;
import com.idiominc.wssdk.component.mt.WSMTRequest;
import com.idiominc.wssdk.linguistic.WSLanguage;
import com.idiominc.wssdk.linguistic.WSLanguagePair;

public class WSLiltMTAdapter extends WSMTAdapterComponent {

    public String getDescription() {
        return "MT Adapter for Lilt";
    }

    public WSVersion getMinimumWorldServerVersion() {
        return new WSVersion(9, 0, 0);
    }

    public String getName() {
        return "Lilt MT Adapter";
    }

    public String getVersion() {
        return "1.0"; // TODO
    }

    @Override
    public WSLanguagePair[] getSupportedLanguagePairs(WSContext arg0) {
        // TODO I don't remember what happens here
        return null;
    }

    @Override
    public void translate(WSContext context, WSMTRequest[] mtRequests, WSLanguage srcLang, WSLanguage trgLang) {
        // TODO Auto-generated method stub
        
    }

}
