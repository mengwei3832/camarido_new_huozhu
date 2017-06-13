package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.PackageListActivity;
import com.yunqi.clientandroid.adapter.AllRoutesAdapter;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.http.request.GetAllRoutesRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 所有路线列表
 */
public class AllRoutesListFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private PullToRefreshListView route_refreshlistview;
	private ListView allRoutesListView;
	private ArrayList<PackagePath> routesItemList = new ArrayList<PackagePath>();
	private AllRoutesAdapter allRoutesAdapter;

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		route_refreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		route_refreshlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		route_refreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		route_refreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		allRoutesListView = route_refreshlistview.getRefreshableView();
		allRoutesListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		allRoutesListView.setDividerHeight(20);
		allRoutesListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		route_refreshlistview.setOnRefreshListener(this);

		route_refreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 跳转到列表详情
						Intent intent = new Intent(getActivity(),
								PackageListActivity.class);
						String packageBeginCity = routesItemList
								.get(position - 1).packageBeginCity;
						String packageBeginCityText = routesItemList
								.get(position - 1).packageBeginCityText;
						String packageEndCity = routesItemList
								.get(position - 1).packageEndCity;
						String packageEndCityText = routesItemList
								.get(position - 1).packageEndCityText;
						String packageCount = routesItemList.get(position - 1).packageCount;
						String orderCount = routesItemList.get(position - 1).orderCount;
						String packageDistance = routesItemList
								.get(position - 1).packageDistance;
						intent.putExtra("packageBeginCity", packageBeginCity);
						intent.putExtra("packageBeginCityText",
								packageBeginCityText);
						intent.putExtra("packageEndCity", packageEndCity);
						intent.putExtra("packageEndCityText",
								packageEndCityText);
						intent.putExtra("packageCount", packageCount);
						intent.putExtra("orderCount", orderCount);
						intent.putExtra("packageDistance", packageDistance);
						startActivity(intent);
					}
				});

		allRoutesAdapter = new AllRoutesAdapter(getActivity(), routesItemList);
		allRoutesListView.setAdapter(allRoutesAdapter);
	}

	// 访问服务器获取数据的方法
	private void getDataFromServer(int pageIndex) {
		count = pageIndex * pageSize;
		httpPostJson(new GetAllRoutesRequest(getActivity(), pageIndex, pageSize));

	}

	@Override
	protected void initData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		// 清除存放所有路线的列表
		routesItemList.clear();
		pageIndex = 1;
		// 访问服务器获取数据
		getDataFromServer(pageIndex);
		// 初始化刷新view
		initPullToRefreshView();

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_allroutes;
	}

	@Override
	protected void initView(View routesView) {

		route_refreshlistview = (PullToRefreshListView) routesView
				.findViewById(R.id.allroutes_refreshlistview);

	}

	@Override
	protected void setListener() {

	}

	// 下拉刷新的方法
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (routesItemList == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					route_refreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (routesItemList != null) {
			allRoutesAdapter.notifyDataSetChanged();
		}
		// 清除存放所有路线的列表
		routesItemList.clear();

		pageIndex = 1;
		getDataFromServer(pageIndex);

	}

	// 上拉加载更多的方法
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getDataFromServer(pageIndex);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					route_refreshlistview.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		isEnd = false;// 服务器还有数据要返回
		if (isSuccess) {
			ArrayList<PackagePath> routesData = response.data;
			totalCount = response.totalCount;
			if (routesData != null) {
				routesItemList.addAll(routesData);
			}

			if (routesItemList.size() == 0) {
				showToast("暂无所有路线信息");
			}

			if (totalCount <= count) {
				isEnd = true;// 服务器没有数据要返回
			}

			allRoutesAdapter.notifyDataSetChanged();// 刷新界面的方法
			route_refreshlistview.onRefreshComplete();// 结束刷新的方法

		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isEnd = false; // 服务器还有数据要返回
		showToast("连接超时,请检查网络");
	}

}
