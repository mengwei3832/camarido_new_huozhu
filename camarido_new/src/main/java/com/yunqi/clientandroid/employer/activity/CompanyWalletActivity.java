package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.entity.GetWalletData;
import com.yunqi.clientandroid.employer.request.GetWalletDataRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.view.RiseNumberTextView;

/**
 * @Description:公司钱包页面
 * @ClassName: CompanyWalletActivity
 * @author: chengtao
 * @date: 2016-7-4 下午1:25:24
 */
public class CompanyWalletActivity extends BaseActivity implements
        OnClickListener {
    private RiseNumberTextView countRemain;// 账户余额
    private TextView daiJieYunFei;// 待结运费
    private TextView yuEJieSuanMoney;// 余额结算金额
    private TextView cashMoney;// 现金结算金额
    private RelativeLayout rlAnQuan;
    private RelativeLayout rlMingXi;
    private LinearLayout llWalletYuE, llWalletXianJin;
    private TextView tvWalletDaiJie;

    // 页面请求
    private GetWalletDataRequest getWalletDataRequest;
    // 请求ID
    private final static int GET_WALL_DATA_REQUEST = 1;

    /* 判断跳转页面标志 */
    private int FERIGHT = 0;
    private int BALANCE = 1;
    private int CASH = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_activity_wallet;
    }

    @Override
    protected void initView() {
        countRemain = obtainView(R.id.tv_wallet_balance);
        daiJieYunFei = obtainView(R.id.tv_daijie_yunfei);
        yuEJieSuanMoney = obtainView(R.id.tv_yu_e_jiesuan);
        cashMoney = obtainView(R.id.tv_xian_jin_jiesuan);
        rlAnQuan = obtainView(R.id.rl_an_quan);
        rlMingXi = obtainView(R.id.rl_ming_xi);
        llWalletYuE = obtainView(R.id.ll_wallet_yu_e);
        llWalletXianJin = obtainView(R.id.ll_wallet_xian_jin);
        tvWalletDaiJie = obtainView(R.id.tv_wallet_daijie);
        initActionBar();
    }

    @Override
    protected void initData() {
        getWallData();
    }

    /**
     * @throws
     * @Description:获取钱包信息
     * @Title:getWallData
     * @return:void
     * @Create: 2016年6月16日 下午11:09:54
     * @Author : chengtao
     */
    private void getWallData() {
        getWalletDataRequest = new GetWalletDataRequest(mContext);
        getWalletDataRequest.setRequestId(GET_WALL_DATA_REQUEST);
        httpPost(getWalletDataRequest);
    }

    @Override
    protected void setListener() {
        rlAnQuan.setOnClickListener(this);
        rlMingXi.setOnClickListener(this);
        llWalletYuE.setOnClickListener(this);
        llWalletXianJin.setOnClickListener(this);
        tvWalletDaiJie.setOnClickListener(this);
        daiJieYunFei.setOnClickListener(this);
    }

    /**
     * @throws
     * @Description:初始化ActionBar
     * @Title:initActionBar
     * @return:void
     * @Create: 2016年6月7日 下午5:05:59
     * @Author : chengtao
     */
    private void initActionBar() {
        setActionBarTitle("企业钱包");
        setActionBarLeft(R.drawable.fanhui);
        setOnActionBarLeftClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_daijie_yunfei:
            case R.id.tv_wallet_daijie://进入待结算运费明细页面
//                Intent intent = new Intent(mContext,ShippingDetailsActivity.class);
//                intent.putExtra("TYPE",FERIGHT);
//                startActivity(intent);
                break;
            case R.id.ll_wallet_yu_e://进入线上结算运费明细页面
//                Intent intent1 = new Intent(mContext,ShippingDetailsActivity.class);
//                intent1.putExtra("TYPE",BALANCE);
//                startActivity(intent1);
                break;
            case R.id.ll_wallet_xian_jin://进入线下结算运费明细页面
//                Intent intent2 = new Intent(mContext,ShippingDetailsActivity.class);
//                intent2.putExtra("TYPE",CASH);
//                startActivity(intent2);
                break;
            case R.id.rl_an_quan:// 安全
                //友盟统计首页
                mUmeng.setCalculateEvents("enterprise_wallet_safety");

                SafeCenterActivity.invoke(CompanyWalletActivity.this);
                break;
            case R.id.rl_ming_xi:// 明细
                //友盟统计首页
                mUmeng.setCalculateEvents("enterprise_wallet_details");

                MingXiActivity.invoke(CompanyWalletActivity.this);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess = response.isSuccess;
        String message = response.message;
        switch (requestId) {
            case GET_WALL_DATA_REQUEST:
                if (isSuccess) {
                    GetWalletData data = (GetWalletData) response.singleData;
                    String AccountBalance = data.AccountBalance; // 账户余额
                    String Freight = data.Freight; // 待结运费
                    String DownOut = data.DownOut; // 现金结算金额
                    String LineOut = data.LineOut; // 余额结算金额

                    if (AccountBalance != null
                            && !TextUtils.isEmpty(AccountBalance)) {
                        startRiseTextView(countRemain, AccountBalance);
                    }
                    if (Freight != null && !TextUtils.isEmpty(Freight)) {
                        daiJieYunFei.setText(Freight);
                    }
                    if (DownOut != null && !TextUtils.isEmpty(DownOut)) {
                        cashMoney.setText(DownOut);
                    }
                    if (LineOut != null && !TextUtils.isEmpty(LineOut)) {
                        yuEJieSuanMoney.setText(LineOut);
                    }
                } else {
                    showToast(message);
                }
                break;

            default:
                break;
        }
    }

    /**
     * @throws
     * @Description:滚动数字
     * @Title:startRiseTextView
     * @return:void
     * @Create: 2016年6月16日 下午11:29:11
     * @Author : chengtao
     */
    private void startRiseTextView(RiseNumberTextView view, String number) {
        // 设置数据
        view.withNumber(Float.parseFloat(number));
        // 设置动画播放时间
        view.setDuration(1000);
        // 开始播放动画
        view.start();
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
    }

    /**
     * @param context
     * @throws
     * @Description:企业钱包跳转
     * @Title:invoke
     * @return:void
     * @Create: 2016年6月7日 下午5:11:12
     * @Author : chengtao
     */
    public static void invoke(Context context) {
        Intent intent = new Intent(context, CompanyWalletActivity.class);
        context.startActivity(intent);
    }
}
