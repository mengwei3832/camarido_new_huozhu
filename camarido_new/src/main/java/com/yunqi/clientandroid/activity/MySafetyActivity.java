package com.yunqi.clientandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;

public class MySafetyActivity extends BaseActivity implements
		View.OnClickListener {
	// 控件对象
	private RelativeLayout rl_safety_setting_pass; // 设置支付密码
	private RelativeLayout rl_safety_forget_pass; // 忘记支付密码
	private TextView tv_safety_help; // 常见问题

	@Override
	protected int getLayoutId() {
		// 加载安全中心页面
		return R.layout.activity_wallet_safety;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		// 得到控件对象
		rl_safety_setting_pass = (RelativeLayout) findViewById(R.id.rl_safety_setting_pass);
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
				MySafetyActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	// 点击事件的监听
	@Override
	protected void setListener() {
		rl_safety_setting_pass.setOnClickListener(this);
		rl_safety_forget_pass.setOnClickListener(this);
		tv_safety_help.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_safety_setting_pass:// 设置支付密码
			Log.e("TAG", "准备跳转-------------------------------");

			InputPasswordActivity.invoke(MySafetyActivity.this);
			MySafetyActivity.this.finish();

			break;
		case R.id.rl_safety_forget_pass:// 忘记支付密码

			break;
		case R.id.tv_safety_help: // 常见问题
			HelpActivity.invoke(MySafetyActivity.this, "safe");
			break;

		default:
			break;
		}
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, MySafetyActivity.class);
		activity.startActivity(intent);
	}

}
