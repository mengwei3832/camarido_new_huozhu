package com.yunqi.clientandroid.employer.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.NewSendPackageActivity;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.HostUtil;

public class EmployerHighWayFragment extends BaseFragment {
	private WebView wvHighWay;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_fragment_highway;
	}

	@Override
	protected void initView(View _rootView) {
		initActionBar();

		wvHighWay = obtainView(R.id.wv_employer_highWay);
	}

	@Override
	protected void initData() {
		WebSettings webSettings = wvHighWay.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 加载需要显示的网页
		wvHighWay.loadUrl(HostUtil.getWebHost() + "RoadIndex/MobileIndex");
		// 设置Web视图
		wvHighWay.setWebViewClient(new webViewClient());
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

	// 初始化TitleBar
	private void initActionBar() {
		EmployerMainActivity eActivity = (EmployerMainActivity) getActivity();
		eActivity.getActionBar().show();
		eActivity
				.setActionBarTitle(getString(R.string.employer_activity_main_gonglu));
		eActivity.setActionBarRight(true, 0,
				getActivity().getString(R.string.employer_fragment_home_fabao));

		eActivity.setActionBarLeft(0);
		eActivity.setOnActionBarLeftClickListener(null);
		eActivity.setOnActionBarRightClickListener(false,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 进入发包页面
						NewSendPackageActivity.invoke(getActivity(), "");
					}
				});
	}

}
