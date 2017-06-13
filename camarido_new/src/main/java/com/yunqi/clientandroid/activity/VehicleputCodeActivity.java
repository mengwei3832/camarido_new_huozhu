package com.yunqi.clientandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.CarPutCodeRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @deprecated:车辆列表的邀请码页面
 */
public class VehicleputCodeActivity extends BaseActivity implements
		OnClickListener {

	private EditText mEtInputCode;
	private ImageView mIvDelete1;
	private Button mBtnValidation;
	private String inviteCode;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_yunqicarputcode;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();
		// 初始化控件
		mEtInputCode = (EditText) findViewById(R.id.et_carputcode_invitationcode);
		mIvDelete1 = (ImageView) findViewById(R.id.iv_carputcode_delete1);
		mBtnValidation = (Button) findViewById(R.id.bt_carputcode_validation);

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
		// 初始化EditText
		initEditTextListener();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.et_carinputcode_invitationcode));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VehicleputCodeActivity.this.finish();
			}
		});

	}

	// 初始化点击事件
	private void initOnClick() {
		mIvDelete1.setOnClickListener(this);
		mBtnValidation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_carputcode_delete1:
			// 邀请码输入框的删除按钮
			mEtInputCode.setText("");
			break;

		case R.id.bt_carputcode_validation:
			// 邀请码验证
			inviteCode = mEtInputCode.getText().toString().trim();

			if (TextUtils.isEmpty(inviteCode)) {
				showToast("请输入邀请码");
				return;
			}

			// 访问服务器验证邀请码
			addVehicleByInviteCode(inviteCode);

			// 设置验证按钮不可点击
			setViewEnable(false);

			break;

		default:
			break;
		}

	}

	// 访问服务器提交要求码
	private void addVehicleByInviteCode(String inviteCode) {
		// TODO--httpPost(new CarPutCodeRequest(this, inviteCode));
	}

	// 输入框有内容监听
	private void initEditTextListener() {
		mEtInputCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
				setCarCodeChangeEditText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable enable) {
			}
		});
	}

	// 输入框有内容时执行的方法
	protected void setCarCodeChangeEditText() {
		if (mEtInputCode.getText().toString().length() != 0
				&& mEtInputCode.isFocused()) {
			mIvDelete1.setVisibility(View.VISIBLE);
		} else {
			mIvDelete1.setVisibility(View.GONE);
		}
	}

	// 设置按钮不可重复点击的方法
	private void setViewEnable(boolean bEnable) {
		mBtnValidation.setEnabled(bEnable);
	}

	@Override
	public void onSuccess(int requestId, Response response) {

		boolean isSuccess = response.isSuccess;
		String message = response.message;

		if (isSuccess) {
			// 成功成为司机
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}

			// 结束当前页面
			VehicleputCodeActivity.this.finish();

		} else {
			// 不成功
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}

		// 设置验证按钮可点击
		setViewEnable(true);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		// 设置验证按钮可点击
		setViewEnable(true);
		showToast(this.getResources().getString(R.string.net_error_toast));

	}

	/**
	 * 输入邀请码界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, VehicleputCodeActivity.class);
		activity.startActivity(intent);

	}

}
