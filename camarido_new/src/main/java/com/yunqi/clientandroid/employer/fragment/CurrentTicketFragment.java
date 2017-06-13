package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.baidu.pano.platform.http.s;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.OrderDetailActivity;
//import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity.IRequestOrder;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.adapter.CurrentOrderAdapter;
import com.yunqi.clientandroid.employer.adapter.CurrentTicketAdapter;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.employer.request.TicketAllCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketCheckCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketHistoryCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketZhiXingCurrentRequest;
import com.yunqi.clientandroid.employer.util.CurrentTicketListItemOnClick;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;

/**
 * @Description:class 当前订单列表展示数据
 * @ClassName: CurrentTicketFragment
 * @author: mengwei
 * @date: 2016-6-12 下午2:35:22
 * 
 */
public class CurrentTicketFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	/* 界面控件对象 */
	private PullToRefreshListView lvCurrentListView;
	private ProgressBar pbCurrentProgress;
	private ImageView ivCurrentBlank;
	private LinearLayout llCurrentTicket;

	private Handler handler = new Handler();
	private SpManager mSpManager;
	private String mKeyWord = "";
	private int mStatusType;
	private String packageId;

	private AlertDialog alertDialog;

	private ArrayList<TicketCurrentBean> performListItem = new ArrayList<TicketCurrentBean>();
	private CurrentTicketAdapter mCurrentTicketAdapter;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回

	/* 请求类 */
	private TicketAllCurrentRequest mTicketAllCurrentRequest;
	private TicketZhiXingCurrentRequest mTicketZhiXingCurrentRequest;
	private TicketCheckCurrentRequest mTicketCheckCurrentRequest;
	private TicketHistoryCurrentRequest mTicketHistoryCurrentRequest;

	/* 请求ID */
	private final int GET_ALL_TICKET = 1;
	private final int GET_ZHIXING_TICKET = 2;
	private final int GET_CHECK_TICKET = 3;
	private final int GET_HISTORY_TICKET = 4;

	@Override
	protected void initData() {
		mCurrentTicketAdapter = new CurrentTicketAdapter(performListItem,
				getActivity());
		lvCurrentListView.setAdapter(mCurrentTicketAdapter);
		// 请求列表
		getTicketCurrentRequest();
	}

	// @Override
	// public void onResume() {
	// super.onResume();
	// mCurrentTicketAdapter = new CurrentTicketAdapter(performListItem,
	// getActivity());
	// lvCurrentListView.setAdapter(mCurrentTicketAdapter);
	// // 请求列表
	// getTicketCurrentRequest();
	//
	// }

	/**
	 * 请求列表
	 */
	private void getTicketCurrentRequest() {
		llCurrentTicket.setVisibility(View.INVISIBLE);
		pbCurrentProgress.setVisibility(View.VISIBLE);

		performListItem.clear();

		mCurrentTicketAdapter.notifyDataSetChanged();

		// 清空存放当前订单列表的集合
		performListItem.clear();
		// 起始页
		pageIndex = 1;
		// 请求服务器数据
		getDateFromService(pageIndex);
	}

	// 初始化刷新view的方法
	// private void initPullToRefreshView() {
	// lvCurrentListView.setMode(PullToRefreshBase.Mode.BOTH);
	// lvCurrentListView.getLoadingLayoutProxy(false, true).setPullLabel(
	// getString(R.string.pull_to_loadmore));
	// lvCurrentListView.getLoadingLayoutProxy(false, true)
	// .setRefreshingLabel(getString(R.string.pull_to_loading));
	// lvCurrentListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
	// getString(R.string.pull_to_release));
	// currentListView = lvCurrentListView.getRefreshableView();
	// currentListView.setDivider(new ColorDrawable(getResources().getColor(
	// R.color.carlistBackground)));
	// currentListView.setDividerHeight(20);// 设置条目间距
	// currentListView.setSelector(android.R.color.transparent);//
	// 隐藏listview默认的selector
	// lvCurrentListView.setOnRefreshListener(this);
	//
	// lvCurrentListView
	// .setOnItemClickListener(new AdapterView.OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// // TODO--将ticketId传过去跳转到列表详情
	// String ticketId = String.valueOf(performListItem
	// .get(position - 1).id);
	// if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
	// OrderDetailActivity.invoke(getActivity(), ticketId);
	// }
	// }
	// });
	//
	// mCurrentTicketAdapter = new CurrentTicketAdapter(getActivity(),
	// performListItem);
	// currentListView.setAdapter(mCurrentTicketAdapter);
	//
	// }

	/**
	 * 请求服务器数据
	 */
	private void getDateFromService(int pageIndex) {
		count = pageIndex * pageSize;

		mStatusType = mSpManager.getStatusType();

		packageId = mSpManager.getPackageId();

		switch (mStatusType) {
		case -1:
			mTicketAllCurrentRequest = new TicketAllCurrentRequest(
					getActivity(), pageSize, pageIndex, mKeyWord, packageId);
			mTicketAllCurrentRequest.setRequestId(GET_ALL_TICKET);
			httpPost(mTicketAllCurrentRequest);
			break;
		case 2:
			mTicketZhiXingCurrentRequest = new TicketZhiXingCurrentRequest(
					getActivity(), pageSize, pageIndex, mKeyWord, packageId);
			mTicketZhiXingCurrentRequest.setRequestId(GET_ZHIXING_TICKET);
			httpPost(mTicketZhiXingCurrentRequest);
			break;
		case 3:
			mTicketCheckCurrentRequest = new TicketCheckCurrentRequest(
					getActivity(), pageSize, pageIndex, mKeyWord, packageId);
			mTicketCheckCurrentRequest.setRequestId(GET_CHECK_TICKET);
			httpPost(mTicketCheckCurrentRequest);
			break;
		case 4:
			mTicketHistoryCurrentRequest = new TicketHistoryCurrentRequest(
					getActivity(), pageSize, pageIndex, mKeyWord, packageId);
			mTicketHistoryCurrentRequest.setRequestId(GET_HISTORY_TICKET);
			httpPost(mTicketHistoryCurrentRequest);
			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_ALL_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;
			if (isSuccess) {
				isEnd = false;
				ArrayList<TicketCurrentBean> allList = response.data;

				if (allList.size() == 0) {
					showToast("暂时没有订单相关信息");
				}

				if (allList != null) {
					performListItem.addAll(allList);
				}

				if (totalCount <= performListItem.size()) {
					isEnd = true;
				}

				Log.e("TAG",
						"-------allList的数据-------------" + allList.toString());

				mCurrentTicketAdapter.notifyDataSetChanged();

				// onPullDownToRefresh(lvCurrentListView);
			}

			break;
		case GET_ZHIXING_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				isEnd = false;
				ArrayList<TicketCurrentBean> zhixingList = response.data;

				if (zhixingList.size() == 0) {
					showToast("暂时没有订单相关信息");
				}

				if (zhixingList != null) {
					performListItem.addAll(zhixingList);
				}

				if (totalCount <= performListItem.size()) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

				// onPullDownToRefresh(lvCurrentListView);
			}

			break;
		case GET_CHECK_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				isEnd = false;
				ArrayList<TicketCurrentBean> checkList = response.data;

				if (checkList.size() == 0) {
					showToast("暂时没有订单相关信息");
				}

				if (checkList != null) {
					performListItem.addAll(checkList);
				}

				if (totalCount <= performListItem.size()) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

				// onPullDownToRefresh(lvCurrentListView);
			}

			break;
		case GET_HISTORY_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				isEnd = false;
				ArrayList<TicketCurrentBean> historyList = response.data;

				if (historyList.size() == 0) {
					showToast("暂时没有订单相关信息");
				}

				if (historyList != null) {
					performListItem.addAll(historyList);
				}

				if (totalCount <= performListItem.size()) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

				// onPullDownToRefresh(lvCurrentListView);
			}

			break;

		default:
			break;
		}

		pbCurrentProgress.setVisibility(View.GONE);
		lvCurrentListView.onRefreshComplete();
		lvCurrentListView.getmHeaderLoadingView().setVisibility(View.GONE);
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
	protected int getLayoutId() {
		return R.layout.employer_fragment_currentlist_ticket_employer;
	}

	@Override
	protected void initView(View _rootView) {
		mSpManager = SpManager.instance(getActivity());

		lvCurrentListView = obtainView(R.id.currentlist_refreshlistview_employer);
		pbCurrentProgress = obtainView(R.id.pb_currentlist_progress_employer);
		ivCurrentBlank = obtainView(R.id.iv_currentlist_blank);
		llCurrentTicket = obtainView(R.id.ll_currentlist_ticket_employer);
	}

	@Override
	protected void setListener() {
		lvCurrentListView.setOnItemClickListener(this);
	}

	// @Override
	// public void filterRequest(int ticketStatus, String keyWord) {
	// this.mKeyWord = keyWord;
	// mSpManager.setStatusType(ticketStatus);
	// getDateFromService(pageIndex);
	// }

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		performListItem.clear();
		mCurrentTicketAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getDateFromService(pageIndex);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getDateFromService(pageIndex);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCurrentListView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
