package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
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
public class HelpPkgActivity extends BaseActivity {
	private WebView wv_register;
	private ProgressBar pbBar;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		wv_register = obtainView(R.id.wv_register);
		pbBar = obtainView(R.id.pb_help_bar);
	}

	@Override
	protected void initData() {
		pbBar.setVisibility(View.VISIBLE);
		// 设置Web视图
		wv_register.setWebViewClient(new webViewClient());
		wv_register.setWebChromeClient(new WebChromeClient());
		wv_register.getSettings().setBuiltInZoomControls(false);
		wv_register.getSettings().setDomStorageEnabled(true);
		// 如果有缓存 就使用缓存数据 如果没有 就从网络中获取
		wv_register.getSettings().setCacheMode(
				WebSettings.LOAD_CACHE_ELSE_NETWORK);
		wv_register.getSettings().setDatabaseEnabled(true);
		wv_register.getSettings().setAppCacheEnabled(true);
		// 加载需要显示的网页
		wv_register.loadUrl(HostUtil.getWebHost() + "pfh/ShipperHelp");
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			pbBar.setVisibility(View.GONE);
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
				HelpPkgActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, HelpPkgActivity.class);
		context.startActivity(intent);
	}

}
