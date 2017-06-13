package com.yunqi.clientandroid.employer.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.ShippingDetailAdapter;
import com.yunqi.clientandroid.employer.request.BalanceRequest;
import com.yunqi.clientandroid.employer.request.CashRequest;
import com.yunqi.clientandroid.employer.request.FerightRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.ProgressWheel;

import java.util.ArrayList;

/**
 * 运费明细页面
 */
public class ShippingDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    /* 控件对象 */
    private ListView lvShipView;
    private ProgressWheel progressShipDetail;
    private ImageView ivEmpty;

    /* 判断标志，0代表待结运费，1代表余额结算，2代表现金结算 */
    private int mIsType = 0;

    /* 请求ID */
    private final int GET_DAIJIE_FREIGHT = 1;
    private final int GET_BALANCE_SETTLE = 2;
    private final int GET_CASH_SETTLE = 3;

    /* 请求类 */
    private FerightRequest mFerightRequest;
    private BalanceRequest mBalanceRequest;
    private CashRequest mCashRequest;

    /* 适配器和集合 */
    private ArrayList<String> shipList = new ArrayList<>();
    private ShippingDetailAdapter mShippingDetailAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipping_details;
    }

    @Override
    protected void initView() {
        /* 得到传递的标志 */
        mIsType = getIntent().getIntExtra("TYPE",0);
        /* 根据标志赋值相应标题 */
        initActionBar();

        lvShipView = obtainView(R.id.lv_ship_view);
        progressShipDetail = obtainView(R.id.progress_ship_detail);
        ivEmpty = obtainView(R.id.iv_ship_empty);

        /* 适配器 */
        mShippingDetailAdapter = new ShippingDetailAdapter(shipList,mContext);
        lvShipView.setAdapter(mShippingDetailAdapter);
    }

    /**
     * 标题
     */
    private void initActionBar() {
        if (mIsType == 0){
            setActionBarTitle(mContext.getString(R.string.activity_ship_detail_title_feright));
        } else if (mIsType == 1){
            setActionBarTitle(mContext.getString(R.string.activity_ship_detail_title_balance));
        } else if (mIsType == 2){
            setActionBarTitle(mContext.getString(R.string.activity_ship_detail_title_cash));
        }
        setActionBarLeft(R.drawable.fanhui);
        setOnActionBarLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        showProgress(true);
        /* 根据标志判断请求的接口 */
        if (mIsType == 0){
            mFerightRequest = new FerightRequest(mContext);
            mFerightRequest.setRequestId(GET_DAIJIE_FREIGHT);
            httpPost(mFerightRequest);
        } else if(mIsType == 1) {
            mBalanceRequest = new BalanceRequest(mContext);
            mBalanceRequest.setRequestId(GET_BALANCE_SETTLE);
            httpPost(mBalanceRequest);
        } else if(mIsType == 2) {
            mCashRequest = new CashRequest(mContext);
            mCashRequest.setRequestId(GET_CASH_SETTLE);
            httpPost(mCashRequest);
        }
    }

    @Override
    protected void setListener() {

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
        switch (requestId){
            case GET_DAIJIE_FREIGHT:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess){

                }
                break;
            case GET_BALANCE_SETTLE:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess){

                }
                break;
            case GET_CASH_SETTLE:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess){

                }
                break;
        }
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
        ivEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //进入承运统计页面

    }

    /**
     * 判断进度条是否显示
     * @param isshow
     */
    private void showProgress(boolean isshow){
        if (isshow){
            progressShipDetail.setVisibility(View.VISIBLE);
            lvShipView.setVisibility(View.GONE);
        } else {
            progressShipDetail.setVisibility(View.GONE);
            lvShipView.setVisibility(View.VISIBLE);
        }
    }
}
