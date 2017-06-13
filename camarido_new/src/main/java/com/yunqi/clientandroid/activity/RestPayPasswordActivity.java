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
import com.yunqi.clientandroid.http.request.OldPassIsTrueRequest;
import com.yunqi.clientandroid.http.request.UploadPassRequest;
import com.yunqi.clientandroid.http.request.UploadResetPassRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.InputPassUtils;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 
 * @Description:class 重置支付密码 输入密码页面
 * @ClassName: RestPayPasswordActivity
 * @author: zhm
 * @date: 2016-4-14 上午10:35:21
 * 
 */
public class RestPayPasswordActivity extends BaseActivity implements
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
	private String passOld, passNewOne, passNewTwo;
	private int count = 0;

	// 本页面请求类
	private UploadResetPassRequest uploadResetPassRequest;
	private OldPassIsTrueRequest oldPassIsTrueRequest;

	// 本页面请求ID
	private final int GET_OLD_PASS_ISTRUE = 1;
	private final int GET_RESET_PAY_PASS = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_input_reset_pass;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		tv_show_password = (TextView) findViewById(R.id.tv_show_password);
		gv_pass_keyboard = (GridView) findViewById(R.id.gv_pass_keyboard);
		bt_pass_complete = (Button) findViewById(R.id.bt_pass_complete);

		isPass();

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
				RestPayPasswordActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 对上方的textView进行修改
		tv_show_password.setText("请输入旧密码");

		// 实例化键盘集合
		list = new ArrayList<String>();
		// 把键盘的数据添加进集合中
		for (int i = 0, len = InputPassUtils.dataArray.length; i < len; i++) {
			list.add(InputPassUtils.dataArray[i]);
		}

		// 对GridView的监听
		gAdapter = new GridViewKeybroadAdapter(list,
				RestPayPasswordActivity.this);
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
		// 对密码进行Md5加密
		String md5FormerPassword = StringUtils.md5(passOld);
		String md5NewPassword = StringUtils.md5(passNewTwo);

		// TODO
		uploadResetPassRequest = new UploadResetPassRequest(
				RestPayPasswordActivity.this, md5FormerPassword, md5NewPassword);
		uploadResetPassRequest.setRequestId(GET_RESET_PAY_PASS);
		httpPost(uploadResetPassRequest);
		// 设置注册按钮不可点击
		// setViewEnable(false);
	}

	// private void setViewEnable(boolean bEnable) {
	// if (bEnable) {
	// bt_pass_complete.setText("完成");
	// } else {
	// bt_pass_complete.setText("重置中 ...");
	// }
	// bt_pass_complete.setEnabled(bEnable);
	//
	// }

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
		case GET_RESET_PAY_PASS:// 上传密码
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// TODO 请求数据的结果
				// 重置成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 回到重置的页面
				MyWalletActivity.invoke(RestPayPasswordActivity.this, null,
						null);

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				count = 0;

				gv_pass_keyboard.setVisibility(View.VISIBLE);
				bt_pass_complete.setVisibility(View.GONE);
				for (int i = 0; i < 6; i++) {
					passTV = (TextView) findViewById(passID[i]);
					passTV.setText("");
				}

				isPass();
			}
			break;
		case GET_OLD_PASS_ISTRUE:// 判断旧密码是否正确
			isSuccess = response.isSuccess;
			message = response.message;

			Log.e("TAG", "返回的数据--------------" + response.isSuccess);

			Log.e("TAG", "返回的数据--------------" + response.message);

			if (isSuccess) {

				tv_show_password.setText("请输入新密码");
				count = 1;
			} else {
				tv_show_password.setText("请输入旧密码");
				count = 0;
				showToast(message);
			}
			break;

		default:
			break;
		}

		// 设置注册按钮可点击
		// setViewEnable(true);
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
		case 0:
			tv_show_password.setText("请输入旧密码");
			if (passList.size() == 6) {
				// count = 1;
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
					passTV = (TextView) findViewById(passID[i]);
					passTV.setText("");
				}
				passOld = buffer.toString();
				// 请求进行判断旧密码是否正确
				getOldPassIsTrue(passOld);
				passList.clear();
				// tv_show_password.setText("请输入新密码");
			}
			break;
		case 1:
			if (passList.size() == 6) {
				count = 2;
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
					passTV = (TextView) findViewById(passID[i]);
					passTV.setText("");
				}
				passNewOne = buffer.toString();
				passList.clear();
				tv_show_password.setText("请再次输入新密码");
			}
			break;
		case 2:
			if (passList.size() == 6) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < passList.size(); i++) {
					buffer.append(passList.get(i).toString());
				}
				passNewTwo = buffer.toString();
				if (!passNewOne.equals(passNewTwo)) {
					showToast("两次输入密码不一致");
				} else {
					gv_pass_keyboard.setVisibility(View.GONE);
					bt_pass_complete.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
	}

	// 请求进行判断旧密码是否正确
	private void getOldPassIsTrue(String oldPwdPay) {
		// 对密码进行Md5加密
		String md5FormerPassword = StringUtils.md5(oldPwdPay);

		oldPassIsTrueRequest = new OldPassIsTrueRequest(
				RestPayPasswordActivity.this, md5FormerPassword);
		oldPassIsTrueRequest.setRequestId(GET_OLD_PASS_ISTRUE);
		httpPost(oldPassIsTrueRequest);
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, RestPayPasswordActivity.class);
		activity.startActivity(intent);
	}

}
