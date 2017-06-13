package com.yunqi.clientandroid.utils;

/**
 * 
 * @Description:class 发包方支付时多次点击时进行判断
 * @ClassName: PayTimeUtils
 * @author: zhm
 * @date: 2016-4-28 上午9:48:10
 * 
 */
public class PayTimeUtils {
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 5000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public synchronized static boolean isFastClickQuote() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1800000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
