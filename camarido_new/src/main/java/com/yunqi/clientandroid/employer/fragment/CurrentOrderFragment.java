package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentOrderActivity;
import com.yunqi.clientandroid.employer.activity.OrderDetailActivity;
import com.yunqi.clientandroid.employer.adapter.CurrentOrderAdapter;
import com.yunqi.clientandroid.employer.request.GetCurrentOrderListRequest;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方当前订单fragment
 * @date 16/1/18
 */
public class CurrentOrderFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		CurrentOrderActivity.IRequestOrder {

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private ArrayList<PerformListItem> performListItem = new ArrayList<PerformListItem>();
	private PullToRefreshListView mCurrentRefreshlistview;
	private ListView currentListView;
	private CurrentOrderAdapter currentAdapter;// 当前订单列表条目的适配器
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private SpManager mSpManager;
	private String mKeyWord = "";
	private ImageView ivBlank;

	// 本页请求
	private GetCurrentOrderListRequest getExecutingTicketListRequest;

	// 本页请求ID
	private final int GET_EXECUTING_TICKETLIST_REQUEST = 1;

	@Override
	protected void initData() {
		mSpManager = SpManager.instance(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		// 请求列表
		requestCurrentOrder();
	}

	/**
	 * 请求列表的方法
	 */
	private void requestCurrentOrder() {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放当前订单列表的集合
		performListItem.clear();
		// 起始页
		pageIndex = 1;
		// 访问服务器获取数据
		getDataFromService(pageIndex);
		// 初始化刷新view
		initPullToRefreshView();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_currentlist_employer;
	}

	@Override
	protected void initView(View currentView) {

		mCurrentRefreshlistview = (PullToRefreshListView) currentView
				.findViewById(R.id.currentlist_refreshlistview_employer);
		mLlGlobal = (LinearLayout) currentView
				.findViewById(R.id.ll_currentlist_global_employer);
		mProgress = (ProgressBar) currentView
				.findViewById(R.id.pb_currentlist_progress_employer);
		ivBlank = (ImageView) currentView
				.findViewById(R.id.iv_currentlist_blank);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		mCurrentRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mCurrentRefreshlistview.getLoadingLayoutProxy(false, true)
				.setPullLabel(getString(R.string.pull_to_loadmore));
		mCurrentRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mCurrentRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		currentListView = mCurrentRefreshlistview.getRefreshableView();
		currentListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		currentListView.setDividerHeight(20);// 设置条目间距
		currentListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		mCurrentRefreshlistview.setOnRefreshListener(this);

		mCurrentRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO--将ticketId传过去跳转到列表详情
						String ticketId = performListItem.get(position - 1).id;
						if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
							OrderDetailActivity.invoke(getActivity(), ticketId);
						}
					}
				});

		currentAdapter = new CurrentOrderAdapter(getActivity(), performListItem);
		currentListView.setAdapter(currentAdapter);

	}

	// 访问服务器获取当前运单数据的方法
	private void getDataFromService(int pageIndex) {
		count = pageIndex * pageSize;

		getExecutingTicketListRequest = new GetCurrentOrderListRequest(
				getActivity(), pageSize, pageIndex, mSpManager.getStatusType(),
				mKeyWord);
		getExecutingTicketListRequest
				.setRequestId(GET_EXECUTING_TICKETLIST_REQUEST);
		httpPost(getExecutingTicketListRequest);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (performListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mCurrentRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (performListItem != null) {
			currentAdapter.notifyDataSetChanged();
		}
		// 清空存放当前订单列表的集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
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
					mCurrentRefreshlistview.onRefreshComplete();
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
		case GET_EXECUTING_TICKETLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				isEnd = false;// 服务器还有数据要返回
				// 刷新列表成功
				ArrayList<PerformListItem> performData = response.data;
				totalCount = response.totalCount;
				// 判断获取到的数据是否为null
				if (performData != null) {
					performListItem.addAll(performData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无该订单类型的信息");
					ivBlank.setVisibility(View.VISIBLE);
				} else {
					ivBlank.setVisibility(View.GONE);
				}

				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据要返回
				}

				currentAdapter.notifyDataSetChanged();// 刷新界面
				mCurrentRefreshlistview.onRefreshComplete();// 结束刷新的方法

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
		showToast("连接超时,请检查网络");
	}

	@Override
	public void filterRequest(int ticketStatus, String keyWord) {
		this.mKeyWord = keyWord;
		mSpManager.setStatusType(ticketStatus);
		// 请求列表
		requestCurrentOrder();
	}
}
