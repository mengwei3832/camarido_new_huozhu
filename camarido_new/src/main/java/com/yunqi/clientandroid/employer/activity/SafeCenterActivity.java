package com.yunqi.clientandroid.employer.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.request.IsExitsPwdPayRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:安全中心
 * @ClassName: SafeCenterActivity
 * @author: chengtao
 * @date: 2016年6月16日 下午1:40:32
 * 
 */
public class SafeCenterActivity extends BaseActivity implements OnClickListener {
	private TextView tvPayPassword;// 设置支付密码 或 重置支付密码
	private TextView tvForgetPayPassword;// 忘记支付密码
	private TextView tvFQA;// 常见问题
	private RelativeLayout rlSafeCenter;
	private ProgressBar progressBar;
	private boolean isExitsPwdPay = false;
	private boolean isFirstIn = true;

	// 页面请求
	private IsExitsPwdPayRequest isExitsPwdPayRequest;
	// 请求ID
	private final static int IS_EXITS_PWD_PAY_REQUEST = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_acitvity_safe_center;
	}

	@Override
	protected void initView() {
		tvPayPassword = obtainView(R.id.tv_pay_password);
		tvForgetPayPassword = obtainView(R.id.tv_forget_pay_password);
		tvFQA = obtainView(R.id.tv_fqa);
		rlSafeCenter = obtainView(R.id.rl_safe_center);
		progressBar = obtainView(R.id.pb_safe_center);
		initActionbar();
	}

	private void initActionbar() {
		setActionBarLeft(R.drawable.fanhui);
		setActionBarTitle("安全中心");
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		isExitsPwdPay();
	}

	/**
	 * 
	 * @Description:判断是否存在支付密码
	 * @Title:isExitsPwdPay
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午6:33:30
	 * @Author : chengtao
	 */
	private void isExitsPwdPay() {
		rlSafeCenter.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		isExitsPwdPayRequest = new IsExitsPwdPayRequest(mContext);
		isExitsPwdPayRequest.setRequestId(IS_EXITS_PWD_PAY_REQUEST);
		httpGet(isExitsPwdPayRequest);
	}

	@Override
	protected void setListener() {
		tvForgetPayPassword.setOnClickListener(this);
		tvFQA.setOnClickListener(this);
		tvPayPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pay_password:// 设置支付密码或重置密码
			//友盟统计首页
			mUmeng.setCalculateEvents("safety_reset_payment_password");

			SetOrResetSafePasswordActivity.invoke(SafeCenterActivity.this,
					isExitsPwdPay);
			break;
		case R.id.tv_forget_pay_password:// 忘记支付密码
			//友盟统计首页
			mUmeng.setCalculateEvents("safety_forget_payment_password");

			if (isExitsPwdPay) {
				ForgetPayPasswordActivity.invoke(SafeCenterActivity.this);
			} else {
				showToast("您当前还没有支付密码");
			}
			break;
		case R.id.tv_fqa:// 常见问题

			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isFirstIn) {
			isExitsPwdPay();
		}
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case IS_EXITS_PWD_PAY_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				tvPayPassword.setText("重置支付密码");
				rlSafeCenter.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				isExitsPwdPay = true;
			} else {
				tvPayPassword.setText("设置支付密码");
				rlSafeCenter.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				isExitsPwdPay = false;
			}
			isFirstIn = false;
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	/**
	 * 
	 * @Description:安全中心页面跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午7:15:18
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, SafeCenterActivity.class);
		activity.startActivity(intent);
	}
}
