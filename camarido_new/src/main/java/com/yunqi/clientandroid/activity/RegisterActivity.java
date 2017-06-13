package com.yunqi.clientandroid.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentOrderActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyRenZhengActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.entity.PushInfo;
import com.yunqi.clientandroid.http.request.DecideCompanyMessageRequest;
import com.yunqi.clientandroid.http.request.GetPhoneInfoRequest;
import com.yunqi.clientandroid.http.request.GetPushInfoRequest;
import com.yunqi.clientandroid.http.request.LoginRequest;
import com.yunqi.clientandroid.http.request.RegisterCodeRequest;
import com.yunqi.clientandroid.http.request.RegisterOwnRequest;
import com.yunqi.clientandroid.http.request.RegisterPkgRequest;
import com.yunqi.clientandroid.http.request.RegisterRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.GetPhoneInfo;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.MyCountTimer;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @deprecated:注册
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private ImageView mIvDelete1;
	private ImageView mIvDelete2;
	private ImageView mIVDelete3;
	private EditText mEtNumber;
	private EditText mEtPwd;
	private EditText mEtInvitecode;
	private EditText mEtVerificationcode;
	private TextView mTvAgreement;
	private Button mBtnVerification;
	private Button mBtnAgreed;
	private Handler handler;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	private String userName;
	private String password;
	private String strContent;
//	private RadioGroup rgFigure; // 选择身份
	private RadioButton rbDiver, rbEmployer;

	private String mChooseIdentity = "2"; // 选择身份的标记
	private FilterManager filterManager;

	// 匹配短信中间的4个数字（验证码等）

	private String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
	// SP的key
	private final String TOKENVALUE = "TokenValue";
	private final String USER_NAME = "USER_NAME";
	private final String PASSWORD = "PASSWORD";
	private final String USER_TYPE = "USER_TYPE";
	public static final String ISTEMPUSER = "ISTEMPUSER";

	// 本页请求id
	private final int REGISTER_REQUEST = 12;
	private final int REGISTER_CODE_REQUEST = 13;
	private final int LOGIN_REQUEST = 14;
	private final int GET_PUSH_INFO_REQUEST = 15;
	private final int GET_PHONE_INFO = 16;
	private final int REGISTER_OWN_REQUEST = 17;
	private final int LOGIN_PKG = 18;
	private final int DECIDE_COMPANY_MESSAGE = 19;

	// 本页请求
	private DecideCompanyMessageRequest decideCompanyMessageRequest;
	private RegisterRequest mRegisterRequest;
	private RegisterCodeRequest mRegisterCodeRequest;
	private LoginRequest mLoginRequest;
	private GetPushInfoRequest mGetPushInfoRequest;
	private GetPhoneInfoRequest mGetPhoneInfoRequest;
	private RegisterOwnRequest mRegisterOwnRequest;
	private RegisterPkgRequest mRegisterPkgRequest;

	private GetPhoneInfo getPhoneInfo;

	//友盟统计
	private UmengStatisticsUtils mUmeng;

	private boolean isHidden = true;

	// 初始化handler的方法
	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!TextUtils.isEmpty(strContent)) {
					mEtVerificationcode.setText(strContent);
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
		return R.layout.fragment_driver_register;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		filterManager = FilterManager.instance(RegisterActivity.this);
		mUmeng = UmengStatisticsUtils.instance(mContext);

		try {
			getPhoneInfo = new GetPhoneInfo(RegisterActivity.this);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		mIvDelete1 = (ImageView) findViewById(R.id.iv_register_delete1);
		mIvDelete2 = (ImageView) findViewById(R.id.iv_register_delete2);
		mIVDelete3 = (ImageView) findViewById(R.id.iv_register_delete3);
		mEtNumber = (EditText) findViewById(R.id.et_register_number);
		mEtPwd = (EditText) findViewById(R.id.et_register_pwd);
		mEtInvitecode = (EditText) findViewById(R.id.et_register_invitecode);
		mEtVerificationcode = (EditText) findViewById(R.id.et_register_verificationcode);
		mBtnVerification = (Button) findViewById(R.id.bt_register_verification);
		mBtnAgreed = (Button) findViewById(R.id.bt_register_agreed);
		mTvAgreement = (TextView) findViewById(R.id.tv_register_agreement);
//		rgFigure = obtainView(R.id.rg_figure);
//		rbDiver = obtainView(R.id.rb_diver);
//		rbEmployer = obtainView(R.id.rb_employer);

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
		// 初始化EditText
		initEditTextListener();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.register));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mIvDelete1.setOnClickListener(this);
		mIvDelete2.setOnClickListener(this);
		mIVDelete3.setOnClickListener(this);
		mBtnVerification.setOnClickListener(this);
		mBtnAgreed.setOnClickListener(this);
		mTvAgreement.setOnClickListener(this);
//		rgFigure.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_register_delete1:
			// 用户名输入框删除按钮
			mEtNumber.setText("");
			break;
		case R.id.iv_register_delete2:
			// 密码输入框删除按钮
//			mEtPwd.setText("");
			if (isHidden){
				mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				mIvDelete2.setImageResource(R.drawable.password_show);
			} else {
				mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				mIvDelete2.setImageResource(R.drawable.password_hide);
			}
			isHidden = !isHidden;
			mEtPwd.postInvalidate();
			//切换后将EditText光标置于末尾
			CharSequence charSequence = mEtPwd.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}
			break;
		case R.id.iv_register_delete3:
			// 验证码输入框删除按钮
			mEtInvitecode.setText("");
			break;

		case R.id.bt_register_verification:
			// 点击按钮请求服务器获取验证码
			requestCode();
			break;
		case R.id.bt_register_agreed:
			userName = mEtNumber.getText().toString().trim();
			if (mChooseIdentity == null && TextUtils.isEmpty(mChooseIdentity)) {
				showToast("请选择身份");
			} else {
				//友盟统计
				Map<String,String> map_code = new HashMap<>();
				map_code.put("username",userName);
				mUmeng.setCalculateEvents("register_click",map_code);
				if (mChooseIdentity.equals("2")) {
					// 点击按钮请求服务器注册货主
					requestRegisterOwn();
				} else if (mChooseIdentity.equals("1")) {
					// 点击按钮请求服务器注册承运人
					requestRegister();
				}
			}

			break;

		case R.id.tv_register_agreement:
			// 跳转到注册协议界面
			RegisterMessageActivity.invoke(RegisterActivity.this);
			break;

		default:
			break;
		}
	}

	// 监听输入框内容的方法
	private void initEditTextListener() {
		mEtNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
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

		mEtPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
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

		mEtInvitecode.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
				setPwdRepeatChangeEditText();
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

	// 重复密码输入框有内容的时候UI变化的方法
	protected void setPwdRepeatChangeEditText() {
		if (mEtInvitecode.getText().toString().length() != 0
				&& mEtInvitecode.isFocused()) {
			mIVDelete3.setVisibility(View.VISIBLE);
		} else if (mEtInvitecode.getText().toString().length() == 0
				&& mEtNumber.getText().toString().length() != 0) {
			mIVDelete3.setVisibility(View.GONE);
		} else {
			mIVDelete3.setVisibility(View.GONE);
		}

	}

	// 密码输入框有内容的时候UI变化的方法
	protected void setPwdChangeEditText() {
		if (mEtPwd.getText().toString().length() != 0 && mEtPwd.isFocused()) {
			mIvDelete2.setVisibility(View.VISIBLE);
		} else if (mEtPwd.getText().toString().length() == 0
				&& mEtNumber.getText().toString().length() != 0) {
			mIvDelete2.setVisibility(View.GONE);
		} else {
			mIvDelete2.setVisibility(View.GONE);
		}
	}

	// 电话号码输入框有内容的时候UI变化的方法
	protected void setNumberChangeEditText() {
		if (mEtNumber.getText().toString().length() != 0
				&& mEtNumber.isFocused()) {
			mIvDelete1.setVisibility(View.VISIBLE);
		} else {
			mIvDelete1.setVisibility(View.GONE);
		}
	}

	/**
	 * @Description:请求服务器注册货主的方法
	 * @Title:requestRegisterOwn
	 * @return:void
	 * @throws
	 * @Create: 2016-6-1 下午2:40:16
	 * @Author :
	 */
	private void requestRegisterOwn() {
		userName = mEtNumber.getText().toString().trim();
		password = mEtPwd.getText().toString().trim();
		String inviteCode = mEtInvitecode.getText().toString().trim();
		String shortMsg = mEtVerificationcode.getText().toString().trim();

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

		if (TextUtils.isEmpty(shortMsg)) {
			showToast("请输入验证码");
			return;
		}

		// MD5后的密码
		final String md5Password = StringUtils.md5(password);

		// 请求服务器
		mRegisterOwnRequest = new RegisterOwnRequest(RegisterActivity.this,
				userName, md5Password, shortMsg, inviteCode);
		mRegisterOwnRequest.setRequestId(REGISTER_OWN_REQUEST);
		httpPost(mRegisterOwnRequest);

		// 设置注册按钮不可点击
		setViewEnable(false);
	}

	/**
	 * @Description:请求服务器注册承运人的方法
	 * @Title:requestRegister
	 * @return:void
	 * @throws
	 * @Create: 2016-6-1 下午2:39:18
	 * @Author : chengtao
	 */
	private void requestRegister() {
		password = mEtPwd.getText().toString().trim();
		String inviteCode = mEtInvitecode.getText().toString().trim();
		String shortMsg = mEtVerificationcode.getText().toString().trim();

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

		if (TextUtils.isEmpty(shortMsg)) {
			showToast("请输入验证码");
			return;
		}

		// MD5后的密码
		final String md5Password = StringUtils.md5(password);
		// 请求服务器
		mRegisterRequest = new RegisterRequest(RegisterActivity.this, userName,
				md5Password, shortMsg, inviteCode);
		mRegisterRequest.setRequestId(REGISTER_REQUEST);
		httpPost(mRegisterRequest);
		// 设置注册按钮不可点击
		setViewEnable(false);
	}

	// 请求服务器获取验证码的方法
	private void requestCode() {
		userName = mEtNumber.getText().toString().trim();
		if (TextUtils.isEmpty(userName)) {
			showToast("请输入手机号码");
			return;
		}

		// 检测手机号码
		if (!userName.matches("^[1][3-8][0-9]{9}$")) {
			showToast("请输入正确的手机号码");
			return;
		}

		// 验证码倒计时
		// MyCountTimer countTimer = new MyCountTimer(mBtnVerification);
		// countTimer.start();
		showProgressDialog("请稍候...");

		// 请求服务器
		mRegisterCodeRequest = new RegisterCodeRequest(RegisterActivity.this,
				userName);
		mRegisterCodeRequest.setRequestId(REGISTER_CODE_REQUEST);
		httpPost(mRegisterCodeRequest);

		//友盟统计
		Map<String,String> map_code = new HashMap<>();
		map_code.put("username",userName);
		mUmeng.setCalculateEvents("register_code",map_code);

		setViewEnableCode(false);
	}

	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			mBtnAgreed.setText("同意协议并注册");
		} else {
			mBtnAgreed.setText("注册中 ...");
		}
		mBtnAgreed.setEnabled(bEnable);
		// switch (view.getId()) {
		// case R.id.bt_register_agreed:
		// if (bEnable) {
		// mBtnAgreed.setText("同意协议并注册");
		// } else {
		// mBtnAgreed.setText("注册中 ...");
		// }
		// mBtnAgreed.setEnabled(bEnable);
		// break;
		// case R.id.bt_register_verification:
		// mBtnVerification.setEnabled(bEnable);
		// break;
		//
		// default:
		// break;
		// }
	}

	// 获取验证码按钮是否点击
	private void setViewEnableCode(boolean bEnable) {
		mBtnVerification.setEnabled(bEnable);
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
		case REGISTER_OWN_REQUEST: // 货主注册成功
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 注册成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				LoginInfo li = (LoginInfo) response.singleData;

				// 保存登录返回的token到SP
				// CacheUtils.putString(getApplicationContext(), TOKENVALUE,
				// li.tokenValue);

				// 获取手机的信息
				String pIMEI = getPhoneInfo.getPhoneIMEI();
				String pIMSI = getPhoneInfo.getPhoneIMSI();
				String aVersion = getPhoneInfo.getAppVersion().trim();
				int mActionType = 0;

				Log.e("TAG", "-------------IMEI------------------" + pIMEI);
				Log.e("TAG", "-------------IMSI------------------" + pIMSI);
				Log.e("TAG", "-------------AppVersion------------------"
						+ aVersion);

				// 获取手机信息的请求
				mGetPhoneInfoRequest = new GetPhoneInfoRequest(
						RegisterActivity.this, pIMEI, pIMSI, aVersion,
						mActionType);
				mGetPhoneInfoRequest.setRequestId(GET_PHONE_INFO);
				httpPost(mGetPhoneInfoRequest);

				// 对密码进行MD5
				String md5Password = StringUtils.md5(password);
				mRegisterPkgRequest = new RegisterPkgRequest(
						RegisterActivity.this, userName, md5Password);
				mRegisterPkgRequest.setRequestId(LOGIN_PKG);
				httpPost(mRegisterPkgRequest);

				//友盟统计
				Map<String,String> map_code = new HashMap<>();
				map_code.put("username",userName);
				mUmeng.setCalculateEvents("register_success",map_code);
			} else {
				// 注册失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 设置注册按钮可点击
			setViewEnable(true);
			break;

		case REGISTER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 注册成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				LoginInfo li = (LoginInfo) response.singleData;

				// 保存登录返回的token到SP
				CacheUtils.putString(getApplicationContext(), TOKENVALUE,
						li.tokenValue);

				// 获取手机的信息
				String pIMEI = getPhoneInfo.getPhoneIMEI();
				String pIMSI = getPhoneInfo.getPhoneIMSI();
				String aVersion = getPhoneInfo.getAppVersion().trim();
				int mActionType = 0;

				Log.e("TAG", "-------------IMEI------------------" + pIMEI);
				Log.e("TAG", "-------------IMSI------------------" + pIMSI);
				Log.e("TAG", "-------------AppVersion------------------"
						+ aVersion);

				// 获取手机信息的请求
				mGetPhoneInfoRequest = new GetPhoneInfoRequest(
						RegisterActivity.this, pIMEI, pIMSI, aVersion,
						mActionType);
				mGetPhoneInfoRequest.setRequestId(GET_PHONE_INFO);
				httpPost(mGetPhoneInfoRequest);

				// TODO 对密码进行MD5
				String md5Password = StringUtils.md5(password);
				// mLoginRequest = new LoginRequest(RegisterActivity.this,
				// userName, md5Password);
				// mLoginRequest.setRequestId(LOGIN_REQUEST);
				// httpPost(mLoginRequest);
				mRegisterPkgRequest = new RegisterPkgRequest(
						RegisterActivity.this, userName, md5Password);
				mRegisterPkgRequest.setRequestId(LOGIN_PKG);
				httpPost(mRegisterPkgRequest);

			} else {
				// 注册失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 设置注册按钮可点击
			setViewEnable(true);
			break;
		case REGISTER_CODE_REQUEST:
			// 请求服务器获取验证码成功
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 验证码倒计时
				MyCountTimer countTimer = new MyCountTimer(mBtnVerification);
				countTimer.start();

				showToast(message);
			} else {
				showToast(message);
			}
			setViewEnableCode(true);
			hideProgressDialog();
			break;

		case LOGIN_REQUEST:
			// 请求服务器成功
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				LoginInfo loginInfo = (LoginInfo) response.singleData;
				String tokenValue = loginInfo.tokenValue;
				// 登录成功

				// 保存登录返回的token到SP
				CacheUtils.putString(getApplicationContext(), TOKENVALUE,
						tokenValue);
				// 登录成功后保存账号密码到SP
				CacheUtils.putString(getApplicationContext(), USER_NAME,
						userName);
				CacheUtils.putString(getApplicationContext(), PASSWORD,
						password);
				// 保存token的过期时间
				PreManager.instance(this).setTokenExpires(
						loginInfo.tokenExpires);
				// 登录成功跳转到主界面
				MainActivity.invoke(RegisterActivity.this);
				// 关闭注册页面
				RegisterActivity.this.finish();
				// TODO --登录成功后关闭当前登录界面还需要关闭登录界面
				CamaridoApp.destoryActivity("LoginActivity");

				mGetPushInfoRequest = new GetPushInfoRequest(
						RegisterActivity.this);
				mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
				httpPost(mGetPushInfoRequest);
			}
			break;

		case GET_PUSH_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				List<PushInfo> pushInfos = response.data;
				for (PushInfo pushInfo : pushInfos) {
					if (pushInfo.pushDataType == 1) {
						MiPushUtil.setMiPushTopic(RegisterActivity.this,
								pushInfo.receiverMark);
					}
				}
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;

		case GET_PHONE_INFO:
			isSuccess = response.isSuccess;
			message = response.message;
			break;

		case LOGIN_PKG:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				LoginInfo loginInfo = (LoginInfo) response.singleData;
				String tokenValue = loginInfo.tokenValue;
				String userType = loginInfo.userType;
				boolean isTempUser = loginInfo.isTempUser;
				// 登录成功

				// 保存登录返回的token到SP
				CacheUtils.putString(getApplicationContext(), TOKENVALUE,
						tokenValue);
				// 登录成功后保存账号密码到SP
				CacheUtils.putString(getApplicationContext(), USER_NAME,
						userName);
				CacheUtils.putString(getApplicationContext(), PASSWORD,
						password);
				// 保存token的过期时间
				PreManager.instance(this).setTokenExpires(
						loginInfo.tokenExpires);

				if (!TextUtils.isEmpty(userType) && userType != null) {
					// 保存用户类型
					CacheUtils.putString(getApplicationContext(), USER_TYPE,
							userType);
					if (userType.equals("1")) {
						// 登录成功跳转到主界面
						MainActivity.invoke(RegisterActivity.this);
					} else if (userType.equals("2")) {
						// 跳转到发包方当前订单界面
						// EmployerMainActivity.invoke(RegisterActivity.this);
						// 请求接口判断公司信息是否采集
						decideCompanyMessageRequest = new DecideCompanyMessageRequest(
								RegisterActivity.this);
						decideCompanyMessageRequest
								.setRequestId(DECIDE_COMPANY_MESSAGE);
						httpGet(decideCompanyMessageRequest);
					}
				} else {
					// 登录成功跳转到主界面
					EmployerMainActivity.invoke(RegisterActivity.this);
				}

				// 登录成功跳转到主界面
				// EmployerMainActivity.invoke(RegisterActivity.this);
				// 关闭注册页面
				RegisterActivity.this.finish();
				// TODO --登录成功后关闭当前登录界面还需要关闭登录界面
				CamaridoApp.destoryActivity("LoginActivity");

				mGetPushInfoRequest = new GetPushInfoRequest(
						RegisterActivity.this);
				mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
				httpPost(mGetPushInfoRequest);

			}
			break;

		case DECIDE_COMPANY_MESSAGE:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				CacheUtils.putInt(mContext, "COMPANY_MESSAGE", 1);
				// 跳转到发包方当前订单界面
				EmployerMainActivity.invoke(RegisterActivity.this);
			} else {
				CacheUtils.putInt(mContext, "COMPANY_MESSAGE", 0);
				// 跳转到信息采集页面
				EmployerCompanyRenZhengActivity.invoke(RegisterActivity.this);
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		switch (requestId) {
		case REGISTER_REQUEST:
			// 设置注册按钮可点击
			setViewEnable(true);
			break;

		case REGISTER_CODE_REQUEST:
			setViewEnableCode(true);
            hideProgressDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * 注册界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, RegisterActivity.class);
		activity.startActivity(intent);

	}

	/**
	 * 选择身份的监听
	 * 
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == rbDiver.getId()) {
			// 选择了承运人
			mChooseIdentity = "1";
			mEtInvitecode.setHint("推荐码,没有可以不填");
		} else if (checkedId == rbEmployer.getId()) {
			// 选择了货主
			mChooseIdentity = "2";
			mEtInvitecode.setHint("推荐人,没有可以不填");
		}
	}
}
