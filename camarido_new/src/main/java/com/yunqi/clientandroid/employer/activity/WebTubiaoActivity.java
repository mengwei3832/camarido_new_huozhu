package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.util.SyncCookie;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;

import java.io.File;

/**
 * 图表展示页面
 * Created by mengwei on 2016/12/27.
 */

public class WebTubiaoActivity extends BaseActivity {
    private WebView wv_tubiao;
    private ProgressWheel progress_tubiao;

    /* 传递过来的数据 */
    private String mPackageId;
    private String mBegin;
    private String mEnd;

    private SyncCookie mSyncCookie;
    private PreManager preManager;
    
    @Override
    protected int getLayoutId() {
        L.e("-----------第二次是否走了getLayoutId（）-----------------");
        return R.layout.employer_activity_web_tubiao;
    }

    @Override
    protected void initView() {
        initActionBar();

        mSyncCookie = new SyncCookie(mContext);
        preManager = PreManager.instance(mContext);

        mPackageId = getIntent().getStringExtra("packageId");

        wv_tubiao = obtainView(R.id.wv_tubiao);
        progress_tubiao = obtainView(R.id.progress_tubiao);

        // 设置Web视图
        wv_tubiao.setWebViewClient(new webViewClient());
        wv_tubiao.setWebChromeClient(new WebChromeClient());
        wv_tubiao.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        wv_tubiao.getSettings().setJavaScriptEnabled(true);
        wv_tubiao.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_tubiao.getSettings().setAllowFileAccess(true);
        wv_tubiao.getSettings().setAppCacheEnabled(true);
        wv_tubiao.getSettings().setDomStorageEnabled(true);
        wv_tubiao.getSettings().setDatabaseEnabled(true);

        String cookies = preManager.getToken();

        L.e("-------cookies连接----------"+cookies);

        String URL = HostPkgUtil.getApiHost()+"Owner/ChartOverview?packageId="+mPackageId+"&token="+cookies;

        wv_tubiao.clearCache(true);
        wv_tubiao.clearHistory();
//        clearCookies(mContext);
        wv_tubiao.loadUrl(URL);
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            L.d("Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            L.d("Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        // 设置titileBar的标题
        setActionBarTitle(this.getResources().getString(
                R.string.employer_activity_web_title));
        // 设置左边的返回箭头
        setActionBarLeft(R.drawable.nav_back);
        setActionBarRight(false, 0, null);

        // 给左边的返回箭头加监听
        setOnActionBarLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebTubiaoActivity.this.finish();
            }
        });
    }

    // Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setShowOrHide(true);
        }

//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }

//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            L.d("==== WebView ajax 请求 ====", url);
//            mSyncCookie.syncCookie(url);
//            //将加好cookie的url传给父类继续执行
//            return super.shouldInterceptRequest(view, url);
//        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setShowOrHide(false);
        }
    }

    private void setShowOrHide(boolean isShow){
        if (isShow){
            wv_tubiao.setVisibility(View.GONE);
            progress_tubiao.setVisibility(View.VISIBLE);
        } else {
            wv_tubiao.setVisibility(View.VISIBLE);
            progress_tubiao.setVisibility(View.GONE);
        }
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCookie();
    }

    private void clearCookie(){
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();

        wv_tubiao.removeAllViews();
        wv_tubiao.setTag(null);
        wv_tubiao.getSettings().setJavaScriptEnabled(false);
        wv_tubiao.clearCache(true);
        wv_tubiao.onPause();
        wv_tubiao.destroy();
        wv_tubiao = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 再按一次退出程序
//            clearCookie();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
            finish();
        }
        return false;
    }

    public void clearWebviewCache(){
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e){
            e.printStackTrace();
        }

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");
        L.e("webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }

    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        L.i("delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            L.e("delete file no exists " + file.getAbsolutePath());
        }
    }


    /**
     * 图表展示页面跳转
     * @param context
     */
    public static void invoke(Context context,String packageId){
        Intent intent = new Intent(context,WebTubiaoActivity.class);
        intent.putExtra("packageId",packageId);
        context.startActivity(intent);
    }
}
