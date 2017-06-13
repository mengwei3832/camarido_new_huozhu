package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.request.GetCurrentPayPassExistRequest;
import com.yunqi.clientandroid.http.request.WithdrawMsgRequest;
import com.yunqi.clientandroid.http.request.WithdrawRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 提现界面
 * @date 15/12/13
 */
public class WithdrawCashActivity extends BaseActivity implements
		View.OnClickListener {
	// 提现接口
	private WithdrawRequest mWithdrawRequest;
	// 提现短信接口
	private WithdrawMsgRequest mWithdrawMsgRequest;

	private GetCurrentPayPassExistRequest getCurrentPayPassExistRequest;

	private final int WITHDRAW_REQUEST = 1;
	private final int WITHDRAW_MSG_REQUEST = 2;
	private final int GET_CURRENT_PAYPASS_EXIST = 3;

	private static final String EXTRA_MONEY = "MONEY";

	// 控件
	private EditText etName;
	private EditText etCardNo;
	private EditText etMoney;
	private EditText etIdentifyingCode;
	private Button btnCommit;
	private Button btnIdentify;
	private LinearLayout llSelectBank;
	private TextView tvBank;
	private TextView tvMoney;
	private TextView tvQuestion;

	// 数据
	private String mName;
	private String mCardNo;
	private String mMoney;
	// private String mIdentifyingCode;
	private VehicleType mBank;

	private TimeCount mTime;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_withdrawcash;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		etName = obtainView(R.id.et_name);
		etCardNo = obtainView(R.id.et_card_number);
		etMoney = obtainView(R.id.et_amount);
		// etIdentifyingCode = obtainView(R.id.et_identifying_code);
		btnCommit = obtainView(R.id.btn_commit);
		// btnIdentify = obtainView(R.id.btn_identfy);
		llSelectBank = obtainView(R.id.ll_select_bank);
		tvBank = obtainView(R.id.tv_bank);
		tvMoney = obtainView(R.id.tv_can_withdraw);
		tvQuestion = obtainView(R.id.tv_question);
	}

	@Override
	protected void initData() {
		String tName = CacheUtils.getString(WithdrawCashActivity.this, "Name",
				"");

		// String tName = getIntent().getStringExtra("name");
		etName.setText(tName);
		// 让EditText失去焦点
		etName.clearFocus();
		etName.setFocusable(false);

		mTime = new TimeCount(60000, 1000);// 构造CountDownTimer对象

		// String money =
		// String.valueOf(CacheUtils.getInt(WithdrawCashActivity.this,
		// "SUMMONEY", 0));
		String money = getIntent().getStringExtra(EXTRA_MONEY);
		Log.e("TAG", "可提现金额" + money);

		// String money = getIntent().getStringExtra(EXTRA_MONEY);
		if (!TextUtils.isEmpty(money) && money != null) {
			tvMoney.setText(Html.fromHtml("可提现金额<font color='#ff4400'>" + money
					+ "</font>元"));
		}

	}

	@Override
	protected void setListener() {
		btnCommit.setOnClickListener(this);
		// btnIdentify.setOnClickListener(this);l
		llSelectBank.setOnClickListener(this);
		tvQuestion.setOnClickListener(this);
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.wallet_withdraw));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WithdrawCashActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit: // 确认
			mName = etName.getText().toString().trim();
			mCardNo = etCardNo.getText().toString().trim();
			mMoney = etMoney.getText().toString().trim();
			// mIdentifyingCode = etIdentifyingCode.getText().toString().trim();

			// 缓存银行卡号
			CacheUtils.putString(WithdrawCashActivity.this, "YINHANGKAHAO",
					mCardNo);
			// 缓存提取的金额
			CacheUtils.putString(WithdrawCashActivity.this, "TIQUJINE", mMoney);

			if (mName == null || "".equals(mName.trim())) {
				showToast("请输入姓名");
				return;
			}

			if (mCardNo == null || "".equals(mCardNo.trim())) {
				showToast("请输入卡号");
				return;
			}

			if (mBank == null) {
				showToast("请选择银行");
			}

			if (mMoney == null || "".equals(mMoney.trim())) {
				showToast("请输入金额");
				return;
			}

			// if (mIdentifyingCode == null ||
			// "".equals(mIdentifyingCode.trim())) {
			// showToast("请输入验证码");
			// return;
			// }

			// mWithdrawRequest = new WithdrawRequest(WithdrawCashActivity.this,
			// mName, mCardNo, mBank.k, mMoney, mIdentifyingCode);
			// mWithdrawRequest.setRequestId(WITHDRAW_REQUEST);
			// httpPost(mWithdrawRequest);

			// 从服务器获取支付密码是否已设置
			getDataFromServicePassExist();
			// AdvanceInputPayPassActivity.invoke(WithdrawCashActivity.this,
			// mName, mCardNo, mBank.k, mMoney);

			break;

		case R.id.ll_select_bank: // 选择银行
			BankListActivity.invoke(WithdrawCashActivity.this);
			break;
		case R.id.tv_question: // 常见问题
			HelpActivity.invoke(WithdrawCashActivity.this, "withdraw");
			break;
		}
	}

	// 从服务器获取支付密码是否已设置
	private void getDataFromServicePassExist() {
		getCurrentPayPassExistRequest = new GetCurrentPayPassExistRequest(
				WithdrawCashActivity.this);
		getCurrentPayPassExistRequest.setRequestId(GET_CURRENT_PAYPASS_EXIST);
		httpGet(getCurrentPayPassExistRequest);
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
		case WITHDRAW_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				WithdrawCashActivity.this.finish();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case WITHDRAW_MSG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
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
				AdvanceInputPayPassActivity.invoke(WithdrawCashActivity.this,
						mName, mCardNo, mBank.k, mMoney);
			} else {
				// if (!TextUtils.isEmpty(message) && message != null) {
				// showToast(message);
				// }
				MySafetyActivity.invoke(WithdrawCashActivity.this);
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnIdentify.setText("重新获取");
			btnIdentify.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnIdentify.setClickable(false);
			btnIdentify.setTextColor(WithdrawCashActivity.this.getResources()
					.getColor(R.color.attention_first_text_color));
			btnIdentify.setBackgroundColor(WithdrawCashActivity.this
					.getResources().getColor(R.color.hintColor));
			btnIdentify.setText(millisUntilFinished / 1000 + "秒");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case BankListActivity.RESULT_CODE:
			mBank = (VehicleType) data
					.getSerializableExtra(BankListActivity.EXTRA_BANK);
			String v = mBank.v;
			// 设置选中的银行
			if (!TextUtils.isEmpty(v) && v != null) {
				tvBank.setText(v);
				CacheUtils.putString(WithdrawCashActivity.this, "YINHANG",
						mBank.v);
			}
			break;

		case AdvanceInputPayPassActivity.RESULT_CODE_PAY:
			// 获取银行
			String cbank = CacheUtils.getString(WithdrawCashActivity.this,
					"YINHANG", "");
			tvBank.setText(cbank);
			// 获取卡号
			String cbankkaohao = CacheUtils.getString(
					WithdrawCashActivity.this, "YINHANGKAHAO", "");
			etCardNo.setText(cbankkaohao);
			// 获取提取的金额
			String ctiqujine = CacheUtils.getString(WithdrawCashActivity.this,
					"TIQUJINE", "");
			etMoney.setText(ctiqujine);

			break;

		default:
			break;
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, String money, String tName) {
		Intent intent = new Intent(context, WithdrawCashActivity.class);
		intent.putExtra(EXTRA_MONEY, money);
		intent.putExtra("name", tName);
		context.startActivity(intent);
	}

}
