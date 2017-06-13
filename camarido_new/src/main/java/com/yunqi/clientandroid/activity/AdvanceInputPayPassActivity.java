package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.GridViewKeybroadAdapter;
import com.yunqi.clientandroid.entity.UploadSettingPayPwd;
import com.yunqi.clientandroid.http.request.UploadPassRequest;
import com.yunqi.clientandroid.http.request.WithdrawRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.InputPassUtils;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 
 * @Description:class 提现输入密码
 * @ClassName: AdvanceInputPayPassActivity
 * @author: zhm
 * @date: 2016-4-14 上午8:59:37
 * 
 */
public class AdvanceInputPayPassActivity extends BaseActivity implements
		View.OnClickListener, OnItemClickListener {
	private TextView tv_show_password;// 输入密码
	private GridView gv_pass_keyboard;
	private Button bt_pass_complete;
	private GridViewKeybroadAdapter gAdapter;
	// 密码框TextView的ID
	private int[] passID = { R.id.tv_pass_one, R.id.tv_pass_two,
			R.id.tv_pass_three, R.id.tv_pass_four, R.id.tv_pass_fives,
			R.id.tv_pass_six };
	private TextView passTV = null;
	private List<String> list = null; // 存储键盘数据的集合
	private List<String> passList = new ArrayList<String>();// 存储所输密码的集合
	private String passOne;
	private String md5Password, tName, cardNo, money;
	private int cardType;

	public final static int RESULT_CODE_PAY = 0x02;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_input_pass;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		tv_show_password = (TextView) findViewById(R.id.tv_show_password);
		gv_pass_keyboard = (GridView) findViewById(R.id.gv_pass_keyboard);
		bt_pass_complete = (Button) findViewById(R.id.bt_pass_complete);

		bt_pass_complete.setText("下一步");
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
				AdvanceInputPayPassActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 赋值
		tv_show_password.setText("请输入支付密码");

		// 实例化键盘集合
		list = new ArrayList<String>();
		// 把键盘的数据添加进集合中
		for (int i = 0, len = InputPassUtils.dataArray.length; i < len; i++) {
			list.add(InputPassUtils.dataArray[i]);
		}

		// 对GridView的监听
		gAdapter = new GridViewKeybroadAdapter(list,
				AdvanceInputPayPassActivity.this);
		gv_pass_keyboard.setAdapter(gAdapter);

	}

	@Override
	protected void setListener() {
		// 设置密码成功的监听
		bt_pass_complete.setOnClickListener(this);
		// GridView的点击监听
		gv_pass_keyboard.setOnItemClickListener(this);
	}

	// 点击监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_pass_complete: // 设置密码成功
			// showToast("成功设置密码！");
			passList.clear();
			// 将获取到的密码上传到后台服务器
			uploadPayPass();

			break;

		default:
			break;
		}

	}

	// 将获取到的密码上传到后台服务器的请求
	private void uploadPayPass() {
		// 对得到的密码进行MD5加密
		md5Password = StringUtils.md5(passOne);
		tName = getIntent().getStringExtra("name");// 真实姓名
		cardNo = getIntent().getStringExtra("cardNo");//
		cardType = getIntent().getIntExtra("cardType", 0);
		money = getIntent().getStringExtra("money");

		Log.e("TAG", "加密后的密码:" + md5Password);
		// TODO
		httpPost(new WithdrawRequest(AdvanceInputPayPassActivity.this, tName,
				cardNo, cardType, money, md5Password));
		// 设置完成按钮不可点击
		// bt_pass_complete.setEnabled(false);
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

		if (isSuccess) {
			// showToast("设置支付密码成功!");
			// bt_pass_complete.setEnabled(true);
			AdvanceDetailsActivity.invoke(AdvanceInputPayPassActivity.this,
					money, cardNo, cardType);
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
			Intent intent = new Intent(AdvanceInputPayPassActivity.this,
					WithdrawCashActivity.class);
			setResult(RESULT_CODE_PAY, intent);
			AdvanceInputPayPassActivity.this.finish();
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// GridView的点击监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String dPass = list.get(position).toString();
		// 判断 如果点击的X号 同时passList集合中的数据不为空
		if (dPass.equals("×") && passList.size() > 0) {
			// 给密码框最后一个赋值为" "(空)
			passTV = (TextView) findViewById(passID[passList.size() - 1]);
			passTV.setText("");
			// 删除passList最后面的值
			passList.remove(passList.size() - 1);
		} else if (dPass.equals("完成")) {
			// 对密码框 和判断密码的操作
			if (passList.size() == 6) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
					// passTV = (TextView) findViewById(passID[i]);
					// passTV.setText("");
				}
				passOne = buffer.toString();
				gv_pass_keyboard.setVisibility(View.GONE);
				bt_pass_complete.setVisibility(View.VISIBLE);
			}
		} else if (!dPass.equals("完成") && !dPass.equals("×")) {
			// 如果点击的是数字 添加到集合中 并给密码框赋值*
			if (passList.size() <= 5) {
				passList.add(dPass);
				passTV = (TextView) findViewById(passID[passList.size() - 1]);
				passTV.setText("●");
			}
		}

		Log.i("Log", passList.size() + "个数据");
	}

	// // 对密码框 和判断密码的操作
	// private void isPass() {
	// switch (count) {
	// case 1:
	// tv_show_password.setText("请再次输入密码");
	// if (passList.size() == 6) {
	// StringBuffer buffer = new StringBuffer();
	// for (int i = 0; i < passList.size(); i++) {
	// buffer.append(passList.get(i).toString());
	// passTV = (TextView) findViewById(passID[i]);
	// passTV.setText("");
	// }
	// passOne = buffer.toString();
	// }
	// break;
	//
	// }
	// }

	// 本界面的跳转方法
	public static void invoke(Activity activity, String name, String cardNo,
			int cardType, String money) {
		Intent intent = new Intent(activity, AdvanceInputPayPassActivity.class);

		intent.putExtra("name", name);
		intent.putExtra("cardNo", cardNo);
		intent.putExtra("cardType", cardType);
		intent.putExtra("money", money);
		activity.startActivity(intent);
	}

}
