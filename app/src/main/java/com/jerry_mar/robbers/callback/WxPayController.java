package com.jerry_mar.robbers.callback;

import android.app.Activity;
import android.os.Bundle;

import com.jerry_mar.robbers.Robber;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WxPayController extends Activity implements IWXAPIEventHandler {
    public static final int RETURN_MSG_SUCCESS = 0;
    public static final int RETURN_MSG_TYPE_FAILURE = -1;
    public static final int RETURN_MSG_TYPE_CANCLE = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Robber.initCore(getApplicationContext(), null)
                .handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case RETURN_MSG_SUCCESS:
                    onPaySuccess();
                break;
                case RETURN_MSG_TYPE_FAILURE:
                    onPayFailure("支付失败");
                    break;
                case RETURN_MSG_TYPE_CANCLE:
                    onPayFailure("支付取消");
                    break;
            }
        }
        finish();
    }

    public void onPayFailure(String msg) {}
    public void onPaySuccess() {}
}
