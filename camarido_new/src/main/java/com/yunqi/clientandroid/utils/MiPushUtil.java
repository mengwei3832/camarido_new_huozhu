package com.yunqi.clientandroid.utils;

import android.app.ActivityManager;
import android.content.Context;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 小米推送工具类
 * @date 15/12/14
 */
public class MiPushUtil {

	public static final String MIPUSH_APP_ID = "2882303761517419004";
	public static final String MIPUSH_APP_KEY = "5981741944004";

	/**
	 * 初始化小米推送
	 * 
	 * @param context
	 */
	public static void initPush(Context context) {
		// 初始化小米推送
		if (shouldInit(context)) {
			MiPushClient.registerPush(context, MIPUSH_APP_ID, MIPUSH_APP_KEY);
		}

		String id = MiPushClient.getRegId(context);

		L.e("--------RegId------------"+id);
	}

	/**
	 * 是否是该进程
	 * 
	 * @return
	 */
	private static boolean shouldInit(Context context) {
		ActivityManager am = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am
				.getRunningAppProcesses();
		String mainProcessName = context.getPackageName();
		int myPid = android.os.Process.myPid();
		for (ActivityManager.RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置小米推送的userAccount
	 * 
	 * @param context
	 * @param userAccount
	 */
	public static void miPushUserAccount(Context context, String userAccount) {
		MiPushClient.setUserAccount(context, userAccount, null);
	}

	/**
	 * 取消设置小米推送的userAccount
	 * 
	 * @param context
	 * @param userAccount
	 */
	public static void unSetmiPushUserAccount(Context context,
			String userAccount) {
		MiPushClient.unsetUserAccount(context, userAccount, null);
	}

	/**
	 * 设置小米推送的topic
	 * 
	 * @param context
	 * @param topic
	 */
	public static void setMiPushTopic(Context context, String topic) {
		MiPushClient.subscribe(context, topic, null);
	}

	/**
	 * 取消设置小米推送的topic
	 * 
	 * @param context
	 * @param topic
	 */
	public static void setMiPushUnTopic(Context context, String topic) {
		MiPushClient.unsubscribe(context, topic, null);
	}

	/**
	 * 调用unregisterPush()之后，服务器不会向app发送任何消息。
	 * @param context
     */
	public static void unregisterPush(Context context){
		MiPushClient.unregisterPush(context);
	}
}
