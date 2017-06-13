package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.request.GetInviteCodeRequest;
import com.yunqi.clientandroid.http.request.ShareVehicleIdRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.umeng.share.ShareUtil;
import com.yunqi.clientandroid.umeng.share.SharelistHelper;

public class SendCodeActivity extends BaseActivity implements
		View.OnClickListener {

	private String vehicleId;// 车辆id
	private TextView mInvitation;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private Button btnInvite;
	private Share mShare;

	// 本页请求
	private GetInviteCodeRequest mGetInviteCodeRequest;
	private ShareVehicleIdRequest mShareVehicleIdRequest;

	// 本页请求id
	private final int GET_INVITE_CODE_REQUEST = 1;
	private final int SHARE_VEHICLE_REQUEST = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_sendcode;
	}

	// 初始化控件的方法
	@Override
	protected void initView() {
		// 初始化titileBar
		initActionbar();

		mInvitation = (TextView) findViewById(R.id.tv_sendcode_invitation);
		mLlGlobal = (LinearLayout) findViewById(R.id.ll_sendcode_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_sendcode_progress);
		btnInvite = obtainView(R.id.btn_sendcode_invitation);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从车辆列表传过来的车辆ID
		if (bundle != null && bundle.containsKey("vehicleId")) {
			vehicleId = bundle.getString("vehicleId");
		}
		// 访问服务器获取邀请码
		if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
			getServiceData(vehicleId);
		}

		mShareVehicleIdRequest = new ShareVehicleIdRequest(
				SendCodeActivity.this, vehicleId);
		mShareVehicleIdRequest.setRequestId(SHARE_VEHICLE_REQUEST);
		httpPost(mShareVehicleIdRequest);

	}

	@Override
	protected void setListener() {
		btnInvite.setOnClickListener(this);
	}

	// 初始化titileBar的方法
	private void initActionbar() {
		setActionBarTitle("发送邀请码");
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SendCodeActivity.this.finish();
			}
		});

	}

	// 从服务器获取邀请码
	private void getServiceData(String vechicleId) {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetInviteCodeRequest = new GetInviteCodeRequest(SendCodeActivity.this,
				vechicleId);
		mGetInviteCodeRequest.setRequestId(GET_INVITE_CODE_REQUEST);
		httpPost(mGetInviteCodeRequest);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_INVITE_CODE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取邀请码成功显示邀请码
				if (!TextUtils.isEmpty(message) && message != null) {
					mInvitation.setText("邀请码:" + message);
				}
			} else {
				// 获取邀请码失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;
		case SHARE_VEHICLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mShare = (Share) response.singleData;
			}
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sendcode_invitation:
			if (mShare != null) {
				ShareUtil.share(SendCodeActivity.this,
						SharelistHelper.FROM_TYPE_VEHICLE, mShare);
			} else {
				showToast("无法获取分享内容");
			}
			break;
		}
	}

	/**
	 * 发送邀请码界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Context activity, String vehicleId) {
		Intent intent = new Intent();
		intent.setClass(activity, SendCodeActivity.class);
		intent.putExtra("vehicleId", vehicleId);// 车辆id
		activity.startActivity(intent);
	}

}
