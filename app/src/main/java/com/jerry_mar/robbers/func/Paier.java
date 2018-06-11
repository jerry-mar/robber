package com.jerry_mar.robbers.func;

import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.jerry_mar.robbers.Intent;
import com.jerry_mar.robbers.Robber;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Map;

public class Paier {
    private String resultStatus;
    private String result;
    private String memo;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String packageValue;
    private String sign;
    private String extData;

    public String pay(final Intent intent) {
        String result = null;
        int provider = intent.getExtra(Intent.PROVIDER, Robber.PROVIDER_WECHAT);
        switch (provider) {
            case Robber.PROVIDER_ALI : {
                new Thread(){
                    @Override
                    public void run() {
                        Message message = intent.getHandler().obtainMessage();
                        try {
                            PayTask task = new PayTask(intent.getActivity());
                            Paier paier = new Paier(task.payV2(intent.getAppid(), true));
                            int resultStatus = paier.getResultStatus();
                            switch (resultStatus) {
                                case 9000: {
                                    message.obj = "支付成功";
                                }
                                break;
                                case 8000: {
                                    message.obj = "支付结果确认中";
                                }
                                break;
                                default: {
                                    throw new RuntimeException();
                                }
                            }
                        } catch (Exception e) {
                            message.obj = "支付失败或取消";
                        }
                        message.what = Robber.PROVIDER_ALI;
                        intent.getHandler().sendMessage(message);
                    }
                }.start();
            }
            break;
            case Robber.PROVIDER_WECHAT : {
                IWXAPI wxapi = Robber.initCore(intent.getContext(), intent.getAppid());
                if (wxapi.isWXAppInstalled()) {
                    PayReq request = new PayReq();
                    Paier paier = intent.getPaier();
                    request.appId = paier.getAppId();
                    request.partnerId = paier.getPartnerId();
                    request.prepayId = paier.getPrepayId();
                    request.nonceStr = paier.getNonceStr();
                    request.timeStamp = paier.getTimeStamp();
                    request.packageValue = paier.getPackageValue();
                    request.sign = paier.getSign();
                    request.extData = paier.getExtData();
                    wxapi.sendReq(request);
                } else {
                    result = "您还未安装微信客户端!";
                }
            }
            break;
        }
        return result;
    }

    public Paier() {}

    public Paier(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public int getResultStatus() {
        return Integer.parseInt(resultStatus);
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}
