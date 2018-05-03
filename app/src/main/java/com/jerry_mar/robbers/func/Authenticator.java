package com.jerry_mar.robbers.func;

import com.jerry_mar.robbers.Intent;
import com.jerry_mar.robbers.Robber;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class Authenticator {
    public void login(Intent intent) {
        int provider = intent.getExtra(Intent.PROVIDER, Robber.PROVIDER_WECHAT);
        switch (provider) {
            case Robber.PROVIDER_WECHAT : {
                IWXAPI wxapi = Robber.initCore(intent.getContext(), intent.getAppid());
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "diandi_wx_login";
                wxapi.sendReq(req);
            }
            break;
        }
    }
}
