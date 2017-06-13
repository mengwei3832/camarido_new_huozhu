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
import com.yunqi.clientandroid.http.request.UploadNewPayPassRequest;
import com.yunqi.clientandroid.http.request.UploadPassRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.InputPassUtils;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 
 * @Description:class 忘记支付密码后的再次输入密码
 * @ClassName: CommitNewPayPassActivity
 * @author: zhm
 * @date: 2016-4-14 上午10:32:01
 * 
 */
public class CommitNewPayPassActivity extends BaseActivity implements
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
	private String passOne, passTwo;
	private int count = 1;
	private FormerPayPassActivity formerPayPass;
	private String fCode;

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
				CommitNewPayPassActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		tv_show_password.setText("请输入密码");

		// 实例化键盘集合
		list = new ArrayList<String>();
		// 把键盘的数据添加进集合中
		for (int i = 0, len = InputPassUtils.dataArray.length; i < len; i++) {
			list.add(InputPassUtils.dataArray[i]);
		}

		// 对GridView的监听
		gAdapter = new GridViewKeybroadAdapter(list,
				CommitNewPayPassActivity.this);
		gv_pass_keyboard.setAdapter(gAdapter);

		formerPayPass = new FormerPayPassActivity();
		fCode = formerPayPass.gCode;

		Log.e("TAG", "验证码---------------------" + fCode);
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
		String md5Password = StringUtils.md5(passTwo);

		String gCode = getIntent().getStringExtra("gCode");

		Log.e("TAG", "加密后的密码:" + md5Password);

		Log.e("TAG", "出啊过来的验证码:" + gCode);
		// TODO
		httpPost(new UploadNewPayPassRequest(CommitNewPayPassActivity.this,
				md5Password, gCode));
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
			showToast("设置支付密码成功!");
			// bt_pass_complete.setEnabled(true);
			ResetPassActivity.invoke(CommitNewPayPassActivity.this);
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
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
			isPass();

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

	// 对密码框 和判断密码的操作
	private void isPass() {
		switch (count) {
		case 1:
			tv_show_password.setText("请再次输入密码");
			if (passList.size() == 6) {
				count = 2;
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
					passTV = (TextView) findViewById(passID[i]);
					passTV.setText("");
				}
				passOne = buffer.toString();
				passList.clear();
			}
			break;
		case 2:
			if (passList.size() == 6) {
				count = 2;
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
				}
				passTwo = buffer.toString();
				if (!passOne.equals(passTwo)) {
					// Toast.makeText(InputPasswordActivity.this, "两次输入密码不一致",
					// Toast.LENGTH_SHORT).show();
					showToast("两次输入密码不一致");
				} else {
					gv_pass_keyboard.setVisibility(View.GONE);
					bt_pass_complete.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity, String gCode) {
		Intent intent = new Intent(activity, CommitNewPayPassActivity.class);
		intent.putExtra("gCode", gCode);
		activity.startActivity(intent);
	}

}
