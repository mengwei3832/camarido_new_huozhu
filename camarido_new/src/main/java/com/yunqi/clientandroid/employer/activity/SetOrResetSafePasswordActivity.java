package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.KeyboardAdapter;
import com.yunqi.clientandroid.employer.request.ForgetTenantPwdPayRequest;
import com.yunqi.clientandroid.employer.request.SafeCenterResetPwdPayRequest;
import com.yunqi.clientandroid.employer.request.SafeCenterSetPwdPayRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.InputPassUtils;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.StarPassword;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 
 * @Description:设置密码或重置密码
 * @ClassName: SetOrResetSafePasswordActivity
 * @author: chengtao
 * @date: 2016年6月16日 下午7:27:15
 * 
 */
public class SetOrResetSafePasswordActivity extends BaseActivity implements
		OnItemClickListener, OnFocusChangeListener {
	private TextView tvPassword;// 提示输入什么密码
	private StarPassword etPassword;// 自定义密码输入框
	private GridView gvKeyboard;// 自定义键盘
	private List<String> keyList;// 存储键盘按键
	private KeyboardAdapter adapter;// 键盘按键适配器
	private String passwordStr = "";// 临时保存密码字符串
	private String passwordBefore = "";// 输入新密码的第一次
	private String passwordAfter = "";// 输入新密码的第二次
	private String OldPwdPay = "";// 旧密码
	private String NewPwdPay = "";// 新密码
	// 是否存在支付密码
	private boolean isExitsPwdPay = false;
	// 是否是忘记密码
	private boolean isForgetPassword = false;
	// 短信验证码
	private String ShortMsg = "";
	// 输入密码的次数
	private int inputPasswordCount = 1;// 设置密码2次，重置密码3次

	// 页面请求
	private SafeCenterSetPwdPayRequest setPwdPayRequest;
	private SafeCenterResetPwdPayRequest resetPwdPayRequest;
	private ForgetTenantPwdPayRequest forgetTenantPwdPayRequest;
	// 请求ID
	private final static int SET_PWD_PAY_REQUEST = 1;
	private final static int RESET_PWD_PAY_REQUEST = 2;
	private final static int FORGET_TENAT_PWD_PAY_REQUEST = 3;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_set_password;
	}

	@Override
	protected void initView() {
		Log.v("TAG", "SetOrResetSafePasswordActivity");
		isExitsPwdPay = getIntent().getBooleanExtra("isExitsPwdPay", false);
		isForgetPassword = getIntent().getBooleanExtra("isForgetPassword",
				false);
		ShortMsg = getIntent().getStringExtra("ShortMsg");
		etPassword = obtainView(R.id.et_password);
		gvKeyboard = obtainView(R.id.gv_keyboard);
		tvPassword = obtainView(R.id.tv_password);
		keyList = new ArrayList<String>();
		if (isExitsPwdPay) {
			tvPassword.setText("请输入旧密码");
		}
		initActionBar();
	}

	/**
	 * 
	 * @Description:初始化initActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月14日 下午6:54:53
	 * @Author : chengtao
	 */
	private void initActionBar() {
		if (isExitsPwdPay) {
			setActionBarTitle("重置密码");
		} else {
			setActionBarTitle("设置密码");
		}
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		// 初始化自定义键盘数据
		for (String object : InputPassUtils.dataArray) {
			keyList.add(object);
			Log.v("TAG", "------keyList-----" + keyList.get(keyList.size() - 1));
		}
		Log.v("TAG", keyList.toString());
		adapter = new KeyboardAdapter(keyList, mContext);
		gvKeyboard.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		Log.v("TAG", "------------setListener-----------------");
		etPassword.setOnFocusChangeListener(this);
		gvKeyboard.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.v("TAG", "-------------onItemClick----------------");
		String str = keyList.get(position);
		if (str.equals("完成")) {
			/*
			 * if (passwordBefore.length() != etPassword.getPasswordLength()) {
			 * showToast("密码输入格式有误"); }else { md5Password =
			 * MD5.toMD5(passwordBefore); showToast("完成"+md5Password); }
			 */
		} else if (str.equals("×")) {
			if (passwordStr.length() == 0) {
				passwordStr = "";
				etPassword.setText(passwordStr);
			} else {
				passwordStr = passwordStr
						.substring(0, passwordStr.length() - 1);
				etPassword.setText(passwordStr);
			}
			Log.v("TAG", "------------passwordStr-----------------"
					+ passwordStr);
		} else {
			if (isExitsPwdPay) {// 重置密码
				if (inputPasswordCount == 1) {
					if (passwordStr.length() < etPassword.getPasswordLength()) {
						passwordStr += str;
						etPassword.setText(passwordStr);
						if (passwordStr.length() == etPassword
								.getPasswordLength()) {
							// 即输入旧密码完成
							inputPasswordCount++;
							// 保存旧密码
							OldPwdPay = passwordStr;
							// 初始化
							passwordStr = "";
							etPassword.setText("");
							// 设置密码提示
							tvPassword.setText("请输入新密码");
						}
					}
				} else if (inputPasswordCount == 2) {
					if (passwordStr.length() < etPassword.getPasswordLength()) {
						passwordStr += str;
						etPassword.setText(passwordStr);
						if (passwordStr.length() == etPassword
								.getPasswordLength()) {
							// 即第一次输入新密码完成
							inputPasswordCount++;
							// 保存第一次输入新密码
							passwordBefore = passwordStr;
							// 初始化
							passwordStr = "";
							etPassword.setText("");
							// 设置密码提示
							tvPassword.setText("请确认输入的密码");
						}
					}
				} else if (inputPasswordCount == 3) {
					if (passwordStr.length() < etPassword.getPasswordLength()) {
						passwordStr += str;
						etPassword.setText(passwordStr);
						if (passwordStr.length() == etPassword
								.getPasswordLength()) {
							// 即第二次输入新密码完成
							// 保存第二次输入新密码
							passwordAfter = passwordStr;
							if (passwordBefore.equals(passwordAfter)) {
								// 重置密码
								NewPwdPay = passwordAfter;
								restPassword();
							} else {
								showToast("密码输入不一致");
								// 初始化
								passwordStr = "";
								etPassword.setText("");
								inputPasswordCount = 2;
								// 设置密码提示
								tvPassword.setText("请输入新密码");
							}
						}
					}
				}
			} else {// 设置密码
				if (inputPasswordCount == 1) {
					if (passwordStr.length() < etPassword.getPasswordLength()) {
						passwordStr += str;
						Log.v("TAG", "------------passwordStr-----------------"
								+ passwordStr);
						etPassword.setText(passwordStr);
						if (passwordStr.length() == etPassword
								.getPasswordLength()) {
							// 即第一次输入密码完成
							inputPasswordCount++;
							// 保存第一次输入密码
							passwordBefore = passwordStr;
							Log.v("TAG",
									"------------passwordBefore-----------------"
											+ passwordBefore);
							// 初始化
							passwordStr = "";
							etPassword.setText("");
							// 设置密码提示
							tvPassword.setText("请确认输入的密码");
						}
					}
				} else if (inputPasswordCount == 2) {
					if (passwordStr.length() < etPassword.getPasswordLength()) {
						passwordStr += str;
						Log.v("TAG", "------------passwordStr-----------------"
								+ passwordStr);
						etPassword.setText(passwordStr);
						if (passwordStr.length() == etPassword
								.getPasswordLength()) {
							// 即第二次输入密码完成
							// 保存第二次输入密码
							passwordAfter = passwordStr;
							Log.v("TAG",
									"------------passwordBefore----------------"
											+ passwordBefore);
							Log.v("TAG",
									"------------passwordAfter-----------------"
											+ passwordAfter);
							if (passwordBefore.equals(passwordAfter)) {
								// 设置密码
								OldPwdPay = passwordAfter;
								if (isForgetPassword) {
									setPasswordByYanZhengMa();
								} else {
									setPassword();
								}
							} else {
								// 提示两次输入密码不一致
								showToast("密码输入不一致");
								Log.v("TAG", "two different passwords.....");
								// 初始化
								passwordStr = "";
								etPassword.setText("");
								inputPasswordCount = 1;
								// 设置密码提示
								tvPassword.setText("请输入密码");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description:设置密码（忘记密码）
	 * @Title:setPasswordByYanZhengMa
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午9:14:16
	 * @Author : chengtao
	 */
	private void setPasswordByYanZhengMa() {
		OldPwdPay = StringUtils.md5(OldPwdPay);
		forgetTenantPwdPayRequest = new ForgetTenantPwdPayRequest(mContext,
				ShortMsg, OldPwdPay);
		forgetTenantPwdPayRequest.setRequestId(FORGET_TENAT_PWD_PAY_REQUEST);
		httpPostJson(forgetTenantPwdPayRequest);
	}

	/**
	 * 
	 * @Description:设置支付密码
	 * @Title:setPassword
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午7:59:05
	 * @Author : chengtao
	 */
	private void setPassword() {
		OldPwdPay = StringUtils.md5(OldPwdPay);
		setPwdPayRequest = new SafeCenterSetPwdPayRequest(mContext, OldPwdPay);
		setPwdPayRequest.setRequestId(SET_PWD_PAY_REQUEST);
		httpPostJson(setPwdPayRequest);
	}

	/**
	 * 
	 * @Description:重置支付密码
	 * @Title:restPassword
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午7:59:21
	 * @Author : chengtao
	 */
	private void restPassword() {
		NewPwdPay = StringUtils.md5(NewPwdPay);
		OldPwdPay = StringUtils.md5(OldPwdPay);
		resetPwdPayRequest = new SafeCenterResetPwdPayRequest(mContext,
				OldPwdPay, NewPwdPay);
		resetPwdPayRequest.setRequestId(RESET_PWD_PAY_REQUEST);
		httpPostJson(resetPwdPayRequest);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case RESET_PWD_PAY_REQUEST:// 重置支付密码请求
			isSuccess = response.isSuccess;
			message = response.message;
			showToast(message);
			if (isSuccess) {
				finish();
			} else {
				// 初始化
				etPassword.setText("");// 初始化密码框
				tvPassword.setText("请输入旧密码");
				passwordStr = "";
				inputPasswordCount = 1;
			}
			break;
		case SET_PWD_PAY_REQUEST:// 设置支付密码请求
			isSuccess = response.isSuccess;
			message = response.message;
			showToast(message);
			if (isSuccess) {
				finish();
			} else {
				// 初始化
				etPassword.setText("");// 初始化密码框
				tvPassword.setText("请输入密码");
				passwordStr = "";
				inputPasswordCount = 1;
			}
			break;
		case FORGET_TENAT_PWD_PAY_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			showToast(message);
			if (isSuccess) {
				finish();
				CamaridoApp.destoryActivity("ForgetPayPasswordActivity");
			} else {
				// 初始化
				etPassword.setText("");// 初始化密码框
				tvPassword.setText("请输入密码");
				passwordStr = "";
				inputPasswordCount = 1;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		// TODO Auto-generated method stub
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Log.v("TAG", "--------------onFocusChange---------------");
		switch (v.getId()) {
		case R.id.et_password:
			if (hasFocus) {
				gvKeyboard.setVisibility(View.VISIBLE);
			} else {
				gvKeyboard.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:设置或重置密码跳转(不是忘记密码)
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午7:19:12
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, boolean isExitsPwdPay) {
		Intent intent = new Intent(activity,
				SetOrResetSafePasswordActivity.class);
		intent.putExtra("isExitsPwdPay", isExitsPwdPay);
		activity.startActivity(intent);
	}

	/**
	 * 
	 * @Description:设置或重置密码跳转(忘记密码)
	 * @Title:invoke
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午9:01:43
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, boolean isForgetPassword,
			String ShortMsg) {
		Intent intent = new Intent(activity,
				SetOrResetSafePasswordActivity.class);
		intent.putExtra("isForgetPassword", isForgetPassword);
		intent.putExtra("ShortMsg", ShortMsg);
		activity.startActivity(intent);
	}
}
