package com.yunqi.clientandroid.utils;

import android.app.Dialog;
import android.content.Context;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.view.LoadingDialog;

public class ShowLoadUtils {
	private static Dialog dialog;

	public static void showLoading(Context context, boolean show) {

		try {
			if (show) {
				// 由于这个dialog可能是由不同的activity唤起，所以每次都新建
				if (dialog == null) {
					dialog = new LoadingDialog(context, R.style.lodingDialog);

				}
				try {
					dialog.show();
				} catch (Exception e) {
					LogHelper.print("==dialog Exception" + e.getMessage());
					e.printStackTrace();
				}
			} else {
				if (dialog != null && dialog.isShowing()) {
					dialog.cancel();
					dialog = null;
				}
			}
		} catch (Exception e) {

		}
	}
}
