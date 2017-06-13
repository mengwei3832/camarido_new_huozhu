package com.yunqi.clientandroid.activity;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.GetFormerPayPassRequest;
import com.yunqi.clientandroid.http.response.Response;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @Description:class 重置支付密码页面
 * @ClassName: ResetPassActivity
 * @author: zhm
 * @date: 2016-4-14 上午10:32:48
 * 
 */
public class ResetPassActivity extends BaseActivity implements
		View.OnClickListener {
	// 控件对象
	private RelativeLayout rl_safety_reset_pass; // 重置支付密码
	private RelativeLayout rl_safety_forget_pass; // 忘记支付密码
	private TextView tv_safety_help; // 常见问题

	@Override
	protected int getLayoutId() {
		// 加载安全中心页面
		return R.layout.activity_reset_pass;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		// 得到控件对象
		rl_safety_reset_pass = (RelativeLayout) findViewById(R.id.rl_safety_reset_pass);
		rl_safety_forget_pass = (RelativeLayout) findViewById(R.id.rl_safety_forget_pass);
		tv_safety_help = (TextView) findViewById(R.id.tv_safety_help);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.my_safety_center));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				ResetPassActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {

	}

	// 点击事件的监听
	@Override
	protected void setListener() {
		rl_safety_reset_pass.setOnClickListener(this);
		rl_safety_forget_pass.setOnClickListener(this);
		tv_safety_help.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_safety_reset_pass: // 重置支付密码
			RestPayPasswordActivity.invoke(ResetPassActivity.this);

			// //请求获取原来的密码
			// getFormerPayPass();

			break;
		case R.id.rl_safety_forget_pass:// 忘记支付密码
			FormerPayPassActivity.invoke(ResetPassActivity.this);
			break;
		case R.id.tv_safety_help: // 常见问题
			HelpActivity.invoke(ResetPassActivity.this, "safe");
			break;

		default:
			break;
		}
	}

	// //请求获取原来的密码
	// private void getFormerPayPass() {
	// httpPost(new GetFormerPayPassRequest(ResetPassActivity.this));
	//
	// }

	// @Override
	// public void onStart(int requestId) {
	// super.onStart(requestId);
	// }
	//
	// @Override
	// public void onSuccess(int requestId, Response response) {
	// super.onSuccess(requestId, response);
	//
	// boolean isSuccess = response.isSuccess;
	// String message = response.message;
	//
	// if (isSuccess) {
	// //TODO 请求数据的结果
	// } else {
	// if (!TextUtils.isEmpty(message) && message != null) {
	// showToast(message);
	// }
	// }
	// }
	//
	// @Override
	// public void onFailure(int requestId, int httpCode, Throwable error) {
	// super.onFailure(requestId, httpCode, error);
	// showToast(this.getResources().getString(R.string.net_error_toast));
	// }

	// 对返回键的监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyWalletActivity.invoke(ResetPassActivity.this, null, null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, ResetPassActivity.class);
		activity.startActivity(intent);
	}
}
