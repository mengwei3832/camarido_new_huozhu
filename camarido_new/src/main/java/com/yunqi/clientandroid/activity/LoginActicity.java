package com.yunqi.clientandroid.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentOrderActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyRenZhengActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.NewSendPackageActivity;
import com.yunqi.clientandroid.employer.activity.PermissionsActivity;
import com.yunqi.clientandroid.employer.activity.SetOrResetSafePasswordActivity;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.util.PermissionsChecker;
import com.yunqi.clientandroid.employer.util.PermissionsUtils;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.entity.PushInfo;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.HostUtil.HostType;
import com.yunqi.clientandroid.http.request.DecideCompanyMessageRequest;
import com.yunqi.clientandroid.http.request.GetCodeQieRequest;
import com.yunqi.clientandroid.http.request.GetPhoneInfoRequest;
import com.yunqi.clientandroid.http.request.GetPushInfoRequest;
import com.yunqi.clientandroid.http.request.LoginRequest;
import com.yunqi.clientandroid.http.request.RegisterPkgRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.GetPhoneInfo;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.PayTimeUtils;
import com.yunqi.clientandroid.utils.PermissionsResultListener;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;

/**
 * @deprecated:登录
 */
public class LoginActicity extends BaseActivity implements OnClickListener {

    private Button mBtnEnterLogin;
    private EditText mEtLoginPhone;
    private EditText mEtLoginPwd;
    private ImageView mIvDelete1;
    private ImageView mIvDelete2;
    private TextView mTvForgetpwd;
    private CheckBox mCbAuto;
    private ImageView iv_login_head;
    private String mUserName;
    private String mPassWord;

    private int flag = 0;
    private AlertDialog alertDialog_input;// 验证框
    private AlertDialog alertDialog;// 环境切换
    private RadioGroup rg_alert_ceshi;
    private RadioButton rb_alert_bug;
    private RadioButton rb_alert_demo;
    private RadioButton rb_alert_zhengshi;
    private EditText et_alert_input;

    private SharedPreferences spf;
    private Editor editor;
    private int type;

    // 存放SP的key
    private final String TOKENVALUE = "TokenValue";
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String AUTO_ISCHECK = "AUTO_ISCHECK";

    // 本页请求
    private LoginRequest mLoginRequest;
    private GetPushInfoRequest mGetPushInfoRequest;
    private GetCodeQieRequest mGetCodeQieRequest;
    private GetPhoneInfoRequest mGetPhoneInfoRequest;
    private RegisterPkgRequest mRegisterPkgRequest;
    private DecideCompanyMessageRequest decideCompanyMessageRequest;

    private GetPhoneInfo getPhoneInfo;

    // 本页请求id
    private final int DECIDE_COMPANY_MESSAGE = 6;
    private final int LOGIN_REQUEST = 1;
    private final int GET_PUSH_INFO_REQUEST = 2;
    private final int GET_INPUT_CODEKEY = 3;
    private final int GET_PHONE_INFO = 4;
    private final int LOGIN_PKG = 5;

    private PreManager mPreManager;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsUtils mPermissionsUtils;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    static final String[] PERMISSIONS_ALL = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA
    };

    //友盟统计
    private UmengStatisticsUtils mUmeng;

    private boolean isHidden = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
//        GrowingIO.getInstance().setPageGroup();
        // 初始化titileBar
        initActionBar();
        // 添加进销毁队列
        CamaridoApp.addDestoryActivity(LoginActicity.this, "LoginActivity");

        mPreManager = PreManager.instance(mContext);
        mUmeng = UmengStatisticsUtils.instance(mContext);
        mPermissionsChecker = new PermissionsChecker(mContext);
        mPermissionsUtils = new PermissionsUtils(mContext);

        if (mPermissionsChecker.lacksPermissions(PERMISSIONS_ALL)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS_ALL);
        }

        try {
            getPhoneInfo = new GetPhoneInfo(LoginActicity.this);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        mBtnEnterLogin = (Button) findViewById(R.id.bt_login_enterlogin);
        mEtLoginPhone = (EditText) findViewById(R.id.et_login_account);
        mEtLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
        mIvDelete1 = (ImageView) findViewById(R.id.iv_login_delete1);
        mIvDelete2 = (ImageView) findViewById(R.id.iv_login_delete2);
        mTvForgetpwd = (TextView) findViewById(R.id.tv_login_forgetpwd);
        mCbAuto = (CheckBox) findViewById(R.id.cb_login_auto);

        iv_login_head = (ImageView) findViewById(R.id.iv_login_head);

    }

    @Override
    protected void initData() {
        // 实例化SharedPreferences
        type = CacheUtils.getInt(mContext, "type", 0);

        Log.d("TAG", "-------选中的环境是--------" + type);

        if (type == 1) {
            HostUtil.type = HostType.DEBUG_HOST;
            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.DEBUG_HOST;
        } else if (type == 2) {
            HostUtil.type = HostType.DEMO_HOST;
            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.DEMO_HOST;
        } else if (type == 3) {
            HostUtil.type = HostType.PUBLIC_HOST;
            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.PUBLIC_HOST;
        }

        // 设置已勾选
        mCbAuto.setChecked(true);
        // 自动登录已选中
        CacheUtils.putBoolean(getApplicationContext(), AUTO_ISCHECK, true);
        // 初始化SP
        initSharedPre();
    }

    @Override
    protected void setListener() {
        // 初始化点击事件
        initOnClick();
        // 初始化EditText
        initEditTextListener();
        // 初始化勾选框的监听
        initChecked();
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        setActionBarTitle(this.getResources().getString(R.string.login));
        setActionBarLeft(R.drawable.nav_back);
        setOnActionBarLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActicity.this.finish();
            }
        });

        setActionBarRight(true, 0,
                this.getResources().getString(R.string.register));
        setOnActionBarRightClickListener(false, new OnClickListener() {
            @Override
            public void onClick(View V) {
                // 跳转到注册界面
                RegisterActivity.invoke(LoginActicity.this);
                //友盟统计
                mUmeng.setCalculateEvents("register_enter");
                // NewSendPackageActivity.invoke(LoginActicity.this,"");
                // SetOrResetSafePasswordActivity.invoke(LoginActicity.this,false);
            }
        });
    }

    // 初始化自动登录状态
    private void initSharedPre() {
        // 判断自动登录勾选框的状态
        if (CacheUtils.getBoolean(getApplicationContext(), AUTO_ISCHECK, false)) {
            // 保存勾选框勾选状态
            mCbAuto.setChecked(true);
            String username = CacheUtils.getString(getApplicationContext(),
                    USER_NAME, "");
            String password = CacheUtils.getString(getApplicationContext(),
                    PASSWORD, "");
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                mEtLoginPhone.setText(username);
                mEtLoginPwd.setText(password);
            }
        } else {
            mCbAuto.setChecked(false);
        }
    }

    // 监听自动登录勾选框
    private void initChecked() {
        mCbAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (mCbAuto.isChecked()) {
                    // 自动登录已选中
                    CacheUtils.putBoolean(getApplicationContext(),
                            AUTO_ISCHECK, true);
                } else {
                    // 自动登录没有选中
                    CacheUtils.putBoolean(getApplicationContext(),
                            AUTO_ISCHECK, false);
                }
            }

        });
    }

    // 监听输入框
    private void initEditTextListener() {
        // 监听用户名输入框
        mEtLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 用户名输入框输入的时候
                setPhoneChangeEditText();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable enable) {
            }
        });

        // 监听密码输入框
        mEtLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 密码输入框输入的时候
                setPwdChangeEditText();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable enable) {
            }
        });
    }

    // 密码输入框有内容的时候UI变化的方法
    protected void setPwdChangeEditText() {
        if (mEtLoginPwd.getText().toString().length() != 0
                && mEtLoginPwd.isFocused()) {
            mIvDelete2.setVisibility(View.VISIBLE);
        } else if (mEtLoginPwd.getText().toString().length() == 0
                && mEtLoginPhone.getText().toString().length() != 0) {
            mIvDelete2.setVisibility(View.GONE);
        } else {
            mIvDelete2.setVisibility(View.GONE);
        }
    }

    // 电话号码输入框有内容的时候UI变化的方法
    protected void setPhoneChangeEditText() {
        if (mEtLoginPhone.getText().toString().length() != 0
                && mEtLoginPhone.isFocused()) {
            mIvDelete1.setVisibility(View.VISIBLE);
        } else {
            mIvDelete1.setVisibility(View.GONE);
        }
    }

    // 初始化点击事件
    private void initOnClick() {
        mBtnEnterLogin.setOnClickListener(this);
        mIvDelete1.setOnClickListener(this);
        mIvDelete2.setOnClickListener(this);
        mTvForgetpwd.setOnClickListener(this);
        iv_login_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_delete1:
                // 点击用户名框的删除按钮
                mEtLoginPhone.setText("");
                break;
            case R.id.iv_login_delete2:
                // 点击密码框的删除按钮
//                mEtLoginPwd.setText("");
                // TODO
                if (isHidden){
                    mEtLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvDelete2.setImageResource(R.drawable.password_show);
                } else {
                    mEtLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvDelete2.setImageResource(R.drawable.password_hide);
                }
                isHidden = !isHidden;
                mEtLoginPwd.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEtLoginPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;

            case R.id.tv_login_forgetpwd:
                // 跳转到忘记密码界面--传用户名过去
                String phoneNumber = mEtLoginPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    ForgetpwdActivity.invoke(this, phoneNumber);
                } else {
                    startActivity(new Intent(this, ForgetpwdActivity.class));
                }

                break;

            case R.id.bt_login_enterlogin:
                //友盟统计登录
                Map<String,String> map_click = new HashMap<>();
                map_click.put("number","1");
                MobclickAgent.onEvent(mContext,"login_click",map_click);

                // 点击登录
                mUserName = mEtLoginPhone.getText().toString().trim();
                mPassWord = mEtLoginPwd.getText().toString().trim();
                if (TextUtils.isEmpty(mUserName)) {
                    showToast("请输入账号");
                    return;
                }

                // 检测手机号码
                if (!mUserName.matches("^[1][3-8][0-9]{9}$")) {
                    showToast("请输入正确的手机号码");
                    return;
                }

                if (TextUtils.isEmpty(mPassWord)) {
                    showToast("请输入密码");
                    return;
                }

                // 检测密码
                if (!mPassWord.matches("^[0-9a-zA-Z]{6,14}$")) {
                    showToast("密码必须为6~14位数字或字母,请重新输入");
                    return;
                }

                if (mPermissionsUtils.isPermissionsChecker(PERMISSIONS)) {
                    mPermissionsUtils.showMissingPermissionDialog(getString(R.string.dialog_permission_phone));
                    return;
                } else {
                    // 对密码进行MD5
                    String md5Password = StringUtils.md5(mPassWord);

                    showProgressDialog("登录中，请稍候...");

                    loginAct(mUserName, md5Password);

                    //友盟统计登录
                    Map<String,String> map_value = new HashMap<>();
                    map_value.put("username",mUserName);
                    MobclickAgent.onEventValue(mContext,"login",map_value,1);
                }

                break;

            case R.id.iv_login_head:// 切换测试环境使用
                Log.e("TAG", "点击的值" + flag);

                if (PayTimeUtils.isFastClick()) {

                    Log.e("TAG", "点击的值-----------" + flag);
                    if (flag == 10) {
                        // 弹出输入验证码
                        showInputPass();
                        // //弹出切换环境框
                        // showQieHuan();d
                    } else {
                        flag++;
                    }
                } else {
                    flag = 0;
                }

                // if (flag == 10) {
                // //弹出输入验证码
                // showInputPass();
                // // //弹出切换环境框
                // // showQieHuan();d
                // } else {
                // flag++;
                // }
                break;

            default:
                break;
        }
    }

    // 输入验证码
    private void showInputPass() {
        View input_view = LayoutInflater.from(LoginActicity.this).inflate(
                R.layout.alertdialog_ceshi_input, null);

        et_alert_input = (EditText) input_view
                .findViewById(R.id.et_alert_input);

        alertDialog_input = new AlertDialog.Builder(LoginActicity.this)
                .setTitle("切换环境").setView(input_view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog_input.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // alertDialog.dismiss();
                        // 获得输入的密码
                        String code = et_alert_input.getText().toString()
                                .trim();

                        Log.e("TAG", "获取输入的验证码------------" + code);
                        // 请求该验证码是否正确
                        getCodeQie(code);
                    }
                }).create();
        alertDialog_input.show();

    }

    // 请求该验证码是否正确
    private void getCodeQie(String code) {
        mGetCodeQieRequest = new GetCodeQieRequest(LoginActicity.this, code);
        mGetCodeQieRequest.setRequestId(GET_INPUT_CODEKEY);
        httpPost(mGetCodeQieRequest);
    }

    // 切换环境的框
    private void showQieHuan() {

        Log.e("TAG", "------进入弹出框---------");
        View alert_view = LayoutInflater.from(LoginActicity.this).inflate(
                R.layout.alertdialog_ceshihuanjing, null);

        Log.e("TAG", "------进入找对象---------");
        rb_alert_bug = (RadioButton) alert_view.findViewById(R.id.rb_alert_bug);
        rb_alert_demo = (RadioButton) alert_view
                .findViewById(R.id.rb_alert_demo);
        rb_alert_zhengshi = (RadioButton) alert_view
                .findViewById(R.id.rb_alert_zhengshi);
        rg_alert_ceshi = (RadioGroup) alert_view
                .findViewById(R.id.rg_alert_ceshi);

        Log.e("TAG", "------创建弹出框---------");
        alertDialog = new AlertDialog.Builder(LoginActicity.this)
                .setTitle("切换环境").setView(alert_view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        flag = 0;
                    }
                }).create();
        alertDialog.show();

        rg_alert_ceshi
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == rb_alert_bug.getId()) {
                            HostUtil.type = HostType.DEBUG_HOST;
                            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.DEBUG_HOST;
                            CacheUtils.putInt(mContext, "type", 1);
                            showToast("现在是测试环境-----");
                        } else if (checkedId == rb_alert_demo.getId()) {
                            HostUtil.type = HostType.DEMO_HOST;
                            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.DEMO_HOST;
                            CacheUtils.putInt(mContext, "type", 2);
                            showToast("现在是demo环境-----");
                        } else if (checkedId == rb_alert_zhengshi.getId()) {
                            HostUtil.type = HostType.PUBLIC_HOST;
                            HostPkgUtil.type = com.yunqi.clientandroid.employer.request.HostPkgUtil.HostType.PUBLIC_HOST;
                            CacheUtils.putInt(mContext, "type", 3);
                            showToast("现在是正式环境-----");
                        }
                    }
                });
    }

    // 访问服务器登录
    private void loginAct(String userName, String password) {
        // mLoginRequest = new LoginRequest(this, userName, password);
        // mLoginRequest.setRequestId(LOGIN_REQUEST);
        // httpPost(mLoginRequest);

        // 发包方登录
        mRegisterPkgRequest = new RegisterPkgRequest(LoginActicity.this,
                userName, password);
        mRegisterPkgRequest.setRequestId(LOGIN_PKG);
        httpPost(mRegisterPkgRequest);

        // 设置登录按钮不可点击
        setViewEnable(false);
    }

    // 设置按钮不可重复点击的方法
    private void setViewEnable(boolean bEnable) {
        if (bEnable) {
            mBtnEnterLogin.setText("登录");
        } else {
            mBtnEnterLogin.setText("登录中...");
        }
        mBtnEnterLogin.setEnabled(bEnable);
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess;
        String message;
        switch (requestId) {
            case LOGIN_REQUEST:
                // 登录请求服务器成功
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    // 登录成功
                    LoginInfo loginInfo = (LoginInfo) response.singleData;
                    String tokenValue = loginInfo.tokenValue;
                    String userId = loginInfo.userId;
                    long tokenExpires = loginInfo.tokenExpires;
                    String userType = loginInfo.userType;
                    boolean isTempUser = loginInfo.isTempUser;

                    // 登录成功后保存账号密码到SP
                    CacheUtils.putString(getApplicationContext(), USER_NAME,
                            mUserName);
                    CacheUtils.putString(getApplicationContext(), PASSWORD,
                            mPassWord);

                    if (isTempUser) {
                        // 跳转到企业信息收集页面
                        EmployerCompanyRenZhengActivity.invoke(LoginActicity.this);
                    } else {
                        // 保存登录返回的token到SP
                        CacheUtils.putString(getApplicationContext(), TOKENVALUE,
                                tokenValue);
                        // 保存token的过期时间
                        if (!TextUtils.isEmpty(tokenExpires + "")
                                && tokenExpires >= 0) {
                            PreManager.instance(this).setTokenExpires(tokenExpires);
                        }

                        if (!TextUtils.isEmpty(userType) && userType != null) {
                            // 保存用户类型
                            CacheUtils.putString(getApplicationContext(),
                                    USER_TYPE, userType);
                            if (userType.equals("1")) {
                                // 登录成功跳转到主界面
                                MainActivity.invoke(LoginActicity.this);
                            } else if (userType.equals("2")) {
                                // 跳转到发包方当前订单界面
                                EmployerMainActivity.invoke(LoginActicity.this);
                            }
                        } else {
                            // 登录成功跳转到主界面
                            MainActivity.invoke(LoginActicity.this);
                        }
                    }

                    // 用户登录统计
                    if (!TextUtils.isEmpty(userId) && userId != null) {
                        MobclickAgent.onProfileSignIn(userId);
                    }

                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }

                    // 登录成功后关闭当前登录界面
                    LoginActicity.this.finish();

                    // 获取手机的信息
                    String pIMEI = getPhoneInfo.getPhoneIMEI();
                    String pIMSI = getPhoneInfo.getPhoneIMSI();
                    String aVersion = getPhoneInfo.getAppVersion().trim();
                    int mActionType = 1;

                    Log.e("TAG", "-------------IMEI------------------" + pIMEI);
                    Log.e("TAG", "-------------IMSI------------------" + pIMSI);
                    Log.e("TAG", "-------------AppVersion------------------"
                            + aVersion);

                    // 获取手机信息的请求
                    mGetPhoneInfoRequest = new GetPhoneInfoRequest(
                            LoginActicity.this, pIMEI, pIMSI, aVersion, mActionType);
                    mGetPhoneInfoRequest.setRequestId(GET_PHONE_INFO);
                    httpPost(mGetPhoneInfoRequest);

                    mGetPushInfoRequest = new GetPushInfoRequest(LoginActicity.this);
                    mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
                    httpPost(mGetPushInfoRequest);
                } else {
                    // 登录失败
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                    // 设置登录按钮可点击
                    setViewEnable(true);
                }

                break;

            case LOGIN_PKG:
                // 登录请求服务器成功
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    // 登录成功
                    LoginInfo loginInfo = (LoginInfo) response.singleData;
                    String tokenValue = loginInfo.tokenValue;
                    String userId = loginInfo.userId;
                    long tokenExpires = loginInfo.tokenExpires;
                    String userType = loginInfo.userType;
                    boolean isTempUser = loginInfo.isTempUser;

                    // 登录成功后保存账号密码到SP
                    CacheUtils.putString(getApplicationContext(), USER_NAME,
                            mUserName);
                    CacheUtils.putString(getApplicationContext(), PASSWORD,
                            mPassWord);

                    // 保存登录返回的token到SP
                    CacheUtils.putString(getApplicationContext(), TOKENVALUE,
                            tokenValue);
                    // 保存token的过期时间
                    if (!TextUtils.isEmpty(tokenExpires + "") && tokenExpires >= 0) {
                        PreManager.instance(this).setTokenExpires(tokenExpires);
                    }

                    L.e("---------userType-------------" + userType);

                    if (!TextUtils.isEmpty(userType) && userType != null) {
                        // 保存用户类型
                        CacheUtils.putString(getApplicationContext(), USER_TYPE,
                                userType);
                        if (userType.equals("1")) {
                            // 登录成功跳转到主界面
                            MainActivity.invoke(LoginActicity.this);
                        } else if (userType.equals("2")) {
                            // 跳转到发包方当前订单界面
                            // EmployerMainActivity.invoke(LoginActicity.this);
                            decideCompanyMessageRequest = new DecideCompanyMessageRequest(
                                    LoginActicity.this);
                            decideCompanyMessageRequest
                                    .setRequestId(DECIDE_COMPANY_MESSAGE);
                            httpGet(decideCompanyMessageRequest);
                        }
                    } else {
                        // 登录成功跳转到主界面
                        EmployerMainActivity.invoke(LoginActicity.this);
                    }

                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }

                    mGetPushInfoRequest = new GetPushInfoRequest(LoginActicity.this);
                    mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
                    httpPost(mGetPushInfoRequest);

                    // 用户登录统计
                    if (!TextUtils.isEmpty(userId) && userId != null) {
                        MobclickAgent.onProfileSignIn(userId);
                    }

                    // 登录成功后关闭当前登录界面
                    //                    LoginActicity.this.finish();

                    // 清空当前用户的缓存信息
                    mPreManager.clearHomeCache();
                    //                    performRequestPermissions("为了应用可以正常使用，请您点击确认申请权限。", new String[]{
                    //                            Manifest.permission.READ_PHONE_STATE}, 1, new PermissionsResultListener() {
                    //                        @Override
                    //                        public void onPermissionGranted() {
                    //                            try {
                    // 获取手机的信息
                    String pIMEI = getPhoneInfo.getPhoneIMEI();
                    String pIMSI = getPhoneInfo.getPhoneIMSI();
                    String aVersion = getPhoneInfo.getAppVersion().trim();
                    int mActionType = 1;

                    Log.e("TAG", "-------------IMEI------------------" + pIMEI);
                    Log.e("TAG", "-------------IMSI------------------" + pIMSI);
                    Log.e("TAG", "-------------AppVersion------------------"
                            + aVersion);
                    // 获取手机信息的请求
                    mGetPhoneInfoRequest = new GetPhoneInfoRequest(
                            LoginActicity.this, pIMEI, pIMSI, aVersion, mActionType);
                    mGetPhoneInfoRequest.setRequestId(GET_PHONE_INFO);
                    httpPost(mGetPhoneInfoRequest);
                    //                            } catch (Throwable throwable) {
                    //                                showToast("请到设置中开启应用电话、通讯录权限");
                    //                            }
                    //                        }
                    //
                    //                        @Override
                    //                        public void onPermissionDenied() {
                    //                        }
                    //                    });

                } else {
                    // 登录失败
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                    // 设置登录按钮可点击
                    setViewEnable(true);
                    hideProgressDialog();
                }

                break;

            case DECIDE_COMPANY_MESSAGE:
                isSuccess = response.isSuccess;
                message = response.message;

                // 保存公司信息是否采集的判断

                if (isSuccess) {
                    CacheUtils.putInt(mContext, "COMPANY_MESSAGE", 1);
                    // 跳转到发包方当前订单界面
                    EmployerMainActivity.invoke(LoginActicity.this);
                } else {
                    CacheUtils.putInt(mContext, "COMPANY_MESSAGE", 0);
                    // 跳转到信息收集页面
                    EmployerCompanyRenZhengActivity.invoke(LoginActicity.this);
                }
                hideProgressDialog();
                LoginActicity.this.finish();
                break;

            case GET_PUSH_INFO_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<PushInfo> pushInfos = response.data;
                    for (PushInfo pushInfo : pushInfos) {
                        if (pushInfo.pushDataType == 1) {
                            MiPushUtil.setMiPushTopic(LoginActicity.this,
                                    pushInfo.receiverMark);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                }
                break;

            case GET_INPUT_CODEKEY:// 获取输入的密码
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    showToast(message);
                    // 弹出切换环境框
                    showQieHuan();
                } else {
                    showToast(message);
                    return;
                }

                break;

            case GET_PHONE_INFO:
                isSuccess = response.isSuccess;
                message = response.message;
                break;
        }

    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(this.getResources().getString(R.string.net_error_toast));
        // 设置登录按钮可点击
        setViewEnable(true);
        hideProgressDialog();
        switch (requestId) {
            case DECIDE_COMPANY_MESSAGE:
                EmployerMainActivity.invoke(mContext);
                LoginActicity.this.finish();
                break;
        }
    }

    /**
     * 登录界面跳转
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, LoginActicity.class);
        activity.startActivity(intent);
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
    //        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
    //            showToast("拒绝授权，将无法登录");
    //        } else {
    //            // 对密码进行MD5
    //            String md5Password = StringUtils.md5(mPassWord);
    //
    //            loginAct(mUserName, md5Password);
    //        }
    //    }
}
