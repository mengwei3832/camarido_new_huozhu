package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.StopPackageAdapter;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.StopPackageEntity;
import com.yunqi.clientandroid.employer.fragment.EmploterBaoBiaoFragment;
import com.yunqi.clientandroid.employer.request.AddYuanYinRequest;
import com.yunqi.clientandroid.employer.request.GetBaoDetailContentRequest;
import com.yunqi.clientandroid.employer.request.StopPackaageListRequest;
import com.yunqi.clientandroid.employer.request.StopPackageRequest;
import com.yunqi.clientandroid.employer.util.QuoteQuXiaoItemOnClick;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 承运设置页面
 */
public class StopPackageActivity extends BaseActivity implements
        PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
    /* 控件对象 */
    private TextView tvBegin, tvBeginCity, tvEnd, tvEndCity;
    private TextView tvDate;
    private TextView tvDaizhixing, tvZaituzhong, tvDaijiesuan, tvYijiesuan;
    private Button btStopPackageAll;
    private PullToRefreshListView lvStopPackageView;
    private ProgressWheel pbStopBar;
    private LinearLayout llShowHide;
    private ImageView ivBlank;
    private Button btJiankong;

    /* 传递的值 */
    private String packageId;

    /* 分页请求参数 */
    private int pageIndex = 1;// 起始页
    private int pageSize = 10;// 每页显示数量
    private int totalCount;// 返回数据的总数量
    private int count;// 实际返回的数据数量
    private boolean isEnd = false;// 是否服务器无数据返回
    private Handler handler = new Handler();

    /* 请求类 */
    private GetBaoDetailContentRequest mGetBaoDetailContentRequest;
    private StopPackageRequest mStopPackageRequest;
    private StopPackaageListRequest mStopPackaageListRequest;
    private AddYuanYinRequest mAddYuanYinRequest;

    /* 请求Id */
    private final int GET_PACKAGE_DETAIL = 1;
    private final int GET_STOP_PACKAGE = 2;
    private final int GET_STOP_LIST = 3;
    private final int ADD_YUANYIN_QUXIAO = 4;

    /* 适配器 */
    private ArrayList<StopPackageEntity> mStopList = new ArrayList<StopPackageEntity>();
    private StopPackageAdapter mStopPackageAdapter;

    private AlertDialog dialog;

    private String mPackageBeginName;
    private String mPackageEndName;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_activity_stop_package;
    }

    @Override
    protected void initView() {
        initActionBar();
        // 获取传递的值
        packageId = getIntent().getStringExtra("packageId");
        // 找控件对象
        initFindView();
    }

    /**
     * 找控件对象
     */
    private void initFindView() {
        tvBegin = obtainView(R.id.tv_stop_package_begin);
        tvBeginCity = obtainView(R.id.tv_stop_package_begin_city);
        tvEnd = obtainView(R.id.tv_stop_package_end);
        tvEndCity = obtainView(R.id.tv_stop_package_end_city);
        tvDate = obtainView(R.id.tv_stop_package_date);
        tvDaizhixing = obtainView(R.id.tv_stop_package_daizhixing);
        tvZaituzhong = obtainView(R.id.tv_stop_package_zaituzhong);
        tvDaijiesuan = obtainView(R.id.tv_stop_package_daijiesuan);
        tvYijiesuan = obtainView(R.id.tv_stop_package_yijiesuan);
        btStopPackageAll = obtainView(R.id.bt_stop_package_all);
        lvStopPackageView = obtainView(R.id.lv_stop_package_view);
        pbStopBar = obtainView(R.id.pb_stop_package);
        llShowHide = obtainView(R.id.ll_stop_package_show);
        ivBlank = obtainView(R.id.iv_stop_package);
        btJiankong = obtainView(R.id.bt_stop_package_jiankong);

        lvStopPackageView.setMode(PullToRefreshBase.Mode.BOTH);
        lvStopPackageView.setOnRefreshListener(this);

        mStopPackageAdapter = new StopPackageAdapter(mContext, mStopList,
                new QuoteQuXiaoItemOnClick() {
                    @Override
                    public void onClick(View item, int position, String id,
                                        String mCarName) {
                        //友盟统计首页
                        mUmeng.setCalculateEvents("carrier_settings_item_stop");

                        showCancalPop(id, mCarName);
                    }
                });
        lvStopPackageView.setAdapter(mStopPackageAdapter);
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        // 设置titileBar的标题
        setActionBarTitle(this.getResources().getString(
                R.string.employer_activity_stop_title));
        // 设置左边的返回箭头
        setActionBarLeft(R.drawable.nav_back);
        setActionBarRight(false, 0, null);

        // 给左边的返回箭头加监听
        setOnActionBarLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StopPackageActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        showOrHide(true);
        getPackageDetail();
    }

    /**
     * 请求包的详细信息
     */
    private void getPackageDetail() {
        mGetBaoDetailContentRequest = new GetBaoDetailContentRequest(mContext,
                packageId);
        mGetBaoDetailContentRequest.setRequestId(GET_PACKAGE_DETAIL);
        httpPost(mGetBaoDetailContentRequest);
    }

    /**
     * 终止整包请求
     */
    private void getStopAllPackage() {
        mStopPackageRequest = new StopPackageRequest(mContext, packageId);
        mStopPackageRequest.setRequestId(GET_STOP_PACKAGE);
        httpGet(mStopPackageRequest);
    }

    /**
     * 获取列表信息
     */
    private void getStopPackagelist() {
        count = pageIndex * pageSize;
        mStopPackaageListRequest = new StopPackaageListRequest(mContext,
                packageId, pageIndex, pageSize);
        mStopPackaageListRequest.setRequestId(GET_STOP_LIST);
        httpPost(mStopPackaageListRequest);
    }

    @Override
    protected void setListener() {
        btStopPackageAll.setOnClickListener(this);
        btJiankong.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_stop_package_all:// 终止整包
                //友盟统计首页
                mUmeng.setCalculateEvents("carrier_settings_package_stop");

                showStopPackageDialog();
                setButtonEnabled(false);
                break;
            case R.id.bt_stop_package_jiankong://外部监控
                //友盟统计首页
                mUmeng.setCalculateEvents("carrier_settings_monitoring");

                //进入外部监控人员页面
                ExternalMonitoringActivity.invoke(mContext,packageId,mPackageBeginName,mPackageEndName);
                break;
            default:
                break;
        }
    }

    /**
     * 按钮置灰
     */
    private void setButtonEnabled(boolean isEnabled) {
        if (isEnabled) {
            btStopPackageAll.setEnabled(true);
            btStopPackageAll.setBackgroundResource(R.drawable.quote_btn_red);
        } else {
            btStopPackageAll.setEnabled(false);
            btStopPackageAll.setBackgroundResource(R.drawable.btn_zhihui);
        }
    }

    /**
     * @Description:显示取消的弹窗
     */
    private void showCancalPop(final String infoId, String mCarName) {
        LayoutInflater inflater = LayoutInflater.from(StopPackageActivity.this);
        View view = inflater
                .inflate(R.layout.employer_popupwindow_quxiao, null);
        TextView tvZhong = (TextView) view.findViewById(R.id.tv_quote_zhongzhi);
        TextView btCancle = (TextView) view
                .findViewById(R.id.btn_zhongzhi_cancle);
        TextView btSure = (TextView) view.findViewById(R.id.btn_zhongzhi_sure);
        AlertDialog.Builder builder = new Builder(StopPackageActivity.this);
        dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        tvZhong.setText("订单终止之后," + mCarName + "将不会为您继续承运。请谨慎操作!");
        dialog.show();

        btCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addYuanYin(infoId, "");
                dialog.dismiss();
                showOrHide(true);
            }
        });
    }

    /**
     * @param infoId
     * @param yuanYin
     * @throws
     * @Description:报价单取消的请求
     * @Title:addYuanYin
     * @return:void
     * @Create: 2016-6-30 下午1:46:08
     * @Author : mengwei
     */
    private void addYuanYin(String infoId, String yuanYin) {
        int infoID = Integer.parseInt(infoId);
        mAddYuanYinRequest = new AddYuanYinRequest(mContext, infoID, yuanYin);
        mAddYuanYinRequest.setRequestId(ADD_YUANYIN_QUXIAO);
        httpPost(mAddYuanYinRequest);
    }

    /**
     * 终止整个包
     */
    private void showStopPackageDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.employer_dialog_tingyun_package,
                null);
        RelativeLayout btnCancle = (RelativeLayout) view
                .findViewById(R.id.rl_package_tingyun_cancle);
        RelativeLayout btnSure = (RelativeLayout) view
                .findViewById(R.id.rl_package_tingyun_sure);
        TextView tvStopT1 = (TextView) view.findViewById(R.id.tv_stop_t1);
        TextView tvStopT2 = (TextView) view.findViewById(R.id.tv_stop_t2);
        AlertDialog.Builder builder = new Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        tvStopT2.setText("如果终止，所有报价单将不能继续议价执行!");
        tvStopT1.setText("是否坚持终止?");
        dialog.show();
        btnCancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setButtonEnabled(true);
            }
        });
        btnSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 停运包
                getStopAllPackage();
                dialog.dismiss();
                showOrHide(true);
            }
        });
    }

    /**
     * 显示或隐藏界面
     *
     * @param isShow
     */
    private void showOrHide(boolean isShow) {
        if (isShow) {
            llShowHide.setVisibility(View.GONE);
            pbStopBar.setVisibility(View.VISIBLE);
        } else {
            llShowHide.setVisibility(View.VISIBLE);
            pbStopBar.setVisibility(View.GONE);
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
            case ADD_YUANYIN_QUXIAO://取消单个报价单
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    showToast(message);
                    mStopList.clear();
                    getStopPackagelist();
                } else {
                    showToast(message);
                }

                break;
            case GET_STOP_LIST:// 包列表信息
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    ArrayList<StopPackageEntity> mSList = response.data;

                    if (mSList != null) {
                        mStopList.addAll(mSList);
                    }
                    if (mSList.size() == 0) {
                        showToast(message);
                        ivBlank.setVisibility(View.VISIBLE);
                    } else {
                        ivBlank.setVisibility(View.GONE);
                    }
                    if (totalCount <= count) {
                        isEnd = true;
                    }

                }
                lvStopPackageView.onRefreshComplete();
                mStopPackageAdapter.notifyDataSetChanged();
                showOrHide(false);
                break;
            case GET_PACKAGE_DETAIL:// 包的详细信息
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    GetBiaoLieBiao mGetBiaoLieBiao = (GetBiaoLieBiao) response.singleData;

                    // 给相关控件赋值
                    setPackageView(mGetBiaoLieBiao);
                }

                // TODO 请求列表数据
                getStopPackagelist();
                break;
            case GET_STOP_PACKAGE:// 终止整包
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    // 返回包列表界面
                    EmploterBaoBiaoFragment.isBack = true;
                    EmployerMainActivity.newInvoke(mContext, "FROM");
                    finish();
                } else {
                    showToast(message);
                    setButtonEnabled(true);
                }
                showOrHide(false);
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
        switch (requestId) {
            case GET_PACKAGE_DETAIL:

                break;
            case GET_STOP_PACKAGE:
                setButtonEnabled(true);
                break;
            default:
                break;
        }
        lvStopPackageView.onRefreshComplete();
        showOrHide(false);
    }

    /**
     * 给包相关控件赋值
     */
    private void setPackageView(GetBiaoLieBiao mGetBiaoLieBiao) {
        // 获取详细信息
        mPackageBeginName = mGetBiaoLieBiao.PackageBeginName;
        mPackageEndName = mGetBiaoLieBiao.PackageEndName;
        String mPackageBeginCityText = mGetBiaoLieBiao.PackageBeginCityText;
        String mPackageBeginCountryText = mGetBiaoLieBiao.PackageBeginCountryText;
        String mPackageEndCityText = mGetBiaoLieBiao.PackageEndCityText;
        String mPackageEndCountryText = mGetBiaoLieBiao.PackageEndCountryText;
        String mPackageStartTime = mGetBiaoLieBiao.PackageStartTime;
        String mPackageEndTime = mGetBiaoLieBiao.PackageEndTime;
        String mBeforeExecute = mGetBiaoLieBiao.BeforeExecute;
        String mOnTheWayCount = mGetBiaoLieBiao.onTheWayCount;
        String mOrderBeforeSettleCount = mGetBiaoLieBiao.OrderBeforeSettleCount;
        String mOrderSettledCount = mGetBiaoLieBiao.OrderSettledCount;
        // 开始地址名称
        if (StringUtils.isStrNotNull(mPackageBeginName)) {
            tvBegin.setText(mPackageBeginName);
        } else {
            tvBegin.setText("");
        }
        // 结束地址名称
        if (StringUtils.isStrNotNull(mPackageEndName)) {
            tvEnd.setText(mPackageEndName);
        } else {
            tvEnd.setText("");
        }
        // 开始二三级地址
        if (StringUtils.isStrNotNull(mPackageBeginCityText)
                && StringUtils.isStrNotNull(mPackageBeginCountryText)) {
            tvBeginCity.setText(mPackageBeginCityText
                    + mContext.getString(R.string.tv_drop)
                    + mPackageBeginCountryText);
        }
        // 结束二三级地址
        if (StringUtils.isStrNotNull(mPackageEndCityText)
                && StringUtils.isStrNotNull(mPackageEndCountryText)) {
            tvEndCity.setText(mPackageEndCityText
                    + mContext.getString(R.string.tv_drop)
                    + mPackageEndCountryText);
        }
        // 承运期限
        if (StringUtils.isStrNotNull(mPackageStartTime)
                && StringUtils.isStrNotNull(mPackageEndTime)) {
            String mBeginTime = StringUtils
                    .formatChengQixian(mPackageStartTime);
            String mEndTime = StringUtils.formatChengQixian(mPackageEndTime);
            tvDate.setText(mBeginTime + "~" + mEndTime);
        } else {
            tvDate.setText("");
        }
        // 待执行
        if (StringUtils.isStrNotNull(mBeforeExecute)) {
            tvDaizhixing.setText(Html.fromHtml("待执行：<font color='#ff4444'>"
                    + mBeforeExecute + "</font>车"));
        } else {
            tvDaizhixing.setText(Html.fromHtml("待执行：<font color='#ff4444'>"
                    + "0" + "</font>车"));
        }
        // 在途中
        if (StringUtils.isStrNotNull(mOnTheWayCount)) {
            tvZaituzhong.setText(Html.fromHtml("在途中：<font color='#ff4444'>"
                    + mOnTheWayCount + "</font>车"));
        } else {
            tvZaituzhong.setText(Html.fromHtml("在途中：<font color='#ff4444'>"
                    + "0" + "</font>车"));
        }
        // 待结算
        if (StringUtils.isStrNotNull(mOrderBeforeSettleCount)) {
            tvDaijiesuan.setText(Html.fromHtml("待结算：<font color='#ff4444'>"
                    + mOrderBeforeSettleCount + "</font>车"));
        } else {
            tvDaijiesuan.setText(Html.fromHtml("待结算：<font color='#ff4444'>"
                    + "0" + "</font>车"));
        }
        // 已结算
        if (StringUtils.isStrNotNull(mOrderSettledCount)) {
            tvYijiesuan.setText(Html.fromHtml("已结算：<font color='#ff4444'>"
                    + mOrderSettledCount + "</font>车"));
        } else {
            tvYijiesuan.setText(Html.fromHtml("已结算：<font color='#ff4444'>"
                    + "0" + "</font>车"));
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        // 清空存放当前订单列表的集合
        mStopList.clear();
        mStopPackageAdapter.notifyDataSetChanged();
        // 起始页置为1
        pageIndex = 1;
        // 请求服务器获取当前运单的数据列表
        getStopPackagelist();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!isEnd) {
            ++pageIndex;
            getStopPackagelist();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lvStopPackageView.onRefreshComplete();
                    showToast("已经是最后一页了");
                }
            }, 100);
        }
    }

    /**
     * 承运设置页面跳转
     */
    public static void invoke(Activity activity, String packageId) {
        Intent intent = new Intent(activity, StopPackageActivity.class);
        intent.putExtra("packageId", packageId);
        activity.startActivity(intent);
    }

}
