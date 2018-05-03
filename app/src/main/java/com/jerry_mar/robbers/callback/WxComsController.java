package com.jerry_mar.robbers.callback;

import android.app.Activity;
import android.os.Bundle;

import com.jerry_mar.robbers.Robber;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public abstract class WxComsController extends Activity implements IWXAPIEventHandler {
    public static final int RETURN_MSG_TYPE_LOGIN = 1;
    public static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Robber.initCore(getApplicationContext(), null)
                .handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {}

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_LOGIN == baseResp.getType()) {
                    onLoginFailure(baseResp.errStr);
                    return;
                }
                if (RETURN_MSG_TYPE_SHARE == baseResp.getType()) {
                    onShareFailure();
                    return;
                }
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.getType() == RETURN_MSG_TYPE_LOGIN) {
                    String code = ((SendAuth.Resp) baseResp).code;
                    onLoginSuccess(code);
                    return;
                }
                if (baseResp.getType() == RETURN_MSG_TYPE_SHARE) {
                    onShareSuccess();
                }
        }
    }

    public void onLoginFailure(String msg) {}
    public void onLoginSuccess(String code) {}
    public void onShareFailure() {}
    public void onShareSuccess() {}
}
