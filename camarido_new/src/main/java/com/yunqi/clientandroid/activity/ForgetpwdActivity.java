package com.yunqi.clientandroid.activity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.entity.PushInfo;
import com.yunqi.clientandroid.http.request.GetPushInfoRequest;
import com.yunqi.clientandroid.http.request.LoginRequest;
import com.yunqi.clientandroid.http.request.ResetPwdCodeRequest;
import com.yunqi.clientandroid.http.request.ResetPwdRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.*;

/**
 * @deprecated:忘记密码
 */
public class ForgetpwdActivity extends BaseActivity implements OnClickListener {
	private ImageView mIvDetele1;
	private ImageView mIvDetele2;
	private TextView mTvConfirmlogin;
	private EditText mEtPwd;
	private EditText mEtPhoneNumber;
	private EditText mEtValidationCode;
	private Button mBtnGetCode;
	private Button mBtnReset;
	private Handler handler;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	private String beforePhone;
	private String strContent;
	private String userName;
	private String password;
	private AlertDialog alertDialog;
	// 匹配短信中间的4个数字（验证码等）
	private String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
	// 请求登录成功后服务器返回的token
	private final String TOKENVALUE = "TokenValue";
	private final String USER_NAME = "USER_NAME";
	private final String PASSWORD = "PASSWORD";

	// 本页请求
	private LoginRequest mLoginRequest;
	private ResetPwdRequest mResetPwdCodeRequest;
	private ResetPwdCodeRequest mForgetPwdCodeRequest;
	private GetPushInfoRequest mGetPushInfoRequest;
	// 本页请求id
	private final int LOGIN_REQUEST = 9;
	private final int RESET_PWD_REQUEST = 10;
	private final int FORGET_PWD_REQUEST = 11;
	private final int GET_PUSH_INFO_REQUEST = 12;

	// 初始化handler的方法
	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!TextUtils.isEmpty(strContent)) {
					mEtValidationCode.setText(strContent);
				}
			}
		};

		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					Log.d("logo", "message     " + message);
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					Log.d("logo", "from     " + from);
					// Time time = new Time();
					// time.set(sms.getTimestampMillis());
					// String time2 = time.format3339(true);
					// Log.d("logo", from + "   " + message + "  " + time2);
					// strContent = from + "   " + message;
					// handler.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		registerReceiver(smsReceiver, filter);
	}

	/**
	 * 匹配短信中间的4个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_yunqiforgetpwd;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();
		// 获取从别的页面传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("phone")) {
			beforePhone = bundle.getString("phone");
		}
		mEtPhoneNumber = (EditText) findViewById(R.id.et_codelogin_number);
		mEtValidationCode = (EditText) findViewById(R.id.et_codelogin_verificationcode);
		mBtnGetCode = (Button) findViewById(R.id.bt_codelogin_verification);
		mBtnReset = (Button) findViewById(R.id.bt_codelogin_agreed);
		mIvDetele1 = (ImageView) findViewById(R.id.iv_codelogin_delete1);
		mEtPwd = (EditText) findViewById(R.id.et_codelogin_pwd);
		mIvDetele2 = (ImageView) findViewById(R.id.iv_codelogin_delete2);

		// 设置电话号码
		if (!TextUtils.isEmpty(beforePhone) && beforePhone != null) {
			mEtPhoneNumber.setText(beforePhone);
		}
	}

	@Override
	protected void initData() {
		// 初始化handler
		initHandler();
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();

		// EditText输入框监听
		initEditTextListener();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mBtnGetCode.setOnClickListener(this);
		mBtnReset.setOnClickListener(this);
		mIvDetele1.setOnClickListener(this);
		mIvDetele2.setOnClickListener(this);

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.forgetpwd));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ForgetpwdActivity.this.finish();
			}
		});

	}

	// EditText输入框监听的方法
	private void initEditTextListener() {
		// 监听电话号码输入框
		mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 电话号码输入框有内容的时候
				setNumberChangeEditText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable enable) {
			}
		});

		// 监听密码输入框
		mEtPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 密码输入框有内容的时候
				setPwdChangeEditText();
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

	// 密码输入框有内容的时候UI变化的方法
	protected void setPwdChangeEditText() {
		if (mEtPwd.getText().toString().length() != 0 && mEtPwd.isFocused()) {
			mIvDetele2.setVisibility(View.VISIBLE);
		} else if (mEtPwd.getText().toString().length() == 0
				&& mEtPhoneNumber.getText().toString().length() != 0) {
			mIvDetele2.setVisibility(View.GONE);
		} else {
			mIvDetele2.setVisibility(View.GONE);
		}
	}

	// 电话号码输入框有内容的时候UI变化的方法
	protected void setNumberChangeEditText() {
		if (mEtPhoneNumber.getText().toString().length() != 0
				&& mEtPhoneNumber.isFocused()) {
			mIvDetele1.setVisibility(View.VISIBLE);
		} else {
			mIvDetele1.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_codelogin_delete1:
			// 用户名输入框删除按钮
			mEtPhoneNumber.setText("");
			break;
		case R.id.iv_codelogin_delete2:
			// 密码输入框删除按钮
			mEtPwd.setText("");

		case R.id.bt_codelogin_verification:
			// 点击按钮获取验证码
			requestCode();
			break;

		case R.id.bt_codelogin_agreed:
			// 请求重置密码的方法
			passwordRequest();
			break;

		default:
			break;
		}
	}

	// 请求验证码的方法
	private void requestCode() {
		userName = mEtPhoneNumber.getText().toString().trim();
		if (TextUtils.isEmpty(userName)) {
			showToast("请输入手机号码");
			return;
		}

		// 检测手机号码
		if (!userName.matches("^[1][3-8][0-9]{9}$")) {
			showToast("请输入正确的手机号码");
			return;
		}



		// 请求服务器获取短信验证码
		mForgetPwdCodeRequest = new ResetPwdCodeRequest(this, userName);
		mForgetPwdCodeRequest.setRequestId(FORGET_PWD_REQUEST);
		httpPost(mForgetPwdCodeRequest);
	}

	// 请求重置密码的方法
	private void passwordRequest() {
		userName = mEtPhoneNumber.getText().toString().trim();
		String shortMsg = mEtValidationCode.getText().toString().trim();
		password = mEtPwd.getText().toString().trim();

		if (TextUtils.isEmpty(userName)) {
			showToast("请输入手机号码");
			return;
		}

		// 检测手机号码
		if (!userName.matches("^[1][3-8][0-9]{9}$")) {
			showToast("请输入正确的手机号码");
			return;
		}

		if (TextUtils.isEmpty(shortMsg)) {
			showToast("请输入验证码");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			showToast("请输入密码");
			return;
		}

		// 检测密码
		if (!password.matches("^[0-9a-zA-Z]{6,14}$")) {
			showToast("密码必须为6~14位数字或字母,请重新输入");
			return;
		}

		// MD5后的密码
		final String md5Password = StringUtils.md5(password);

		// 请求服务器
		mResetPwdCodeRequest = new ResetPwdRequest(this, userName, md5Password,
				shortMsg);
		mResetPwdCodeRequest.setRequestId(RESET_PWD_REQUEST);
		httpPost(mResetPwdCodeRequest);
		// 设置按钮不可点击
		setViewEnable(false);

	}

	// 确认登录对话框
	private void confirmLogin(final String userName, final String password) {
		AlertDialog.Builder builder = new Builder(this);
		// 设置对话框不能被取消
		builder.setCancelable(false);

		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_confirmlogin, null);
		mTvConfirmlogin = (TextView) view
				.findViewById(R.id.tv_diarep_confirmlogin);
		mTvConfirmlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击确认登录请求服务器进行登录
				if (TextUtils.isEmpty(userName)) {
					showToast("请输入手机号码");
					return;
				}

				// 检测手机号码
				if (!userName.matches("^[1][3-8][0-9]{9}$")) {
					showToast("请输入正确的手机号码");
					return;
				}

				if (TextUtils.isEmpty(password)) {
					showToast("请输入密码");
					return;
				}

				// 检测密码
				if (!password.matches("^[0-9a-zA-Z]{6,14}$")) {
					showToast("密码必须为6~14位数字或字母,请重新输入");
					return;
				}
				// MD5后的密码
				final String md5Password = StringUtils.md5(password);

				mLoginRequest = new LoginRequest(ForgetpwdActivity.this,
						userName, md5Password);
				mLoginRequest.setRequestId(LOGIN_REQUEST);
				httpPost(mLoginRequest);

			}
		});

		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	// 设置按钮不可重复点击的方法
	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			mBtnReset.setText("重置密码");
		} else {
			mBtnReset.setText("重置密码中...");
		}
		mBtnReset.setEnabled(bEnable);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case LOGIN_REQUEST:
			// 请求服务器成功
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 登录成功
				LoginInfo loginInfo = (LoginInfo) response.singleData;
				String tokenValue = loginInfo.tokenValue;
				String userType = loginInfo.userType;

				// 保存登录返回的token到SP
				CacheUtils.putString(getApplicationContext(), TOKENVALUE,
						tokenValue);
				CacheUtils.putString(getApplicationContext(), "USER_TYPE",
						userType);
				// 登录成功后保存账号密码到SP
				CacheUtils.putString(getApplicationContext(), USER_NAME,
						userName);
				CacheUtils.putString(getApplicationContext(), PASSWORD,
						password);
				// 保存token的过期时间
				PreManager.instance(this).setTokenExpires(
						loginInfo.tokenExpires);
				// TODO --登录成功后关闭当前登录界面还需要关闭登录界面
				CamaridoApp.destoryActivity("LoginActivity");

				if (!TextUtils.isEmpty(userType) && userType != null) {
					// 保存用户类型
					if (userType.equals("1")) {
						// 登录成功跳转到主界面
						MainActivity.invoke(ForgetpwdActivity.this);
					} else if (userType.equals("2")) {
						// 跳转到发包方当前订单界面
						EmployerMainActivity.invoke(ForgetpwdActivity.this);
					}
				} else {
					// 登录成功跳转到主界面
					MainActivity.invoke(ForgetpwdActivity.this);
				}

				// 消除对话框
				alertDialog.dismiss();

				mGetPushInfoRequest = new GetPushInfoRequest(
						ForgetpwdActivity.this);
				mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
				httpPost(mGetPushInfoRequest);
			} else {
				// 登录失败
				alertDialog.dismiss();
			}
			ForgetpwdActivity.this.finish();

			break;
		case RESET_PWD_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 重置密码成功后跳转到登录对话框
				// confirmLogin(userName, password);
				LoginActicity.invoke(ForgetpwdActivity.this);
				finish();
			} else {
				// 重置密码失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 设置重置按钮可点击
			setViewEnable(true);
			break;
		case FORGET_PWD_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess){
				showToast(message);
				// 获取短信倒计时
				MyCountTimer countTimer = new MyCountTimer(mBtnGetCode);
				countTimer.start();
			} else {
				showToast(message);
			}
			break;

		case GET_PUSH_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				List<PushInfo> pushInfos = response.data;
				for (PushInfo pushInfo : pushInfos) {
					if (pushInfo.pushDataType == 1) {
						MiPushUtil.setMiPushTopic(ForgetpwdActivity.this,
								pushInfo.receiverMark);
					}
				}
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
		switch (requestId) {
		case RESET_PWD_REQUEST:
			// 设置重置按钮可点击
			setViewEnable(true);
			break;
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, String phone) {
		Intent intent = new Intent(context, ForgetpwdActivity.class);
		intent.putExtra("phone", phone);
		context.startActivity(intent);
	}

}
