package com.jerry_mar.robbers.func;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jerry_mar.robbers.Intent;
import com.jerry_mar.robbers.Robber;
import com.jerry_mar.robbers.callback.WxComsController;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.List;

public class Authenticator implements IUiListener {
    private WxComsController controller;

    public String login(Intent intent) {
        String result = null;
        int provider = intent.getExtra(Intent.PROVIDER, Robber.PROVIDER_WECHAT);
        switch (provider) {
            case Robber.PROVIDER_WECHAT : {
                IWXAPI wxapi = Robber.initCore(intent.getContext(), intent.getAppid());
                if (wxapi.isWXAppInstalled()) {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "diandi_wx_login";
                    wxapi.sendReq(req);
                } else {
                    result = "您还未安装微信客户端!";
                }
            }
            break;
            case Robber.PROVIDER_QQCHAT : {
                Context context = intent.getContext().getApplicationContext();
                Tencent qqapi = Robber.initCore(context, Long.parseLong(intent.getAppid()));
                if (isQQAvailable(context)) {
                    if (controller == null) {
                        String pkg = context.getPackageName();
                        try {
                            Class<WxComsController> cls = (Class<WxComsController>) Class.forName(pkg + ".wxapi.WXEntryActivity");
                            controller = cls.newInstance();
                            qqapi.login((Activity) intent.getContext(), "all", this);
                        } catch (Exception e) {
                            result = "清完善回调接口!";
                        }
                    } else {
                        qqapi.login((Activity) intent.getContext(), "all", this);
                    }
                } else {
                    result = "您还未安装微信客户端!";
                }
            }
            break;
        }
        return result;
    }

    @Override
    public void onComplete(Object o) {
        controller.onLoginSuccess((String) o);
    }

    @Override
    public void onError(UiError uiError) {
        controller.onLoginFailure(uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        controller.onLoginFailure("登录已取消!");
    }

    public static boolean isQQAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.tim") || pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
