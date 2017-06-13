package com.yunqi.clientandroid.utils;

import android.content.Context;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 用户信息工具类
 * @date 15/12/22
 */
public class UserUtil {

	/**
	 * 保存userId
	 * 
	 * @param context
	 * @param userId
	 */
	public static void setUserId(Context context, String userId) {
		PreManager preManager = PreManager.instance(context);
		preManager.setUserId(userId);
		MiPushUtil.miPushUserAccount(context, userId);
	}

	/**
	 * 清空userId
	 * 
	 * @param context
	 */
	public static void unSetUserId(Context context) {
		PreManager preManager = PreManager.instance(context);
		MiPushUtil.unSetmiPushUserAccount(context, preManager.getUserId() + "");
		preManager.setUserId("0");
	}
}
