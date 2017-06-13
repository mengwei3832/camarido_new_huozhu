package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.VehicleListAdapter;
import com.yunqi.clientandroid.entity.VehicleListInfo;
import com.yunqi.clientandroid.http.request.GetVehicleListRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 车辆列表
 * @date 15/11/20
 */
public class VehicleListActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private PullToRefreshListView vehicleRefreshListView;// 下啦刷新view
	private ProgressBar mProgress;
	private ListView vehicleListView;
	private FrameLayout fl_carlist_nolist;
	private ArrayList<VehicleListInfo> vehicleItemList = new ArrayList<VehicleListInfo>();// 车放车辆信息的集合
	private VehicleListAdapter vehicleAdapter;// 车辆列表的适配器
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private boolean isEnd;// 是否服务器无数据返回
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量

	@Override
	protected int getLayoutId() {
		return R.layout.activity_carlist_item;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		vehicleRefreshListView = (PullToRefreshListView) findViewById(R.id.carlist_refreshlistview);
		mProgress = (ProgressBar) findViewById(R.id.pb_carlist_progress);
		fl_carlist_nolist = (FrameLayout) findViewById(R.id.fl_carlist_nolist);
	}

	@Override
	protected void initData() {
		// onResume();
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 清空存放车辆列表数据的集合
		vehicleItemList.clear();
		// 显示加载进度条
		mProgress.setVisibility(View.VISIBLE);
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取车辆列表数据
		getDataFromeServiceVehicleList(pageIndex);

		// 初始化刷新view
		initPullToRefreshView();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.vehicle_list));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VehicleListActivity.this.finish();
			}
		});

		setActionBarRight(true, 0,
				this.getResources().getString(R.string.addVehicle));
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View V) {
				// 得到传过来的真实姓名
				String tName = getIntent().getStringExtra("name");

				if (!TextUtils.isEmpty(tName) && tName != null) {
					// 跳转到添加车辆页面的点击事件
					StartNewVehicleActivity.invoke(VehicleListActivity.this);
				} else {
					showToast("请先进行实名认证！");
				}
			}
		});
	}

	// 跳转到添加车辆页面的点击事件
	public void AddMyCar(View view) {
		StartNewVehicleActivity.invoke(VehicleListActivity.this);

	}

	// 跳转到输入邀请码页面的点击事件
	public void InputNumber(View view) {
		VehicleputCodeActivity.invoke(VehicleListActivity.this);
	}

	// 从服务器获取数据的方法
	private void getDataFromeServiceVehicleList(int pageIndex) {
		count = pageIndex * pageSize;
		httpPost(new GetVehicleListRequest(this, pageSize, pageIndex));
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		isEnd = false;// 服务器还有数据要返回
		if (isSuccess) {
			// 获取车辆列表数据成功
			ArrayList<VehicleListInfo> vehicleData = response.data;
			totalCount = response.totalCount;// 返回数据的总数量
			// 将数据存放进车辆列表的集合
			if (vehicleData != null) {
				vehicleItemList.addAll(vehicleData);
			}

			if (vehicleItemList.size() == 0) {
				showToast("暂无车辆列表信息");
				fl_carlist_nolist.setVisibility(View.VISIBLE);
			} else {
				fl_carlist_nolist.setVisibility(View.GONE);
			}

			// 判断返回数据的总数量 与 实际返回数据的大小
			if (totalCount <= count) {
				isEnd = true;// 服务器没有数据要返回
			}

			vehicleAdapter.notifyDataSetChanged();// 刷新界面
			vehicleRefreshListView.onRefreshComplete();// 结束刷新的方法

		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
		// 隐藏加载进度条
		mProgress.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isEnd = false;// 服务器还有数据要返回
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// 列表刷新的方法
	private void initPullToRefreshView() {

		vehicleRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		vehicleRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		vehicleRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		vehicleRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));

		vehicleListView = vehicleRefreshListView.getRefreshableView();
		vehicleListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.color_ffffff)));

		vehicleListView.setDividerHeight(25);
		vehicleListView.setSelector(android.R.color.transparent);// 隐藏listView默认的selector
		vehicleRefreshListView.setOnRefreshListener(this);

		// item的点击跳转到车辆详情界面
		vehicleRefreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						String vehicleId = vehicleItemList.get(position - 1).vehicleId;// 车辆id
						String vehicleNo = vehicleItemList.get(position - 1).vehicleNo;// 车牌号码
						if (!TextUtils.isEmpty(vehicleId) && vehicleId != null
								&& !TextUtils.isEmpty(vehicleNo)
								&& vehicleNo != null) {
							VehicleListDetailActivity.invoke(
									VehicleListActivity.this, vehicleId,
									vehicleNo);
						} else {
							// showToast("进不去了------------------");
						}

					}
				});
		vehicleAdapter = new VehicleListAdapter(VehicleListActivity.this,
				vehicleItemList);
		vehicleListView.setAdapter(vehicleAdapter);

	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (vehicleItemList == null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					vehicleRefreshListView.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (vehicleItemList != null) {
			vehicleAdapter.notifyDataSetChanged();
		}
		// 清空存放车辆列表的集合
		vehicleItemList.clear();
		// 起始页置为1
		pageIndex = 1;
		// 访问服务器获取车辆列表数据
		getDataFromeServiceVehicleList(pageIndex);

	}

	// 上拉加载
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			// 起始页置为++
			++pageIndex;
			// 访问服务器获取车辆列表数据
			getDataFromeServiceVehicleList(pageIndex);
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					vehicleRefreshListView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 本界面的跳转方法
	 */
	public static void invoke(Activity activity, String tName) {
		Intent intent = new Intent(activity, VehicleListActivity.class);
		intent.putExtra("name", tName);
		activity.startActivity(intent);
	}

}
