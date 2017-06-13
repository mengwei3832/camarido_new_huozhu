package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.ActiveActivity;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.activity.MessageActivity;
import com.yunqi.clientandroid.activity.SearchListActivity;
import com.yunqi.clientandroid.employer.activity.DiscoverZiXunActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.EmployerWebviewActivity;
import com.yunqi.clientandroid.employer.activity.GetMessageActivity;
import com.yunqi.clientandroid.employer.activity.HangYeLieBiaoActivity;
import com.yunqi.clientandroid.employer.activity.SearchLieBiaoActivity;
import com.yunqi.clientandroid.employer.activity.ZiXunLieBiaoActivity;
import com.yunqi.clientandroid.employer.adapter.JiaGeAdapter;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;
import com.yunqi.clientandroid.employer.request.JiageDiscoverRequest;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @Description:class 发包方的发现界面
 * @ClassName: EmployerDiscoverFragment
 * @author: zhm
 * @date: 2016-5-16 上午11:08:07
 */
public class EmployerDiscoverFragment extends BaseFragment implements
        View.OnClickListener, OnItemClickListener {
    private EditText etSearch; // 搜索输入框
    private Button btSearch; // 搜索按钮
    private TextView tvEmployerMsg; // 消息
    private TextView tvEmployerActive; // 活动
    private TextView tvEmployerMsgBubble; // 消息角上的红点
    private TextView tvEmployerActiveBubble;// 活动角上的红点
    private RelativeLayout rlEmployerHuowu; // 煤炭价格
    private RelativeLayout rlEmployerZixun; // 物流价格
    private RelativeLayout rlEmployerxinwen; // 行业资讯
    private ListView lvDiscoverView;
    private RelativeLayout rlDongtaiHelp;//最新动态
    private ImageView ivJiage,ivZixun;

    private JiageDiscoverRequest jiageDiscoverRequest;
    private ArrayList<ZiXunLieBiaoBean> jiageList = new ArrayList<ZiXunLieBiaoBean>();
    private JiaGeAdapter jiaGeAdapter;

    PreManager mPreManager;

    @Override
    protected void initData() {
        mPreManager = PreManager.instance(getActivity());
        // 请求接口
        getDiscoverName();

        L.e("=========mPreManager.getShowRed()==========="+mPreManager.getShowRed());

        if (mPreManager.getShowRed()){
            ivZixun.setVisibility(View.VISIBLE);
        }
    }

    private void getDiscoverName() {
//        jiaGeAdapter.notifyDataSetChanged();
        jiageList.clear();
        httpPost(new JiageDiscoverRequest(getActivity()));

    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess = response.isSuccess;
        String message = response.message;

        if (isSuccess) {
            ArrayList<ZiXunLieBiaoBean> jiaArrayList = response.data;

            if (jiaArrayList != null) {
                jiageList.addAll(jiaArrayList);
            }

//            jiaGeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
    }

    @Override
    protected int getLayoutId() {

        Log.e("TAG", "进行加载发现页面-------------------");
        return R.layout.employer_fragment_discover;
    }

    @Override
    protected void initView(View _rootView) {
        initActionBar();// 初始化titileBar

        etSearch = obtainView(R.id.et_employer_search);
        btSearch = obtainView(R.id.bt_employer_search);
        tvEmployerMsg = obtainView(R.id.tv_employer_discover_msg);
        tvEmployerActive = obtainView(R.id.tv_employer_discover_active);
        tvEmployerMsgBubble = obtainView(R.id.tv_employer_discover_msg_bubble);
        tvEmployerActiveBubble = obtainView(R.id.tv_employer_discover_active_bubble);
        // rlEmployerHuowu = obtainView(R.id.rl_discover_huowu_help);
        // rlEmployerZixun = obtainView(R.id.rl_dixcover_zixun_help);
        lvDiscoverView = obtainView(R.id.lv_employer_discover_view);
        rlEmployerxinwen = obtainView(R.id.rl_dixcover_xinwen_help);
        rlDongtaiHelp = obtainView(R.id.rl_dixcover_dongtai_help);
        ivJiage = obtainView(R.id.iv_employer_discover_jiage_red);
        ivZixun = obtainView(R.id.iv_employer_discover_zixun_red);

//        jiaGeAdapter = new JiaGeAdapter(jiageList, getActivity());
//        lvDiscoverView.setAdapter(jiaGeAdapter);

    }

    @Override
    protected void setListener() {
        btSearch.setOnClickListener(this);
        tvEmployerMsg.setOnClickListener(this);
        tvEmployerActive.setOnClickListener(this);
        rlEmployerxinwen.setOnClickListener(this);
        lvDiscoverView.setOnItemClickListener(this);
        rlDongtaiHelp.setOnClickListener(this);

        etSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String hint;
                if (hasFocus) {
                    hint = etSearch.getHint().toString();
                    etSearch.setTag(hint);
                    etSearch.setHint("");
                    etSearch.setBackgroundResource(R.drawable.chazhaodianji);
                } else {
                    hint = etSearch.getTag().toString();
                    etSearch.setHint(hint);
                    etSearch.setBackgroundResource(R.drawable.chazhao);
                }
            }
        });
    }

    /**
     * 初始化titileBar
     */
    private void initActionBar() {
        EmployerMainActivity activity = (EmployerMainActivity) getActivity();
        activity.getActionBar().show();
        activity.setActionBarTitle(getString(R.string.main_discovery));
        activity.setActionBarLeft(0);
        activity.setActionBarRight(false, 0, "");
        activity.setOnActionBarLeftClickListener(null);
        activity.setOnActionBarRightClickListener(true, null);
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

        // 显示未读数量
        showBubble();

        // 注册广播接收者--接收情况消息提示
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageActivity.messageActivity);
        getActivity().registerReceiver(messgeDetail, filter);
    }

    // 广播接收者--接收情况消息提示
    BroadcastReceiver messgeDetail = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MessageActivity.messageActivity)) {
                tvEmployerMsgBubble.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_employer_search: // 搜索按钮
                SearchLieBiaoActivity.invoke(getActivity(), "搜索信息", etSearch
                        .getText().toString());
                // ZiXunLieBiaoActivity.invoke(getActivity(),
                // ZiXunLieBiaoActivity.FROM_SEARCH, etSearch.getText()
                // .toString(), "搜索信息",0);
                break;
            case R.id.tv_employer_discover_msg: // 进入消息界面
                //友盟统计首页
                mUmeng.setCalculateEvents("find_click_message");

                mPreManager.setMsgBubble(0);
                MessageActivity.invoke(getActivity());
                break;
            case R.id.tv_employer_discover_active: // 进入活动页面
                //友盟统计首页
                mUmeng.setCalculateEvents("find_click_activity");

                mPreManager.setActiveBubble(0);
                ActiveActivity.invoke(getActivity());
//                Intent intent1 = new Intent(getActivity(), EmployerWebviewActivity.class);
//                intent1.putExtra("Url","http://mp.weixin.qq.com/profile?src=3&timestamp=1489632286&ver=1&signature=DsLJwH3ZaAbrM-xS-sLeaVpCvxTmj0Umb8gHWPXu3MLQP9nvce18uo3-txopKCC*SAUXerAHKc7adp5vMAqhPg==");
//                intent1.putExtra("Title","活动");
//                getActivity().startActivity(intent1);
                break;
            case R.id.rl_dixcover_xinwen_help: // 进入行业资讯页面
//                DiscoverZiXunActivity.invoke(getActivity());
//                if (jiageList.size() != 0){
//                    String messageId = String.valueOf(jiageList.get(0).id);
//                    GetMessageActivity.invoke(getActivity(),messageId,1);
//                } else {
//                    showToast("没有最新行业资讯");
//                }
                //友盟统计首页
                mUmeng.setCalculateEvents("find_click_industry_information");

                mPreManager.setShowRed(false);
                ivZixun.setVisibility(View.GONE);
                Intent intent2 = new Intent(getActivity(), EmployerWebviewActivity.class);
                intent2.putExtra("Url","http://mp.weixin.qq.com/mp/homepage?__biz=MjM5MDIyMTk2NA==&hid=1&sn=6b3aad87acb9514103e59b5e8f9e3fa2&uin=MTY0MzM5OTU1&key=133c70a3e904573d3cb935f96c4e65449f83943f34a0be1d3dcbed1e0bf50fb20b46e1472fb0d9fe71e4b531d43db54f38c448fda851d8d5a2eadcf099dff2dd63da840dfbc2271628365d40b756c734&devicetype=android-23&version=26050630&lang=zh_CN&nettype=cmnet&pass_ticket=9n4dpZzHCYD6JlrGH0hAEJy0cpQVknrzGf7RWT1HOYU%3D&wx_header=1&scene=1");
                intent2.putExtra("Title","行业资讯");
                getActivity().startActivity(intent2);
                break;
            case R.id.rl_dixcover_dongtai_help://进入物流价格页面
                //友盟统计首页
                mUmeng.setCalculateEvents("find_click_logistics_price");
//                DiscoverZiXunActivity.invoke(getActivity());
//                if (jiageList.size() != 0){
//                    String messageId = String.valueOf(jiageList.get(0).id);
//                    GetMessageActivity.invoke(getActivity(),messageId,1);
//                } else {
//                    showToast("没有最新物流价格");
//                }
                Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
                intent.putExtra("Url","http://yqtms.com/RoadIndex/RoadMobile");
                intent.putExtra("Title","物流价格");
                getActivity().startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 显示气泡
     */
    private void showBubble() {
        // 消息气泡
        if (mPreManager.getMsgBubble() > 0) {
            tvEmployerMsgBubble.setVisibility(View.VISIBLE);
            tvEmployerMsgBubble.setText(mPreManager.getMsgBubble() + "");
        } else {
            tvEmployerMsgBubble.setVisibility(View.GONE);
        }

        // 活动气泡
        if (mPreManager.getActiveBubble() > 0) {
            tvEmployerActiveBubble.setVisibility(View.VISIBLE);
            tvEmployerActiveBubble.setText(mPreManager.getActiveBubble() + "");
        } else {
            tvEmployerActiveBubble.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ZiXunLieBiaoBean ziXunLieBiaoBean = jiageList.get(position);
        String messageId = String.valueOf(ziXunLieBiaoBean.id);

        GetMessageActivity.invoke(getActivity(), messageId, 1);

    }

}
