package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.employer.activity.BaoLieBiaoDetailActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.HistoryDingDanActicity;
import com.yunqi.clientandroid.employer.activity.NewSendPackageActivity;
import com.yunqi.clientandroid.employer.activity.QuoteActivity;
import com.yunqi.clientandroid.employer.adapter.GetBaoBiaoAdapter;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.request.DefaultPriceRequest;
import com.yunqi.clientandroid.employer.request.GetBiaoLieBiaoRequest;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.employer.util.interfaces.YuShePriceSure;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.UserUtil;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * @Description:class 订单管理页面
 * @ClassName: EmploterBaoBiaoFragment
 * @author: zhm
 * @date: 2016-5-16 下午3:59:04
 * 
 */
public class EmploterBaoBiaoFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		AdapterView.OnItemClickListener, View.OnClickListener {
	/* 界面的listview的控件对象 */
	private PullToRefreshListView lvEmployeBaoDetail;
	private ProgressWheel pbEmployerBaoProgress;
	private ImageView ivEmployerBaoBlank;
	private LinearLayout llEmployerEmpty;
	private Button btEmployerSendBao;
	private LinearLayout llShowHide;

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	private ArrayList<GetBiaoLieBiao> mBaoList = new ArrayList<GetBiaoLieBiao>();// 存储包列表数据集合
	private GetBaoBiaoAdapter mGetBaoBiaoAdapter;

	/* 请求类 */
	private GetBiaoLieBiaoRequest mGetBiaoLieBiaoRequest;
	private DefaultPriceRequest mDefaultPriceRequest;

	/* 请求ID */
	private final int GET_BIAO_BAO_LIEBIAO = 1;
	private final int GET_DEFAULT_PRICE = 2;

	//
	public static boolean isBack = false;

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	@Override
	protected void initData() {
		llShowHide.setVisibility(View.GONE);
		// 进度条加载
		pbEmployerBaoProgress.setVisibility(View.VISIBLE);
		// 请求数据
		getBaoLieBiaoContent();
	}

	/**
	 * @Description:得到包列表的详情数据
	 * @Title:getBaoLieBiaoContent
	 * @return:void
	 * @throws
	 * @Create: 2016-5-23 下午1:35:12
	 * @Author : zhm
	 */
	private void getBaoLieBiaoContent() {
		// 清空集合的数据
		// mBaoList.clear();

		count = pageIndex * pageSize;

		mGetBiaoLieBiaoRequest = new GetBiaoLieBiaoRequest(getActivity(),
				pageSize, pageIndex);
		mGetBiaoLieBiaoRequest.setRequestId(GET_BIAO_BAO_LIEBIAO);
		httpPost(mGetBiaoLieBiaoRequest);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_BIAO_BAO_LIEBIAO: // 包列表详情数据
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;
			if (isSuccess) {
				ArrayList<GetBiaoLieBiao> mBiaoList = response.data;

				if (mBiaoList != null) {
					mBaoList.addAll(mBiaoList);
				}
				if (mBaoList.size() == 0) {
					showToast("暂时没有相关包的信息");
					llEmployerEmpty.setVisibility(View.VISIBLE);
					// setBaiBan(true);
				} else {
					llEmployerEmpty.setVisibility(View.GONE);
					// setBaiBan(false);
				}

				if (totalCount <= count) {
					isEnd = true;
				}
				
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					if (!message.startsWith("could")) {
						showToast(message);
					}
				}
				setBaiBan(true);
				L.e("---EmploterBaoBiaoFragment----onSuccess----"+response.ErrCode);
				if (response.ErrCode == 10001) {
					// TODO 退出登录
					// 删除token过期时间
					PreManager.instance(getActivity()).removeTokenExpires();
					// 清空userId
					UserUtil.unSetUserId(getActivity());
					// 跳转到登录界面
					LoginActicity.invoke(getActivity());
					// 用户退出统计
					MobclickAgent.onProfileSignOff();
					// finish主界面
					(getActivity()).finish();
					CamaridoApp.destoryActivity("EmployerMainActivity");
				}
			}
			pbEmployerBaoProgress.setVisibility(View.GONE);
			llShowHide.setVisibility(View.VISIBLE);
			lvEmployeBaoDetail.setVisibility(View.VISIBLE);
			mGetBaoBiaoAdapter.notifyDataSetChanged();
			lvEmployeBaoDetail.onRefreshComplete();
			lvEmployeBaoDetail.getmHeaderLoadingView().setVisibility(
					View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		L.e("---EmploterBaoBiaoFragment----onFailure----"+httpCode);
		if (httpCode == 401){
			// 删除token过期时间
			PreManager.instance(getActivity()).removeTokenExpires();
			// 清空userId
			UserUtil.unSetUserId(getActivity());
			// 跳转到登录界面
			LoginActicity.invoke(getActivity());
			// 用户退出统计
			MobclickAgent.onProfileSignOff();
			// finish主界面
			(getActivity()).finish();
			CamaridoApp.destoryActivity("EmployerMainActivity");
		}
		switch (requestId) {
		case GET_BIAO_BAO_LIEBIAO: // 包列表详情数据
			showToast("网络超时，请检查网络");
			lvEmployeBaoDetail.onRefreshComplete();
			llShowHide.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		setBaiBan(true);
		pbEmployerBaoProgress.setVisibility(View.GONE);
		lvEmployeBaoDetail.onRefreshComplete();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.employer_fragment_baobiao;
	}

	@Override
	protected void initView(View _rootView) {
		initActionBar();

		mUmeng = UmengStatisticsUtils.instance(getActivity());

		lvEmployeBaoDetail = obtainView(R.id.lv_employer_bao_detail);
		pbEmployerBaoProgress = obtainView(R.id.pb_employer_bao);
		// ivEmployerBaoBlank = obtainView(R.id.iv_employer_bao_detail_blank);
		llEmployerEmpty = obtainView(R.id.ll_employer_bao_detail_empty);
		btEmployerSendBao = obtainView(R.id.bt_employer_bao_detail_sendbao);
		llShowHide = obtainView(R.id.ll_employer_bao);

		lvEmployeBaoDetail.setMode(PullToRefreshBase.Mode.BOTH);
		lvEmployeBaoDetail.setOnRefreshListener(this);
		// 给listview适配数据
		mGetBaoBiaoAdapter = new GetBaoBiaoAdapter(getActivity(), mBaoList, new YuShePriceSure() {
			@Override
			public void onNextRequest(String packageId, String mPackageBeginName, String beginCity, String beginCounty, String mPackageEndName, String endCity, String endCounty, String dateTime, int mInsuranceType, int checkId, String mBeforeExcute, String mOnTheWayCount, String mOrderBeforeSettleCount, String mOrderSettledCount, String mPrice, String mPackageWeight) {

				// 进入报价单页面
				QuoteActivity.invoke(getActivity(), packageId, mPackageBeginName,
						beginCity, beginCounty, mPackageEndName, endCity,
						endCounty, dateTime, mInsuranceType, 0,
						mBeforeExcute, mOnTheWayCount,
						mOrderBeforeSettleCount, mOrderSettledCount,
						mPrice, mPackageWeight);
			}
		});
		lvEmployeBaoDetail.setAdapter(mGetBaoBiaoAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// // 请求包列表的数据
		// getBaoLieBiaoContent();

		L.e("---------总条数------------"+count);

		if (isBack) {
			Log.d("TAG", "-----------isBack-----------");
			// 进度条加载
			pbEmployerBaoProgress.setVisibility(View.VISIBLE);
			lvEmployeBaoDetail.setVisibility(View.GONE);
			mBaoList.clear();
			getBaoLieBiaoContent();
		}
		isBack = false;
		Log.d("TAG", "-----------onResume-----------");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		initActionBar();
		// getBaoLieBiaoContent();
	}

	// 初始化TitleBar
	private void initActionBar() {
		EmployerMainActivity eActivity = (EmployerMainActivity) getActivity();
		eActivity.getActionBar().show();
		eActivity
				.setActionBarTitle(getString(R.string.employer_bao_fabaoguanli));
		eActivity.setActionBarLeft(R.drawable.package_history);
		eActivity.setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HistoryDingDanActicity.invoke(getActivity());
				//友盟统计发包
				mUmeng.setCalculateEvents("order_click_history");
			}
		});
		eActivity.setActionBarRight(true, 0, "发包");
		eActivity.setOnActionBarRightClickListener(false,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进入发包界面
						NewSendPackageActivity.invoke(getActivity(), "");
						//友盟统计发包
						mUmeng.setCalculateEvents("ship_order");
					}
				});
	}

	@Override
	protected void setListener() {
		lvEmployeBaoDetail.setOnItemClickListener(this);
		btEmployerSendBao.setOnClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空集合
		mBaoList.clear();
		llShowHide.setVisibility(View.GONE);
		pbEmployerBaoProgress.setVisibility(View.VISIBLE);
		mGetBaoBiaoAdapter.notifyDataSetChanged();
		pageIndex = 1;
		isEnd = false;
		setBaiBan(false);
		getBaoLieBiaoContent();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		L.e("---------总条数------------"+count);
		setBaiBan(false);
		Log.e("TAG", "--------isEnd--------" + isEnd + "");
		if (!isEnd) {
			pageIndex++;

			getBaoLieBiaoContent();
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvEmployeBaoDetail.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.v("TAG", "-------------onItemClick----------------");
		GetBiaoLieBiao mGetBiaoLieBiao = (GetBiaoLieBiao) mGetBaoBiaoAdapter
				.getItem(position - 1);
		// 获取baoID
		String packageID = String.valueOf(mGetBiaoLieBiao.Id);
		// 获取bao状态
		int packageStatus = mGetBiaoLieBiao.PackageStatus;

		Log.e("TAG", "------packageID------" + packageID);

		if (!TextUtils.isEmpty(packageID) && packageID != null) {
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_details_item");

			// TODO 跳转到包详情页面
			BaoLieBiaoDetailActivity.invoke(getActivity(), packageID, 0);
		}
	}

	@Override
	public void onClick(View v) {
		//友盟统计发包
		mUmeng.setCalculateEvents("ship_order");
		// 跳转发包页面
		NewSendPackageActivity.invoke(getActivity(), "");
	}
}
