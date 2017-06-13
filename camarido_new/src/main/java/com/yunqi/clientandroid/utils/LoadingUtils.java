package com.yunqi.clientandroid.utils;

import com.yunqi.clientandroid.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @Description:进度工具
 * @ClassName: ViewUtils
 * @author: chengtao
 * @date: Jul 18, 2016 5:54:44 PM
 * 
 */
public class LoadingUtils {
	private AlertDialog dialog;

	public LoadingUtils(Context context) {
		super();
		init(context);
	}

	private void init(Context context) {
		if (context != null) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_progress_utils, null);
			AlertDialog.Builder builder = new Builder(context);
			dialog = builder.create();
			dialog.setView(view);
			dialog.setCancelable(false);
		}
	}

	public void showDialog() {
		if (dialog != null) {
			dialog.show();
		} else {
			return;
		}
	}

	public void hideDialog() {
		if (dialog != null) {
			dialog.dismiss();
		} else {
			return;
		}
	}
}
