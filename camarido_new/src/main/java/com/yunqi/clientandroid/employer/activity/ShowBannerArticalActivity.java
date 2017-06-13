package com.yunqi.clientandroid.employer.activity;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebSettings;

import android.widget.ProgressBar;

/**
 * 
 * @Description:货主广告轮播图详情界面
 * @ClassName: ShowBannerInfoActivity
 * @author: chengtao
 * @date: Jul 14, 2016 1:45:11 PM
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class ShowBannerArticalActivity extends BaseActivity {
	private WebView wvBannerInfo;
	private ProgressBar progressBar;
	private final static String BANNER_URL = "BANNER_URL";
	private String bannerUrl;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_show_bannner_artical;
	}

	@Override
	protected void initView() {
		wvBannerInfo = obtainView(R.id.wv_banner_info);
		progressBar = obtainView(R.id.progress_bar);
		initActionBar();
	}

	private void initActionBar() {
		setActionBarTitle("");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		bannerUrl = getIntent().getStringExtra(BANNER_URL) + "";
		WebSettings webSettings = wvBannerInfo.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		wvBannerInfo.setWebViewClient(new webViewClient());
		wvBannerInfo.setWebChromeClient(new WebChromeClient());
		webSettings.setBuiltInZoomControls(false);
		webSettings.setDomStorageEnabled(true);

		// 如果有缓存 就使用缓存数据 如果没有 就从网络中获取
		wvBannerInfo.getSettings().setCacheMode(
				WebSettings.LOAD_CACHE_ELSE_NETWORK);
		wvBannerInfo.getSettings().setDatabaseEnabled(true);
		wvBannerInfo.getSettings().setAppCacheEnabled(true);

		wvBannerInfo.loadUrl(bannerUrl);
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
			wvBannerInfo.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			showToast(R.string.net_error_toast);
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void setListener() {

	}

	/**
	 * 
	 * @Description:
	 * @Title:invoke
	 * @return:void
	 * @throws
	 * @Create: Jul 14, 2016 1:48:19 PM
	 * @Author : chengtao
	 */
	public static void invoke(Context context, String url) {
		Intent intent = new Intent(context, ShowBannerArticalActivity.class);
		intent.putExtra(BANNER_URL, url);
		context.startActivity(intent);
	}
}
