package com.yunqi.clientandroid.employer.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.CalendarContract.Colors;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.R.color;
import com.yunqi.clientandroid.employer.activity.EmployerWebviewActivity;
import com.yunqi.clientandroid.employer.activity.HelpPkgActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.entity.BillingDetail;
import com.yunqi.clientandroid.employer.request.BillingDetailRequest;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:结算完成后的结算详情页面
 * @ClassName: BillingDetailFragment
 * @author: mengwei
 * @date: 2016-6-27 下午3:15:12
 */
public class BillingDetailFragment extends BaseFragment implements
        OnClickListener {
    // 控件对象
    private TextView tvBillingBegin;
    private TextView tvBillingBeginCity;
    private TextView tvBillingEnd;
    private TextView tvBillingEndCity;
    private TextView tvBillingKuangFa;
    private TextView tvBillingQianShou;
    private TextView tvBillingTuSun;
    private TextView tvBillingUnitPrice;
    private TextView tvBillingChoose;
    private TextView tvBillingYing;
    private TextView tvBillingShi;
    private TextView tvBillingBeiZhu;
    private TextView tvBillingTime;
    private LinearLayout llBillingDetail;
    private ProgressBar pbBillingBar;
    private TextView tvBillingXiaHua;
    private LinearLayout llBillingGuiZe;
    private TextView tvBillingYingMoney;
    private LinearLayout rlInsurance, rlJieTime;

    private String ticketId;
    private String ticketStatus;
    private String packageBeginName;
    private String packageEndName;
    private String beginCity;
    private String beginCounty;
    private String endCity;
    private String endCounty;

    // ------------------新增-------------

    // -----------------控件--------------
    private TextView tvCarNumber;// 车牌号码
    private TextView tvXinXiBu;// 信息部
    private TextView tvSendPackageTime;// 发包时间
    private TextView tvYunDanNumber;// 运单号码
    private TextView tvTuSunLv;
    private TextView tvBeginDetail;// 具体起点
    private TextView tvEndDetail;// 具体终点
    private TextView tvShopPrice; // 货品单价
    private TextView tvShopCateGory;// 货品种类
    private TextView tvInsuranceNumber;// 保险单号
    private LinearLayout llShowHide;// 显示隐藏的信息
    private RelativeLayout rlDianDetail;
    private ImageView ivArraw;
    private LinearLayout llBillingYingjieDetail;//应结运费详细信息

    // 显示承运信息的标记
    private boolean isShowHide = false;

    //应结运费详细信息弹出框
    private boolean isShowDetail;
    private LinearLayout llShowBillingDetail;
    private TextView tvYingjieDetailYunfei;//运费（总的）
    private TextView tvYingjieDetailChaoDun;//超额途损（吨数）
    private TextView tvYingjieDetailChaoYun;//超额途损（运费）
    private TextView tvYingjieDetailHeDun;//合理途损（吨数）

    @Override
    protected int getLayoutId() {
        return R.layout.employer_fragment_billing_detail;
    }

    @Override
    protected void initView(View _rootView) {
        // 初始化控件对象
        initFindView();
        ticketId = ((UploadOrderActivity) getActivity()).getTicketId();
        ticketStatus = ((UploadOrderActivity) getActivity()).getTicketStatus();

		/*
         * packageBeginName = ((UploadOrderActivity) getActivity())
		 * .getPackageBeginName();
		 * 
		 * packageEndName = ((UploadOrderActivity) getActivity())
		 * .getPackageEndName();
		 */
    }

    /**
     * @throws @Create: 2016-6-27 下午4:17:40
     * @Description:初始化控件对象
     * @Title:initFindView
     * @return:void
     * @Author : mengwei
     */
    private void initFindView() {
        tvBillingBegin = obtainView(R.id.tv_billing_begin);
        // tvBillingBeginCity = obtainView(R.id.tv_billing_begin_city);
        tvBillingEnd = obtainView(R.id.tv_billing_end);
        // tvBillingEndCity = obtainView(R.id.tv_billing_end_city);
        tvBillingKuangFa = obtainView(R.id.tv_billing_kuangfa);
        tvBillingQianShou = obtainView(R.id.tv_billing_qianshou);
        tvBillingTuSun = obtainView(R.id.tv_billing_tusun);
        tvBillingUnitPrice = obtainView(R.id.tv_billing_unitprice);
        tvBillingChoose = obtainView(R.id.tv_billing_choose);
        // tvBillingYing = obtainView(R.id.tv_billing_ying);
        tvBillingShi = obtainView(R.id.tv_billing_shi);
        tvBillingBeiZhu = obtainView(R.id.tv_billing_zhuang);
        llBillingDetail = obtainView(R.id.ll_billing_detail);
        pbBillingBar = obtainView(R.id.pb_billing_bar);
        tvBillingTime = obtainView(R.id.tv_billing_jietime);
        // llBillingGuiZe = obtainView(R.id.ll_billing_guize);
        tvBillingXiaHua = obtainView(R.id.tv_billing_xiahuaxian);
        tvBillingYingMoney = obtainView(R.id.tv_billing_yingjie_money);
        tvShopPrice = obtainView(R.id.tv_billing_shop_price);
        tvShopCateGory = obtainView(R.id.tv_billing_shop_category);
        tvInsuranceNumber = obtainView(R.id.tv_billing_insurance_number);
        tvBeginDetail = obtainView(R.id.tv_biling_begin_detail);
        tvEndDetail = obtainView(R.id.tv_biling_end_detail);
        llShowHide = obtainView(R.id.ll_billing_show_hide);
        rlDianDetail = obtainView(R.id.rl_billing_dian_detail);
        ivArraw = obtainView(R.id.iv_biling_arraw);
        rlInsurance = obtainView(R.id.rl_billing_insurance);
        rlJieTime = obtainView(R.id.rl_billing_jieTime);

        tvBillingXiaHua.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvBillingXiaHua.setText("途损规则?");
        tvBillingXiaHua.setTextColor(Color.RED);

        // ------------新增------
        tvCarNumber = obtainView(R.id.tv_biling_car_number);
        tvXinXiBu = obtainView(R.id.tv_biling_xin_xi_bu);
        tvSendPackageTime = obtainView(R.id.tv_biling_send_package_time);
        tvYunDanNumber = obtainView(R.id.tv_biling_yun_dan_number);
        tvTuSunLv = obtainView(R.id.tv_billing_tusunlv);
        llBillingYingjieDetail = obtainView(R.id.ll_billing_yingjie_detail);
        tvYingjieDetailYunfei = obtainView(R.id.tv_billing_yingjie_detail_yunfei);
        tvYingjieDetailChaoDun = obtainView(R.id.tv_billing_yingjie_detail_chao_dun);
        tvYingjieDetailChaoYun = obtainView(R.id.tv_billing_yingjie_detail_chao_yun);
        tvYingjieDetailHeDun = obtainView(R.id.tv_billing_yingjie_detail_he_dun);
        llShowBillingDetail = obtainView(R.id.ll_billing_detail_yingjie);
    }

    @Override
    protected void initData() {
        pbBillingBar.setVisibility(View.VISIBLE);
        llBillingDetail.setVisibility(View.GONE);
        // 请求接口
        httpPost(new BillingDetailRequest(getActivity(), ticketId));
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess = response.isSuccess;
        String message = response.message;

        if (isSuccess) {
            BillingDetail billingDetail = (BillingDetail) response.singleData;

            String mTicketWeightInit = billingDetail.ticketWeightInit;
            String mTicketWeightReach = billingDetail.ticketWeightReach;
            String mTicketWeightShortfall = billingDetail.ticketWeightShortfall;
            String mFreightPrice = billingDetail.freightPrice;
            int mSettleType = billingDetail.settleType;
            String mTicketExpectedAmount = billingDetail.ticketExpectedAmount;
            String mTicketSettleAmount = billingDetail.ticketSettleAmount;
            String memo = billingDetail.memo;
            String mSettletime = billingDetail.settletime;// 结算时间

            // 新增
            String StartAddressName = billingDetail.StartAddressName; // 起始地址名称
            String StartCityName = billingDetail.StartCityName; // 起始市级名称
            String StartSubName = billingDetail.StartSubName; // 起始区县名称
            String EndAddressName = billingDetail.EndAddressName; // 目的地址名称
            String EndCityName = billingDetail.EndCityName; // 目的市级名称
            String EndSubName = billingDetail.EndSubName; // 目的区县名称
            String InfoDerpartTenant = billingDetail.InfoDerpartTenant;// 信息部公司名称
            String VehicleNo = billingDetail.VehicleNo;// 车牌号
            String PackageStartTime = billingDetail.PackageStartTime;// 起始时间
            String PackageEndTime = billingDetail.PackageEndTime;// 截止时间
            String TicketLoadTime = billingDetail.TicketLoadTime;// 矿发时间
            String TicketSignTime = billingDetail.TicketSignTime;// 签收时间
            String TicketCode = billingDetail.TicketCode;// 单号
            String mPackageLoseRate = billingDetail.packageLoseRate;
            String mInfoDerpartTenantAliasesname = billingDetail.InfoDerpartTenantAliasesname;
            String mPackageBeginAddress = billingDetail.PackageBeginAddress;
            String mPackageEndAddress = billingDetail.PackageEndAddress;
            String mCategoryName = billingDetail.CategoryName;
            String mPolicyNo = billingDetail.PolicyNo;
            String mGoodsPrice = billingDetail.goodsPrice;
            String mTicketAmount = billingDetail.TicketAmount;
            String mExcessWeight = billingDetail.ExcessWeight;
            String mExcessAmount = billingDetail.ExcessAmount;
            String mResonWeight = billingDetail.ResonWeight;
            int mShortFallType = billingDetail.ShortFallType;

            Log.e("TAG", "-------------mTicketAmount---------------"
                    + mTicketAmount);
            Log.e("TAG", "-------------mExcessWeight---------------"
                    + mExcessWeight);
            Log.e("TAG", "-------------mExcessAmount---------------"
                    + mExcessAmount);
            Log.e("TAG", "-------------mResonWeight---------------"
                    + mResonWeight);

            //给应结运费详细信息赋值
            if (StringUtils.isStrNotNull(mTicketAmount)){
                tvYingjieDetailYunfei.setText(mTicketAmount+"元");
            } else {
                tvYingjieDetailYunfei.setText("0.00元");
            }
            if (StringUtils.isStrNotNull(mExcessWeight)){
                tvYingjieDetailChaoDun.setText(mExcessWeight+"吨");
            } else {
                tvYingjieDetailChaoDun.setText("0.00吨");
            }
            if (StringUtils.isStrNotNull(mExcessAmount)){
                tvYingjieDetailChaoYun.setText(mExcessAmount+"元");
            } else {
                tvYingjieDetailChaoYun.setText("0.00元");
            }
            if (StringUtils.isStrNotNull(mResonWeight)){
                tvYingjieDetailHeDun.setText(mResonWeight+"吨");
            } else {
                tvYingjieDetailHeDun.setText("0.00吨");
            }

            // 始发地
            if (StringUtils.isStrNotNull(StartAddressName)
                    && StringUtils.isStrNotNull(StartCityName)
                    && StringUtils.isStrNotNull(StartSubName)) {
                tvBillingBegin.setText(StartAddressName);
                // tvBillingBeginCity.setText(" (" + StartCityName
                // + getActivity().getResources().getString(R.string.tv_drop) +
                // StartSubName + ")");
            }

            // 具体起点
            if (StringUtils.isStrNotNull(mPackageBeginAddress)
                    && StringUtils.isStrNotNull(StartCityName)
                    && StringUtils.isStrNotNull(StartSubName)) {
                tvBeginDetail.setText(mPackageBeginAddress
                        + " ("
                        + StartCityName
                        + getActivity().getResources().getString(
                        R.string.tv_drop) + StartSubName + ")");
            }

            // 目的地
            if (StringUtils.isStrNotNull(EndAddressName)
                    && StringUtils.isStrNotNull(EndCityName)
                    && StringUtils.isStrNotNull(EndSubName)) {
                tvBillingEnd.setText(EndAddressName);
                // tvBillingEndCity.setText(" (" + EndCityName +
                // getActivity().getResources().getString(R.string.tv_drop)
                // + EndSubName + ")");
            }

            // 具体终点
            if (StringUtils.isStrNotNull(mPackageEndAddress)
                    && StringUtils.isStrNotNull(EndCityName)
                    && StringUtils.isStrNotNull(EndSubName)) {
                tvEndDetail.setText(mPackageEndAddress
                        + " ("
                        + EndCityName
                        + getActivity().getResources().getString(
                        R.string.tv_drop) + EndSubName + ")");
            }

            if (StringUtils.isStrNotNull(mPackageLoseRate)) {
                if (mShortFallType == 10){
                    double mTuSun = Double.valueOf(mPackageLoseRate);
                    tvTuSunLv.setText(StringUtils.sanToQianFenHao(mTuSun));
                } else if (mShortFallType == 20){
                    tvTuSunLv.setText(mPackageLoseRate + "吨/车");
                }
            } else {
                tvTuSunLv.setText("0‰");
            }

            // 车牌号
            if (StringUtils.isStrNotNull(VehicleNo)) {
                tvCarNumber.setText(VehicleNo);
            }
            // 信息部
            if (StringUtils.isStrNotNull(mInfoDerpartTenantAliasesname)) {
                tvXinXiBu.setText(getActivity().getString(
                        R.string.employer_jingjiren_name1)
                        + mInfoDerpartTenantAliasesname
                        + getActivity().getString(
                        R.string.employer_jingjiren_name2));
            }
            // 发包时间
            if (StringUtils.isStrNotNull(PackageStartTime)
                    && StringUtils.isStrNotNull(PackageEndTime)) {
                tvSendPackageTime.setText(StringUtils
                        .formatModify(PackageStartTime)
                        + " 至 "
                        + StringUtils.formatModify(PackageEndTime));
            }

            // 货品单价
            if (StringUtils.isStrNotNull(mGoodsPrice)) {
                tvShopPrice.setText(mGoodsPrice + "元/吨");
            }

            // 货品种类
            if (StringUtils.isStrNotNull(mCategoryName)) {
                tvShopCateGory.setText(mCategoryName);
            }

            // 运单单号
            if (StringUtils.isStrNotNull(TicketCode)) {
                tvYunDanNumber.setText(TicketCode);
            }

            // 保险单号
            if (StringUtils.isStrNotNull(mPolicyNo)) {
                tvInsuranceNumber.setText(mPolicyNo);
            } else {
                rlInsurance.setVisibility(View.GONE);
                tvInsuranceNumber.setText("");
            }

            // 给控件赋值
            if (StringUtils.isStrNotNull(mTicketWeightInit)) {
                tvBillingKuangFa.setText(StringUtils
                        .saveTwoNumber(mTicketWeightInit) + "吨");
            } else {
                tvBillingKuangFa.setText("0.00吨");
            }

            if (StringUtils.isStrNotNull(mTicketWeightReach)) {
                tvBillingQianShou.setText(StringUtils
                        .saveTwoNumber(mTicketWeightReach) + "吨");
            } else {
                tvBillingQianShou.setText("0.00吨");
            }

            if (StringUtils.isStrNotNull(mTicketWeightShortfall)) {
                tvBillingTuSun.setText(StringUtils
                        .saveTwoNumber(mTicketWeightShortfall) + "吨");
            } else {
                tvBillingTuSun.setText("0.00吨");
            }

            if (StringUtils.isStrNotNull(mFreightPrice)) {
                tvBillingUnitPrice.setText(StringUtils
                        .saveTwoNumber(mFreightPrice) + "元/吨");
            } else {
                tvBillingUnitPrice.setText("0.00元/吨");
            }

            if (mSettleType == 0) {
                tvBillingChoose.setText("余额支付");
            } else if (mSettleType == 1) {
                tvBillingChoose.setText("现金支付");
            }
            // tvBillingYing.setText(mTicketExpectedAmount +"元");
            if (StringUtils.isStrNotNull(mTicketExpectedAmount)) {
                tvBillingYingMoney.setText(StringUtils
                        .saveTwoNumber(mTicketExpectedAmount));
            }

            if (StringUtils.isStrNotNull(mTicketSettleAmount)) {
                tvBillingShi.setText(StringUtils
                        .saveTwoNumber(mTicketSettleAmount));
            }

            if (!TextUtils.isEmpty(packageBeginName)
                    && packageBeginName != null) {
                tvBillingBegin.setText(packageBeginName);
            }

            if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
                tvBillingEnd.setText(packageEndName);
            }

            if (StringUtils.isStrNotNull(memo)) {
                tvBillingBeiZhu.setText(memo);
            }

            if (StringUtils.isStrNotNull(mSettletime)) {
                String billTime = StringUtils.formatPerform(mSettletime);
                tvBillingTime.setText(billTime);
            } else {
                rlJieTime.setVisibility(View.GONE);
                tvBillingTime.setText("");
            }
        }

        pbBillingBar.setVisibility(View.GONE);
        llBillingDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
    }

    @Override
    protected void setListener() {
        tvBillingXiaHua.setOnClickListener(this);
        rlDianDetail.setOnClickListener(this);
        llBillingYingjieDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_billing_yingjie_detail://点击显示运单应结运费详细信息弹窗
                if (!isShowDetail){
                    llShowBillingDetail.setVisibility(View.VISIBLE);
                    isShowDetail = true;
                } else {
                    llShowBillingDetail.setVisibility(View.GONE);
                    isShowDetail = false;
                }
                break;
            case R.id.rl_billing_dian_detail:// 显示隐藏的承运信息
                if (!isShowHide) {
                    llShowHide.setVisibility(View.VISIBLE);
                    ivArraw.setImageResource(R.drawable.billing_arrow_down);
                    isShowHide = true;
                } else {
                    llShowHide.setVisibility(View.GONE);
                    ivArraw.setImageResource(R.drawable.billing_arrow_right);
                    isShowHide = false;
                }
                break;
            case R.id.tv_billing_xiahuaxian:
                // 跳转到帮助页面
//                HelpPkgActivity.invoke(getActivity());
                Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
                intent.putExtra("Url", HostUtil.getWebHost() + "pfh/ShipperHelp");
                intent.putExtra("Title",getString(R.string.help));
                getActivity().startActivity(intent);
                break;

            default:
                break;
        }
    }

//    /**
//     * 显示应结运费弹窗详细信息
//     */
//    private void showTicketYingjieDetail() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.employer_dialog_ticket_yingjie_detail, null);
//        tvYingjieDetailYunfei = (TextView) view.findViewById(R.id.tv_dialog_yingjie_detail_yunfei);
//        tvYingjieDetailChaoDun = (TextView) view.findViewById(R.id.tv_dialog_yingjie_detail_chao_dun);
//        tvYingjieDetailChaoYun = (TextView) view.findViewById(R.id.tv_dialog_yingjie_detail_chao_yun);
//        tvYingjieDetailHeDun = (TextView) view.findViewById(R.id.tv_dialog_yingjie_detail_he_dun);
//        tvYingjieDetailHeYun = (TextView) view.findViewById(R.id.tv_dialog_yingjie_detail_he_yun);
//        Button btSure = (Button) view.findViewById(R.id.bt_dialog_yingjie_detail_cancel);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        dialog = builder.create();
//        dialog.setView(view);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//        btSure.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }

}
