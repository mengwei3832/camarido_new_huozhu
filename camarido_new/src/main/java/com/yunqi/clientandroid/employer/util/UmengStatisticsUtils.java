package com.yunqi.clientandroid.employer.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 友盟统计帮助类
 * Created by mengwei on 2017/4/13.
 */

public class UmengStatisticsUtils {
    private static UmengStatisticsUtils mUmengStatisticsUtils;
    private Context mContext;

    public UmengStatisticsUtils(Context context) {
        mContext = context;
    }

    public static synchronized UmengStatisticsUtils instance(Context context) {
        if (mUmengStatisticsUtils == null) {
            synchronized (UmengStatisticsUtils.class) {
                if (null == mUmengStatisticsUtils) {
                    mUmengStatisticsUtils = new UmengStatisticsUtils(context);
                }
            }
        }
        return mUmengStatisticsUtils;
    }

    /**
     * 不添加属性的计算事件统计
     *
     * @param eventId 事件Id
     */
    public void setCalculateEvents(String eventId) {
        String str = HostPkgUtil.getApiHost();
        if (str == "http://pkgapi.yqtms.com/" || str.equals("http://pkgapi.yqtms.com/")) {
            Map<String, String> map_value = new HashMap<>();
            MobclickAgent.onEventValue(mContext, eventId, map_value, 1);
        }
    }

    /**
     * 添加属性的计算事件统计
     *
     * @param eventId 事件Id
     * @param map     当前事件的属性和取值
     */
    public void setCalculateEvents(String eventId, Map<String, String> map) {
        String str = HostPkgUtil.getApiHost();
        if (str == "http://pkgapi.yqtms.com/" || str.equals("http://pkgapi.yqtms.com/")) {
            MobclickAgent.onEventValue(mContext, eventId, map, 1);
        }
    }
}
