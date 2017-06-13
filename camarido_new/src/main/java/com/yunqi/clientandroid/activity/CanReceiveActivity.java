package com.yunqi.clientandroid.activity;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.GetTicketCashableUsers;
import com.yunqi.clientandroid.http.request.GetTicketCashableMsgRequest;
import com.yunqi.clientandroid.http.request.GetTicketCashableUsersRequest;
import com.yunqi.clientandroid.http.request.SetTicketCashableMoneyRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.MyCountTimer;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 待结算列表点进去的可领取分配页面
 * @date 15/12/16
 */
public class CanReceiveActivity extends BaseActivity implements OnClickListener {

	private String ticketId;// 订单id
	private int userId;
	private int user2Id;
	private String ticketCode;// 订单号
	private String vehicleNo;// 车牌号码
	private String createTime;// 创建订单的时间戳
	private String name;// 执行司机名字
	private String packageBeginName;// 起点名称
	private String packageEndName;// 终点名称
	private String shortMsg;
	private String userName;
	private String user2Name;
	private String strContent;
	// 匹配短信中间的4个数字（验证码等）
	private String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
	private String userMoney;
	private String user2Money;
	private String totalMoney;
	private double moneyOne;// 金额1
	private double moneyTwo;// 金额2

	private TextView tvTicketCode;
	private TextView tvVehicle;
	private TextView tvCreateTime;
	private TextView tvDriver;
	private TextView tvProvenance;
	private TextView tvDestination;
	private TextView tvTotalMoney;
	private TextView tvFreightPayable;
	private TextView tvShortfallDebit;
	private TextView tvQuestion;
	private EditText etDrivernameOne;
	private EditText etInputCode;
	private EditText etMoneyOne;
	private EditText etDrivernameTwo;
	private EditText etMoneyTwo;
	private Button btnVerification;
	private Button btnSubmit;
	private ProgressBar mProgress;
	private LinearLayout mllGlobal;

	private Handler handler;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	private BigDecimal bigMoneyOne;

	// 本页请求
	private GetTicketCashableUsersRequest mGetTicketCashableUsersRequest;
	private GetTicketCashableMsgRequest mGetTicketCashableMsgRequest;
	private SetTicketCashableMoneyRequest mSetTicketCashableMoneyRequest;

	// 本页面请求id
	private final int GET_TICKET_CASHABLEUSERS_REQUEST = 1;
	private final int GET_TICKET_CASHABLEMSG_REQUEST = 2;
	private final int GET_TICKET_CASHABLEMONEY_REQUEST = 3;

	// 初始化handler的方法
	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 判断是否有数据
				if (!TextUtils.isEmpty(strContent)) {
					btnVerification.setText(strContent);
				}
			}
		};

		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					Log.d("logo", "message     " + message);
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					Log.d("logo", "from     " + from);
					// Time time = new Time();
					// time.set(sms.getTimestampMillis());
					// String time2 = time.format3339(true);
					// Log.d("logo", from + "   " + message + "  " + time2);
					// strContent = from + "   " + message;
					// handler.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		registerReceiver(smsReceiver, filter);
	}

	/**
	 * 匹配短信中间的4个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_canreceive;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从当前运单界面传过来的数据
		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getString("ticketId");
		}
		if (bundle != null && bundle.containsKey("ticketCode")) {
			ticketCode = bundle.getString("ticketCode");
		}
		if (bundle != null && bundle.containsKey("vehicleNo")) {
			vehicleNo = bundle.getString("vehicleNo");
		}
		if (bundle != null && bundle.containsKey("createTime")) {
			createTime = bundle.getString("createTime");
		}
		if (bundle != null && bundle.containsKey("name")) {
			name = bundle.getString("name");
		}
		if (bundle != null && bundle.containsKey("packageBeginName")) {
			packageBeginName = bundle.getString("packageBeginName");
		}
		if (bundle != null && bundle.containsKey("packageEndName")) {
			packageEndName = bundle.getString("packageEndName");
		}

		tvTicketCode = (TextView) findViewById(R.id.tv_canreceive_ticketCode);
		tvVehicle = (TextView) findViewById(R.id.tv_canreceive_vehicle);
		tvCreateTime = (TextView) findViewById(R.id.tv_canreceive_createTime);
		tvDriver = (TextView) findViewById(R.id.tv_canreceive_driver);
		tvProvenance = (TextView) findViewById(R.id.tv_canreceive_provenance);
		tvDestination = (TextView) findViewById(R.id.tv_canreceive_destination);
		etDrivernameOne = (EditText) findViewById(R.id.et_canreveice_drivername_one);
		etMoneyOne = (EditText) findViewById(R.id.et_canreveice_money_one);
		etDrivernameTwo = (EditText) findViewById(R.id.et_canreveice_drivername_two);
		etMoneyTwo = (EditText) findViewById(R.id.et_canreveice_money_two);
		tvTotalMoney = (TextView) findViewById(R.id.tv_canreceive_totalMoney);
		tvFreightPayable = (TextView) findViewById(R.id.tv_canreceive_freightPayable);
		tvShortfallDebit = (TextView) findViewById(R.id.tv_canreceive_shortfallDebit);
		etInputCode = (EditText) findViewById(R.id.et_canreveice_inputCode);
		btnVerification = (Button) findViewById(R.id.btn_canreceive_verification);
		btnSubmit = (Button) findViewById(R.id.btn_canreceive_submit);
		tvQuestion = (TextView) findViewById(R.id.tv_canreceive_question);
		mProgress = (ProgressBar) findViewById(R.id.pb_canreceive_progress);
		mllGlobal = (LinearLayout) findViewById(R.id.ll_canreceive_global);

		// 设置订单号
		if (!TextUtils.isEmpty(ticketCode) && ticketCode != null) {
			tvTicketCode.setText("单号: " + ticketCode);
		}

		// 设置车牌号
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			tvVehicle.setText("车辆: " + vehicleNo);
		}

		// 设置订单下单时间
		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			String formatCanReceive = StringUtils.formatCanReceive(createTime);
			tvCreateTime.setText("时间: " + formatCanReceive);
		}

		// 显示执行人名称
		if (!TextUtils.isEmpty(name) && name != null) {
			tvDriver.setText("司机: " + name);
		}

		// 显示起点名称
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			tvProvenance.setText(packageBeginName);
		}

		// 显示终点名称
		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			tvDestination.setText(packageEndName);
		}

	}

	@Override
	protected void initData() {
		// 可领取页面获取领钱人数据
		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			getDataFormServiceCashableUsers(ticketId);
		}

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.myticket_distribution));
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(false, 0, "");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CanReceiveActivity.this.finish();
			}
		});
	}

	// 初始化点击事件
	private void initOnClick() {
		btnVerification.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		tvQuestion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_canreceive_verification:
			// 点击按钮请求服务器获取验证码
			MyCountTimer countTimer = new MyCountTimer(btnVerification);
			countTimer.start();

			mGetTicketCashableMsgRequest = new GetTicketCashableMsgRequest(
					this, ticketId);
			mGetTicketCashableMsgRequest
					.setRequestId(GET_TICKET_CASHABLEMSG_REQUEST);
			httpPost(mGetTicketCashableMsgRequest);

			break;

		case R.id.btn_canreceive_submit:
			// 提交
			String oneMoney = etMoneyOne.getText().toString().trim();
			String twoMoney = etMoneyTwo.getText().toString().trim();
			String inputCode = etInputCode.getText().toString().trim();
			String drivernameOne = etDrivernameOne.getText().toString().trim();
			String drivernameTwo = etDrivernameTwo.getText().toString().trim();

			// 判断姓名1是否为空
			if (TextUtils.isEmpty(drivernameOne)) {
				showToast("请输入司机1的姓名");
				return;
			}

			// 判断金额1是否为空
			if (TextUtils.isEmpty(oneMoney)) {
				showToast("请输入司机" + drivernameOne + "的金额");
				return;
			}
			// 金额1字符串强转成double
			moneyOne = Double.valueOf(oneMoney);
			bigMoneyOne = new BigDecimal(oneMoney);

			// 判断短信验证码是否为空
			if (TextUtils.isEmpty(inputCode)) {
				showToast("请输入短信验证码");
				return;
			}

			// 判断姓名2是否为空
			if (!TextUtils.isEmpty(drivernameTwo)) {
				// 判断金额2是否为空
				if (TextUtils.isEmpty(twoMoney)) {
					showToast("请输入司机" + drivernameTwo + "的金额");
					return;
				}
				// 字符串强转成double
				moneyTwo = Double.valueOf(twoMoney);
				BigDecimal bigMoneyTwo = new BigDecimal(twoMoney);

				double doubleValue = bigMoneyOne.add(bigMoneyTwo).doubleValue();

				BigDecimal moneySum = new BigDecimal(doubleValue);
				// 将总金额由String转换为double
				double totalMoneyDouble = Double.parseDouble(totalMoney);
				BigDecimal moneyTotal = new BigDecimal(totalMoneyDouble);

				if (moneySum.compareTo(moneyTotal) == 0) {
				} else {
					showToast("您分配的金额总数与总金额不符,请重新分配");
					return;
				}

				// 请求服务器提交
				mSetTicketCashableMoneyRequest = new SetTicketCashableMoneyRequest(
						this, ticketId, userId, moneyOne, user2Id, moneyTwo,
						totalMoney, inputCode);
				mSetTicketCashableMoneyRequest
						.setRequestId(GET_TICKET_CASHABLEMONEY_REQUEST);
				httpPost(mSetTicketCashableMoneyRequest);

			} else {
				// 没有司机2不用分配金额

				BigDecimal money1 = new BigDecimal(moneyOne);
				// 将总金额由String转换为double
				double totalMoneyDouble = Double.parseDouble(totalMoney);
				BigDecimal moneyTotal = new BigDecimal(totalMoneyDouble);

				if (money1.compareTo(moneyTotal) == 0) {
				} else {
					showToast("您分配的金额总数与总金额不符,请重新分配");
					return;
				}
				// 请求服务器提交
				mSetTicketCashableMoneyRequest = new SetTicketCashableMoneyRequest(
						this, ticketId, userId, moneyOne, totalMoney, inputCode);
				mSetTicketCashableMoneyRequest
						.setRequestId(GET_TICKET_CASHABLEMONEY_REQUEST);
				httpPost(mSetTicketCashableMoneyRequest);

			}

			// 设置提交按钮不可点击
			btnSubmit.setEnabled(false);

			break;

		case R.id.tv_canreceive_question:
			// 跳转到常见问题
			HelpActivity.invoke(CanReceiveActivity.this, "withdraw");
			break;

		default:
			break;
		}
	}

	// 可领取页面获取领钱人数据
	private void getDataFormServiceCashableUsers(String ticketId) {
		mllGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetTicketCashableUsersRequest = new GetTicketCashableUsersRequest(
				this, ticketId);
		mGetTicketCashableUsersRequest
				.setRequestId(GET_TICKET_CASHABLEUSERS_REQUEST);
		httpPost(mGetTicketCashableUsersRequest);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_TICKET_CASHABLEUSERS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取信息成功
				GetTicketCashableUsers getTicketCashableUsers = (GetTicketCashableUsers) response.singleData;

				userId = getTicketCashableUsers.userId;// 用户1的Id
				userName = getTicketCashableUsers.userName;// 用户1的名称
				user2Id = getTicketCashableUsers.user2Id;// 用户2的Id
				user2Name = getTicketCashableUsers.user2Name;// 用户2的名称
				userMoney = getTicketCashableUsers.userMoney;// 用户1的金额
				user2Money = getTicketCashableUsers.user2Money;// 用户2的金额
				totalMoney = getTicketCashableUsers.totalMoney;// 总金额
				shortMsg = getTicketCashableUsers.shortMsg;// 短信信息
				String freightPayable = getTicketCashableUsers.freightPayable;// 运费
				String shortfallDebit = getTicketCashableUsers.shortfallDebit;// 亏吨扣款

				// 设置用户1
				if (!TextUtils.isEmpty(userName) && userName != null) {
					etDrivernameOne.setText(userName);
					// 设置用户1的金额
					if (!TextUtils.isEmpty(userMoney) && userMoney != null) {
						etMoneyOne.setText(userMoney);
					}
				}
				// 设置用户2
				if (!TextUtils.isEmpty(user2Name) && user2Name != null) {
					etDrivernameTwo.setText(user2Name);
					// 设置用户2的金额
					if (!TextUtils.isEmpty(user2Money) && user2Money != null) {
						etMoneyTwo.setText(user2Money);
					}
				} else {
					// 设置金额2输入框不可获取焦点不能输入
					etMoneyTwo.setFocusable(false);
				}

				// 设置运费
				if (!TextUtils.isEmpty(freightPayable)
						&& freightPayable != null) {
					tvFreightPayable.setText(Html.fromHtml("运费:"
							+ "<font color='#ff4400'>" + freightPayable
							+ "</font>" + "元"));
					tvFreightPayable.setVisibility(View.VISIBLE);
				} else {
					tvFreightPayable.setVisibility(View.INVISIBLE);
				}
				// 设置亏吨扣款
				if (!TextUtils.isEmpty(shortfallDebit)
						&& shortfallDebit != null) {
					tvShortfallDebit.setText(Html.fromHtml("亏吨扣款:"
							+ "<font color='#ff4400'>" + shortfallDebit
							+ "</font>" + "元"));
					tvShortfallDebit.setVisibility(View.VISIBLE);
				} else {
					tvShortfallDebit.setVisibility(View.INVISIBLE);
				}

				// 设置总金额
				if (!TextUtils.isEmpty(totalMoney) && totalMoney != null) {
					tvTotalMoney.setText(Html.fromHtml("总金额:"
							+ "<font color='#ff4400'>" + totalMoney + "</font>"
							+ "元"));
					tvTotalMoney.setVisibility(View.VISIBLE);
				} else {
					tvTotalMoney.setVisibility(View.INVISIBLE);
				}

			} else {
				// 获取数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 显示界面
			mllGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;

		case GET_TICKET_CASHABLEMSG_REQUEST:
			// 请求服务器获取短信成功
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取短信验证码成功
			} else {
				// 获取短信验证码失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			break;

		case GET_TICKET_CASHABLEMONEY_REQUEST:

			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 请求服务器提交分配金额成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前界面
				CanReceiveActivity.this.finish();

			} else {
				// 请求服务器提交分配金额失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 设置提交按钮可点击
				btnSubmit.setEnabled(true);
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		switch (requestId) {
		case GET_TICKET_CASHABLEUSERS_REQUEST:
			showToast(this.getResources().getString(R.string.net_error_toast));
			break;

		case GET_TICKET_CASHABLEMONEY_REQUEST:
			showToast(this.getResources().getString(R.string.net_error_toast));
			// 设置提交按钮可点击
			btnSubmit.setEnabled(true);
			break;

		default:
			break;
		}

	}

	// 当前界面的跳转方法
	public static void invoke(Context activity, String ticketId,
			String ticketCode, String vehicleNo, String createTime,
			String name, String packageBeginName, String packageEndName) {
		Intent intent = new Intent();
		intent.setClass(activity, CanReceiveActivity.class);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketCode", ticketCode);
		intent.putExtra("vehicleNo", vehicleNo);
		intent.putExtra("createTime", createTime);
		intent.putExtra("name", name);
		intent.putExtra("packageBeginName", packageBeginName);
		intent.putExtra("packageEndName", packageEndName);
		activity.startActivity(intent);
	}

}
