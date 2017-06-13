package com.yunqi.clientandroid.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.yunqi.clientandroid.R;

public class LoadingDialog extends Dialog {

	Context context;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_loading);
	}

}
