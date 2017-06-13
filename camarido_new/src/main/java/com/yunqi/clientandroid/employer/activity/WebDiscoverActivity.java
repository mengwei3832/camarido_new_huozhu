package com.yunqi.clientandroid.employer.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.utils.ProgressWheel;

/**
 * 发现页面下的web页面展示
 * Created by mengwei on 2016/12/13.
 */

public class WebDiscoverActivity extends BaseActivity {
    private WebView wvDiscover;
    private ProgressWheel progressDiscover;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_activity_web_discover;
    }

    @Override
    protected void initView() {
        initActionBar();
        wvDiscover = obtainView(R.id.wv_discover);
        progressDiscover = obtainView(R.id.progress_discover);
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
                WebDiscoverActivity.this.finish();
            }
        });
        setActionBarRight(false, 0, "");
        setOnActionBarRightClickListener(false, null);
    }

    @Override
    protected void initData() {
        // 设置Web视图
        wvDiscover.setWebViewClient(new webViewClient());
        wvDiscover.setWebChromeClient(new WebChromeClient());
        wvDiscover.getSettings().setBuiltInZoomControls(false);
        wvDiscover.getSettings().setDomStorageEnabled(true);
        // 如果有缓存 就使用缓存数据 如果没有 就从网络中获取
        wvDiscover.getSettings().setCacheMode(
                WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvDiscover.getSettings().setDatabaseEnabled(true);
        wvDiscover.getSettings().setAppCacheEnabled(true);
        // 加载需要显示的网页
        wvDiscover.loadUrl(HostUtil.getWebHost() + "pfh/ShipperHelp");
    }

    // Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showOrHide(true);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            showOrHide(false);
        }
    }

    @Override
    protected void setListener() {

    }
    
    private void showOrHide(boolean isShow){
        if (isShow){
            wvDiscover.setVisibility(View.GONE);
            progressDiscover.setVisibility(View.VISIBLE);
        } else {
            wvDiscover.setVisibility(View.VISIBLE);
            progressDiscover.setVisibility(View.GONE);
        }
    }
}
