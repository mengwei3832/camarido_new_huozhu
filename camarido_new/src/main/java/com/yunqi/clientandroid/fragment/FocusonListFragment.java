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
import com.yunqi.clientandroid.activity.PackageListDetailActivity;
import com.yunqi.clientandroid.adapter.FocusonRouteAdapter;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.http.request.GetPackageSubscriptsRequestTwo;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @deprecated:关注路线列表
 */
public class FocusonListFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private PullToRefreshListView focuson_refreshlistview;
	private ListView focusonListView;
	private ArrayList<FocusonRoute> focusonItemList = new ArrayList<FocusonRoute>();
	private FocusonRouteAdapter focusonRouteAdapter;

	// 初始化刷新view的方法
	private void initPullToRefreshView() {

		focuson_refreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		focuson_refreshlistview.getLoadingLayoutProxy(false, true)
				.setPullLabel(getString(R.string.pull_to_loadmore));
		focuson_refreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		focuson_refreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		focusonListView = focuson_refreshlistview.getRefreshableView();
		focusonListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		focusonListView.setDividerHeight(20);
		focusonListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		focuson_refreshlistview.setOnRefreshListener(this);

		focuson_refreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 跳转到列表详情
						Intent intent = new Intent(getActivity(),
								PackageListDetailActivity.class);
						String packageId = focusonItemList.get(position - 1).id;
						intent.putExtra("packageId", packageId);
						startActivity(intent);
					}
				});

		focusonRouteAdapter = new FocusonRouteAdapter(getActivity(),
				focusonItemList);
		focusonListView.setAdapter(focusonRouteAdapter);

	}

	// 访问服务器获取数据的方法
	private void getDataFromService(int pageIndex) {
		count = pageIndex * pageSize;
		httpPostJson(new GetPackageSubscriptsRequestTwo(getActivity(),
				pageSize, pageIndex));
	}

	@Override
	protected void initData() {

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_focusonlist;
	}

	@Override
	protected void initView(View focusonView) {

		focuson_refreshlistview = (PullToRefreshListView) focusonView
				.findViewById(R.id.focusonlist_refreshlistview);

	}

	@Override
	protected void setListener() {

	}

	@Override
	public void onResume() {
		super.onResume();
		focusonItemList.clear();// 情况集合
		pageIndex = 1;
		// 访问服务器获取数据
		getDataFromService(pageIndex);
		// 初始化刷新view
		initPullToRefreshView();
	}

	// 下拉刷新的方法
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (focusonItemList == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					focuson_refreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (focusonItemList != null) {
			focusonRouteAdapter.notifyDataSetChanged();
		}

		focusonItemList.clear();
		pageIndex = 1;
		getDataFromService(pageIndex);

	}

	// 上拉加载更多的方法
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getDataFromService(pageIndex);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					focuson_refreshlistview.onRefreshComplete();
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
		isEnd = false;
		if (isSuccess) {
			ArrayList<FocusonRoute> focusonData = response.data;
			totalCount = response.totalCount;
			if (focusonData != null) {
				focusonItemList.addAll(focusonData);
			}

			if (focusonItemList.size() == 0) {
				showToast("暂无所有路线信息");
			}

			if (totalCount <= count) {
				isEnd = true;
			}

			focusonRouteAdapter.notifyDataSetChanged();
			focuson_refreshlistview.onRefreshComplete();// 结束刷新的方法

		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isEnd = false;// 服务器还有数据要返回
		showToast("连接超时,请检查网络");
	}

}
