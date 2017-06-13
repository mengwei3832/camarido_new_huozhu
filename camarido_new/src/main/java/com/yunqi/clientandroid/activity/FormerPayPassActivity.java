package com.yunqi.clientandroid.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.FormerPayPassCodeRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.MyCountTimer;

/**
 * 
 * @Description:class 忘记支付密码 输入验证码界面
 * @ClassName: FormerPayPassActivity
 * @author: zhm
 * @date: 2016-4-14 上午10:34:26
 * 
 */
public class FormerPayPassActivity extends BaseActivity implements
		View.OnClickListener {
	// 控件对象
	private TextView reset_textView_phone;// 需要填手机号
	private EditText et_input_getCode; // 填写验证码
	private TextView tv_click_getCode; // 点击获取验证码
	private String sTextPhone; // 经过屏蔽的电话号码
	private String userName; // 电话号码
	private final String USER_NAME = "USER_NAME";
	private Button bt_next;
	private Handler handler;
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	public String gCode;
	public String fCode;

	// 匹配短信中间的4个数字（验证码等）

	private String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";

	public FormerPayPassActivity() {
		super();
	}

	// 初始化handler的方法
	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!TextUtils.isEmpty(fCode)) {
					et_input_getCode.setText(fCode);
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
					L.d("logo", "message     " + message);
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
							fCode = code;
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
		return R.layout.activity_formet_paypass;
	}

	@Override
	protected void initView() {
		reset_textView_phone = (TextView) findViewById(R.id.reset_textView_phone);
		tv_click_getCode = (TextView) findViewById(R.id.tv_click_getCode);
		et_input_getCode = (EditText) findViewById(R.id.et_input_getCode);
		bt_next = (Button) findViewById(R.id.bt_next);
	}

	@Override
	protected void initData() {
		initActionBar();

		// TODO Auto-generated method stub
		Log.e("TAG", "手机号" + sTextPhone + "-------------");

		initHandler();

		// 获取手机号码（用户名）
		userName = CacheUtils.getString(this, USER_NAME, "");// 从缓存中获取用户

		// 显示电话号码
		if (!TextUtils.isEmpty(userName) && userName != null
				&& userName.length() >= 10) {
			sTextPhone = userName.substring(0, 3) + "*****"
					+ userName.substring(8, userName.length());
			reset_textView_phone.setText("证码将发送至您的" + sTextPhone + "。");
			// getPhoneTextView(usernumber, userName);
			CacheUtils.putString(this, USER_NAME, userName);
		}

		reset_textView_phone.setText("证码将发送至您的" + sTextPhone + "。");
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
				FormerPayPassActivity.this.finish();
			}
		});

	}

	@Override
	protected void setListener() {
		tv_click_getCode.setOnClickListener(this);
		bt_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_click_getCode: // 点击获取验证码
			requestCode();
			break;
		case R.id.bt_next:
			gCode = et_input_getCode.getText().toString().trim();

			Log.e("TAG", "yanzhengma-------------" + gCode);
			// 下一步
			if (!TextUtils.isEmpty(gCode) && gCode != null) {
				CommitNewPayPassActivity.invoke(FormerPayPassActivity.this,
						gCode);
			} else {
				showToast("请输入验证码");
			}
			break;
		default:
			break;
		}

	}

	// 请求服务器获取验证码的方法
	private void requestCode() {
		// 验证码倒计时
		MyCountTimer countTimer = new MyCountTimer(tv_click_getCode);
		countTimer.start();

		Log.e("TAG", "请求验证码的用户名:=========" + userName);

		// 请求服务器
		httpPost(new FormerPayPassCodeRequest(FormerPayPassActivity.this,
				userName));
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess = response.isSuccess;
		String message = response.message;
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, FormerPayPassActivity.class);
		context.startActivity(intent);
	}

}
