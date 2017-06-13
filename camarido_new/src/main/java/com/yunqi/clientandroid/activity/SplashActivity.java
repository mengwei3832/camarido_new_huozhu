package com.yunqi.clientandroid.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.Manifest;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentOrderActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyRenZhengActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.PermissionsActivity;
import com.yunqi.clientandroid.employer.util.PermissionsChecker;
import com.yunqi.clientandroid.employer.util.PermissionsUtils;
import com.yunqi.clientandroid.employer.util.UpdateManager;
import com.yunqi.clientandroid.entity.GetLastAppInfo;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.entity.PushInfo;
import com.yunqi.clientandroid.http.request.GetLastAppInfoRequest;
import com.yunqi.clientandroid.http.request.GetPhoneInfoRequest;
import com.yunqi.clientandroid.http.request.GetPushInfoRequest;
import com.yunqi.clientandroid.http.request.LoginRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.GetPhoneInfo;
import com.yunqi.clientandroid.utils.Keys;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.NumberSeekBar;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
    private Handler mHandler;
    private Thread mThread;
    private PreManager mPreManager;
    private int appVersion;
    private String appDownloadUrl;
    private String appDescription;
    private AlertDialog alertDialog;
    private Button btnSure;
    private Button btnCancel;
    private LinearLayout llBottom;
    private NumberSeekBar seekBar;
    private TextView tvDesc;
    private DownLoadListener listener;

    // 本页请求
    private LoginRequest mLoginRequest;
    private GetPushInfoRequest mGetPushInfoRequest;
    private GetLastAppInfoRequest mGetLastAppInfoRequest;
    private GetPhoneInfoRequest mGetPhoneInfoRequest;

    private GetPhoneInfo getPhoneInfo;

    // 本页请求id
    private final int LOGIN_REQUEST = 1;
    private final int GET_PUSH_INFO_REQUEST = 2;
    private final int GET_LASTAPP_INFO_REQUEST = 3;
    private final int GET_PHONE_INFO = 4;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsUtils mPermissionsUtils;
    public boolean isShowDialog = false;
    private static final String PACKAGE_URL_SCHEME = "package:";

    static final String[] PERMISSIONS_SD = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    static final String[] PERMISSIONS_ALL = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        // 先获取渠道号，缓存到SP中，防止强制更新之后渠道号丢失
        boolean hasChannel = CacheUtils
                .getBoolean(this, Keys.hasChannel, false);
        if (!hasChannel) {
            String channel = CommonUtil.getChannel(this);
            CacheUtils.putString(this, Keys.CHANNEL, channel);
            CacheUtils.putBoolean(this, Keys.hasChannel, true);
        }

        mPermissionsChecker = new PermissionsChecker(mContext);

        try {
            getPhoneInfo = new GetPhoneInfo(SplashActivity.this);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        L.e("------------进入权限--------------");

    }

    @Override
    protected void initData() {
        mPreManager = PreManager.instance(this);
        mPermissionsUtils = new PermissionsUtils(mContext);

        if (mPreManager.getFirstStart()) {
            mPreManager.setFirstStart(false);
            startActivity(new Intent(this, GuideActivity.class));
            // 关闭启动页
            SplashActivity.this.finish();
        } else {
            mHandler = new Handler();
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS_SD)) {
                L.e("------------进入权限申请--------------");
                showMissingPermissionDialog(getString(R.string.dialog_permission_save));
                return;
            }
            // 每次进入之前先删除下载文件夹的安装包
            File root = Environment.getExternalStorageDirectory();
            File apkFile = new File(root, "yunqitech/download/camarido.apk");
            if (apkFile.exists() && apkFile.isFile()) {
                apkFile.delete();
            }
            // 更新版本--不用自己服务器的更新版本使用友盟更新
            updateVersion();

            // UpdateManager mUpdateManager = new UpdateManager(mContext,
            // mVersion, mUri);
            // mUpdateManager.checkUpdate();
            // 进入主界面
            // begin();
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {

        super.onResume();
        if (Build.VERSION.SDK_INT >= 23){
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS_SD)) {
                if (isShowDialog) {
                    //                mPermissionsUtils.showMissingPermissionDialog(getString(R.string.dialog_permission_save));
                    showToast(getString(R.string.dialog_permission_save_toast));
                    SplashActivity.this.finish();// 关闭启动页面
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                isShowDialog = true;
            } else {
                updateVersion();
            }
        }

        // JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        // JPushInterface.onPause(this);
    }

    /**
     * 程序开始启动线程，跳转到首页
     */
    public void begin() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent;
                            // TODO 判断token,暂时注掉token逻辑
                            if (CacheUtils.getBoolean(CamaridoApp.instance,
                                    LoginActicity.AUTO_ISCHECK, false)) { // 先判断
                                L.e("-----------StringUtils.getCurrentTime()--------------"+StringUtils.getCurrentTime());
                                L.e("-----------mPreManager.getTokenExpires()--------------"+mPreManager.getTokenExpires());

                                // 是否是自动登录
                                if (mPreManager.getTokenExpires() >= (StringUtils.getCurrentTime())) { // 再判断是token是否过期

                                    L.e("-----------进入判断Token过期--------------");

                                    if (CacheUtils.getString(
                                            SplashActivity.this,
                                            LoginActicity.USER_TYPE, "1")
                                            .equals("1")) {
                                        MainActivity
                                                .invoke(SplashActivity.this);
                                    } else if (CacheUtils.getString(
                                            SplashActivity.this,
                                            LoginActicity.USER_TYPE, "1")
                                            .equals("2")) {
                                        Log.e("TAG",
                                                "-------COMPANY_MESSAGE---------"
                                                        + CacheUtils
                                                        .getInt(mContext,
                                                                "COMPANY_MESSAGE",
                                                                0));
                                        if (CacheUtils.getInt(mContext,
                                                "COMPANY_MESSAGE", 0) == 0) {
                                            // 跳转到信息收集界面
                                            EmployerCompanyRenZhengActivity
                                                    .invoke(SplashActivity.this);
                                        } else if (CacheUtils.getInt(mContext,
                                                "COMPANY_MESSAGE", 0) == 1) {
                                            EmployerMainActivity
                                                    .invoke(SplashActivity.this);
                                        }
                                    }
                                    // 关闭当前界面
                                    finish();

                                    // 获取手机的信息
                                    String pIMEI = getPhoneInfo.getPhoneIMEI();
                                    String pIMSI = getPhoneInfo.getPhoneIMSI();
                                    String aVersion = getPhoneInfo
                                            .getAppVersion().trim();
                                    int mActionType = 2;

                                    Log.e("TAG",
                                            "-------------IMEI------------------"
                                                    + pIMEI);
                                    Log.e("TAG",
                                            "-------------IMSI------------------"
                                                    + pIMSI);
                                    Log.e("TAG",
                                            "-------------AppVersion------------------"
                                                    + aVersion);

                                    // 获取手机信息的请求
                                    mGetPhoneInfoRequest = new GetPhoneInfoRequest(
                                            SplashActivity.this, pIMEI, pIMSI,
                                            aVersion, mActionType);
                                    mGetPhoneInfoRequest
                                            .setRequestId(GET_PHONE_INFO);
                                    httpPost(mGetPhoneInfoRequest);
                                } else {
//                                    String username = CacheUtils.getString(
//                                            getApplicationContext(),
//                                            LoginActicity.USER_NAME, "");
//                                    String password = CacheUtils.getString(
//                                            getApplicationContext(),
//                                            LoginActicity.PASSWORD, "");
//                                    mLoginRequest = new LoginRequest(
//                                            SplashActivity.this, username,
//                                            password);
//                                    mLoginRequest.setRequestId(LOGIN_REQUEST);
//                                    httpPost(mLoginRequest);
                                    LoginActicity.invoke(SplashActivity.this);
                                    SplashActivity.this.finish();
                                }
                            } else {
                                LoginActicity.invoke(SplashActivity.this);
                                SplashActivity.this.finish();
                            }

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mThread.start();
    }

    /**
     * 当处于启动页时，按回退键退出程序，杀死线程
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mPermissionsChecker.lacksPermissions(PERMISSIONS_SD)) {
                mThread.interrupt();// 中断线程
            }
            SplashActivity.this.finish();// 关闭启动页面
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess;
        String message;

        switch (requestId) {
            case LOGIN_REQUEST:
                // 如果成功跳转到 主界面
                isSuccess = response.isSuccess;
                if (isSuccess) {
                    LoginInfo loginInfo = (LoginInfo) response.singleData;
                    mPreManager.setTokenExpires(loginInfo.tokenExpires);
                    String userType = loginInfo.userType;
                    if (!TextUtils.isEmpty(userType) && userType != null) {
                        CacheUtils.putString(SplashActivity.this,
                                LoginActicity.USER_TYPE, userType);
                        if (userType.equals("1")) {
                            MainActivity.invoke(SplashActivity.this);
                        } else if (userType.equals("2")) {
                            EmployerMainActivity.invoke(SplashActivity.this);
                        }
                    } else {
                        // 登录成功跳转到主界面
                        MainActivity.invoke(SplashActivity.this);
                    }
                    SplashActivity.this.finish();

                    mGetPushInfoRequest = new GetPushInfoRequest(
                            SplashActivity.this);
                    mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
                    httpPost(mGetPushInfoRequest);
                } else {
                    LoginActicity.invoke(SplashActivity.this);
                    SplashActivity.this.finish();
                }
                break;

            case GET_PUSH_INFO_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<PushInfo> pushInfos = response.data;
                    for (PushInfo pushInfo : pushInfos) {
                        if (pushInfo.pushDataType == 1) {
                            MiPushUtil.setMiPushTopic(SplashActivity.this,
                                    pushInfo.receiverMark);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                }
                break;

            case GET_LASTAPP_INFO_REQUEST:
                // 从服务器获取版本号
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    // 获取版本号成功
                    GetLastAppInfo getLastAppInfo = (GetLastAppInfo) response.singleData;
                    appVersion = getLastAppInfo.appVersion;// 服务器返回的apk的版本号
                    appDownloadUrl = getLastAppInfo.appDownloadUrl;// 服务器返回的apk的url
                    appDescription = getLastAppInfo.appDescription;// 服务器返回的apk的更新描述

                    // 获取现有apk版本的版本号
                    int currentCode = CommonUtil.getVersionCode();

                    // 判断现有apk的版本号和服务器返回的版本号大小
                    if (currentCode < appVersion) {
                        // 现有版本号小于服务器返回的版本号--更新版本

                        AlertDialog.Builder builder = new Builder(
                                SplashActivity.this);
                        // 设置对话框不能被取消
                        builder.setCancelable(false);

                        View view = View.inflate(SplashActivity.this,
                                R.layout.dialog_download, null);
                        tvDesc = (TextView) view.findViewById(R.id.desc);
                        llBottom = (LinearLayout) view.findViewById(R.id.bottom);
                        btnSure = (Button) view.findViewById(R.id.btn_ok);
                        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
                        seekBar = (NumberSeekBar) view.findViewById(R.id.bar0);

                        seekBar.setTextColor(Color.WHITE);
                        seekBar.setTextSize(30);
                        seekBar.setMax(100);

                        // 设置更新提示信息
                        if (!TextUtils.isEmpty(appDescription)
                                && appDescription != null) {
                            SplashActivity.this.setDesc(appDescription.replace(
                                    "\\n", "\n"));
                        }

                        // 监听下载进度
                        SplashActivity.this
                                .setDownLoadListener(new DownLoadListener() {
                                    @Override
                                    public void start(int max) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void finish() {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void downLoad() {
                                        // 下载安装apk
                                        if (!TextUtils.isEmpty(appDownloadUrl)
                                                && appDownloadUrl != null) {
                                            downLoadAndInstall(appDownloadUrl);
                                        }
                                    }

                                    @Override
                                    public void cancel(int progress) {
                                        // TODO Auto-generated method stub
                                    }
                                });

                        // 点击取消按钮
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 消除对话框
                                alertDialog.dismiss();
                                // 进入主界面
                                begin();
                            }
                        });
                        btnSure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                llBottom.measure(0, 0);
                                int height = llBottom.getMeasuredHeight();

                                ValueAnimator animWidth = ValueAnimator.ofInt(
                                        height, 0);
                                animWidth
                                        .addUpdateListener(new AnimatorUpdateListener() {

                                            @Override
                                            public void onAnimationUpdate(
                                                    ValueAnimator valueAnimator) {
                                                int value = (Integer) valueAnimator
                                                        .getAnimatedValue();
                                                btnSure.getLayoutParams().height = value;
                                                btnCancel.getLayoutParams().height = value;
                                                btnSure.requestLayout();
                                                btnCancel.requestLayout();
                                            }
                                        });

                                animWidth.setDuration(300);
                                animWidth.start();

                                ValueAnimator animator = ValueAnimator.ofInt(0,
                                        height);

                                animator.addUpdateListener(new AnimatorUpdateListener() {

                                    @Override
                                    public void onAnimationUpdate(
                                            ValueAnimator valueAnimator) {
                                        int value = (Integer) valueAnimator
                                                .getAnimatedValue();
                                        seekBar.getLayoutParams().height = value;
                                        seekBar.requestLayout();
                                    }
                                });

                                animator.setDuration(300);
                                animator.start();

                                if (listener != null) {
                                    listener.start(seekBar.getMax());
                                    listener.downLoad();
                                }

                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.setView(view, 0, 0, 0, 0);
                        alertDialog.show();

                    } else {
                        // 现有版本号不小于服务器返回的版本号不更新版本--跳转到主界面
                        begin();
                    }

                } else {
                    // 获取版本号失败--跳转到主界面
                    begin();
                }

                break;

        }

    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        // 请求失败处理--跳转到登录界面
        LoginActicity.invoke(SplashActivity.this);
        // 关闭启动页
        SplashActivity.this.finish();
    }

    // 访问服务器版本更新
    private void updateVersion() {
        mGetLastAppInfoRequest = new GetLastAppInfoRequest(SplashActivity.this);
        mGetLastAppInfoRequest.setRequestId(GET_LASTAPP_INFO_REQUEST);
        httpPost(mGetLastAppInfoRequest);

    }

    /**
     * 下载最新版本
     */
    protected void download(final String downUrl) {
        File rootFile = Environment.getExternalStorageDirectory();
        File file = new File(rootFile, "yunqitech/download");

        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(new File(file,
                        "camarido.apk"));

                byte[] buf = new byte[1024];

                int len = -1;
                int total = conn.getContentLength();

                // 设置加载进度数
                SplashActivity.this.setMax(total / 10);

                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    fos.flush();
                    // 设置加载进度
                    SplashActivity.this.setProgres(SplashActivity.this
                            .getProgres() + len / 10);
                }

                fos.close();
                is.close();
                conn.disconnect();
                // 取消对话框
                alertDialog.dismiss();
                // 安装apk
                installApk();
                // 关闭当前页面
                SplashActivity.this.finish();

            } else {
                // 下载失败
                alertDialog.dismiss();
                // 显示一个重试的dialog
                showTryAgainDialog(downUrl);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            // 显示一个重试的dialog
            showTryAgainDialog(downUrl);
        } catch (IOException e) {
            e.printStackTrace();
            // 显示一个重试的dialog
            showTryAgainDialog(downUrl);
        }

    }

    /**
     * 显示一个重试的dialog
     *
     * @param downUrl
     */
    private void showTryAgainDialog(final String downUrl) {
        CommonUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false);
                builder.setTitle("提示");

                builder.setMessage("下载失败，请重试");

                builder.setPositiveButton("重试",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 下载安装apk
                                downLoadAndInstall(downUrl);
                            }
                        });

                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                // 进入主界面
                                begin();
                            }
                        });

                builder.create().show();

            }
        });

    }

    /**
     * 下载并安装新版本
     *
     * @param downUrl
     */
    private void downLoadAndInstall(final String downUrl) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            showToast("没有检测到存储设备");
            return;
        }

        // 获取下载路径
        File sdCardFile = Environment.getExternalStorageDirectory();
        File file = new File(sdCardFile, "yunqitech");
        if (!file.exists()) {
            file.mkdirs();
        }

        // 开始下载
        if (TextUtils.isEmpty(downUrl)) {
            return;
        }
        new Thread() {
            public void run() {
                // 下载apk的url
                download(downUrl);
            }

            ;
        }.start();
    }

    /**
     * 安装apk
     */
    protected void installApk() {
        /**
         * <intent-filter> <action android:name="android.intent.action.VIEW" />
         * <category android:name="android.intent.category.DEFAULT" /> <data
         * android:scheme="content" /> <data android:scheme="file" /> <data
         * android:mimeType="application/vnd.android.package-archive" />
         * </intent-filter>
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");

        File rootFile = Environment.getExternalStorageDirectory();
        File file = new File(rootFile, "yunqitech/download/camarido.apk");

        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        // 当当前的activity退出的时候，会调用以前的activity的OnActivityResult方法
        startActivityForResult(intent, 0);
    }

    // 接收安装完apk后的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else {
            updateVersion();
        }
    }

    public void setDesc(String desc) {
        this.tvDesc.setText(desc);
    }

    public void setMax(int max) {
        seekBar.setMax(max);
    }

    public void setProgres(int progress) {
        seekBar.setProgress(progress);
    }

    public int getProgres() {
        return seekBar.getProgress();
    }

    public void setDownLoadListener(DownLoadListener listener) {
        this.listener = listener;
    }

    // 更新监听回调的接口
    public interface DownLoadListener {
        // 开始下载
        void start(int max);

        // 取消
        void cancel(int progress);

        // 完成
        void finish();

        // 下载中
        void downLoad();
    }

    // 显示缺失权限提示
    public void showMissingPermissionDialog(String text) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_permission_tishi);
        builder.setMessage(mContext.getString(R.string.dialog_permission_help_txt1) + text
                + mContext.getString(R.string.string_help_text));

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showToast(getString(R.string.dialog_permission_save_toast));
                SplashActivity.this.finish();// 关闭启动页面
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

}