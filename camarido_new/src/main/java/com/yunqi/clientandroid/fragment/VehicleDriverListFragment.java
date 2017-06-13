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
import com.yunqi.clientandroid.activity.DriverListDetailActivity;
import com.yunqi.clientandroid.activity.VehicleListDetailActivity;
import com.yunqi.clientandroid.adapter.DriverListAdapter;
import com.yunqi.clientandroid.entity.DriverListInfo;
import com.yunqi.clientandroid.http.request.GetDriverListRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @deprecated:司机列表
 */
public class VehicleDriverListFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private String vehicleId;// 车辆ID
	private String vehicleNo;// 车牌号码
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int count;// 实际返回的数据数量
	private int totalCount;// 返回数据的总数量
	private boolean isEnd;// 是否服务器无数据返回
	private TextView mVehicleNo;
	private PullToRefreshListView driverRefreshlistview;
	private ListView driverListView;
	private ArrayList<DriverListInfo> driverItemList = new ArrayList<DriverListInfo>();
	private DriverListAdapter driverListAdapter;
	private Handler handler = new Handler();
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;

	@Override
	protected void initData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		// 清空存放司机列表的集合
		driverItemList.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取数据
		if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
			getDataFromServiceDriverList(pageIndex, vehicleId);
		}
		// 初始化刷新view
		initPullToRefreshView();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_cardriver;
	}

	@Override
	protected void initView(View cardriver) {
		vehicleId = ((VehicleListDetailActivity) getActivity()).getId();
		vehicleNo = ((VehicleListDetailActivity) getActivity()).getVehicleNo();
		mVehicleNo = (TextView) cardriver
				.findViewById(R.id.tv_cardriver_vehicleNo);
		driverRefreshlistview = (PullToRefreshListView) cardriver
				.findViewById(R.id.driverlist_refreshlistview);
		mLlGlobal = (LinearLayout) cardriver
				.findViewById(R.id.ll_cardriver_global);
		mProgress = (ProgressBar) cardriver
				.findViewById(R.id.pb_cardriver_progress);

		// 设置车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			mVehicleNo.setText(vehicleNo);
		}

	}

	@Override
	protected void setListener() {

	}

	// 初始化刷新view
	private void initPullToRefreshView() {

		driverRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		driverRefreshlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		driverRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		driverRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		driverListView = driverRefreshlistview.getRefreshableView();
		driverListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		driverListView.setDividerHeight(25);
		driverListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		driverRefreshlistview.setOnRefreshListener(this);

		// item的点击事件
		driverRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String vehicleDriverId = driverItemList
								.get(position - 1).id;
						String headPortraitUrl = driverItemList
								.get(position - 1).headPortraitUrl;
						// 跳转到司机详情界面
						if (!TextUtils.isEmpty(vehicleDriverId)
								&& vehicleDriverId != null) {
							DriverListDetailActivity.invoke(getActivity(),
									vehicleDriverId, headPortraitUrl);
						}
					}
				});

		driverListAdapter = new DriverListAdapter(getActivity(), driverItemList);
		driverListView.setAdapter(driverListAdapter);

	}

	// 访问服务器获取数据的方法
	private void getDataFromServiceDriverList(int pageIndex, String vehicleId) {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		count = pageIndex * pageSize;
		httpPostJson(new GetDriverListRequest(getActivity(), pageSize,
				pageIndex, vehicleId));

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (driverItemList == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					driverRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (driverItemList != null) {
			driverListAdapter.notifyDataSetChanged();
		}

		// 清空存放司机列表的集合
		driverItemList.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取司机列表数据
		if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
			getDataFromServiceDriverList(pageIndex, vehicleId);
		}

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
				getDataFromServiceDriverList(pageIndex, vehicleId);
			}
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					driverRefreshlistview.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		isEnd = false;// 服务器还有数据要返回
		if (isSuccess) {
			// 获取司机列表数据成功
			ArrayList<DriverListInfo> driverListData = response.data;
			totalCount = response.totalCount;
			if (driverListData != null) {
				driverItemList.addAll(driverListData);
			}

			if (driverItemList.size() == 0) {
				showToast("暂无司机信息");
			}

			if (totalCount <= count) {
				isEnd = true;// 服务器没有数据要返回
			}

			driverListAdapter.notifyDataSetChanged();// 刷新界面
			driverRefreshlistview.onRefreshComplete();// 结束刷新的方法
		} else {
			// 获取司机列表数据失败
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}

		mLlGlobal.setVisibility(View.VISIBLE);
		mProgress.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isEnd = false;// 服务器还有数据要返回
		showToast("连接超时,请检查网络");
	}
}
