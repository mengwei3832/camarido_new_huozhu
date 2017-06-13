package com.yunqi.clientandroid.employer.util;

import android.content.Context;
import android.text.TextUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengwei on 2017/1/3.
 */

public class SyncCookie {
    private Context context;
    private PreManager preManager;

    public SyncCookie(Context context) {
        super();
        this.context = context;
        preManager = PreManager.instance(context);
    }

    public Map<String, String> syncCookie(String url){
        //获取cookies
        String cookies = preManager.getToken();
        try {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            L.e("---------保存mCookies------------"+cookies);
            String cookieString = null;
            if (cookies != null && !TextUtils.isEmpty(cookies)){
                cookieString = String.format("YQ_API_TOKEN=%s", cookies);
            }

            cookieManager.setCookie(url, cookieString);
            CookieSyncManager.getInstance().sync();
            Map<String, String> abc = new HashMap<String, String>();
            L.e("---------应用cookieString------------"+cookieString);
            abc.put("Cookie", cookieString);
            return abc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
