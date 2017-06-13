package com.yunqi.clientandroid.activity;

import java.util.List;

import android.util.Log;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.LoginRequest;
import com.yunqi.clientandroid.http.response.Response;

public class TestActivity extends BaseActivity {

	@Override
	protected int getLayoutId() {
		return R.layout.test_activity;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		LoginRequest request = new LoginRequest(mContext, "", "");
		request.setRequestId(100);
		httpPost(new LoginRequest(mContext, "", ""));
	}

	@Override
	protected void setListener() {

	}

	@Override
	public void onStart(int requestId) {
		switch (requestId) {
		case 100:

			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		switch (requestId) {
		case 100:

			break;

		default:
			break;
		}
	}
}
