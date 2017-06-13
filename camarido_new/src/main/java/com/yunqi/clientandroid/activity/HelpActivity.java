package com.yunqi.clientandroid.activity;

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

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.request.HelpRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 帮助界面
 * @date 15/12/15
 */
public class HelpActivity extends BaseActivity {
	private TextView tvTitle;
	private TextView tvContent;
	private Message mMessage;
	private WebView wv_register;
	private String tag;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		// tvTitle = obtainView(R.id.tv_help_title);
		// tvContent = obtainView(R.id.tv_help_content);
		wv_register = (WebView) findViewById(R.id.wv_register);
	}

	@Override
	protected void initData() {
		// httpPost(new HelpRequest(HelpActivity.this));
		// 得到传归来的数据
		tag = getIntent().getStringExtra("tag");

		WebSettings webSettings = wv_register.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 加载需要显示的网页

		wv_register.loadUrl(HostUtil.getWebHost() + "pfh/Help?tag=" + tag);

		// 设置Web视图
		wv_register.setWebViewClient(new webViewClient());
//		wv_register.addJavascriptInterface(this, "js");
	}
	
//	@JavascriptInterface
//	public void H5ToAndroid(String str){
////		js.H5ToAndroid(String);
//		
//	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	
	}

	@Override
	protected void setListener() {

	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.help));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HelpActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
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
			// 获取帮助信息成功
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
			// 获取帮助信息失败
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
	public static void invoke(Context context, String tag) {
		Intent intent = new Intent(context, HelpActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}

}
