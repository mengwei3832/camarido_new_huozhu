package com.yunqi.clientandroid.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.http.request.GetUserInfoRequest;
import com.yunqi.clientandroid.http.request.SetUserInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.view.CircleImageView;

/**
 * @deprecated:个人设置
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {

	private TextView mTvAgeshow;
	private TextView mTvSexshow;
	private TextView mTvPhonenumber;
	private TextView mTvNickname;
	private TextView mTvRealname;
	private TextView mTvDriver;
	private EditText mEtNickshow;
	private ImageView mIvRealnamePhoto;
	private ImageView mIvDriverPhoto;
	private ImageButton mIbPersonalBack;
	private RelativeLayout mRlPersonalAge;
	private RelativeLayout mRlPersonalSex;
	private RelativeLayout mRlRealName;
	private RelativeLayout mRlDriver;
	private View mLlGlobal;
	private ProgressBar mProgress;
	private CircleImageView mCivHeaderphoto;
	private View parentView;
	private Button mBtnSix;
	private Button mBtnSeven;
	private Button mBtnEigh;
	private Button mBtnNine;
	private Button mBtnAgecancel;
	private Button mBtnMan;
	private Button mBtnFemale;
	private Button mBtnSexCancel;
	private PopupWindow sexPopupWindow;
	private PopupWindow agePopupWindow;

	// SP缓存key
	private final String GENDER = "GENDER";
	private final String USER_NAME = "USER_NAME";
	private final String NICKNAME = "NICKNAME";
	private final String BIRTHDAY = "BIRTHDAY";
	private final String AGE = "AGE";
	private final String REALNAME = "REALNAME";
	private final String DRIVERNAME = "DRIVERNAME";

	private String age;
	private String sex;
	private String userName;
	private String nickname;
	private String name;
	private String birthday;
	private String gender;
	private String isReal;
	private String isDriver;
	private String years;
	private String headPortraitUrl;

	// 本页请求
	private GetUserInfoRequest mGetUserInfoRequest;
	private SetUserInfoRequest mSetUserInfoRequest;
	// 本页请求id
	private final int GET_USER_INFO_REQUEST = 17;
	private final int SET_USER_INFO_REQUEST = 18;

	// TODO 广播接受者需要用的活动
	public static final String action = "personal.broadcast.action";

	@Override
	protected int getLayoutId() {
		return R.layout.activity_personal;
	}

	@Override
	protected void initView() {
		// 初始化标题栏
		initActionBar();
		parentView = getLayoutInflater().inflate(R.layout.activity_personal,
				null);
		mTvAgeshow = (TextView) findViewById(R.id.tv_personal_ageshow);
		mTvSexshow = (TextView) findViewById(R.id.tv_personal_sexshow);
		mEtNickshow = (EditText) findViewById(R.id.tv_personal_nickshow);
		mTvPhonenumber = (TextView) findViewById(R.id.tv_personal_phonenumber);
		mTvNickname = (TextView) findViewById(R.id.tv_personal_nickname);
		mIvRealnamePhoto = (ImageView) findViewById(R.id.iv_personal_realname_photo);
		mIvDriverPhoto = (ImageView) findViewById(R.id.iv_personal_driver_photo);
		mTvRealname = (TextView) findViewById(R.id.tv_personal_realname);
		mTvDriver = (TextView) findViewById(R.id.tv_personal_driver);
		mIbPersonalBack = (ImageButton) findViewById(R.id.ib_personal_back);
		mRlPersonalAge = (RelativeLayout) findViewById(R.id.rl_personal_age);
		mRlPersonalSex = (RelativeLayout) findViewById(R.id.rl_personal_sex);
		mRlRealName = (RelativeLayout) findViewById(R.id.rl_personal_nameauthentication);
		mRlDriver = (RelativeLayout) findViewById(R.id.rl_personal_driverauthentication);
		mCivHeaderphoto = (CircleImageView) findViewById(R.id.iv_personal_headerphoto);
		mProgress = (ProgressBar) findViewById(R.id.pb_personal_progress);
		mLlGlobal = findViewById(R.id.ll_personal_global);

		// 监听软键盘弹出和退出
		mLlGlobal.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						int heightDiff = mLlGlobal.getRootView().getHeight()
								- mLlGlobal.getHeight();

						if (heightDiff > 100) { // 说明键盘是弹出状态
												// 键盘弹出状态
							mRlPersonalAge.setEnabled(false);
							mRlPersonalSex.setEnabled(false);
						} else {
							// 键盘收起状态
							mRlPersonalAge.setEnabled(true);
							mRlPersonalSex.setEnabled(true);
						}
					}
				});
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 从服务器获取个人信息数据
		initPersonalRequest();

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化标题栏的方法
	private void initActionBar() {
		// 隐藏ActionBar
		ActionBar bar = getActionBar();
		bar.hide();
	}

	private void initOnClick() {
		mIbPersonalBack.setOnClickListener(this);
		mRlPersonalAge.setOnClickListener(this);
		mRlPersonalSex.setOnClickListener(this);
		mRlRealName.setOnClickListener(this);
		mRlDriver.setOnClickListener(this);
	}

	@Override
	public void finish() {
		super.finish();
		String gender;
		String userName = CacheUtils.getString(getApplicationContext(),
				USER_NAME, "");// 从缓存中获取用户名
		String years = mTvAgeshow.getText().toString().trim();
		String nickname = mEtNickshow.getText().toString().trim();
		String sex = mTvSexshow.getText().toString().trim();

		if (years.length() > 4) {
			years = CacheUtils.getString(getApplicationContext(), AGE, "");// 从缓存中获取年龄段
		}

		if (sex.length() > 2) {
			sex = CacheUtils.getString(getApplicationContext(), GENDER, "");// 从缓存中获取性别
		}

		if (sex.equals("男")) {
			gender = "1";
		} else if (sex.equals("女")) {
			gender = "2";
		} else {
			gender = "0";
		}

		// 提交设置用户信息
		mSetUserInfoRequest = new SetUserInfoRequest(PersonalActivity.this,
				userName, nickname, gender, years);
		mSetUserInfoRequest.setRequestId(SET_USER_INFO_REQUEST);
		httpPost(mSetUserInfoRequest);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_personal_back:
			// 点击按钮返回
			PersonalActivity.this.finish();
			break;

		case R.id.rl_personal_age:
			// 点击选择年龄
			showAgePopupWindow();

			break;

		case R.id.rl_personal_sex:
			// 点击按钮选择性别
			showSexPopupWindow();

			break;

		case R.id.rl_personal_nameauthentication:
			// 点击按钮进入实名认证界面
			if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(isReal + "")) {
				RealNameActivity.invoke(this, userName, isReal);
			}

			break;

		case R.id.rl_personal_driverauthentication:
			// 点击按钮进入司机认证界面

			boolean realTrue = CacheUtils.getBoolean(getApplicationContext(),
					REALNAME, false);
			if (!realTrue) {
				showToast("请先完成实名认证");
				return;
			}

			if (!TextUtils.isEmpty(name) && name != null
					&& !TextUtils.isEmpty(isDriver + "")) {
				DriverCertifiedActivity.invoke(this, name, "", isDriver);
			}

			break;

		default:
			break;
		}
	}

	// 初始化界面时从服务器获取数据的方法
	private void initPersonalRequest() {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		mGetUserInfoRequest = new GetUserInfoRequest(PersonalActivity.this);
		mGetUserInfoRequest.setRequestId(GET_USER_INFO_REQUEST);
		httpPost(mGetUserInfoRequest);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_USER_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				UserInfo userInfo = (UserInfo) response.singleData;
				// 获取个人信息成功
				userName = userInfo.userName;// 用户名
				nickname = userInfo.nickname;// 昵称
				name = userInfo.name;// 姓名
				birthday = userInfo.birthday;// 生日
				gender = userInfo.gender;// 性别：0：未知，1：男，2：女
				age = userInfo.age;// 年龄
				String idCode = userInfo.iDCode;// 身份证号
				isReal = userInfo.isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
				isDriver = userInfo.isDriver;// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
				years = userInfo.years;// 出生年代
				headPortraitUrl = userInfo.headPortraitUrl;// 头像Url

				// 缓存生日
				if (!TextUtils.isEmpty(birthday) && birthday != null) {
					CacheUtils.putString(getApplicationContext(), BIRTHDAY,
							birthday);
				}

				// 显示用户名
				if (!TextUtils.isEmpty(userName) && userName != null) {
					String usernumber = userName.substring(0, 3) + "*****"
							+ userName.substring(8, userName.length());
					mTvPhonenumber.setText(usernumber);
					CacheUtils.putString(getApplicationContext(), USER_NAME,
							userName);
				}

				// 显示昵称
				if (!TextUtils.isEmpty(nickname) && nickname != null) {
					mTvNickname.setText(nickname);
					mEtNickshow.setText(nickname);
					mEtNickshow.setTextColor(android.graphics.Color
							.parseColor("#333333"));
					CacheUtils.putString(getApplicationContext(), NICKNAME,
							nickname);
				}

				// 显示性别
				if (gender != null && gender.equals("1")) {
					mTvSexshow.setText("男");
					mTvSexshow.setTextColor(android.graphics.Color
							.parseColor("#333333"));
					CacheUtils.putString(getApplicationContext(), GENDER, "男");
				} else if (gender != null && gender.equals("2")) {
					mTvSexshow.setText("女");
					mTvSexshow.setTextColor(android.graphics.Color
							.parseColor("#333333"));
					CacheUtils.putString(getApplicationContext(), GENDER, "女");
				}

				// 显示年龄
				if (!TextUtils.isEmpty(years)) {
					mTvAgeshow.setText(years);
					mTvAgeshow.setTextColor(android.graphics.Color
							.parseColor("#333333"));
					CacheUtils.putString(getApplicationContext(), AGE, years);

				}

				// 显示实名认证情况
				if (isReal != null && isReal.equals("2")) {
					CacheUtils.putBoolean(getApplicationContext(), REALNAME,
							true);
					mIvRealnamePhoto.setImageDrawable(getResources()
							.getDrawable(R.drawable.certification_ok));
					mTvRealname.setText("已实名认证");
				} else {
					mIvRealnamePhoto.setImageDrawable(getResources()
							.getDrawable(R.drawable.certification_nomal));
				}

				// 显示司机认证的情况
				if (isDriver != null && isDriver.equals("2")) {
					CacheUtils.putBoolean(getApplicationContext(), DRIVERNAME,
							true);
					mIvDriverPhoto.setImageDrawable(getResources().getDrawable(
							R.drawable.certification_ok));
					mTvDriver.setText("已司机认证");
				} else {
					mIvDriverPhoto.setImageDrawable(getResources().getDrawable(
							R.drawable.certification_nomal));
				}

				// 显示头像
				if (!TextUtils.isEmpty(headPortraitUrl)
						&& headPortraitUrl != null) {
					ImageLoader.getInstance().displayImage(headPortraitUrl,
							mCivHeaderphoto, ImageLoaderOptions.options);
				}

			} else {
				// 获取信息失败
				showToast(message);
			}
			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;
		case SET_USER_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				UserInfo userInfo = (UserInfo) response.singleData;
				// 获取信息成功
				userName = userInfo.userName;// 用户名
				nickname = userInfo.nickname;// 昵称
				name = userInfo.name;// 姓名
				birthday = userInfo.birthday;// 生日
				gender = userInfo.gender;// 性别：0：未知，1：男，2：女
				age = userInfo.age;// 年龄
				String idCode = userInfo.iDCode;// 身份证号
				isReal = userInfo.isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
				isDriver = userInfo.isDriver;// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
				years = userInfo.years;// 出生年代
				headPortraitUrl = userInfo.headPortraitUrl;// 头像Url

				// 将修改后的昵称传递出去给LeftFragment
				Intent intent = new Intent(action);
				intent.putExtra("data", nickname);
				sendBroadcast(intent);

				// 缓存生日
				if (!TextUtils.isEmpty(birthday) && birthday != null) {
					CacheUtils.putString(getApplicationContext(), BIRTHDAY,
							birthday);
				}

				// 缓存年龄
				if (!TextUtils.isEmpty(years) && years != null) {
					CacheUtils.putString(getApplicationContext(), AGE, years);
				}

				// 缓存昵称
				if (!TextUtils.isEmpty(nickname) && nickname != null) {
					CacheUtils.putString(getApplicationContext(), NICKNAME,
							nickname);
				}

				// 缓存性别
				if (gender != null && gender.equals("1")) {
					CacheUtils.putString(getApplicationContext(), GENDER, "男");
				} else if (gender != null && gender.equals("2")) {
					CacheUtils.putString(getApplicationContext(), GENDER, "女");
				}

			} else {
				// 设置失败
				showToast(message);
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	// 选择性别对话框
	private void showSexPopupWindow() {
		sexPopupWindow = new PopupWindow(PersonalActivity.this);
		View sexview = getLayoutInflater().inflate(
				R.layout.item_sex_popupwindows, null);

		sexPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		sexPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		sexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		sexPopupWindow.setFocusable(true);
		sexPopupWindow.setOutsideTouchable(true);
		sexPopupWindow.setContentView(sexview);
		sexPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		mBtnMan = (Button) sexview.findViewById(R.id.item_popupwindows_man);
		mBtnFemale = (Button) sexview
				.findViewById(R.id.item_popupwindows_female);
		mBtnSexCancel = (Button) sexview
				.findViewById(R.id.item_popupwindows_sexcancel);

		mBtnMan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sex = mBtnMan.getText().toString().trim();
				mTvSexshow.setText(sex);
				mTvSexshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				sexPopupWindow.dismiss();
			}
		});

		mBtnFemale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex = mBtnFemale.getText().toString().trim();
				mTvSexshow.setText(sex);
				mTvSexshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				sexPopupWindow.dismiss();
			}
		});

		mBtnSexCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View V) {
				sexPopupWindow.dismiss();
			}
		});

	}

	// 选择年龄对话框
	private void showAgePopupWindow() {
		agePopupWindow = new PopupWindow(PersonalActivity.this);
		View ageview = getLayoutInflater().inflate(
				R.layout.item_age_popupwindows, null);

		agePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		agePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		agePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		agePopupWindow.setFocusable(true);
		agePopupWindow.setOutsideTouchable(true);
		agePopupWindow.setContentView(ageview);
		agePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		mBtnSix = (Button) ageview.findViewById(R.id.item_popupwindows_six);
		mBtnSeven = (Button) ageview.findViewById(R.id.item_popupwindows_seven);
		mBtnEigh = (Button) ageview.findViewById(R.id.item_popupwindows_eigh);
		mBtnNine = (Button) ageview.findViewById(R.id.item_popupwindows_nine);
		mBtnAgecancel = (Button) ageview
				.findViewById(R.id.item_popupwindows_agecancel);

		mBtnSix.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				age = mBtnSix.getText().toString().trim();
				mTvAgeshow.setText(age);
				mTvAgeshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				agePopupWindow.dismiss();
			}
		});

		mBtnSeven.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				age = mBtnSeven.getText().toString().trim();
				mTvAgeshow.setText(age);
				mTvAgeshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				agePopupWindow.dismiss();
			}
		});

		mBtnEigh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				age = mBtnEigh.getText().toString().trim();
				mTvAgeshow.setText(age);
				mTvAgeshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				agePopupWindow.dismiss();
			}
		});

		mBtnNine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				age = mBtnNine.getText().toString().trim();
				mTvAgeshow.setText(age);
				mTvAgeshow.setTextColor(android.graphics.Color
						.parseColor("#333333"));
				agePopupWindow.dismiss();
			}
		});

		mBtnAgecancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				agePopupWindow.dismiss();
			}
		});

	}

	/**
	 * 个人设置界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, PersonalActivity.class);
		activity.startActivity(intent);

	}

}
