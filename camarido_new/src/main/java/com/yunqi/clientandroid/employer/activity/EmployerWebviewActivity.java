package com.yunqi.clientandroid.employer.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.ProgressWheel;

public class EmployerWebviewActivity extends BaseActivity {
    private WebView wv;
    private ProgressWheel wvProgress;
    private String mUrl;
    private String mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_employer_webview;
    }

    @Override
    protected void initView() {
        //获取传递过来的数据
        mUrl = getIntent().getStringExtra("Url");
        mTitle = getIntent().getStringExtra("Title");

        L.e("--------mTitle--------"+mTitle);

        wv = obtainView(R.id.wv_employer_webview);
        wvProgress = obtainView(R.id.wv_progress);

        //初始化标题栏
        initActionBar();
        String useragent = wv.getSettings().getUserAgentString();
//        if (!useragent.contains("MicroMessenger")){
//            wv.getSettings().setUserAgentString("MicroMessenger");
//        }

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyWebviewClient());
        wv.setWebChromeClient(new WebChromeClient());
    }

    private class MyWebviewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            wvProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            webView.loadUrl(s);
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            wvProgress.setVisibility(View.GONE);
        }
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        // 设置titileBar的标题
        setActionBarTitle(mTitle);
        // 设置左边的返回箭头
        setActionBarLeft(R.drawable.nav_back);
        setActionBarRight(false, 0, null);

        // 给左边的返回箭头加监听
        setOnActionBarLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployerWebviewActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        wv.loadUrl(mUrl);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()){
            wv.goBack();
            return true;
        } else {
            EmployerWebviewActivity.this.finish();
        }
        return false;
    }
}
