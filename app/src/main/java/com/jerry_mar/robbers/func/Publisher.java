package com.jerry_mar.robbers.func;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jerry_mar.robbers.Intent;
import com.jerry_mar.robbers.Robber;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class Publisher {
    public String share(Intent intent) {
        String result = null;
        int provider = intent.getExtra(Intent.PROVIDER, Robber.PROVIDER_WECHAT);
        switch (provider) {
            case Robber.PROVIDER_WECHAT : {
                IWXAPI wxapi = Robber.initCore(intent.getContext(), intent.getAppid());
                if (wxapi.isWXAppInstalled()) {
                    share(intent, wxapi);
                } else {
                    result = "您还未安装微信客户端!";
                }
            }
            break;
        }
        return result;
    }

    private void share(Intent intent, IWXAPI wxapi) {
        int extra = intent.getExtra(Intent.EXTRA, 0);
        WXMediaMessage msg = null;
        String type = null;
        switch (extra & 0xF0) {
            case Robber.WECHAT_SHARE_TYPE_TEXT : {
                WXTextObject textObj = new WXTextObject();
                textObj.text = intent.getExtra(Intent.CONTENT);
                msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                msg.description = textObj.text;
                type = "textshare";
            }
            break;
            case Robber.WECHAT_SHARE_TYPE_IMAGE : {
                Object thumb = intent.getExtra(Intent.THUMB);
                Bitmap bitmap;
                if (thumb instanceof Integer) {
                    bitmap = BitmapFactory.decodeResource(intent.getContext().getResources(), (int) thumb);
                } else {
                    bitmap = (Bitmap) thumb;
                }
                WXImageObject imgObj = new WXImageObject(bitmap);
                msg = new WXMediaMessage();
                msg.mediaObject = imgObj;
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                bitmap.recycle();
                msg.setThumbImage(thumbBitmap);
                type = "imgshareappdata";
            }
            break;
            case Robber.WECHAT_SHARE_TYPE_VIDEO : {
                type = "video";
            }
            case Robber.WECHAT_SHARE_TYPE_WEBPAGE : {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = intent.getExtra(Intent.URL);
                msg = new WXMediaMessage(webpage);
                msg.title = intent.getExtra(Intent.TITLE);
                msg.description = intent.getExtra(Intent.CONTENT);

                Object thumb = intent.getExtra(Intent.THUMB);
                Bitmap bitmap;
                if (thumb instanceof Integer) {
                    bitmap = BitmapFactory.decodeResource(intent.getContext().getResources(), (int) thumb);
                } else {
                    bitmap = (Bitmap) thumb;
                }
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                bitmap.recycle();
                msg.setThumbImage(thumbBitmap);
                type = type == null ? "webpage" : type;
            }
            break;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(type);
        req.message = msg;
        req.scene = extra & 0x0F;
        wxapi.sendReq(req);
    }

    static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
