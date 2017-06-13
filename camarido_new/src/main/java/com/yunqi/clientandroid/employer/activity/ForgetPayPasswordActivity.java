package com.yunqi.clientandroid.employer.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.RestPayPasswordActivity;
import com.yunqi.clientandroid.employer.request.GetResetPwdPayMsgRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.view.TimeButton;

/**
 * 
 * @Description:忘记支付密码界面
 * @ClassName: ResetPasswordActivity
 * @author: chengtao
 * @date: 2016年6月14日 下午7:38:35
 * 
 */
public class ForgetPayPasswordActivity extends BaseActivity implements
		OnClickListener {
	private TextView tvMessageTip;
	private TextView tvGetTip;
	private EditText etYanZhengMa;
	private TimeButton tbYanZhengMa;
	private Button btnNext;
	private String phoneNumber;
	private String yanZhengMa;
	// 页面请求
	private GetResetPwdPayMsgRequest getResetPwdPayMsgRequest;
	// 页面请求ID
	private final static int GET_REST_PWD_PAY_MSG_REQUEST = 1;
	// 存放SP的key
	public static final String USER_NAME = "USER_NAME";

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_reset_password;
	}

	@Override
	protected void initView() {
		CamaridoApp.addDestoryActivity(ForgetPayPasswordActivity.this,
				"ForgetPayPasswordActivity");
		tvMessageTip = obtainView(R.id.tv_message_tip);
		tvGetTip = obtainView(R.id.tv_get_tip);
		etYanZhengMa = obtainView(R.id.et_yan_zheng_ma);
		tbYanZhengMa = obtainView(R.id.tb_yan_zheng_ma);
		btnNext = obtainView(R.id.btn_next);
		initActionBar();
	}

	private void initActionBar() {
		setActionBarTitle("忘记密码");
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
		phoneNumber = CacheUtils.getString(getApplicationContext(), USER_NAME,
				"");
	}

	@Override
	protected void setListener() {
		btnNext.setOnClickListener(null);
		tbYanZhengMa.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:// 跳转到设置密码
			//友盟统计首页
			mUmeng.setCalculateEvents("safety_forget_payment_password_next");

			yanZhengMa = etYanZhengMa.getText().toString();
			if (yanZhengMa != null && !TextUtils.isEmpty(yanZhengMa)) {
				SetOrResetSafePasswordActivity.invoke(
						ForgetPayPasswordActivity.this, true, yanZhengMa);
			} else {
				showToast("请输入验证码");
			}
			break;
		case R.id.tb_yan_zheng_ma:// 获取验证码
			//友盟统计首页
			mUmeng.setCalculateEvents("safety_forget_payment_password_code");

			getYanZhengMa();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:获取验证码
	 * @Title:getYanZhengMa
	 * @return:void
	 * @throws
	 * @Create: 2016年6月14日 下午8:02:35
	 * @Author : chengtao
	 */
	private void getYanZhengMa() {
		// 获取验证码按钮置灰，不可点击
		tbYanZhengMa.startTimeButton();
		// 显示提示信息
		tvMessageTip.setText(tvMessageTip.getText().toString() + phoneNumber);
		tvMessageTip.setVisibility(View.VISIBLE);
		tvGetTip.setVisibility(View.VISIBLE);

		// 发送验证码请求
		getResetPwdPayMsgRequest = new GetResetPwdPayMsgRequest(mContext,
				phoneNumber);
		getResetPwdPayMsgRequest.setRequestId(GET_REST_PWD_PAY_MSG_REQUEST);
		httpPost(getResetPwdPayMsgRequest);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_REST_PWD_PAY_MSG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			showToast(message);
			if (isSuccess) {
				btnNext.setOnClickListener(this);
			} else {
				btnNext.setOnClickListener(null);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		btnNext.setOnClickListener(null);
		showToast(getString(R.string.net_error_toast));
	}

	/**
	 * 
	 * @Description:忘记密码跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午7:19:12
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, ForgetPayPasswordActivity.class);
		activity.startActivity(intent);
	}
}
