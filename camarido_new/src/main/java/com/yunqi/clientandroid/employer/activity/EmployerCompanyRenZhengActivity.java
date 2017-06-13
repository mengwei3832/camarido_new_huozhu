package com.yunqi.clientandroid.employer.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.request.EmployerCompanyRenZhengRequest;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;

/**
 * @Description:发包方申请企业认证的界面
 * @ClassName: EmployerCompanyRenZhengActivity
 * @author: chengtao
 * @date: 2016-6-3 上午10:49:15
 * 
 */
public class EmployerCompanyRenZhengActivity extends BaseActivity implements
		View.OnClickListener {
	/* 界面上的控件对象 */
	private EditText etCompanyName; // 企业名
	private EditText etCompanyPhone; // 联系电话
	private ImageView ivCompanyDelete1; // 删除
	private ImageView ivCompanyDelete2;
	private Button btCompanyShenQing; // 申请按钮

	private String mCompanyName;
	private String mCompanyPhone;
	private boolean isExit = false;// 是否退出

	/* 请求类 */
	private EmployerCompanyRenZhengRequest mEmployerCompanyRenZhengRequest;

	/* 请求ID */
	private final int SHENQING_COMPANY = 1;

	// 保存使用SharedPreferences
	private SharedPreferences spf;
	private Editor editor;
	private boolean isFirst;

	//友盟统计
	private UmengStatisticsUtils mUmeng;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_company_shenqing;
	}

	@Override
	protected void initView() {
		initActionBar();

		mUmeng = UmengStatisticsUtils.instance(mContext);

		etCompanyName = obtainView(R.id.et_employer_company_name);
		etCompanyPhone = obtainView(R.id.et_employer_company_phone);
		ivCompanyDelete1 = obtainView(R.id.iv_employer_company_delete1);
		ivCompanyDelete2 = obtainView(R.id.iv_employer_company_delete2);
		btCompanyShenQing = obtainView(R.id.bt_employer_company_shenqing);

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_company_title));
		// setActionBarLeft(R.drawable.nav_back);
		// setOnActionBarLeftClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// RegisterActivity.this.finish();
		// }
		// });
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnclick();

		// 初始化EditText
		initEditTextListener();
	}

	// 初始化点击事件
	private void initOnclick() {
		btCompanyShenQing.setOnClickListener(this);
		ivCompanyDelete1.setOnClickListener(this);
		ivCompanyDelete2.setOnClickListener(this);
	}

	// 初始化EditText
	private void initEditTextListener() {
		// 公司名输入框有变化的时候
		etCompanyName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 公司输入框输入内容的时候
				setCompanyNameChange();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		// 联系电话输入框有变化的时候
		etCompanyPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 联系电话输入框输入内容的时候
				setCompanyPhoneChange();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 公司输入框输入内容的时候
	 */
	private void setCompanyNameChange() {
		if (etCompanyName.getText().toString().length() != 0
				&& etCompanyName.isFocused()) {
			ivCompanyDelete1.setVisibility(View.VISIBLE);
		} else {
			ivCompanyDelete1.setVisibility(View.GONE);
		}
	}

	/**
	 * 联系电话输入框输入内容的时候
	 */
	private void setCompanyPhoneChange() {
		if (etCompanyPhone.getText().toString().length() != 0
				&& etCompanyPhone.isFocused()) {
			ivCompanyDelete2.setVisibility(View.VISIBLE);
		} else {
			ivCompanyDelete2.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_employer_company_shenqing: // 点击申请按钮
			// 获取输入框的值
			mCompanyName = etCompanyName.getText().toString().trim();
			mCompanyPhone = etCompanyPhone.getText().toString().trim();

			Log.e("TAG", "------mCompanyPhone------" + mCompanyPhone);

			if (mCompanyName == null && TextUtils.isEmpty(mCompanyName)) {
				showToast("请填写企业名称");
				return;
			}

			if (mCompanyPhone == null && TextUtils.isEmpty(mCompanyPhone)) {
				showToast("请输入手机号或加区号的座机号");
				return;
			}

			// 检测手机号码
			if (!mCompanyPhone
					.matches("^[1][3-8]\\d{9}|^[0]\\d{10}|^[0]\\d{11}$")) {
				showToast("请输入正确手机号或加区号的座机号");
				return;
			}

			//友盟统计
			Map<String,String> map_code = new HashMap<>();
			map_code.put("companyname",mCompanyName);
			map_code.put("companyphone",mCompanyPhone);
			mUmeng.setCalculateEvents("collection_message",map_code);

			showProgressDialog("请稍候...");

			shenQingCompany(); // 申请公司认证
			break;

		case R.id.iv_employer_company_delete1: // 企业名称删除
			etCompanyName.setText("");
			break;
		case R.id.iv_employer_company_delete2: // 企业联系方式删除
			etCompanyPhone.setText("");
			break;

		default:
			break;
		}
	}

	// 申请公司认证
	private void shenQingCompany() {
		String userPhone = CacheUtils.getString(
				EmployerCompanyRenZhengActivity.this, "USER_NAME", "");

		Log.e("TAG", "-----userPhone-------" + userPhone);

		mEmployerCompanyRenZhengRequest = new EmployerCompanyRenZhengRequest(
				EmployerCompanyRenZhengActivity.this, mCompanyName, userPhone,
				mCompanyPhone);
		mEmployerCompanyRenZhengRequest.setRequestId(SHENQING_COMPANY);
		httpPost(mEmployerCompanyRenZhengRequest);

		// 将申请按钮设为不可点击
		setViewEnable(false);
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case SHENQING_COMPANY:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				showToast(message);
				// 让输入框失去焦点
				etCompanyName.setEnabled(false);
				etCompanyPhone.setEnabled(false);
				ivCompanyDelete1.setVisibility(View.GONE);
				ivCompanyDelete2.setVisibility(View.GONE);

				//友盟统计
				Map<String,String> map_code = new HashMap<>();
				map_code.put("companyname",mCompanyName);
				map_code.put("companyphone",mCompanyPhone);
				mUmeng.setCalculateEvents("collection_message_success",map_code);

				CacheUtils.putInt(mContext, "COMPANY_MESSAGE", 1);

				// //进行判断是否第一次进入
				// spf = getSharedPreferences("first", MODE_WORLD_WRITEABLE);
				// isFirst = spf.getBoolean("isFirst", true);
				// editor = spf.edit();
				// if (isFirst) {
				// Log.e("TAG", "第一次进入-------------");
				//
				// // 弹出新手奖励的规则
				// showRecommendPopup();
				//
				// editor.putBoolean("isFirst", false);
				// editor.commit();
				// } else {
				// Log.e("TAG", "不是第一次进入-------------");
				// }

				// 进入主界面
				XinShouHelpActivity
						.invoke(EmployerCompanyRenZhengActivity.this);
				// EmployerCompanyRenZhengActivity.this.finish();
				CamaridoApp.addDestoryActivity(
						EmployerCompanyRenZhengActivity.this,
						"EmployerCompanyRenZhengActivity");
			} else {
				showToast(message);
				setViewEnable(true);
			}

			hideProgressDialog();

			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		setViewEnable(true);
		hideProgressDialog();
	}

	/**
	 * @Description:让按钮不可点击
	 * @Title:setViewEnable
	 * @param enable
	 * @return:void
	 * @throws
	 * @Create: 2016-6-3 上午11:50:18
	 * @Author : mengwei
	 */
	private void setViewEnable(boolean enable) {
		if (enable) {
			btCompanyShenQing.setText("提交信息");
			btCompanyShenQing.setBackgroundResource(R.drawable.bt_cartype);
		} else {
			btCompanyShenQing.setText("提交中，请耐心等待");
			btCompanyShenQing.setBackgroundResource(R.drawable.btngriy);
		}
		btCompanyShenQing.setEnabled(enable);
	}

	// 监听手机返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 再按一次退出程序
			exit();
		}
		return false;
	}

	// 再按一次退出程序的方法
	private void exit() {
		if (!isExit) {
			isExit = true;
			showToast("再按一次退出程序");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			EmployerCompanyRenZhengActivity.this.finish();
			// EmployerMainActivity.this.finish();
		}
	}

	/**
	 * 注册界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity,
				EmployerCompanyRenZhengActivity.class);
		activity.startActivity(intent);
	}

}
