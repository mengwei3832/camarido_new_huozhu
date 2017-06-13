package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.utils.L;

/**
 * 
 * @Description:class 保险的页面
 * @ClassName: PayMentActivity
 * @author: zhm
 * @date: 2016-4-25 上午10:05:15
 * 
 */
public class PleDgeActivity extends BaseActivity {
	private WebView wvPledge;// webView垫付
	private String tagPledge;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		initActionBar();

		wvPledge = (WebView) findViewById(R.id.wv_register);
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
				PleDgeActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	protected void initData() {

		L.e("TAG", "------------jiazai保险----------------");

		WebSettings webSettings = wvPledge.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);

		Log.e("TAG", "------------jiazai保险tou----------------"
				+ HostUtil.getWebHost().toString());
		// 加载需要显示的网页
		wvPledge.loadUrl(HostUtil.getWebHost() + "Pfh/Insurance");
		// 设置Web视图
		wvPledge.setWebViewClient(new webViewClient());
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, HelpActivity.class);
		context.startActivity(intent);
	}

}
