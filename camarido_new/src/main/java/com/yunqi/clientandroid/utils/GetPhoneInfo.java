package com.yunqi.clientandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 
 * @Description:class 获取手机信息
 * @ClassName: GetPhoneInfo
 * @author: zhm
 * @date: 2016-5-12 下午2:08:15
 * 
 */
public class GetPhoneInfo {
	private TelephonyManager tm;
	private PackageInfo info;
	private Activity activity;

	public GetPhoneInfo(Activity activity) throws Throwable {
		super();
		this.activity = activity;
		tm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		info = activity.getPackageManager().getPackageInfo(
				activity.getPackageName(), 0);
	}

	// 获取手机IMEI
	public String getPhoneIMEI() {
		return tm.getDeviceId();
	}

	// 获取手机IMSI
	public String getPhoneIMSI() {
		return tm.getSubscriberId();
	}

	// 获取Apk的版本号
	public String getAppVersion() {
		return info.versionName;
	}
}
