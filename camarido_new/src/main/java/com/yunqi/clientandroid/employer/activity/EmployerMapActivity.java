package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.HelpActivity;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.util.SyncCookie;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;

/**
 * @Description:地图的预览网页
 * @ClassName: EmployerMapActivity
 * @author: mengwei
 * @date: 2016-7-16 下午4:30:46
 * 
 */
public class EmployerMapActivity extends BaseActivity {
	private WebView wv_register;
	private String ticketCode;
	private String ticketId;
	private ProgressWheel pbHelpBar;

	private SyncCookie mSyncCookie;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registermessage;
	}

	@Override
	protected void initView() {
		initActionBar();
		mSyncCookie = new SyncCookie(mContext);
		ticketCode = getIntent().getStringExtra("TICKET_CODE");
		ticketId = getIntent().getStringExtra("TICKET_ID");
		wv_register = obtainView(R.id.wv_register_map);
		pbHelpBar = obtainView(R.id.pb_help_bar);
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle("");
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EmployerMapActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	protected void initData() {
		WebSettings webSettings = wv_register.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置Web视图
		wv_register.setWebViewClient(new webViewClient());
		wv_register.setWebChromeClient(new WebChromeClient());
		wv_register.getSettings().setBuiltInZoomControls(false);
		wv_register.getSettings().setDomStorageEnabled(true);
		// 如果有缓存 就使用缓存数据 如果没有 就从网络中获取
		wv_register.getSettings().setCacheMode(
				WebSettings.LOAD_NO_CACHE);
		wv_register.getSettings().setDatabaseEnabled(true);
		wv_register.getSettings().setAppCacheEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 加载需要显示的网页

		L.e("---------ticketId-----------"+ticketId);
		L.e("-----------ticketCode--------------"+ticketCode);
		String token = PreManager.instance(mContext).getToken();
		String mUrl = HostPkgUtil.getApiHost()
				+ "LogisticsLine/LogisticsLine?ticketId="+ticketId+"&ticketCode=" + ticketCode+"&YQ_API_TOKEN="+token;

//		synCookies(mContext,mUrl);

		//http://pkgapi.yqtms.com/logisticsLine/logisticsline?ticketid=12345&ticketcode=sdfsdfdsdf
		wv_register.loadUrl(mUrl,mSyncCookie.syncCookie(mUrl));

	}

	/**
	 * 同步一下cookie
	 * @param context
	 * @param url
	 */
	public static void synCookies(Context context,String url){
		String token = PreManager.instance(context).getToken();
		String cookies = String.format("YQ_API_TOKEN=%s", token);
		L.e("=======cookies========"+cookies);
		CookieSyncManager.createInstance(context);
		CookieManager cookiemanager = CookieManager.getInstance();
		cookiemanager.setAcceptCookie(true);
		cookiemanager.removeSessionCookie();
		cookiemanager.removeAllCookie();
		cookiemanager.removeExpiredCookie();
		cookiemanager.flush();
		cookiemanager.setCookie(url,cookies);
		CookieSyncManager.getInstance().sync();
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			setShowOrHide(true);
		}

//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			return true;
//		}


		@Override
		public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
			L.d("==== WebView ajax 请求 ====", url);
//			synCookies(mContext,url);
			mSyncCookie.syncCookie(url);
			//将加好cookie的url传给父类继续执行
			return super.shouldInterceptRequest(webView, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			setShowOrHide(false);
		}
	}

	private void setShowOrHide(boolean isShow){
		if (isShow){
			wv_register.setVisibility(View.GONE);
			pbHelpBar.setVisibility(View.VISIBLE);
		} else {
			wv_register.setVisibility(View.VISIBLE);
			pbHelpBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.e("------------清除缓存-----------------");
		wv_register.clearCache(true);
		wv_register.clearHistory();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, String ticketCode, String ticketId) {
		Intent intent = new Intent(context, EmployerMapActivity.class);
		intent.putExtra("TICKET_ID", ticketId);
		intent.putExtra("TICKET_CODE", ticketCode);
		context.startActivity(intent);
	}

}
