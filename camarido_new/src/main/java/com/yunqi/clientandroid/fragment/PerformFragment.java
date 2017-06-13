package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.MyTicketActivity;
import com.yunqi.clientandroid.activity.MyTicketDetailActivity;
import com.yunqi.clientandroid.adapter.PerformAdapter;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.ExecuteTicketRequest;
import com.yunqi.clientandroid.http.request.GetBeforeExecuteTicketListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 待执行订单列表
 * @date 15/11/28
 */
public class PerformFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回
	private PullToRefreshListView mPerformRefreshlistview;
	private ListView performListView;
	private Handler handler = new Handler();
	private ArrayList<PerformListItem> performListItem = new ArrayList<PerformListItem>();
	private PerformAdapter performAdapter;// 待执行列表条目的适配器
	private AlertDialog alertDialog;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private MyTicketActivity activity;
	private FrameLayout flBlank;

	// 本页请求
	private GetBeforeExecuteTicketListRequest getBeforeExecuteTicketListRequest;
	private ExecuteTicketRequest mExecuteTicketRequest;

	// 本页请求ID
	private final int GET_BEFOREEXECUTE_TICKETLIST_REQUEST = 1;
	private final int EXECUTE_TICKET_REQUEST = 2;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放待执行订单列表的集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取待执行数据
		getDataFromService(pageIndex);
		// 初始化刷新view
		initPullToRefreshView();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_performlist;
	}

	@Override
	protected void initView(View performView) {

		// 获取当前的activity
		activity = (MyTicketActivity) getActivity();

		mPerformRefreshlistview = (PullToRefreshListView) performView
				.findViewById(R.id.performlist_refreshlistview);

		mLlGlobal = (LinearLayout) performView
				.findViewById(R.id.ll_performlist_global);
		mProgress = (ProgressBar) performView
				.findViewById(R.id.pb_performlist_progress);
		flBlank = (FrameLayout) performView
				.findViewById(R.id.fl_performlist_blank);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		mPerformRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mPerformRefreshlistview.getLoadingLayoutProxy(false, true)
				.setPullLabel(getString(R.string.pull_to_loadmore));
		mPerformRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mPerformRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		performListView = mPerformRefreshlistview.getRefreshableView();
		performListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		performListView.setDividerHeight(20);// 设置条目间距
		performListView.setSelector(android.R.color.transparent);// 隐藏ListView默认的selector
		mPerformRefreshlistview.setOnRefreshListener(this);

		mPerformRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 将ticketId传过去跳转到列表详情
						String ticketId = performListItem.get(position - 1).id;
						if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
							MyTicketDetailActivity.invoke(getActivity(),
									ticketId);
						}
					}
				});

		performAdapter = new PerformAdapter(getActivity(), performListItem,
				new PerformListItemOnClick() {
					@Override
					public void onClick(View item, int position, String id) {
						// 显示确认执行的对话框
						if (!TextUtils.isEmpty(id) && id != null) {
							showAffirm(id);
						}
					}
				});
		performListView.setAdapter(performAdapter);

	}

	// 确定执行订单的方法
	protected void executeTicket(String id) {
		mExecuteTicketRequest = new ExecuteTicketRequest(getActivity(), id);
		mExecuteTicketRequest.setRequestId(EXECUTE_TICKET_REQUEST);
		httpPost(mExecuteTicketRequest);
	}

	// 访问服务器获取数据的方法
	private void getDataFromService(int pageIndex) {

		count = pageIndex * pageSize;
		getBeforeExecuteTicketListRequest = new GetBeforeExecuteTicketListRequest(
				getActivity(), pageSize, pageIndex);
		getBeforeExecuteTicketListRequest
				.setRequestId(GET_BEFOREEXECUTE_TICKETLIST_REQUEST);
		httpPost(getBeforeExecuteTicketListRequest);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (performListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPerformRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (performListItem != null) {
			performAdapter.notifyDataSetChanged();
		}

		// 清空存放待执行订单列表的集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取待执行数据
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
					mPerformRefreshlistview.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	// 显示确认执行的对话框
	protected void showAffirm(final String id) {
		AlertDialog.Builder builder = new Builder(getActivity());
		// 设置对话框不能被取消
		builder.setCancelable(false);

		View view = View.inflate(getActivity(), R.layout.dialog_affirm, null);
		TextView tvCancle = (TextView) view
				.findViewById(R.id.tv_diaperform_cancle);
		TextView tvConfirm = (TextView) view
				.findViewById(R.id.tv_diaperform_confirm);

		// 取消按钮
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 消除对话框
				alertDialog.dismiss();
			}
		});

		// 确定按钮
		tvConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(id) && id != null) {
					// 确认执行订单
					executeTicket(id);
				}
			}
		});

		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_BEFOREEXECUTE_TICKETLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				isEnd = false;// 服务器还有数据要返回
				// 获取待执行数据列表数据成功
				ArrayList<PerformListItem> performData = response.data;
				totalCount = response.totalCount;
				// 判断获取到的数据是否为null
				if (performData != null) {
					performListItem.addAll(performData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无待执行订单的信息");
					flBlank.setVisibility(View.VISIBLE);
				} else {
					flBlank.setVisibility(View.GONE);
				}

				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据要返回
				}

				performAdapter.notifyDataSetChanged(); // 界面刷新的方法
				mPerformRefreshlistview.onRefreshComplete();// 结束刷新的方法

			} else {
				// 获取待执行数据列表数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;

		case EXECUTE_TICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 开始执行成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				alertDialog.dismiss();
				// 切换到当前运单界面
				FragmentManager fragmentManager = activity
						.getSupportFragmentManager();
				FragmentTransaction beginTransaction = fragmentManager
						.beginTransaction();
				// 当前按钮被选中
				activity.mRbCurrent.setChecked(true);
				// 切换到当前运单界面
				if (activity.mCurrentFragment == null) {
					activity.mCurrentFragment = new CurrentFragment();
				}
				beginTransaction.replace(R.id.fl_myticket_container,
						activity.mCurrentFragment).commit();

			} else {
				// 开始执行失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		switch (requestId) {
		case GET_BEFOREEXECUTE_TICKETLIST_REQUEST:
			isEnd = false;// 服务器还有数据要返回
			showToast("连接超时,请检查网络");
			break;
		}
	}

}
