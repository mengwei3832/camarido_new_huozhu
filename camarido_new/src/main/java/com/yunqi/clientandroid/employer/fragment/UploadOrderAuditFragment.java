package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.GridViewKeybroadAdapter;
import com.yunqi.clientandroid.employer.activity.ChengYunTongJiActivity;
import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity;
import com.yunqi.clientandroid.employer.activity.EmployerWebviewActivity;
import com.yunqi.clientandroid.employer.activity.HelpPkgActivity;
import com.yunqi.clientandroid.employer.activity.QuoteActivity;
import com.yunqi.clientandroid.employer.activity.SetOrResetSafePasswordActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.adapter.KeyboardAdapter;
import com.yunqi.clientandroid.employer.entity.PingTaiKeFu;
import com.yunqi.clientandroid.employer.request.ApproveAndSettleRequest;
import com.yunqi.clientandroid.employer.request.GetTicketSettlePreviewRequest;
import com.yunqi.clientandroid.employer.request.IsExitsPwdPayRequest;
import com.yunqi.clientandroid.employer.request.PingTaiKeFuRequest;
import com.yunqi.clientandroid.employer.request.SetCurrentNewPrice;
import com.yunqi.clientandroid.entity.OrderAuditInfo;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.request.SetUserInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.InputPassUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PayTimeUtils;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.StarPassword;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方待审核当前界面的fragment
 * @date 16/3/1
 */
public class UploadOrderAuditFragment extends BaseFragment implements
        OnClickListener, OnItemClickListener {

    private UploadOrderAuditActivity activity;
    private int ticketStatus;// 执行状态
    private String ticketId;// 订单id
    private String packageBeginName;
    private String packageBeginAddress;
    private String packageEndName;
    private String packageEndAddress;
    private int pInsuranceType;// 0：无保险 1：平台送保险 2：自己购买保险
    private View parentView;
    private TextView tvCheckBegin;
    private TextView tvCheckBeginCity;
    private TextView tvCheckEnd;
    private TextView tvCheckEndCity;
    private TextView tvCheckKuangFa;
    private TextView tvCheckQianShou;
    private TextView tvCheckTuSun;
    private TextView tvCheckUnitPrice;
    private TextView tvCheckChoose;
    private TextView tvCheckYing;
    private EditText etCheckShi;
    private EditText etCheckBeiZhu;
    private Button btCheckBilling;
    private StarPassword etPassword;// 自定义密码输入框

    // private String beginCity, beginCounty, endCity, endCounty;
    // private String ticketSettleAmount;// 实结金额
    private int btnStatus = 1;// 结算按钮状态--2为置灰不可点击,1为可点击
    private PopupWindow updatePayPopupWindow;// 修改支付方式的弹出框
    private PopupWindow inputPayPassPopupWindow;// 输入支付密码的弹出框
    private TextView tv_update_cancle_mine, tv_update_sure_mine,
            tv_should_money_text;
    private EditText et_update_nickName_mine, et_update_age_mine;
    private ImageView iv_choose_after, iv_choose_cash;
    private String ticketExpectedAmount; // 应结金额
    private int payWay_num = 1; // 记录支付方式的标记
    private String actual_money; // 实结金额
    private double sMoney;// 应结金额上浮10%后的金额数
    private double xMoney;// 应结金额下浮10%后的金额数
    private int pSettleType;// 0：可以线上线下支付；10：只能线上（余额）支付；20：只能线下（现金）支付

    private TextView tv_input_cancle_pay, tv_input_sure_pay,
            tv_actual_money_text;
    private ProgressBar passwordProgressBar;
    private GridView gv_pass_keyboard;
    // 密码框TextView的ID
    private int[] passID = {R.id.tv_pass_one, R.id.tv_pass_two,
            R.id.tv_pass_three, R.id.tv_pass_four, R.id.tv_pass_fives,
            R.id.tv_pass_six};
    // private TextView passTV = null;
    private List<String> list = null; // 存储键盘数据的集合
    private List<String> passList = new ArrayList<String>();// 存储所输密码的集合
    private String fPayPass; // 支付密码
    private KeyboardAdapter gAdapter;
    private View payPass_view;
    private String remake; // 结算备注

    // 存放SP的key
    public static final String TICKETSTATUS = "TICKET_STATUS";

    // 本页面请求
    private GetTicketSettlePreviewRequest mGetTicketSettlePreviewRequest;
    private ApproveAndSettleRequest mApproveAndSettleRequest;
    private IsExitsPwdPayRequest isExitsPwdPayRequest;
    private PingTaiKeFuRequest pingTaiKeFuRequest;

    // 本页面请求id
    private final int GET_TICKET_SETTLEPREVIEW_REQUEST = 1;
    private final int APPROVE_ANDSETTLE_REQUEST = 2;
    private final static int IS_EXITS_PWD_PAY_REQUEST = 3;
    private final static int PING_TAI_KE_FU_REQUEST = 4;

    // 新增的控件
    private CheckBox cb_choose_after;// 余额支付
    private CheckBox cb_choose_cash;// 现金支付
    private CheckBox cb_choose_cash_no;// 有保险下现金支付
    private TextView tv_choose_cash_no1;
    private View pbCheckBar;
    private LinearLayout llCheckHide;
    private LinearLayout llCheckTuSui;
    private TextView tvCheckXiaHua;
    private EditText etCheckShiJie;
    private LinearLayout rlInsurance;

    private int first = 0;// 支付密码框的标记
    private int two = 0;// 修改支付方式框的标记
    private String yingjieMoney;// 应结金额的一个变量
    private double shiJieMoney;// 实结金额输入框获取的值
    // private boolean isExitsPayPasssword = false;// 是否存在支付密码

    // 是否第一次进入
    private boolean isFirstIn = true;
    // 运费异常
    private boolean isYunFeiError = false;
    private String serviceNumber = null;

    // 结算进度条弹框
    private PopupWindow pbPopupWindow;
    // 确认等待结算弹框
    private PopupWindow surePopupWindow;
    private LinearLayout llDengDai;

    // ------------------新增-------------

    // -----------------控件--------------
    private TextView tvCarNumber;// 车牌号码
    private TextView tvXinXiBu;// 信息部
    private TextView tvSendPackageTime;// 发包时间
    private TextView tvYunDanNumber;// 运单号码
    private TextView tvTusunlv;
    private TextView tvBeginDetail;// 具体起点
    private TextView tvEndDetail;// 具体终点
    private TextView tvShopPrice; // 货品单价
    private TextView tvShopCateGory;// 货品种类
    private TextView tvInsuranceNumber;// 保险单号
    private LinearLayout llShowHide;// 显示隐藏的信息
    private RelativeLayout rlDianDetail;
    private ImageView ivArraw;
    private LinearLayout llYingjieDetail;//应结运费的详细

    // 显示承运信息的标记
    private boolean isDianShow = false;

    //应结运费详细信息弹出框
    private LinearLayout llTicketDetailYingjie;
    private TextView tvYingjieDetailYunfei;//运费（总的）
    private TextView tvYingjieDetailChaoDun;//超额途损（吨数）
    private TextView tvYingjieDetailChaoYun;//超额途损（运费）
    private TextView tvYingjieDetailHeDun;//合理途损（吨数）
    private boolean isShowDetail = false;
    //接口返回的值（应结运费详细信息弹出框）
    private String mTicketAmount;
    private String mExcessWeight;
    private String mExcessAmount;
    private String mResonWeight;

    @Override
    protected void initData() {
        pbCheckBar.setVisibility(View.VISIBLE);
        hideView(true);
        // 获取是否有支付密码
        isExitsPwdPay();
        // 根据是否开票显示修改按钮
        // if (pSettleType == 0) {
        // tv_update_knot_rate.setVisibility(View.VISIBLE);
        // } else {
        // tv_update_knot_rate.setVisibility(View.GONE);
        // }

        L.e("---------传递的状态------------"+ticketStatus);

    }

    private void hideView(boolean hide) {
        if (hide) {
            llCheckHide.setVisibility(View.GONE);
            btCheckBilling.setVisibility(View.GONE);
        } else {
            llCheckHide.setVisibility(View.VISIBLE);
            btCheckBilling.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.employer_fragment_billing_detail_shenhe;
    }

    @Override
    protected void initView(View uploadAuditView) {
        // 获取当前的activity
        activity = (UploadOrderAuditActivity) getActivity();
        ticketId = ((UploadOrderAuditActivity) getActivity()).getTicketId();
        ticketStatus = Integer.valueOf(((UploadOrderAuditActivity)  getActivity()).getTicketStatus());
        // TODO--获取缓存状态
//        ticketStatus = CacheUtils.getString(getActivity(), TICKETSTATUS, "");
        parentView = LayoutInflater.from(this.getActivity()).inflate(
                R.layout.fragment_uploadaudit_employer, null);
        tvCheckBegin = obtainView(R.id.tv_check_begin);
        // tvCheckBeginCity = obtainView(R.id.tv_check_begin_city);
        tvCheckEnd = obtainView(R.id.tv_check_end);
        // tvCheckEndCity = obtainView(R.id.tv_check_end_city);
        tvCheckKuangFa = obtainView(R.id.tv_check_kuangfa);
        tvCheckQianShou = obtainView(R.id.tv_check_qianshou);
        tvCheckTuSun = obtainView(R.id.tv_check_tusun);
        tvCheckUnitPrice = obtainView(R.id.tv_check_unitprice);
        tvCheckChoose = obtainView(R.id.tv_check_choose);
        tvCheckYing = obtainView(R.id.tv_check_ying);
        // etCheckShi = obtainView(R.id.et_check_shi);
        etCheckBeiZhu = obtainView(R.id.et_check_zhuang);
        btCheckBilling = obtainView(R.id.bt_check_billing);
        pbCheckBar = obtainView(R.id.pb_check_bar);
        llCheckHide = obtainView(R.id.ll_check_hide);
        llCheckTuSui = obtainView(R.id.ll_check_guize);
        tvCheckXiaHua = obtainView(R.id.tv_check_xiahuaxian);
        etCheckShiJie = obtainView(R.id.et_check_shi);
        tvTusunlv = obtainView(R.id.tv_check_tusunlv);
        tvShopPrice = obtainView(R.id.tv_biling_shop_price);
        tvShopCateGory = obtainView(R.id.tv_biling_shop_category);
        tvInsuranceNumber = obtainView(R.id.tv_biling_insurance_number);
        tvBeginDetail = obtainView(R.id.tv_billing_begin_detail);
        tvEndDetail = obtainView(R.id.tv_billing_end_detail);
        llShowHide = obtainView(R.id.ll_billing_show_hide);
        rlDianDetail = obtainView(R.id.rl_biling_dian_detail);
        ivArraw = obtainView(R.id.iv_billing_arraw);
        rlInsurance = obtainView(R.id.rl_check_insurance);
        llYingjieDetail = obtainView(R.id.ll_ticket_yingjie_detail);

        tvCheckXiaHua.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCheckXiaHua.setText("途损规则?");
        tvCheckXiaHua.setTextColor(Color.RED);

        // ------------新增------
        tvCarNumber = obtainView(R.id.tv_biling_car_number);
        tvXinXiBu = obtainView(R.id.tv_biling_xin_xi_bu);
        tvSendPackageTime = obtainView(R.id.tv_biling_send_package_time);
        tvYunDanNumber = obtainView(R.id.tv_biling_yun_dan_number);
        tvYingjieDetailYunfei = obtainView(R.id.tv_dialog_yingjie_detail_yunfei);
        tvYingjieDetailChaoDun = obtainView(R.id.tv_dialog_yingjie_detail_chao_dun);
        tvYingjieDetailChaoYun = obtainView(R.id.tv_dialog_yingjie_detail_chao_yun);
        tvYingjieDetailHeDun = obtainView(R.id.tv_dialog_yingjie_detail_he_dun);
        llTicketDetailYingjie = obtainView(R.id.ll_ticket_detail_yingjie);
    }

    @Override
    protected void setListener() {
        // 初始化点击事件
        initOnClick();
    }

    /**
     * @throws @Create: 2016年6月16日 下午6:33:30
     * @Description:判断是否存在支付密码
     * @Title:isExitsPwdPay
     * @return:void
     * @Author : chengtao
     */
    private void isExitsPwdPay() {
        isExitsPwdPayRequest = new IsExitsPwdPayRequest(getActivity());
        isExitsPwdPayRequest.setRequestId(IS_EXITS_PWD_PAY_REQUEST);
        httpGet(isExitsPwdPayRequest);
    }

    // 初始化点击事件
    private void initOnClick() {
        btCheckBilling.setOnClickListener(this);
        llCheckTuSui.setOnClickListener(this);
        rlDianDetail.setOnClickListener(this);
        llYingjieDetail.setOnClickListener(this);
        //途损吨数保留两位小数
        etCheckShiJie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2){
                    showToast("您好，运费最小只能输入到分");
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        // tv_update_knot_rate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_ticket_yingjie_detail://显示应结金额详细信息
                //友盟统计首页
                mUmeng.setCalculateEvents("waybill_click_show_detail");

                if (!isShowDetail){
                    llTicketDetailYingjie.setVisibility(View.VISIBLE);
                    isShowDetail = true;
                } else {
                    llTicketDetailYingjie.setVisibility(View.GONE);
                    isShowDetail = false;
                }
                break;
            case R.id.rl_biling_dian_detail:// 点击显示隐藏的承运信息
                //友盟统计首页
                mUmeng.setCalculateEvents("waybill_click_show_carrier");

                if (!isDianShow) {
                    llShowHide.setVisibility(View.VISIBLE);
                    ivArraw.setImageResource(R.drawable.billing_arrow_down);
                    isDianShow = true;
                } else {
                    llShowHide.setVisibility(View.GONE);
                    ivArraw.setImageResource(R.drawable.billing_arrow_right);
                    isDianShow = false;
                }
                break;
            case R.id.ll_check_guize:
                //友盟统计首页
                mUmeng.setCalculateEvents("waybill_click_help_tusun");

//                HelpPkgActivity.invoke(getActivity());
                Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
                intent.putExtra("Url", HostUtil.getWebHost() + "pfh/ShipperHelp");
                intent.putExtra("Title",getString(R.string.help));
                getActivity().startActivity(intent);
                break;
            case R.id.bt_check_billing:
                //友盟统计首页
                mUmeng.setCalculateEvents("waybill_click_settlement");

                // // 点击进行结算
                // String amoutSettlement = etAmountSettlement.getText().toString()
                // .trim();
                // String memo = etNote.getText().toString().trim();
                //
                // if (TextUtils.isEmpty(amoutSettlement)) {
                // showToast("请输入实结金额");
                // return;
                // }
                //
                // // 点击进行结算
                // approveAndSettle(amoutSettlement, memo);

                Log.d("TAG", "----------获得实结输入框的值-----------" + shiJieMoney);

                String shijieText = etCheckShiJie.getText().toString().trim();

                if (!StringUtils.isStrNotNull(shijieText)) {
                    showToast("实结金额不可为空");
                    return;
                }

                // 获取实结输入框的值
                shiJieMoney = Double.valueOf(shijieText);
                if (shiJieMoney == 0) {
                    showToast("实结金额不能为0");
                    return;
                }

                // 清空储存密码的集合
                passList.clear();
                // 点击支付显示支付密码框
                showInputPayPass(shijieText);
                break;

            default:
                break;
        }
    }

    // 点击支付显示支付密码框
    private void showInputPayPass(String mShiJieText) {

        if (first == 1) {
            /*
			 * for (int i = 0; i < 6; i++) { passTV = (TextView)
			 * payPass_view.findViewById(passID[i]); passTV.setText(""); }
			 */
        }

        if (inputPayPassPopupWindow == null) {
            inputPayPass();
        }

        // 设置提示信息
        if (pSettleType == 0) {// 线上结算
            tv_actual_money_text.setText("实结金额（余额）:"
                    + StringUtils.saveTwoNumber(mShiJieText));
        } else if (pSettleType == 1) {// 线下结算
            tv_actual_money_text.setText("实结金额（现金）:"
                    + StringUtils.saveTwoNumber(mShiJieText));
        }

        // 弹出框出来的方式
        inputPayPassPopupWindow
                .showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

    }

    private void isPass() {
        if (passList.size() == 6) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < passList.size(); i++) {
                buffer.append(passList.get(i).toString());
                // passTV = (TextView) payPass_view.findViewById(passID[i]);
                // passTV.setText("");
            }
            fPayPass = buffer.toString();

            Log.e("TAG", "获取的支付密码----------------" + fPayPass);

            approveAndSettle();
        }
    }

    private void inputPayPass() {
        inputPayPassPopupWindow = new PopupWindow(activity);

        payPass_view = getActivity().getLayoutInflater().inflate(
                R.layout.audit_input_pay, null);

        inputPayPassPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        inputPayPassPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        inputPayPassPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        inputPayPassPopupWindow.setContentView(payPass_view);
        inputPayPassPopupWindow.setOutsideTouchable(true);
        inputPayPassPopupWindow.setFocusable(true);
        inputPayPassPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
        inputPayPassPopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 找控件对象
        // tv_input_cancle_pay = (TextView)
        // payPass_view.findViewById(R.id.tv_input_cancle_pay);
        // tv_input_sure_pay = (TextView)
        // payPass_view.findViewById(R.id.tv_input_sure_pay);
        tv_actual_money_text = (TextView) payPass_view
                .findViewById(R.id.tv_actual_money_text);
        gv_pass_keyboard = (GridView) payPass_view
                .findViewById(R.id.gv_pass_keyboard);
        etPassword = (StarPassword) payPass_view.findViewById(R.id.sp_password);
        passwordProgressBar = (ProgressBar) payPass_view
                .findViewById(R.id.password_progress_bar);
        // 为GridView适配数据
        initGridView();

        // GridView的监听
        gv_pass_keyboard.setOnItemClickListener(this);

    }

    // 为GridView适配数据
    private void initGridView() {
        // 实例化键盘集合
        list = new ArrayList<String>();
        // 把键盘的数据添加进集合中
        for (int i = 0, len = InputPassUtils.dataArray.length; i < len; i++) {
            list.add(InputPassUtils.dataArray[i]);
        }

        // 对GridView的监听
        gAdapter = new KeyboardAdapter(list, getActivity());
        gv_pass_keyboard.setAdapter(gAdapter);
    }

    // 结算接口
    private void approveAndSettle() {
        // 获取结算备注
        remake = etCheckBeiZhu.getText().toString().trim();

        Log.e("TAG", "--------------进行结算--");
        // TODO 结算请求-----------
        if (!TextUtils.isEmpty(ticketId) && ticketId != null
                && fPayPass != null) {
            int iIicketId = Integer.parseInt(ticketId);
            // 对得到的密码进行MD5加密
            String md5Password = StringUtils.md5(fPayPass);

            Log.e("TAG", "--------------iIicketId--" + iIicketId);
            Log.e("TAG", "--------------md5Password--" + md5Password.toString());
            Log.e("TAG", "--------------payWay_num--" + payWay_num);
            Log.e("TAG", "--------------ticketExpectedAmount--"
                    + ticketExpectedAmount.toString());
            // Log.e("TAG", "--------------remake--"+remake.toString());

            if (TextUtils.isEmpty(remake) || remake == null) {
                remake = "";
            }
            if (pSettleType == 0) {// 线上结算
                mApproveAndSettleRequest = new ApproveAndSettleRequest(
                        getActivity(), iIicketId, md5Password, shiJieMoney,
                        remake, 1, ticketExpectedAmount);
            } else if (pSettleType == 1) {// 线下结算
                mApproveAndSettleRequest = new ApproveAndSettleRequest(
                        getActivity(), iIicketId, md5Password, shiJieMoney,
                        remake, 2, ticketExpectedAmount);
            }
            // 弹出进度条
//            showProgressBarPop();
            showProgressDialog("结算中...");
            mApproveAndSettleRequest.setRequestId(APPROVE_ANDSETTLE_REQUEST);
            httpPost(mApproveAndSettleRequest);
        }
    }

    private void settingProgressBar() {
        pbPopupWindow = new PopupWindow(activity);

        View pb_view = getActivity().getLayoutInflater().inflate(
                R.layout.employer_popupwindow_progressbar, null);

        pbPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        pbPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        pbPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        pbPopupWindow.setContentView(pb_view);
        pbPopupWindow.setOutsideTouchable(true);
        pbPopupWindow.setFocusable(true);
        pbPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
        pbPopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    private void showProgressBarPop() {
        if (pbPopupWindow == null) {
            // 设置进度条弹出框
            settingProgressBar();
        }

        // 弹出框出来的方式
        pbPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);

    }

    // 从服务器获取待审核当前详情数据的方法
    private void getDataFromServiceOrderAudit(String ticketId) {
        // mLlGlobal.setVisibility(View.INVISIBLE);
        // mProgress.setVisibility(View.VISIBLE);

        mGetTicketSettlePreviewRequest = new GetTicketSettlePreviewRequest(
                getActivity(), ticketId);
        mGetTicketSettlePreviewRequest
                .setRequestId(GET_TICKET_SETTLEPREVIEW_REQUEST);
        httpPost(mGetTicketSettlePreviewRequest);
    }

    // 切换到过程界面
    private void enterModifyDocument() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager
                .beginTransaction();

        // 当前结算按钮不可点击
        activity.mUpload.setEnabled(false);
        // 过程按钮被选中
        activity.mModify.setChecked(true);
        // 切换到当前运单界面
        if (activity.modifyDocumentFragment == null) {
            activity.modifyDocumentFragment = new ModifyOrderAuditFragment();
        }
        beginTransaction.replace(R.id.fl_uploadmodify_audit_container_employer,
                activity.modifyDocumentFragment).commit();
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        boolean isSuccess;
        String message;

        switch (requestId) {
            case GET_TICKET_SETTLEPREVIEW_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    // 获取结算预览数据接口成功
                    OrderAuditInfo orderAuditInfo = (OrderAuditInfo) response.singleData;
                    String ticketWeightInit = orderAuditInfo.TicketWeightInit;// 矿发吨数
                    String ticketWeightReach = orderAuditInfo.TicketWeightReach;// 签收吨数
                    String ticketWeightShortfall = orderAuditInfo.TicketWeightShortfall;// 途损吨数
                    String freightPrice = orderAuditInfo.FreightPrice;// 运费单价
                    ticketExpectedAmount = orderAuditInfo.TicketExpectedAmount;// 应结金额
                    String ticketSettleAmount = orderAuditInfo.TicketSettleAmount;//
                    pSettleType = orderAuditInfo.SettleType;// 0：线上 1：线下
                    int isSettleType = orderAuditInfo.IsSettleType;
                    String memo = orderAuditInfo.Memo;

                    // 新增
                    String StartAddressName = orderAuditInfo.StartAddressName; // 起始地址名称
                    String StartCityName = orderAuditInfo.StartCityName; // 起始市级名称
                    String StartSubName = orderAuditInfo.StartSubName; // 起始区县名称
                    String EndAddressName = orderAuditInfo.EndAddressName; // 目的地址名称
                    String EndCityName = orderAuditInfo.EndCityName; // 目的市级名称
                    String EndSubName = orderAuditInfo.EndSubName; // 目的区县名称
                    String InfoDerpartTenant = orderAuditInfo.InfoDerpartTenant;// 信息部公司名称
                    String VehicleNo = orderAuditInfo.VehicleNo;// 车牌号
                    String PackageStartTime = orderAuditInfo.PackageStartTime;// 起始时间
                    String PackageEndTime = orderAuditInfo.PackageEndTime;// 截止时间
                    String TicketLoadTime = orderAuditInfo.TicketLoadTime;// 矿发时间
                    String TicketSignTime = orderAuditInfo.TicketSignTime;// 签收时间
                    String TicketCode = orderAuditInfo.TicketCode;// 单号
                    String mPackageLoseRate = orderAuditInfo.packageLoseRate;
                    String mInfoDerpartTenantAliasesname = orderAuditInfo.InfoDerpartTenantAliasesname;
                    String mPackageBeginAddress = orderAuditInfo.PackageBeginAddress;
                    String mPackageEndAddress = orderAuditInfo.PackageEndAddress;
                    String mCategoryName = orderAuditInfo.CategoryName;
                    String mPolicyNo = orderAuditInfo.PolicyNo;
                    String mGoodsPrice = orderAuditInfo.GoodsPrice;
                    mTicketAmount = orderAuditInfo.TicketAmount;
                    mExcessWeight = orderAuditInfo.ExcessWeight;
                    mExcessAmount = orderAuditInfo.ExcessAmount;
                    mResonWeight = orderAuditInfo.ResonWeight;
                    int mShortFallType = orderAuditInfo.ShortFallType;

                    Log.e("TAG", "-------------StartAddressName---------------"
                            + StartAddressName);
                    Log.e("TAG", "-------------StartCityName---------------"
                            + StartCityName);
                    Log.e("TAG", "-------------StartSubName---------------"
                            + StartSubName);
                    Log.e("TAG", "-------------EndAddressName---------------"
                            + EndAddressName);
                    Log.e("TAG", "-------------EndCityName---------------"
                            + EndCityName);
                    Log.e("TAG", "-------------EndSubName---------------"
                            + EndSubName);
                    Log.e("TAG", "-------------InfoDerpartTenant---------------"
                            + InfoDerpartTenant);

                    Log.e("TAG", "-------------VehicleNo---------------"
                            + VehicleNo);
                    Log.e("TAG", "-------------PackageStartTime---------------"
                            + PackageStartTime);
                    Log.e("TAG", "-------------PackageEndTime---------------"
                            + PackageEndTime);
                    Log.e("TAG", "-------------TicketLoadTime---------------"
                            + TicketLoadTime);
                    Log.e("TAG", "-------------TicketSignTime---------------"
                            + TicketSignTime);
                    Log.e("TAG", "-------------TicketCode---------------"
                            + TicketCode);
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
                        tvCheckBegin.setText(StartAddressName);
                        // tvCheckBeginCity.setText(" (" + StartCityName
                        // +
                        // getActivity().getResources().getString(R.string.tv_drop)
                        // + StartSubName + ")");
                    } else {
                        tvCheckBegin.setText("");
                    }

                    // 具体起点
                    if (StringUtils.isStrNotNull(mPackageBeginAddress)) {
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
                        tvCheckEnd.setText(EndAddressName);
                        // tvCheckEndCity.setText(" (" + EndCityName +
                        // getActivity().getResources().getString(R.string.tv_drop)
                        // + EndSubName + ")");
                    } else {
                        tvCheckEnd.setText("");
                    }

                    // 具体终点
                    if (StringUtils.isStrNotNull(mPackageEndAddress)) {
                        tvEndDetail.setText(mPackageEndAddress
                                + " ("
                                + EndCityName
                                + getActivity().getResources().getString(
                                R.string.tv_drop) + EndSubName + ")");
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
                    } else {
                        tvXinXiBu.setText(InfoDerpartTenant);
                    }
                    // 发包时间
                    if (StringUtils.isStrNotNull(PackageStartTime)
                            && StringUtils.isStrNotNull(PackageEndTime)) {
                        tvSendPackageTime.setText(StringUtils
                                .formatSimpleDate(PackageStartTime)
                                + " 至 "
                                + StringUtils.formatSimpleDate(PackageEndTime));
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

                    // 途损比率
                    if (StringUtils.isStrNotNull(mPackageLoseRate)) {
                        if (mShortFallType == 10){
                            double mTuSun = Double.valueOf(mPackageLoseRate);
                            tvTusunlv.setText(StringUtils.sanToQianFenHao(mTuSun));
                        } else if (mShortFallType == 20){
                            tvTusunlv.setText(mPackageLoseRate + "吨/车");
                        }
                    } else {
                        tvTusunlv.setText("0‰");
                    }

                    // 设置矿发吨数
                    if (!TextUtils.isEmpty(ticketWeightInit)
                            && ticketWeightInit != null
                            && !ticketWeightInit.equals("0.00")) {
                        tvCheckKuangFa.setText(ticketWeightInit + "吨");
                    } else {
                        tvCheckKuangFa.setText("0.00吨");
                    }

                    // 设置签收吨数
                    if (!TextUtils.isEmpty(ticketWeightReach)
                            && ticketWeightReach != null
                            && !ticketWeightReach.equals("0.00")) {
                        tvCheckQianShou.setText(ticketWeightReach + "吨");
                    } else {
                        tvCheckQianShou.setText("0.00吨");
                    }

                    // 设置途损吨数
                    if (!TextUtils.isEmpty(ticketWeightShortfall)
                            && ticketWeightShortfall != null
                            && !ticketWeightShortfall.equals("0.00")) {
                        tvCheckTuSun.setText(StringUtils
                                .saveTwoNumber(ticketWeightShortfall) + "吨");
                    }

                    // 设置运费单价
                    if (!TextUtils.isEmpty(freightPrice) && freightPrice != null
                            && !freightPrice.equals("0.00")) {
                        tvCheckUnitPrice.setText(freightPrice + "元/吨");
                    }

                    if (pSettleType == 0) {
                        tvCheckChoose.setText("余额支付");
                    } else if (pSettleType == 1) {
                        tvCheckChoose.setText("现金支付");
                    }

                    // 设置应结运费
                    if (StringUtils.isStrNotNull(ticketExpectedAmount)) {
                        tvCheckYing.setText(ticketExpectedAmount);
                        // if (Float.parseFloat(ticketExpectedAmount) < 0) {
                        // isYunFeiError = true;
                        // btCheckBilling.setText("结算异常,联系客服");
                        // getKeFuNumber();
                        // }
                    }

                    // 备注
                    if (StringUtils.isStrNotNull(memo)) {
                        etCheckBeiZhu.setText(memo);
                    }

                    // 设置实结金额
                    if (StringUtils.isStrNotNull(ticketSettleAmount)) {
                        etCheckShiJie.setText(ticketSettleAmount);
                    }

                    if (isSettleType == 0) {
                        setEnabled(true);
                    } else if (isSettleType == 1) {
                        setEnabled(false);
                    } else if (isSettleType == 2) {
                        setEnabled(true);
                    }

                } else {
                    // 获取结算预览数据接口失败
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                }
                // 显示界面
                // mLlGlobal.setVisibility(View.VISIBLE);
                // mProgress.setVisibility(View.INVISIBLE);
                if (!isYunFeiError) {
                    pbCheckBar.setVisibility(View.GONE);
                    hideView(false);
                }
                if (ticketStatus == 1 || ticketStatus == 4){
                    btCheckBilling.setVisibility(View.GONE);
                } else {
                    btCheckBilling.setVisibility(View.VISIBLE);
                }
                break;

            case APPROVE_ANDSETTLE_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    // 结算成功
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                    Log.e("TAG", "------jinlaile+++++++++++++----------");

                    // 关闭支付密码弹出框
                    inputPayPassPopupWindow.dismiss();

                    // TODO--缓存执行状态
                    CacheUtils.putString(getActivity(), TICKETSTATUS, "8");

                    // 跳转到过程界面
                    // enterModifyDocument();
                    // CurrentTicketActivity.invokeNew(getActivity(), 0, "", 1);
                    // 关闭支付弹出框
                    inputPayPassPopupWindow.dismiss();

                    // 显示等待确认弹出框
                    showQueRenPop();

                    setEnabled(false);

                } else {
                    // 结算失败
                    passList.clear();
                    etPassword.setText("");
                    if (!TextUtils.isEmpty(message) && message != null) {
                        showToast(message);
                    }
                }
                gv_pass_keyboard.setFocusable(true);
                gv_pass_keyboard.setOnItemClickListener(this);
                // 隐藏进度条
                // passwordProgressBar.setVisibility(View.GONE);
//                pbPopupWindow.dismiss();
                hideProgressDialog();
                break;
            case IS_EXITS_PWD_PAY_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;
                isFirstIn = false;
                if (isSuccess) {
                    // isExitsPayPasssword = true;
                    // 从服务器获取待审核当前详情数据
                    if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
                        getDataFromServiceOrderAudit(ticketId);
                    }
                } else {
                    // isExitsPayPasssword = false;
                    showToast("您还没有设置支付密码");
                    SetOrResetSafePasswordActivity.invoke(getActivity(), false);
                }
                break;
            case PING_TAI_KE_FU_REQUEST:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    PingTaiKeFu pingTaiKeFu = (PingTaiKeFu) response.singleData;
                    if (StringUtils.isStrNotNull(pingTaiKeFu.PlatServicePhoneNum)) {
                        serviceNumber = pingTaiKeFu.PlatServicePhoneNum;
                    }
                } else {
                    showToast(message);
                }
                pbCheckBar.setVisibility(View.GONE);
                hideView(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast("连接超时,请检查网络");
//        if (pbPopupWindow != null) {
//            pbPopupWindow.dismiss();
//        }
        hideProgressDialog();
        // 结算失败
        passList.clear();
        etPassword.setText("");
        gv_pass_keyboard.setFocusable(true);
        gv_pass_keyboard.setOnItemClickListener(this);
    }

    private void settingQueRenBar() {
        surePopupWindow = new PopupWindow(activity);

        View qr_view = getActivity().getLayoutInflater().inflate(
                R.layout.employer_popupwindow_dengdai_queren, null);

        surePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        surePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        surePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        surePopupWindow.setContentView(qr_view);
        surePopupWindow.setOutsideTouchable(true);
        surePopupWindow.setFocusable(true);
        surePopupWindow.setTouchable(true); // 设置PopupWindow可触摸
        surePopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        llDengDai = (LinearLayout) qr_view.findViewById(R.id.ll_dengdai);
    }

    /**
     * 确认申请结算
     */
    private void showQueRenPop() {
        if (surePopupWindow == null) {
            // 设置进度条弹出框
            settingQueRenBar();
        }

        // 弹出框出来的方式
        surePopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);

        llDengDai.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                surePopupWindow.dismiss();
                // CurrentTicketActivity.invokeNew(getActivity(), 0, "", 1);
                getActivity().finish();
                CurrentTicketActivity.isBack = true;
                ChengYunTongJiActivity.isBack = true;
            }
        });
    }

    /**
     * @param enabled
     * @throws @Create: 2016-8-19 上午9:37:28
     * @Description:按钮置灰
     * @Title:setEnabled
     * @return:void
     * @Author : chengtao
     */
    private void setEnabled(boolean enabled) {
        if (enabled) {

            btCheckBilling.setText("结算");
            btCheckBilling
                    .setBackgroundResource(R.drawable.sendbao_btn_background);
            btCheckBilling.setEnabled(enabled);
            etCheckShiJie.setEnabled(enabled);
            etCheckBeiZhu.setEnabled(enabled);
        } else {
            btCheckBilling.setText("结算确认");
            btCheckBilling.setBackgroundResource(R.drawable.btn_zhihui);
            btCheckBilling.setEnabled(enabled);
            etCheckShiJie.setEnabled(enabled);
            etCheckBeiZhu.setEnabled(enabled);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstIn) {
            initData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String dPass = list.get(position).toString();
        // 判断 如果点击的X号 同时passList集合中的数据不为空
        if (dPass.equals("×") && passList.size() > 0) {
            // 给密码框最后一个赋值为" "(空)
			/*
			 * passTV = (TextView) payPass_view.findViewById(passID[passList
			 * .size() - 1]); passTV.setText(""); // 删除passList最后面的值
			 * passList.remove(passList.size() - 1);
			 */
            passList.remove(passList.size() - 1);
            String passwordStr = "";
            for (String string : passList) {
                passwordStr += string;
            }
            etPassword.setText(passwordStr);
        } else if (dPass.equals("×") && passList.size() == 0) {
            etPassword.setText("");
        } else if (dPass.equals("完成")) {
            // isPass();
        } else if (!dPass.equals("完成") && !dPass.equals("×")) {
            // 如果点击的是数字 添加到集合中 并给密码框赋值*
            if (passList.size() < 6) {
                passList.add(dPass);
                String passwordStr = "";
                for (String string : passList) {
                    passwordStr += string;
                }
                etPassword.setText(passwordStr);
                if (passList.size() == 6) {
                    gv_pass_keyboard.setOnItemClickListener(null);
                    gv_pass_keyboard.setFocusable(false);
                    isPass();
                }
            }
        }

        Log.i("Log", passList.size() + "个数据");
    }

//    /**
//     * 显示应结运费弹窗详细信息
//     */
//    private void showTicketYingjieDetail() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view_ticket = inflater.inflate(R.layout.employer_dialog_ticket_yingjie_detail, null);
//        TextView tvYingjieDetailYunfei = (TextView) view_ticket.findViewById(R.id.tv_dialog_yingjie_detail_yunfei);
//        TextView tvYingjieDetailChaoDun = (TextView) view_ticket.findViewById(R.id.tv_dialog_yingjie_detail_chao_dun);
//        TextView tvYingjieDetailChaoYun = (TextView) view_ticket.findViewById(R.id.tv_dialog_yingjie_detail_chao_yun);
//        TextView tvYingjieDetailHeDun = (TextView) view_ticket.findViewById(R.id.tv_dialog_yingjie_detail_he_dun);
//        Button btSure = (Button) view_ticket.findViewById(R.id.bt_dialog_yingjie_detail_cancel);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        final AlertDialog dialog = builder.create();
//        dialog.setView(view_ticket);
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
