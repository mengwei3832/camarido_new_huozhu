package com.yunqi.clientandroid.activity;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.RegisterMessageRequest;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.TextView;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 注册协议界面
 * @date 15/12/24
 */
public class RegisterMessageActivity extends BaseActivity {

	private TextView tvTitle;
	private TextView tvContent;
	private Message mMessage;
	private WebView wv_register;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		// 初始化tiitleBar
		initActionBar();

		tvTitle = obtainView(R.id.tv_title);
		tvContent = obtainView(R.id.tv_content);
		wv_register = (WebView) findViewById(R.id.wv_register);
	}

	@Override
	protected void initData() {
		WebSettings webSettings = wv_register.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 加载需要显示的网页
		wv_register.loadUrl("http://qa.yqtms.com/pfh/RegisterProtocol");
		// 设置Web视图
		wv_register.setWebViewClient(new webViewClient());
		// httpPost(new RegisterMessageRequest(RegisterMessageActivity.this));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.register_agreement));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterMessageActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;

		if (isSuccess) {
			mMessage = (Message) response.singleData;
			String messageTitle = mMessage.messageTitle;
			String messageContent = mMessage.messageContent;

			if (!TextUtils.isEmpty(messageTitle) && messageTitle != null) {
				tvTitle.setText(Html.fromHtml(messageTitle));
			}

			if (!TextUtils.isEmpty(messageContent) && messageContent != null) {
				tvContent.setText(Html.fromHtml(messageContent));
			}

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

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context activity) {
		Intent intent = new Intent();
		intent.setClass(activity, RegisterMessageActivity.class);
		activity.startActivity(intent);
	}

}
