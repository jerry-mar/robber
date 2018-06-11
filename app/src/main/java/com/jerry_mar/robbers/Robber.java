package com.jerry_mar.robbers;

import android.content.Context;
import android.util.SparseArray;

import com.jerry_mar.robbers.func.Authenticator;
import com.jerry_mar.robbers.func.Paier;
import com.jerry_mar.robbers.func.Publisher;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

public class Robber {
    public static final int PROVIDER_WECHAT = 1;
    public static final int PROVIDER_QQCHAT = 2;
    public static final int PROVIDER_ALI = 2;

    public static final int FUNC_SHARE = 1;
    public static final int FUNC_LOGIN = 2;
    public static final int FUNC_PAY = 3;

    public static final int WECHAT_SHARE_TALK = SendMessageToWX.Req.WXSceneSession;
    public static final int WECHAT_SHARE_FRIEND = SendMessageToWX.Req.WXSceneTimeline;
    public static final int WECHAT_SHARE_TYPE_TEXT = 0x10;
    public static final int WECHAT_SHARE_TYPE_IMAGE = 0x20;
    public static final int WECHAT_SHARE_TYPE_WEBPAGE = 0x30;
    public static final int WECHAT_SHARE_TYPE_VIDEO = 0x40;

    private static SparseArray<Object> coreAPI = new SparseArray<>();
    private static Publisher publisher;
    private static Authenticator authenticator;
    private static Paier paier;

    public static String forward(Intent intent) {
        String result = null;
        int func = intent.getExtra(Intent.FUNC, FUNC_SHARE);
        switch (func) {
            case FUNC_SHARE : {
                if (publisher == null) {
                    publisher = new Publisher();
                }
                result = publisher.share(intent);
            }
            break;
            case FUNC_LOGIN : {
                if (authenticator == null) {
                    authenticator = new Authenticator();
                }
                result = authenticator.login(intent);
            }
            break;
            case FUNC_PAY: {
                if (paier == null) {
                    paier = new Paier();
                }
                result = paier.pay(intent);
            }
            break;
        }
        return result;
    }

    public static IWXAPI initCore(Context context, String appid) {
        IWXAPI wxapi = (IWXAPI) coreAPI.get(Robber.PROVIDER_WECHAT);
        if (wxapi == null) {
            wxapi = WXAPIFactory.createWXAPI(context, appid, false);
            wxapi.registerApp(appid);
            coreAPI.put(Robber.PROVIDER_WECHAT, wxapi);
        }
        return wxapi;
    }

    public static Tencent initCore(Context context, long appid) {
        Tencent qqapi = (Tencent) coreAPI.get(Robber.PROVIDER_QQCHAT);
        if (qqapi == null) {
            qqapi = Tencent.createInstance(Long.toString(appid), context);
            coreAPI.put(Robber.PROVIDER_QQCHAT, qqapi);
        }
        return qqapi;
    }
}