package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.PersonalSingle;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.http.request.ApproveUserRequest;
import com.yunqi.clientandroid.http.request.GetUserInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;

/**
 * @deprecated:实名认证
 */
public class RealNameActivity extends BaseActivity implements OnClickListener {

	private EditText mEtName;
	private EditText mEtIdCard;
	private ImageView mIvDelete1;
	private ImageView mIvDelete2;
	private Button mBtnAgreed;
	private LinearLayout llMsg;
	private RelativeLayout mRlIdCard;
	private RelativeLayout mRlName;
	private RelativeLayout mRlGlobal;
	private ProgressBar mProgress;
	private TextView mTvCustomerPhone;
	private TextView tvEnterprise;
	private TextView tvQuestion;
	private String userName;// 账号
	private String name;// 姓名
	private String idCode;// 身份证号
	private String isReal;// 认证状态
	private String idCard;// 显示的隐藏身份证号码

	// 本页请求
	private GetUserInfoRequest mGetUserInfoRequest;
	private ApproveUserRequest mApproveUserRequest;
	// 本页请求id
	private final int GET_USER_INFO_REQUEST = 15;
	private final int APPROVE_USER_REQUEST = 16;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_realname;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		// 添加进销毁队列
		CamaridoApp.addDestoryActivity(RealNameActivity.this,
				"RealNameActivity");

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从个人设置界面传过来的
		if (bundle != null && bundle.containsKey("userName")) {
			userName = bundle.getString("userName");
		}
		if (bundle != null && bundle.containsKey("isReal")) {
			isReal = bundle.getString("isReal");
		}

		mEtName = (EditText) findViewById(R.id.et_namecertification_name);
		mEtIdCard = (EditText) findViewById(R.id.et_namecertification_idcard);
		mIvDelete1 = (ImageView) findViewById(R.id.iv_namecertification_delete1);
		mIvDelete2 = (ImageView) findViewById(R.id.iv_namecertification_delete2);
		mBtnAgreed = (Button) findViewById(R.id.bt_namecertification_agreed);
		mRlIdCard = (RelativeLayout) findViewById(R.id.rl_namecertification_idcard);
		mRlName = (RelativeLayout) findViewById(R.id.rl_namecertification_name);
		mRlGlobal = (RelativeLayout) findViewById(R.id.ll_realname_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_realname_progress);
		mTvCustomerPhone = (TextView) findViewById(R.id.tv_namecertification_customerPhone);
		tvEnterprise = (TextView) findViewById(R.id.tv_namecertification_enterprise);
		llMsg = (LinearLayout) findViewById(R.id.ll_namecertification_msg);
		tvQuestion = (TextView) findViewById(R.id.tv_namecertification_question);

		// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
		if (isReal != null && !isReal.equals("0")) {
			// 初始化界面时从服务器获取数据
			initRealNameRequest();
		}

		// 只有认证通过显示信息
		if (isReal != null && isReal.equals("2")) {
			llMsg.setVisibility(View.VISIBLE);
		} else {
			llMsg.setVisibility(View.INVISIBLE);
		}

		// 设置客服电话
		mTvCustomerPhone.setText(Html.fromHtml("<u>" + "4006541756" + "</u>"));
		// 设置企业升级
		tvEnterprise.setText(Html.fromHtml("<u>" + "企业认证" + "</u>"));

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
		setActionBarTitle(this.getResources().getString(R.string.realname));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RealNameActivity.this.finish();
			}
		});
	}

	// 初始化点击事件
	private void initOnClick() {
		mIvDelete1.setOnClickListener(this);
		mIvDelete2.setOnClickListener(this);
		mBtnAgreed.setOnClickListener(this);
		mTvCustomerPhone.setOnClickListener(this);
		tvEnterprise.setOnClickListener(this);
		tvQuestion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_namecertification_delete1:
			// 用户名输入框的删除按钮
			mEtName.setText("");
			break;
		case R.id.iv_namecertification_delete2:
			// 身份证号输入框的删除按钮
			mEtIdCard.setText("");
			break;
		case R.id.bt_namecertification_agreed:
			// 点击按钮进行认证
			realNameRequest();
			break;

		case R.id.tv_namecertification_customerPhone:
			// 拨打客服电话
			String customerPhone = mTvCustomerPhone.getText().toString().trim();

			if (!TextUtils.isEmpty(customerPhone)) {
				CommonUtil.callPhoneIndirect(this, customerPhone);
			}

			break;

		case R.id.tv_namecertification_enterprise:
			// 跳转到企业认证界面
			if (!TextUtils.isEmpty(name) && name != null
					&& !TextUtils.isEmpty(idCode) && idCode != null) {
				EnterpriseActivity.invoke(RealNameActivity.this, name, idCode);
			}

			break;

		case R.id.tv_namecertification_question:
			// 跳转到帮助界面
			HelpActivity.invoke(RealNameActivity.this, "isreal");
			break;

		default:
			break;
		}

	}

	// 初始化界面时从服务器获取数据的方法
	private void initRealNameRequest() {
		mRlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		mGetUserInfoRequest = new GetUserInfoRequest(RealNameActivity.this);
		mGetUserInfoRequest.setRequestId(GET_USER_INFO_REQUEST);
		httpPost(mGetUserInfoRequest);

	}

	// EditText文本改变的方法
	private void initEditTextListener() {
		mEtName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
				setNameChangeEditText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable enable) {
			}
		});

		mEtIdCard.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 身份证输入框输入内容的时候
				setIdCardChangeEditText();
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

	// 身份证输入框有内容时执行的方法
	protected void setIdCardChangeEditText() {
		if (mEtIdCard.getText().toString().length() != 0
				&& mEtIdCard.isFocused()) {
			mIvDelete2.setVisibility(View.VISIBLE);
		} else if (mEtIdCard.getText().toString().length() == 0
				&& mEtName.getText().toString().length() != 0) {
			mIvDelete2.setVisibility(View.GONE);
		} else {
			mIvDelete2.setVisibility(View.GONE);
		}
	}

	// 姓名输入框有内容时执行的方法
	protected void setNameChangeEditText() {
		if (mEtName.getText().toString().length() != 0 && mEtName.isFocused()) {
			mIvDelete1.setVisibility(View.VISIBLE);
		} else {
			mIvDelete1.setVisibility(View.GONE);
		}
	}

	// 实名认证的方法
	private void realNameRequest() {
		String name = mEtName.getText().toString().trim();
		String iDCode = mEtIdCard.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			showToast("请输入姓名");
			return;
		}

		if (TextUtils.isEmpty(iDCode)) {
			showToast("请输入身份证号码");
			return;
		}

		// 正则校验身份证
		if (!iDCode.matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")) {
			showToast("请输入正确的身份证号码");
			return;
		}

		// 请求服务器进行实名认证
		mApproveUserRequest = new ApproveUserRequest(RealNameActivity.this,
				userName, name, iDCode);
		mApproveUserRequest.setRequestId(APPROVE_USER_REQUEST);
		httpPost(mApproveUserRequest);
		// 设置实名认证按钮不可点击
		setViewEnable(false);

	}

	// 设置按钮不可重复点击的方法
	private void setViewEnable(boolean bEnable) {
		mBtnAgreed.setEnabled(bEnable);
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
				// 获取用户信息成功
				isReal = userInfo.isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
				name = userInfo.name;// 姓名
				idCode = userInfo.iDCode;// 身份证号

				if (isReal != null && isReal.equals("2")) {
					// 说明已认证
					// 设置认证按钮不可点击
					setViewEnable(false);
					mBtnAgreed.setText("认证通过");
					// 设置姓名
					if (!TextUtils.isEmpty(name) && name != null) {
						mEtName.setText(name);
						mEtName.setFocusable(false);
					}
					// 设置身份证号
					if (!TextUtils.isEmpty(idCode) && idCode != null) {
						if (idCode.length() == 15) {
							idCard = idCode.substring(0, 2) + "*********"
									+ idCode.substring(11, idCode.length());
						} else if (idCode.length() == 18) {
							idCard = idCode.substring(0, 2) + "************"
									+ idCode.substring(14, idCode.length());
						}

						mEtIdCard.setText(idCard);
						mEtIdCard.setFocusable(false);
					}
				} else if (isReal != null && isReal.equals("1")) {
					// 说明认证中
					// 设置认证按钮不可点击
					setViewEnable(false);
					mBtnAgreed.setText("认证中");
					// 设置姓名
					if (!TextUtils.isEmpty(name) && name != null) {
						mEtName.setText(name);
						mEtName.setFocusable(false);
					}
					// 设置身份证号
					if (!TextUtils.isEmpty(idCode) && idCode != null) {
						if (idCode.length() == 15) {
							idCard = idCode.substring(0, 2) + "*********"
									+ idCode.substring(11, idCode.length());
						} else if (idCode.length() == 18) {
							idCard = idCode.substring(0, 2) + "************"
									+ idCode.substring(14, idCode.length());
						}

						mEtIdCard.setText(idCard);
						mEtIdCard.setFocusable(false);
					}
				} else if (isReal != null && isReal.equals("3")) {
					// 说明认证失败
					// 设置认证按钮可点击
					setViewEnable(true);
					mBtnAgreed.setText("提交");
					showToast("认证失败请重新认证");
				}

			} else {
				// 获取用户信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			mRlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;
		case APPROVE_USER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 提交认证成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				PersonalSingle personalSingle = (PersonalSingle) response.singleData;
				isReal = personalSingle.isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
				name = personalSingle.name;// 姓名
				idCode = personalSingle.iDCode;// 身份证号

				if (isReal != null && isReal.equals("2")) {
					// 说明已认证
					// 设置认证按钮不可点击
					setViewEnable(false);
					mBtnAgreed.setText("认证通过");
					// 设置姓名
					if (!TextUtils.isEmpty(name) && name != null) {
						mEtName.setText(name);
						mEtName.setFocusable(false);
					}
					// 设置身份证号
					if (!TextUtils.isEmpty(idCode) && idCode != null) {
						if (idCode.length() == 15) {
							idCard = idCode.substring(0, 2) + "*********"
									+ idCode.substring(11, idCode.length());
						} else if (idCode.length() == 18) {
							idCard = idCode.substring(0, 2) + "************"
									+ idCode.substring(14, idCode.length());
						}
						mEtIdCard.setText(idCard);
						mEtIdCard.setFocusable(false);
					}
				} else if (isReal != null && isReal.equals("1")) {
					// 说明认证中
					// 设置认证按钮不可点击
					setViewEnable(false);
					mBtnAgreed.setText("认证中");
					// 设置姓名
					if (!TextUtils.isEmpty(name) && name != null) {
						mEtName.setText(name);
						mEtName.setFocusable(false);
					}
					// 设置身份证号
					if (!TextUtils.isEmpty(idCode) && idCode != null) {
						if (idCode.length() == 15) {
							idCard = idCode.substring(0, 2) + "*********"
									+ idCode.substring(11, idCode.length());
						} else if (idCode.length() == 18) {
							idCard = idCode.substring(0, 2) + "************"
									+ idCode.substring(14, idCode.length());
						}
						mEtIdCard.setText(idCard);
						mEtIdCard.setFocusable(false);
					}
				} else if (isReal != null && isReal.equals("3")) {
					// 说明认证失败
					// 设置认证按钮可点击
					setViewEnable(true);
					mBtnAgreed.setText("提交");
					showToast("认证失败请重新认证");
				}

			} else {
				// 提交认证失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置认证按钮可点击
				setViewEnable(true);
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		switch (requestId) {
		case APPROVE_USER_REQUEST:
			// 设置认证按钮可点击
			setViewEnable(true);
			break;
		}

	}

	// 本界面跳转方法
	public static void invoke(Context activity, String userName, String isReal) {
		Intent intent = new Intent();
		intent.setClass(activity, RealNameActivity.class);
		intent.putExtra("userName", userName);// 传用户名
		intent.putExtra("isReal", isReal);// 传实名认证状态
		activity.startActivity(intent);
	}

}
