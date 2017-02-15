package com.spartansoftwareinc.ws.mt;

import java.util.Map;

import com.idiominc.wssdk.WSContext;
import com.idiominc.wssdk.WSException;
import com.idiominc.wssdk.asset.WSAssetTask;
import com.idiominc.wssdk.asset.WSAssetTranslation;
import com.idiominc.wssdk.component.autoaction.WSActionResult;
import com.idiominc.wssdk.component.autoaction.WSTaskAutomaticAction;
import com.idiominc.wssdk.mt.WSMTService;
import com.idiominc.wssdk.workflow.WSTask;

public class UpdateMTAutomaticAction extends WSTaskAutomaticAction {
    @Override
    public String getDescription() {
        return "Update the configured MT system with the current target translations" +
               " for this asset, if supported.";
    }

    @Override
    public String getName() {
        return "Update Machine Translation";
    }

    @Override
    public WSActionResult execute(WSContext context, Map params, WSTask task) throws WSException {
        WSAssetTranslation assetTrans = ((WSAssetTask)task).getAssetTranslation();
        WSMTService mt = assetTrans.getMTService();
        if (mt == null) {
            return new WSActionResult(WSActionResult.DONE_RETURN_VALUE, "No MT service configured.");
        }
        if (mt.updatable()) {
            mt.updateMT(assetTrans);
            return new WSActionResult(WSActionResult.DONE_RETURN_VALUE, "MT service " + mt.getDisplayString() + " was updated.");
        }
        else {
            return new WSActionResult(WSActionResult.DONE_RETURN_VALUE, "MT service " + mt.getDisplayString() + " is not updatable.");
        }
    }

    @Override
    public String[] getReturns() {
        return new String[] {
                WSActionResult.DONE_RETURN_VALUE
        };
    }

}
