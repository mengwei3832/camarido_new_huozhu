package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.HostUtil;

/**
 * 
 * @Description:class 垫付的页面
 * @ClassName: PayMentActivity
 * @author: zhm
 * @date: 2016-4-25 上午10:05:15
 * 
 */
public class PayMentActivity extends BaseActivity {
	private WebView wvPayment;// webView垫付
	private String tagPayment;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		initActionBar();

		wvPayment = (WebView) findViewById(R.id.wv_register);
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
				PayMentActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	protected void initData() {
		// 得到穿过来的值
		tagPayment = getIntent().getStringExtra("tag");

		WebSettings webSettings = wvPayment.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 加载需要显示的网页
		wvPayment.loadUrl(HostUtil.getWebHost() + "pfh/Help?tag=" + tagPayment);
		// 设置Web视图
		wvPayment.setWebViewClient(new webViewClient());
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
	public static void invoke(Context context, String tag) {
		Intent intent = new Intent(context, HelpActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}

}
