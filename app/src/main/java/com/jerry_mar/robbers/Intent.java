package com.jerry_mar.robbers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.util.ArrayMap;

import com.jerry_mar.robbers.func.Paier;

import java.util.Map;

public class Intent {
    public static final String PROVIDER = "robber_provider";
    public static final String FUNC = "robber_function";
    public static final String EXTRA = "robber_extra";
    public static final String TITLE = "robber_title";
    public static final String CONTENT = "robber_content";
    public static final String THUMB = "robber_thumb";
    public static final String URL = "robber_url";

    private Context context;
    private Activity activity;
    private Handler handler;
    private String appid;
    private Paier paier;
    private Map<String, Object> bundle;

    public Intent(Context context, String appid) {
        this.context = context;
        this.appid = appid;
    }

    public Intent(Context context, String appid, Paier paier) {
        this.context = context;
        this.appid = appid;
        this.paier = paier;
    }

    public Intent(Activity activity, Handler handler, String appid) {
        this.activity = activity;
        this.handler = handler;
        this.appid = appid;
    }

    public Intent putExtra(String key, Object value) {
        if (bundle == null) {
            bundle = new ArrayMap<>();
        }
        bundle.put(key, value);
        return this;
    }

    public <T> T getExtra(String key) {
        return getExtra(key, null);
    }

    public <T> T getExtra(String key, T defValue) {
        T result = (T) bundle.get(key);
        if (result == null)
            result = defValue;
        return result;
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public Handler getHandler() {
        return handler;
    }

    public Paier getPaier() {
        return paier;
    }

    public String getAppid() {
        return appid;
    }
}
