package com.yunqi.clientandroid.employer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.ExternalAdapter;
import com.yunqi.clientandroid.employer.entity.ExternalEntity;
import com.yunqi.clientandroid.employer.entity.GetCompanyContent;
import com.yunqi.clientandroid.employer.request.DeleteExternalRequest;
import com.yunqi.clientandroid.employer.request.ExternalAddPhoneRequest;
import com.yunqi.clientandroid.employer.request.ExternalListRequest;
import com.yunqi.clientandroid.employer.request.ExternalSendRequest;
import com.yunqi.clientandroid.employer.request.GetCompanyContentRequest;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.util.ExternalDeletePhone;
import com.yunqi.clientandroid.employer.util.ExternalSendDuanxin;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.Constants;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 外部监控人员页面
 * Created by mengwei on 2016/12/27.
 */

public class ExternalMonitoringActivity extends BaseActivity implements
        View.OnClickListener {
    /* 页面控件对象 */
    private EditText etInputPhone;
    private Button btAddPhone;
    private RelativeLayout rlShareWx;
    private ListView lvExternalView;
    private ProgressWheel progressExternal;
    private LinearLayout llExternalAll;

    /* 电话列表展示 */
    private ArrayList<ExternalEntity> externalList = new ArrayList<>();
    private ExternalAdapter mExternalAdapter;

    /* 页面获取值 */
    private String mPhoneName;

    /* 请求Id */
    private final int EXTERNAL_ADD_PHONE = 1;
    private final int EXTERNAL_SEND_SMS = 3;
    private final int EXTERNAL_DELETE = 4;
    private final int PACKAGE_NAME = 5;
    private final int EXTERNAL_LIST = 2;

    /* 请求类 */
    private ExternalAddPhoneRequest mExternalAddPhoneRequest;
    private ExternalSendRequest mExternalSendRequest;
    private DeleteExternalRequest mDeleteExternalRequest;
    private GetCompanyContentRequest mGetCompanyContentRequest;
    private ExternalListRequest mExternalListRequest;

    // IWXAPI：第三方APP和微信通信的接口
    public static IWXAPI api;

    private String chooseId;

    private String packageId;
    private String mPackageName;
    private String mBegin;
    private String mEnd;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_activity_external;
    }

    @Override
    protected void initView() {
        initActionBar();
        // 初始化
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        // 向微信终端注册你的id
        api.registerApp(Constants.APP_ID);

        packageId = getIntent().getStringExtra("packageId");
        mBegin = getIntent().getStringExtra("begin");
        mEnd = getIntent().getStringExtra("end");

        etInputPhone = obtainView(R.id.et_external_input_phone);
        btAddPhone = obtainView(R.id.bt_external_add_phone);
        rlShareWx = obtainView(R.id.rl_external_share_wx);
        lvExternalView = obtainView(R.id.lv_external_view);
        progressExternal = obtainView(R.id.progress_external);
        llExternalAll = obtainView(R.id.ll_external_all);

        mExternalAdapter = new ExternalAdapter(mContext, externalList, new ExternalSendDuanxin() {
            @Override
            public void onClick(View item, int position, String userPhone) {
                //友盟统计首页
                mUmeng.setCalculateEvents("monitoring_click_item_send");

                showProgressDialog("正在发送，请稍候...");
                //调用发短信的接口
                sendSMSRequest(userPhone);
            }
        }, new ExternalDeletePhone() {
            @Override
            public void onClick(View item, int position, String phId) {
                //友盟统计首页
                mUmeng.setCalculateEvents("monitoring_click_item_delete");

                showProgressDialog("正在删除，请稍候...");
                //调用删除电话号码
                chooseId = phId;
                deteleExternalRequest();
            }
        });
        lvExternalView.setAdapter(mExternalAdapter);
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        // 设置titileBar的标题
        setActionBarTitle(this.getResources().getString(
                R.string.employer_activity_external_title));
        // 设置左边的返回箭头
        setActionBarLeft(R.drawable.nav_back);
        setActionBarRight(false, 0, null);

        // 给左边的返回箭头加监听
        setOnActionBarLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalMonitoringActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        showOrHide(true);
        getExternalList();
    }

    private void getExternalList() {
        mExternalListRequest = new ExternalListRequest(mContext, packageId);
        mExternalListRequest.setRequestId(EXTERNAL_LIST);
        httpGet(mExternalListRequest);
    }


    /**
     * 添加电话
     */
    private void addPhoneRequest() {
        mExternalAddPhoneRequest = new ExternalAddPhoneRequest(mContext, mPhoneName, packageId);
        mExternalAddPhoneRequest.setRequestId(EXTERNAL_ADD_PHONE);
        httpGet(mExternalAddPhoneRequest);
    }

    /**
     * 发送短信
     */
    private void sendSMSRequest(String userPhone) {
        mExternalSendRequest = new ExternalSendRequest(mContext, userPhone, packageId);
        mExternalSendRequest.setRequestId(EXTERNAL_SEND_SMS);
        httpGet(mExternalSendRequest);
    }

    /**
     * 删除监控人员接口
     */
    private void deteleExternalRequest() {
        mDeleteExternalRequest = new DeleteExternalRequest(mContext, chooseId);
        mDeleteExternalRequest.setRequestId(EXTERNAL_DELETE);
        httpGet(mDeleteExternalRequest);
    }

    /**
     * 获取发包方的名称
     */
    private void getPackageNameMsg() {
        mGetCompanyContentRequest = new GetCompanyContentRequest(mContext);
        mGetCompanyContentRequest.setRequestId(PACKAGE_NAME);
        httpPost(mGetCompanyContentRequest);
    }

    @Override
    protected void setListener() {
        btAddPhone.setOnClickListener(this);
        rlShareWx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_external_add_phone://添加电话号码
                //获取输入框的值
                mPhoneName = etInputPhone.getText().toString().trim();

                if (!StringUtils.isStrNotNull(mPhoneName)) {
                    showToast("请输入电话号码");
                    return;
                }

                if (!StringUtils.isPhone(mPhoneName)) {
                    showToast("请输入正确电话号码");
                    return;
                }

                //友盟统计首页
                mUmeng.setCalculateEvents("monitoring_click_add");

                Iterator<ExternalEntity> mIterator = externalList.iterator();
                while (mIterator.hasNext()) {
                    String e = mIterator.next().userPhone;
                    if (e.equals(mPhoneName)) {
                        T.showShort(mContext, "该手机号已添加过");
                        return;
                    }
                }

                //调用添加电话接口
                addPhoneRequest();
                break;
            case R.id.rl_external_share_wx://微信分享
                //友盟统计首页
                mUmeng.setCalculateEvents("monitoring_click_share_wx");

                if (StringUtils.isStrNotNull(mPackageName)){
                    //调用接口
                    shareText("货物承运信息", HostPkgUtil.getApiHost() + "owner/TicketChart?packageId=" + packageId,
                            mPackageName + "从" + mBegin + "发往" + mEnd + "的货物，承运结果信息，请前往查看");
                }
                break;
        }
    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess;
        String message;
        switch (requestId) {
            case EXTERNAL_LIST:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ArrayList<ExternalEntity> list = response.data;

                    if (list != null) {
                        externalList.addAll(list);
                    }

                    mExternalAdapter.notifyDataSetChanged();
                }
                getPackageNameMsg();
                break;
            case PACKAGE_NAME:
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    GetCompanyContent mGetCompanyContent = (GetCompanyContent) response.singleData;

                    mPackageName = mGetCompanyContent.tenantShortname;

                }
                showOrHide(false);
                break;
            case EXTERNAL_ADD_PHONE://添加电话
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ExternalEntity externalEntity = (ExternalEntity) response.singleData;

                    L.e("------externalList--------" + externalList.toString());


                    showToast(message);
                    externalList.add(externalEntity);

                    L.e("------externalList--------" + externalList.toString());
                    mExternalAdapter.notifyDataSetChanged();

                } else {
                    showToast(message);
                }
                break;
            case EXTERNAL_SEND_SMS://发送短信
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    showToast(message);
                } else {
                    showToast(message);
                }
                hideProgressDialog();
                break;
            case EXTERNAL_DELETE://删除外部监控人员
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    showToast(message);
                    Iterator<ExternalEntity> mIterator = externalList.iterator();
                    while (mIterator.hasNext()) {
                        String e = mIterator.next().id;
                        if (e.equals(chooseId)) {
                            mIterator.remove();
                        }
                    }
                    mExternalAdapter.notifyDataSetChanged();
                } else {
                    showToast(message);
                }
                hideProgressDialog();
                break;
        }
        showOrHide(false);
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
        showOrHide(false);
        switch (requestId) {
            case EXTERNAL_ADD_PHONE:

                break;
            case EXTERNAL_SEND_SMS:
                hideProgressDialog();
                break;
            case EXTERNAL_DELETE:
                hideProgressDialog();
                break;
        }
    }

    /**
     * 页面布局的隐藏或显示
     *
     * @param isShow
     */
    private void showOrHide(boolean isShow) {
        if (isShow) {
            llExternalAll.setVisibility(View.GONE);
            progressExternal.setVisibility(View.VISIBLE);
        } else {
            llExternalAll.setVisibility(View.VISIBLE);
            progressExternal.setVisibility(View.GONE);
        }
    }

    /**
     * 外部监控页面跳转
     *
     * @param context
     */
    public static void invoke(Context context, String packageId, String begin, String end) {
        Intent intent = new Intent(context, ExternalMonitoringActivity.class);
        intent.putExtra("packageId", packageId);
        intent.putExtra("begin", begin);
        intent.putExtra("end", end);
        context.startActivity(intent);
    }

    // 文本分享
    private void shareText(String title, String url, String detail) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = detail;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
