package com.yunqi.clientandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.yunqi.clientandroid.R;

public class HomeActivity extends BaseActivity implements OnClickListener {

	@Override
	protected int getLayoutId() {
		return R.layout.home_activity;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {

	}

	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, HomeActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {

	}

}