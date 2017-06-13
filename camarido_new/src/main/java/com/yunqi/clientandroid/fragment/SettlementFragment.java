package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.MyTicketDetailActivity;
import com.yunqi.clientandroid.adapter.SettlementAdapter;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.GetCompleteTicketListRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 待结算订单列表
 * @date 15/11/28
 */
public class SettlementFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private ArrayList<PerformListItem> performListItem = new ArrayList<PerformListItem>();
	private PullToRefreshListView mSettlementRefreshlistview;
	private ListView settlementListView;
	private SettlementAdapter settlementAdapter;// 待结算列表条目的适配器
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private FrameLayout flBlank;

	// 本页请求
	private GetCompleteTicketListRequest getCompleteTicketListRequest;

	// 本页请求ID
	private final int GET_COMPLETE_TICKETLIST_REQUEST = 1;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放待结算订单列表的集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取待结算列表数据
		getDataFromService(pageIndex);
		// 初始化刷新view
		initPullToRefreshView();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_settlementlist;
	}

	@Override
	protected void initView(View settlementView) {

		mSettlementRefreshlistview = (PullToRefreshListView) settlementView
				.findViewById(R.id.settlementlist_refreshlistview);
		mLlGlobal = (LinearLayout) settlementView
				.findViewById(R.id.ll_settlementlist_global);
		mProgress = (ProgressBar) settlementView
				.findViewById(R.id.pb_settlementlist_progress);
		flBlank = (FrameLayout) settlementView
				.findViewById(R.id.fl_settlementlist_blank);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		mSettlementRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mSettlementRefreshlistview.getLoadingLayoutProxy(false, true)
				.setPullLabel(getString(R.string.pull_to_loadmore));
		mSettlementRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mSettlementRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		settlementListView = mSettlementRefreshlistview.getRefreshableView();
		settlementListView.setDivider(new ColorDrawable(getResources()
				.getColor(R.color.carlistBackground)));
		settlementListView.setDividerHeight(20);
		settlementListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		mSettlementRefreshlistview.setOnRefreshListener(this);

		mSettlementRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO--将ticketId传过去跳转到列表详情
						String ticketId = performListItem.get(position - 1).id;
						if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
							MyTicketDetailActivity.invoke(getActivity(),
									ticketId);
						}
					}
				});

		settlementAdapter = new SettlementAdapter(getActivity(),
				performListItem);
		settlementListView.setAdapter(settlementAdapter);

	}

	// 访问服务器获取数据的方法
	private void getDataFromService(int pageIndex) {
		count = pageIndex * pageSize;

		getCompleteTicketListRequest = new GetCompleteTicketListRequest(
				getActivity(), pageSize, pageIndex);
		getCompleteTicketListRequest
				.setRequestId(GET_COMPLETE_TICKETLIST_REQUEST);
		httpPost(getCompleteTicketListRequest);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (performListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mSettlementRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (performListItem != null) {
			settlementAdapter.notifyDataSetChanged();
		}
		// 清空存放待结算订单列表的集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取待结算运单的数据列表
		getDataFromService(pageIndex);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getDataFromService(pageIndex);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mSettlementRefreshlistview.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_COMPLETE_TICKETLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				isEnd = false;// 服务器还有数据要返回
				// 刷新列表成功
				ArrayList<PerformListItem> settlementData = response.data;
				totalCount = response.totalCount;
				// 判断获取到的数据是否为null
				if (settlementData != null) {
					performListItem.addAll(settlementData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无已完成订单的信息");
					flBlank.setVisibility(View.VISIBLE);
				} else {
					flBlank.setVisibility(View.GONE);
				}

				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据呀返回
				}

				settlementAdapter.notifyDataSetChanged();// 刷新界面
				mSettlementRefreshlistview.onRefreshComplete();// 结束刷新的方法

			} else {
				// 刷新列表失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

			}
			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isEnd = false;// 服务器还有数据要返回
		showToast("连接超时,请检查网络");
	}

}
