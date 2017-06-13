package com.yunqi.clientandroid.employer.util.interfaces;

/**
 * 调用预设价格接口
 * Created by mengwei on 2017/5/9.
 */

public interface YuShePriceSure {
    void onNextRequest(String packageId,
                       String mPackageBeginName,
                       String beginCity, String beginCounty, String mPackageEndName, String endCity,
                       String endCounty, String dateTime, int mInsuranceType, int checkId,
                       String mBeforeExcute, String mOnTheWayCount,
                       String mOrderBeforeSettleCount, String mOrderSettledCount,
                       String mPrice, String mPackageWeight);
}
