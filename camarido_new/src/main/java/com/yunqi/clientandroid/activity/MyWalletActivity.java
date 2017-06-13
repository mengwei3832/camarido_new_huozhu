package com.yunqi.clientandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Wallet;
import com.yunqi.clientandroid.http.request.GetCurrentPayPassExistRequest;
import com.yunqi.clientandroid.http.request.GetWalletRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.view.RiseNumberTextView;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的钱包界面
 * @date 15/11/30
 */
public class MyWalletActivity extends BaseActivity implements
		View.OnClickListener {

	// 会滚动的账户余额
	private RiseNumberTextView tvWalletBalance;
	// 冻结资金
	private TextView tvFreeze;
	// 待结运费
	private TextView tvWaitPrice;
	// 全部收益
	private TextView tvAllIncomePrice;

	// 提现
	private LinearLayout llWithdraw;
	// 明细
	private LinearLayout llDetail;

	private float mMoney;

	// 本页面请求
	private GetCurrentPayPassExistRequest getCurrentPayPassExistRequest;
	private GetWalletRequest getWalletRequest;

	// 本页面请求ID
	private final int GET_CURRENT_WALLET = 1;
	private final int GET_CURRENT_PAYPASS_EXIST = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_my_wallet;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		tvWalletBalance = obtainView(R.id.tv_wallet_balance);
		llWithdraw = obtainView(R.id.ll_withdraw);
		llDetail = obtainView(R.id.ll_detail);
		tvFreeze = obtainView(R.id.tv_freeze);
		tvWaitPrice = obtainView(R.id.tv_wait_price);
		tvAllIncomePrice = obtainView(R.id.tv_all_income_price);
	}

	@Override
	protected void initData() {
		// httpPost(new GetWalletRequest(MyWalletActivity.this));
		// 获取我的钱包的请求
		getMyWalletData();

	}

	// 获取我的钱包的请求的数据
	private void getMyWalletData() {
		// TODO Auto-generated method stub
		getWalletRequest = new GetWalletRequest(MyWalletActivity.this);
		getWalletRequest.setRequestId(GET_CURRENT_WALLET);
		httpPost(getWalletRequest);
	}

	@Override
	protected void setListener() {
		llWithdraw.setOnClickListener(this);
		llDetail.setOnClickListener(this);

	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.my_wallet));
		setActionBarLeft(R.drawable.nav_back);
		// 设置进入安全中心
		setActionBarRight(true, 0, this.getString(R.string.my_wallet_safety));
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWalletActivity.this.finish();
			}
		});
		// setActionBarRight(false, 0, "");
		// 点击进入安全中心的监听
		setOnActionBarRightClickListener(false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到安全中心页面

				L.e("TAG", "------------------------------------");
				// MySafetyActivity.invoke(MyWalletActivity.this);
				// 从服务器获取支付密码是否已设置
				getDataFromServicePassExist();
			}
		});
	}

	/**
	 * 滚动数字
	 */
	private void startRiseTextView(float balanceNumber) {
		// 设置数据
		tvWalletBalance.withNumber(balanceNumber);
		// 设置动画播放时间
		tvWalletBalance.setDuration(1000);
		// 开始播放动画
		tvWalletBalance.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_withdraw: // 提现
			// 获得传过来的真实姓名和身份证号
			String tName = getIntent().getStringExtra("name");
			String tIDcode = getIntent().getStringExtra("iDcode");

			L.e("TAG", "---------------见来了=------");

			if (tName != null && tIDcode != null) {
				L.e("TAG", "+++++++++++++====见来了=------");
				WithdrawCashActivity.invoke(MyWalletActivity.this, mMoney + "",
						tName);
			} else {
				showToast("请先进行实名认证！");
			}
			break;
		case R.id.ll_detail: // 明细
			CrashDetailActivity.invoke(MyWalletActivity.this);
			break;
		}
	}

	/*
	 * 获取从服务器返回的数据判断支付密码是否已存在
	 */
	private void getDataFromServicePassExist() {
		getCurrentPayPassExistRequest = new GetCurrentPayPassExistRequest(
				MyWalletActivity.this);
		getCurrentPayPassExistRequest.setRequestId(GET_CURRENT_PAYPASS_EXIST);
		httpGet(getCurrentPayPassExistRequest);
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);

		L.i("TAG", "进入----------------------------");
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_CURRENT_WALLET:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				Wallet wallet = (Wallet) response.singleData;
				mMoney = wallet.accountBalance;
				float accountBalance = wallet.accountBalance;
				String accountBalanceLock = wallet.accountBalanceLock;
				String freight = wallet.freight;
				String totalIncome = wallet.totalIncome;

				int sMoney = (int) mMoney;

				// 缓存总金额
				if (mMoney != 0) {
					CacheUtils
							.putInt(MyWalletActivity.this, "SUMMONEY", sMoney);
				}

				if (!TextUtils.isEmpty(accountBalance + "")) {
					startRiseTextView(accountBalance);
				}

				if (!TextUtils.isEmpty(accountBalanceLock)
						&& accountBalanceLock != null) {
					tvFreeze.setText(accountBalanceLock);
				}

				if (!TextUtils.isEmpty(freight) && freight != null) {
					tvWaitPrice.setText(freight);
				}

				if (!TextUtils.isEmpty(totalIncome) && totalIncome != null) {
					tvAllIncomePrice.setText(totalIncome);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case GET_CURRENT_PAYPASS_EXIST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ResetPassActivity.invoke(MyWalletActivity.this);
			} else {
				// if (!TextUtils.isEmpty(message) && message != null) {
				// showToast(message);
				// }
				MySafetyActivity.invoke(MyWalletActivity.this);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	/**
	 * 我的钱包界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity, String tName, String tIDcode) {
		Intent intent = new Intent(activity, MyWalletActivity.class);
		intent.putExtra("name", tName);
		intent.putExtra("iDcode", tIDcode);
		activity.startActivity(intent);
	}

}
