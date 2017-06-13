package com.yunqi.clientandroid.utils;

import android.util.Log;

/**
 * 打印log帮助类
 * 
 * @author user
 */
public class LogHelper {

	private static boolean isTemTest = true;

	public static void print(String msg) {
		if (ConstData.DEBUG || ConstData.TEST || isTemTest)
			System.out.println(msg);
	}

	public static void e(String tag, String msg) {
		if (ConstData.DEBUG || ConstData.TEST)
			Log.e(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (ConstData.DEBUG || ConstData.TEST)
			Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (ConstData.DEBUG || ConstData.TEST)
			Log.i(tag, tag);
	}

	public static void w(String tag, String msg) {
		if (ConstData.DEBUG || ConstData.TEST)
			Log.w(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (ConstData.DEBUG || ConstData.TEST)
			Log.v(tag, msg);
	}
}
