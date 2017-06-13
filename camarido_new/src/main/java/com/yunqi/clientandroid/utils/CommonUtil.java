package com.yunqi.clientandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;

import com.xiaomi.xmpush.thrift.t;
import com.yunqi.clientandroid.CamaridoApp;

/**
 * 一些琐碎的方法
 * 
 * @author chenrenyi
 * 
 */
public class CommonUtil {
	private CommonUtil() {
		throw new UnsupportedOperationException(
				"This class cannot be instantiated");
	}

	/**
	 * 讲一个dp数值转换成px值
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(int dp) {
		return (int) (0.5F + CamaridoApp.getContext().getResources()
				.getDisplayMetrics().density
				* dp);
	}

	/**
	 * 在主线程中执行方法
	 * 
	 * @param r
	 */
	public static void runOnUiThread(Runnable r) {
		CamaridoApp.getHandler().post(r);
	}

	/**
	 * 获取渠道号
	 * 
	 * @param context
	 * @return
	 */
	public static String getChannel(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo appInfo = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString("channel");
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		return "";
	}

	/**
	 * 获取当前应用程序的版本号
	 */
	public static int getVersionCode() {
		// 包的管理者，获取应用程序清单文件中的所有信息
		PackageManager packageManager = CamaridoApp.getContext()
				.getPackageManager();// 获取包的管理者
		try {
			// getPackageName() : 获取应用程序的包名
			PackageInfo packageInfo = packageManager.getPackageInfo(CamaridoApp
					.getContext().getPackageName(), 0);
			int versionCode = packageInfo.versionCode;// 获取应用程序的版本号
			return versionCode;
		} catch (NameNotFoundException e) {
			// 找不到包名的异常
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * 获取应用程序的版本号
	 */
	public String getVersionName() {
		// 包的管理者，获取应用程序清单文件中的所有信息
		PackageManager packageManager = CamaridoApp.getContext()
				.getPackageManager();// 获取包的管理者
		// 获取包的所有信息
		// packageName : 应用程序包名
		// flags ： 指定的信息
		try {
			// getPackageName() : 获取应用程序的包名
			PackageInfo packageInfo = packageManager.getPackageInfo(CamaridoApp
					.getContext().getPackageName(), 0);
			String versionName = packageInfo.versionName;// 获取应用程序的版本号
			return versionName;
		} catch (NameNotFoundException e) {
			// 找不到包名的异常
			e.printStackTrace();
		}
		return null;
	}

	// 拨打电话号码
	public static void callPhoneIndirect(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phoneNumber));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @Description:设置控件是否可点击
	 * @Title:viewEnable
	 * @param isEnable
	 *            是否可点击
	 * @param vies
	 *            View组，直接填写要设置的控件，此处为多个参数
	 * @return:void
	 * @throws
	 * @Create: Jul 12, 2016 4:08:24 PM
	 * @Author : chengtao
	 */
	public static <T extends View> void viewEnable(boolean isEnable, T... vies) {
		if (isEnable) {// 可点击
			for (T t : vies) {
				t.setEnabled(true);
			}
		} else {
			for (T t : vies) {
				t.setEnabled(false);
			}
		}
	}
}
