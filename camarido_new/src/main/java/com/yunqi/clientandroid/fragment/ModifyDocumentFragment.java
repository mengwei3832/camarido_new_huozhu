package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.UploadAndModifyDocumentsActivity;
import com.yunqi.clientandroid.adapter.ModifyAdapter;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.http.request.GetTicketExecutedHisRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单执行过程列表
 * @date 15/12/17
 */
public class ModifyDocumentFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String packageBeginName;// 起始地名称
	private String packageEndName;// 目的地名称
	private String createTime;// 订单创建时间
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private ArrayList<ModifyListItem> modifyListItem = new ArrayList<ModifyListItem>();
	private PullToRefreshListView mModifyRefreshlistview;
	private ListView modifyListView;
	private ModifyAdapter modifyAdapter;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private TextView tvProvenance;
	private TextView tvDestination;
	private LinearLayout llAddress;

	// 本页请求
	private GetTicketExecutedHisRequest mGetTicketExecutedHisRequest;

	// 本页请求ID
	private final int GET_EXECUTING_EXECUTEDHIS_REQUEST = 1;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_modify;
	}

	@Override
	protected void initView(View modifyView) {
		// 获取activity中的字段
		ticketId = ((UploadAndModifyDocumentsActivity) getActivity())
				.getTicketId();
		ticketStatus = ((UploadAndModifyDocumentsActivity) getActivity())
				.getTicketStatus();

		packageBeginName = ((UploadAndModifyDocumentsActivity) getActivity())
				.getPackageBeginName();

		packageEndName = ((UploadAndModifyDocumentsActivity) getActivity())
				.getPackageEndName();

		createTime = ((UploadAndModifyDocumentsActivity) getActivity())
				.getCreateTime();

		mModifyRefreshlistview = (PullToRefreshListView) modifyView
				.findViewById(R.id.modifyDocument_refreshlistview);

		llAddress = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyDocument_address);
		tvProvenance = (TextView) modifyView
				.findViewById(R.id.tv_modifyDocument_provenance);
		tvDestination = (TextView) modifyView
				.findViewById(R.id.tv_modifyDocument_destination);

		mLlGlobal = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyDocument_global);
		mProgress = (ProgressBar) modifyView
				.findViewById(R.id.pb_modifyDocument_progress);

		// 设置起点和终点
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null
				&& !TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			tvProvenance.setText(packageBeginName);
			tvDestination.setText(packageEndName);
		} else {
			llAddress.setVisibility(View.GONE);
		}

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放数据的集合
		modifyListItem.clear();

		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 访问服务器获取过程列表数据
			getDataFromService(ticketId);

		}
		// 初始化刷新view
		initPullToRefreshView();
	}

	// 访问服务器获取过程列表数据的方法
	private void getDataFromService(String ticketId) {

		mGetTicketExecutedHisRequest = new GetTicketExecutedHisRequest(
				getActivity(), ticketId);
		mGetTicketExecutedHisRequest
				.setRequestId(GET_EXECUTING_EXECUTEDHIS_REQUEST);
		httpPost(mGetTicketExecutedHisRequest);

	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		mModifyRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		modifyListView = mModifyRefreshlistview.getRefreshableView();
		modifyListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		// modifyListView.setDividerHeight(20);
		modifyListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		mModifyRefreshlistview.setOnRefreshListener(this);

		mModifyRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO--条目点击方法
					}
				});

		modifyAdapter = new ModifyAdapter(getActivity(), modifyListItem,
				packageBeginName, packageEndName, createTime);
		modifyListView.setAdapter(modifyAdapter);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (modifyListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mModifyRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (modifyListItem != null) {
			modifyAdapter.notifyDataSetChanged();
		}

		// 清空存放过程数据的集合
		modifyListItem.clear();

		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 访问服务器获取过程列表数据
			getDataFromService(ticketId);

		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mModifyRefreshlistview.onRefreshComplete();
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
		case GET_EXECUTING_EXECUTEDHIS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取过程列表数据成功
				isEnd = true;// 服务器没有数据要返回
				ArrayList<ModifyListItem> modifyData = response.data;
				// 判断数据是否为空
				if (modifyData != null) {
					modifyListItem.addAll(modifyData);
				}

				if (modifyListItem.size() == 0) {
					showToast("暂无执行过程列表的信息");
				}

				modifyAdapter.notifyDataSetChanged();
				mModifyRefreshlistview.onRefreshComplete();// 结束刷新的方法

			} else {
				// 获取过程列表数据失败
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
		showToast("连接超时,请检查网络");
	}

}
