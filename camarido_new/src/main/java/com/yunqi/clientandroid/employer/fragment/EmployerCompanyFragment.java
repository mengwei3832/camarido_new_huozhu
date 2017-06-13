package com.yunqi.clientandroid.employer.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.Between;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.employer.activity.AddressListActivity;
import com.yunqi.clientandroid.employer.activity.CarManagerAcitivity;
import com.yunqi.clientandroid.employer.activity.CompanyWalletActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyActicity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.EmployerWebviewActivity;
import com.yunqi.clientandroid.employer.activity.HelpPkgActivity;
import com.yunqi.clientandroid.employer.activity.PermissionsActivity;
import com.yunqi.clientandroid.employer.adapter.AccountAdapter;
import com.yunqi.clientandroid.employer.adapter.EmployerAddressAdapter;
import com.yunqi.clientandroid.employer.entity.AccountEntity;
import com.yunqi.clientandroid.employer.entity.AccountLogin;
import com.yunqi.clientandroid.employer.entity.GetCompanyContent;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.employer.entity.PingTaiKeFu;
import com.yunqi.clientandroid.employer.request.AccountLoginRequest;
import com.yunqi.clientandroid.employer.request.AccountRequest;
import com.yunqi.clientandroid.employer.request.GetCityRequest;
import com.yunqi.clientandroid.employer.request.GetCompanyContentRequest;
import com.yunqi.clientandroid.employer.request.GetEnterpriseInfoRequest;
import com.yunqi.clientandroid.employer.request.GetProvinceRequest;
import com.yunqi.clientandroid.employer.request.PingTaiKeFuRequest;
import com.yunqi.clientandroid.employer.request.SecondaryAccountRequest;
import com.yunqi.clientandroid.employer.request.SetEnterpriseInfoRequest;
import com.yunqi.clientandroid.employer.request.UploadCompanyUrlRequest;
import com.yunqi.clientandroid.employer.util.PermissionsChecker;
import com.yunqi.clientandroid.employer.util.PermissionsUtils;
import com.yunqi.clientandroid.employer.util.SaveProvinceUtils;
import com.yunqi.clientandroid.employer.util.SaveSetUtils;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.request.GetPushInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.PermissionsResultListener;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;
import com.yunqi.clientandroid.utils.UserUtil;
import com.yunqi.clientandroid.view.MMAlert;
import com.yunqi.clientandroid.view.RoundImageView;

/**
 * @Description:企业页面
 * @ClassName: EmployerCompanyFragment
 * @author: chengtao
 * @date: 2016-7-14 下午2:09:27
 */
public class EmployerCompanyFragment extends BaseFragment implements
        OnClickListener {

    private EmployerMainActivity activity;
    private View enterpriseView;
    private RelativeLayout enterprisewalletLayout;
    private RelativeLayout enterprisecarLayout;
    private RelativeLayout enterpriseaddressLayout;
    private RelativeLayout enterprisehelpLayout;
    private RelativeLayout enterpriseserviceLayout;
    private String servicenumberinputStr;
    private File profileImgFile;
    private GetEnterpriseInfoRequest mGetEnterpriseInfoRequest;
    private SetEnterpriseInfoRequest mSetEnterpriseInfoRequest;
    private DisplayImageOptions mOption;
    private RoundImageView ivHead;
    private Uri imageFileUri;
    private PopupWindow enterprisePopupWindow;
    private EditText enterprisenameEditText;
    private EditText enterprisemessagEditText;
    private TextView tvCancle;
    private TextView tvSure;
    private TextView tvEnterpriseCall;
    private RelativeLayout rlEnterpriseBack;
    private TextView tvEnterpriseNickname;
    private String extensionNumber;
    private ProgressWheel progressCompany;
    private RelativeLayout rlAllLayout;
    private RelativeLayout rlProvinceUpdate;

    private String pcsPhone;

    /* 请求ID */
    private final int GET_COMPANY_CONTENT = 1;
    private final int UPLOAD_LOGOURL = 2;
    private final int GET_PINGTAI_KEFU = 3;
    private final int GET_SECONDAR_ACCOUNT = 4;
    private final int GET_ACCOUNT_MESSAGE = 5;
    private final int GET_ACCOUNT_LOGIN = 6;
    private final int GET_PUSH_INFO_REQUEST = 7;
    private final int GET_PROVINCE = 8;
    private final int GET_CITY = 9;
    private final int GET_COUNTRY = 10;

    /* 请求类 */
    private GetCompanyContentRequest mGetCompanyContentRequest;
    private UploadCompanyUrlRequest mUploadCompanyUrlRequest;
    private PingTaiKeFuRequest pingTaiKeFuRequest;
    private SecondaryAccountRequest mAccountRequest;
    private AccountRequest mAccountRequest2;
    private AccountLoginRequest mAccountLoginRequest;
    private GetPushInfoRequest mGetPushInfoRequest;
    private GetProvinceRequest mGetProvinceRequest;
    private GetCityRequest mGetCityRequest;

    /* 请求的数据 */
    private String mTenantShortname; // 公司简称
    private String mLogoUrl; // 公司logo

    /* 是否存在副账户 */
    private boolean isAccount = false;
    private ArrayList<AccountEntity> mAccountList = new ArrayList<AccountEntity>();
    private AccountAdapter mAccountAdapter;
    private final String TOKENVALUE = "TokenValue";

    private PreManager mPreManager;
    private SpManager mSpManager;
    private PermissionsUtils mPermissionsUtils;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    private Bitmap photo;

    // 所需的全部权限
    static final String[] PERMISSIONS_SD = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /* 省市区更新 */
    private int i = 0;
    private int j = 0;
    private int provinceId;
    private int cityId;
    private List<GetProvince> mProvinceList = new ArrayList<>();
    private List<GetProvince> mCityList = new ArrayList<>();
    private boolean isUpdateFinish = false;
    private SharedPreferences userName;
    private SharedPreferences.Editor mEditor;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_fragment_enterprise_mine;
    }

    @Override
    protected void initView(View _rootView) {
        initActionBar();

        // TODO Auto-generated method stub
        enterpriseView = getActivity().getLayoutInflater().inflate(
                R.layout.employer_fragment_enterprise_mine, null);
        ivHead = obtainView(R.id.iv_enterprise_mine_avatar);
        tvEnterpriseNickname = obtainView(R.id.tv_enterprise_mine_nickName);
        enterprisewalletLayout = obtainView(R.id.rl_enterprise_mine_wallet);
        enterprisecarLayout = obtainView(R.id.rl_enterprise_mine_car);
        enterpriseaddressLayout = obtainView(R.id.rl_enterprise_mine_address);
        enterprisehelpLayout = obtainView(R.id.rl_enterprise_mine_help);
        enterpriseserviceLayout = obtainView(R.id.rl_enterprise_mine_service);
        // tvCancle = (TextView) enterpriseView
        // .findViewById(R.id.tv_enterprise_personal_cancle_mine);
        tvEnterpriseCall = obtainView(R.id.tv_enterprise_mine_call);
        rlEnterpriseBack = obtainView(R.id.rl_enterprise_mine_back);
        progressCompany = obtainView(R.id.pb_employer_company);
        rlAllLayout = obtainView(R.id.rl_company_all);
        rlProvinceUpdate = obtainView(R.id.rl_enterprise_mine_province);

        // PreManager类实例化
        mPreManager = PreManager.instance(getContext());
        mSpManager = SpManager.instance(getActivity());
        mPermissionsUtils = new PermissionsUtils(getActivity());
        mPermissionsChecker = new PermissionsChecker(getActivity());
        userName = getActivity().getSharedPreferences("user",0);
        mEditor = userName.edit();

        // 对头像和公司名进行赋值
        setHeaderText();
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        EmployerMainActivity eActivity = (EmployerMainActivity) getActivity();
        eActivity.getActionBar().show();
        eActivity.setActionBarTitle("企业");
        eActivity.setActionBarRight(true, 0, "1.5.5");
        eActivity.setActionBarLeft(0);
        eActivity.setOnActionBarRightClickListener(false, null);
    }

    /**
     * 对头像和公司名进行赋值
     */
    private void setHeaderText() {
        String mAvatar = userName.getString("loadUrl","");
        String mCompanyName = userName.getString("companyName","");


        Log.e("TAG","=======mCompanyName======="+mCompanyName);
        Log.e("TAG","=======mAvatar======="+mAvatar);

        if (StringUtils.isStrNotNull(mAvatar)){
            ImageLoader.getInstance().displayImage(mAvatar, ivHead,
                    mOption);
        }

        if (StringUtils.isStrNotNull(mCompanyName)) {
            // 显示企业名字简称
            tvEnterpriseNickname.setText(mCompanyName);
        }
        // 获取企业信息
        getCompanyContentText();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        initActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        // initActionBar();
        // 获取企业信息
        // getCompanyContentText();
        // 获取客服电话
        getPingTai();
    }

    private void getPingTai() {
        pingTaiKeFuRequest = new PingTaiKeFuRequest(getActivity());
        pingTaiKeFuRequest.setRequestId(GET_PINGTAI_KEFU);
        httpGet(pingTaiKeFuRequest);
    }

    /**
     * 获取企业信息
     */
    private void getCompanyContentText() {
        mGetCompanyContentRequest = new GetCompanyContentRequest(getActivity());
        mGetCompanyContentRequest.setRequestId(GET_COMPANY_CONTENT);
        httpPost(mGetCompanyContentRequest);
    }

    /**
     * 获取用户是否存在副账户信息
     *
     * @Title:getSecondarAccount
     * @Create: 2016年11月22日 上午9:54:17
     */
    private void getSecondarAccount() {
        mAccountRequest = new SecondaryAccountRequest(getActivity());
        mAccountRequest.setRequestId(GET_SECONDAR_ACCOUNT);
        httpGet(mAccountRequest);
    }

    /**
     * 获取副账户信息
     *
     * @Title:getAccountMessage
     * @Create: 2016年11月22日 上午10:44:23
     */
    private void getAccountMessage() {
        mAccountRequest2 = new AccountRequest(getActivity());
        mAccountRequest2.setRequestId(GET_ACCOUNT_MESSAGE);
        httpGet(mAccountRequest2);
    }

    /**
     * 获取副账户登录的信息
     *
     * @param userId
     * @Title:getAccountLogin
     * @Create: 2016年11月22日 上午11:03:03
     */
    private void getAccountLogin(int userId) {
        mAccountLoginRequest = new AccountLoginRequest(getActivity(), userId);
        mAccountLoginRequest.setRequestId(GET_ACCOUNT_LOGIN);
        httpGet(mAccountLoginRequest);
    }

    /**
     * 推送相关服务
     *
     * @Title:setPush
     * @Create: 2016年11月22日 上午11:19:19
     */
    private void setPush() {
        mGetPushInfoRequest = new GetPushInfoRequest(getActivity());
        mGetPushInfoRequest.setRequestId(GET_PUSH_INFO_REQUEST);
        httpPost(mGetPushInfoRequest);
    }

    @Override
    protected void initData() {
        profileImgFile = new File(getActivity().getCacheDir(), "headphoto_");
        // 设置头像
        mOption = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.company_person_header)
                .showImageForEmptyUri(R.drawable.company_person_header)
                .showImageOnFail(R.drawable.company_person_header).build();
    }

    @Override
    protected void setListener() {
        // 初始化点击事件
        initOnClick();
    }

    /**
     * 初始化点击事件
     */
    private void initOnClick() {
        ivHead.setOnClickListener(this);
        enterprisewalletLayout.setOnClickListener(this);
        enterprisecarLayout.setOnClickListener(this);
        enterpriseaddressLayout.setOnClickListener(this);
        enterprisehelpLayout.setOnClickListener(this);
        enterpriseserviceLayout.setOnClickListener(this);
        tvEnterpriseCall.setOnClickListener(this);
        rlEnterpriseBack.setOnClickListener(this);
        rlProvinceUpdate.setOnClickListener(this);
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);

        boolean isSuccess;
        String message;

        switch (requestId) {
            case GET_PINGTAI_KEFU:
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    PingTaiKeFu pingTaiKeFu = (PingTaiKeFu) response.singleData;

                    pcsPhone = pingTaiKeFu.PlatServicePhoneNum;
                }
                break;

            case GET_COMPANY_CONTENT: // 获取的企业信息
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    GetCompanyContent mGetCompanyContent = (GetCompanyContent) response.singleData;

                    mTenantShortname = mGetCompanyContent.tenantShortname;
                    mLogoUrl = mGetCompanyContent.logoUrl;
                    extensionNumber = mGetCompanyContent.extensionNumber;// 客服电话

                    if (mTenantShortname != null
                            && !TextUtils.isEmpty(mTenantShortname)) {
                        L.e("======mTenantShortname======"+mTenantShortname);
                        mEditor.putString("companyName",mTenantShortname);
                        mPreManager.setCompanyName(mTenantShortname);
                        tvEnterpriseNickname.setText(mTenantShortname);
                    }

                    if (mLogoUrl != null && !TextUtils.isEmpty(mLogoUrl)) {
                        mPreManager.setAvatar(mLogoUrl);
                        mEditor.putString("loadUrl",mLogoUrl);
                        ImageLoader.getInstance().displayImage(mLogoUrl, ivHead,
                                mOption);
                    } else {
                        ivHead.setImageResource(R.drawable.company_touxiang);
                    }

                    if (extensionNumber != null
                            && !TextUtils.isEmpty(extensionNumber)) {
                        tvEnterpriseCall.setText(extensionNumber);
                    }
                    mEditor.commit();
                }

                showOrHide(false);
                break;

            case UPLOAD_LOGOURL:
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    showToast(message);
                    ivHead.setImageBitmap(photo);
                } else {
                    showToast(message);
                }

                break;

            case GET_SECONDAR_ACCOUNT://判断用户是否存在副账户信息
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    isAccount = true;
                } else {
                    isAccount = false;
                }

                // 上传企业头像
                showPickDialog(isAccount);
                break;

            case GET_ACCOUNT_MESSAGE://获取副账户信息
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ArrayList<AccountEntity> aelist = response.data;
                    if (aelist != null) {
                        mAccountList.addAll(aelist);
                    }
                    //显示弹窗
                    showAccountDialog();
                } else {
                    showToast(message);
                }
                break;

            case GET_ACCOUNT_LOGIN://副账户登录信息
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    // 清空userId
                    UserUtil.unSetUserId(getActivity());
                    // 用户退出统计
                    MobclickAgent.onProfileSignOff();

                    AccountLogin mAccountLogin = (AccountLogin) response.singleData;

                    String tokenValue = mAccountLogin.TokenValue;
                    String userId = mAccountLogin.UserId;
                    String tokenExpires = mAccountLogin.TokenExpires;

                    // 保存登录返回的token到SP
                    CacheUtils.putString(getActivity(), TOKENVALUE,
                            tokenValue);
                    mPreManager.setToken(tokenValue);

                    // 用户登录统计
                    if (!TextUtils.isEmpty(userId) && userId != null) {
                        MobclickAgent.onProfileSignIn(userId);
                    }

                    getCompanyContentText();
                }
                break;
            case GET_PROVINCE:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<GetProvince> mPList = response.data;
                    if (mPList != null) {
                        mProvinceList.addAll(mPList);
//                        try {
////                            String str = SaveSetUtils.SceneList2String(mProvinceList);
//                            mSpManager.setProvince(str);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        SaveProvinceUtils.saveObjectList((ArrayList<GetProvince>) mProvinceList,"Provinces");

                        provinceId = mProvinceList.get(i).id;
                        getCityRequest(provinceId);
                    }
                }
                break;
            case GET_CITY:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<GetProvince> mCList = response.data;
                    if (mCList != null) {
//                        try {
//                            String str = SaveSetUtils.SceneList2String(mCList);
//                            mSpManager.setCity(String.valueOf(provinceId), str);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        SaveProvinceUtils.saveObjectList((ArrayList<GetProvince>) mCList,provinceId+"");
                    }
                    i++;
                    if (i <= (mProvinceList.size() - 1)) {
                        provinceId = mProvinceList.get(i).id;
                        getCityRequest(provinceId);
                    } else {
                        i = 0;
                        provinceId = mProvinceList.get(i).id;
                        mCityList = SaveProvinceUtils.readObjectList(provinceId+"");
//                        try {
////                            String cityStr = mSpManager.getCity(String.valueOf(provinceId));
////                            mCityList = SaveSetUtils.String2SceneList(cityStr);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        cityId = mCityList.get(j).id;
                        getCountryRequest(cityId);
                    }
                }
                break;
            case GET_COUNTRY:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<GetProvince> mCyList = response.data;
                    if (mCyList != null) {
//                        try {
//                            String str = SaveSetUtils.SceneList2String(mCyList);
//                            mSpManager.setCity(String.valueOf(cityId), str);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        SaveProvinceUtils.saveObjectList((ArrayList<GetProvince>) mCyList,cityId+"");
                    }
                    j++;
                    if (j <= (mCityList.size() - 1)) {
                        cityId = mCityList.get(j).id;
                        getCountryRequest(cityId);
                    } else {
                        getCountryText();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getCountryText() {
        j = 0;
        i++;
        mCityList.clear();
        if (i <= (mProvinceList.size() - 1)) {
            provinceId = mProvinceList.get(i).id;
//            try {
//                String cityStr = mSpManager.getCity(String.valueOf(provinceId));
//                mCityList = SaveSetUtils.String2SceneList(cityStr);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
            mCityList = SaveProvinceUtils.readObjectList(provinceId+"");
            if (mCityList.size() != 0) {
                cityId = mCityList.get(j).id;
                getCountryRequest(cityId);
            } else {
                getCountryText();
            }
        } else {
            i = 0;
            isUpdateFinish = false;
            showToast("省市区更新完成");
        }

    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast("连接超时，请检查网络");
        showOrHide(false);
        switch (requestId){
            case GET_PROVINCE:
            case GET_CITY:
            case GET_COUNTRY:
                isUpdateFinish = false;
                break;
        }
    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_enterprise_mine_avatar:
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_avatar");
                //判断用户是否存在副账户信息
                getSecondarAccount();
                break;
            // case R.id.rl_to_personal:
            // // 点击设置企业信息
            // showSettingenterprise();
            // break;
            case R.id.rl_enterprise_mine_wallet:
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_wallet");
                // 跳转到企业钱包
                CompanyWalletActivity.invoke(getActivity());
                break;

            case R.id.rl_enterprise_mine_car:
                // 跳转到车辆管理
                CarManagerAcitivity.invoke(getActivity());
                break;

            case R.id.rl_enterprise_mine_address:
                // 跳转到地址管理
                AddressListActivity.invoke(getActivity());
                break;
            case R.id.rl_enterprise_mine_help:
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_help");
                // 跳转到系统帮助
                //                HelpPkgActivity.invoke(getActivity());
                Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
                intent.putExtra("Url", HostUtil.getWebHost() + "pfh/ShipperHelp");
                intent.putExtra("Title", getString(R.string.help));
                getActivity().startActivity(intent);
                break;

            case R.id.rl_enterprise_mine_service:
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_customer_service");
                // 获取客服电话

                Log.e("TAG", "------pcsPhone------" + pcsPhone);

                if (pcsPhone != null && !TextUtils.isEmpty(pcsPhone)) {
                    // 拨打客服电话
                    CommonUtil.callPhoneIndirect(getActivity(), pcsPhone);
                }
                break;
            case R.id.rl_enterprise_mine_province:
                if (!isUpdateFinish){
                    isUpdateFinish = true;
                    showToast("省市区开始更新...");
                    getProvinceRequest();
                }
                break;

            case R.id.rl_enterprise_mine_back: // 退出当前系统
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_back_login");

                showLogoutDialog();
                break;

        }

    }

    /**
     * 获取所有省份
     */
    private void getProvinceRequest() {
        mGetProvinceRequest = new GetProvinceRequest(getActivity());
        mGetProvinceRequest.setRequestId(GET_PROVINCE);
        httpPost(mGetProvinceRequest);
    }

    /**
     * 获取所有市
     *
     * @param provinceId 省份Id
     */
    private void getCityRequest(int provinceId) {
        mGetCityRequest = new GetCityRequest(getActivity(), provinceId);
        mGetCityRequest.setRequestId(GET_CITY);
        httpPost(mGetCityRequest);
    }

    /**
     * 获取所有县
     *
     * @param cityId 市区Id
     */
    private void getCountryRequest(int cityId) {
        mGetCityRequest = new GetCityRequest(getActivity(), cityId);
        mGetCityRequest.setRequestId(GET_COUNTRY);
        httpPost(mGetCityRequest);
    }

    /**
     * @throws
     * @Description:退出对话框
     * @Title:showLogoutDialog
     * @return:void
     * @Create: 2016年6月15日 下午4:30:37
     * @Author : chengtao
     */
    private void showLogoutDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.logout_dialog, null);
        Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        Button btnSure = (Button) view.findViewById(R.id.btn_sure);
        AlertDialog.Builder builder = new Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();
        btnCancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_back_login_cancel");

                dialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("business_center_back_login_sure");
                // 删除token过期时间
                PreManager.instance(getActivity()).removeTokenExpires();
                // 清空userId
                UserUtil.unSetUserId(getActivity());
                // 跳转到登录界面
                LoginActicity.invoke(getActivity());
                // 用户退出统计
                MobclickAgent.onProfileSignOff();
                // finish主界面
                getActivity().finish();
                //关闭MiPush推送服务，当用户希望不再使用MiPush推送服务的时候调用，调用成功之后，
                // app将不会接收到任何MiPush服务推送的数据，直到下一次调用registerPush
                MiPushUtil.unregisterPush(getActivity());
                CamaridoApp.destoryActivity("EmployerMainActivity");
            }
        });
    }

    /**
     * 副账户的弹窗信息
     *
     * @Title:showAccountDialog
     * @Create: 2016年11月22日 上午10:15:45
     */
    private void showAccountDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.employer_dialog_account, null);
        ListView lvAccount = (ListView) view.findViewById(R.id.lv_company_listview);
        mAccountAdapter = new AccountAdapter(mAccountList, getActivity());
        lvAccount.setAdapter(mAccountAdapter);
        AlertDialog.Builder builder = new Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();
        lvAccount.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountEntity accountEntity = mAccountList.get(position);

                int mViceUserId = accountEntity.ViceUserId;

                L.e("--------mViceUserId-------" + mViceUserId);
                getAccountLogin(mViceUserId);
                showOrHide(true);
                dialog.dismiss();
            }
        });
        //		btnSure.setOnClickListener(new OnClickListener() {
        //
        //			@Override
        //			public void onClick(View v) {
        //				// 删除token过期时间
        //				PreManager.instance(getActivity()).removeTokenExpires();
        //				// 清空userId
        //				UserUtil.unSetUserId(getActivity());
        //				// 跳转到登录界面
        //				LoginActicity.invoke(getActivity());
        //				// 用户退出统计
        //				MobclickAgent.onProfileSignOff();
        //				// finish主界面
        //				getActivity().finish();
        //				CamaridoApp.destoryActivity("EmployerMainActivity");
        //			}
        //		});
    }

    public void showPickDialog(final boolean isAccount) {
        Log.e("TAG", "-----------showPickDialog---------------");
        String shareDialogTitle = getString(R.string.pick_dialog_title);
        String[] mStr = null;
        if (isAccount) {
            mStr = getResources().getStringArray(R.array.picks_item_account);
        } else {
            mStr = getResources().getStringArray(R.array.picks_item);
        }

        MMAlert.showAlert(getActivity(), shareDialogTitle, mStr, null,
                new MMAlert.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        switch (whichButton) {
                            case 0: // 拍照
                                //友盟统计首页
                                mUmeng.setCalculateEvents("business_avatar_take_pictures");

                                if (mPermissionsUtils.isPermissionsChecker(PERMISSIONS_SD)) {
                                    mPermissionsUtils.showMissingPermissionDialog(getString(R.string.dialog_permission_camera));
                                    return;
                                } else {
                                    getCamera();
                                }
                                break;
                            case 1: // 相册
                                //友盟统计首页
                                mUmeng.setCalculateEvents("business_avatar_album");

                                if (mPermissionsUtils.isPermissionsChecker(PERMISSIONS_SD)) {
                                    mPermissionsUtils.showMissingPermissionDialog(getString(R.string.dialog_permission_camera));
                                    return;
                                } else {
                                    getAlbum();
                                }
                                break;

                            case 2://切换用户
                                //友盟统计首页
                                mUmeng.setCalculateEvents("business_avatar_sub_account");

                                if (isAccount) {
                                    getAccountMessage();
                                }
                                break;
                            default:
                                break;
                        }
                    }

                });

    }

    /**
     * 调用相机
     */
    private void getCamera() {
        try {
            Log.e("TAG", "-----------拍照---------------");
            imageFileUri = getActivity()
                    .getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new ContentValues());
            if (imageFileUri != null) {
                Intent i = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(
                        android.provider.MediaStore.EXTRA_OUTPUT,
                        imageFileUri);
                if (StringUtils.isIntentSafe(getActivity(),
                        i)) {
                    Log.e("TAG",
                            "-----------startActivityForResult(i, 2)---------------");
                    startActivityForResult(i, 2);
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.dont_have_camera_app),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(
                        getActivity(),
                        getString(R.string.cant_insert_album),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            L.e("-----Exception------" + e.toString());
            Toast.makeText(getActivity(),
                    getString(R.string.cant_insert_album),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用相册
     */
    private void getAlbum() {
        Log.e("TAG", "-----------相册---------------");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        Log.e("TAG",
                "-----------startActivityForResult(intent, 1)---------------");
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:// 如果是直接从相册获取
                    if (data != null) {
                        Log.e("TAG", "-----------直接从相册获取---------------");
                        Uri uri = data.getData();
                        Log.e("TAG",
                                "-----------startPhotoZoom(uri)---------------");
                        startPhotoZoom(uri);
                    }
                    break;
                case 2:// 如果是调用相机拍照时
                    String picPath = null;
                    if (imageFileUri != null) {
                        Log.e("TAG", "-----------调用相机拍照---------------");
                        picPath = ImageUtil.getPicPathFromUri(imageFileUri,
                                getActivity());
                        int degree = 0;
                        if (!StringUtils.isEmpty(picPath))
                            degree = ImageUtil.readPictureDegree(picPath);
                        Matrix matrix = new Matrix();
                        if (degree != 0) {// 解决旋转问题
                            matrix.preRotate(degree);
                        }
                        Uri uri = Uri.fromFile(new File(picPath));
                        Log.e("TAG",
                                "-----------startPhotoZoom(uri)---------------");
                        startPhotoZoom(uri);
                    } else {
                        Toast.makeText(getActivity(), "图片获取异常", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case 3:// 取得裁剪后的图片
                    if (data != null) {
                        Log.e("TAG", "-----------setPicToView---------------");
                        setPicToView(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        try {
            Log.e("TAG", "-----------startPhotoZoom(uri)---------------");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            Log.e("TAG",
                    "-----------startActivityForResult(intent, 3)---------------");
            startActivityForResult(intent, 3);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "图片裁剪异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Log.e("TAG", "-----------setPicToView---------------");
        Bundle extras = picdata.getExtras();
        String headBase64 = "";
        String imageName = "";
        Log.e("TAG", "-----------extras---------------" + extras.toString());
        if (extras != null) {
            photo = extras.getParcelable("data");
            int newWidth = 100;
            if (photo.getWidth() >= 100) {
                newWidth = photo.getWidth();
            }
            int newHeight = 100;
            if (photo.getHeight() >= 100) {
                newHeight = photo.getHeight();
            }
            if (photo.getWidth() < 100 || photo.getHeight() < 100) {
                photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight,
                        false);
            }
            try {
                photo.compress(Bitmap.CompressFormat.JPEG, 80,
                        new FileOutputStream(profileImgFile));
                headBase64 = ImageUtil.bitmapToBase64(photo);
                imageName = profileImgFile.getName();
            } catch (FileNotFoundException e) {
                Log.e("TAG", "-----------e.printStackTrace()---------------");
                e.printStackTrace();
            }
            boolean exit = profileImgFile.exists();
            if (!exit) {
                Toast.makeText(getActivity(), R.string.upload_photo_fail,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mUploadCompanyUrlRequest = new UploadCompanyUrlRequest(
                    getActivity(), imageName, headBase64);
            mUploadCompanyUrlRequest.setRequestId(UPLOAD_LOGOURL);
            httpPost(mUploadCompanyUrlRequest);
        }
    }

    /**
     * 显示或隐藏进度条
     *
     * @param isShow
     * @Title:showOrHide
     * @Create: 2016年11月22日 下午3:05:51
     */
    private void showOrHide(boolean isShow) {
        if (isShow) {
            rlAllLayout.setVisibility(View.GONE);
            progressCompany.setVisibility(View.VISIBLE);
        } else {
            rlAllLayout.setVisibility(View.VISIBLE);
            progressCompany.setVisibility(View.GONE);
        }
    }

}
