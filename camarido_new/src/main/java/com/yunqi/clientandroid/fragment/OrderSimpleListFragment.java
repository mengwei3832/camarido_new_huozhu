package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.NewPackageListActivity;
import com.yunqi.clientandroid.adapter.OrderSimpleListAdapter;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.http.request.GetAllRoutesRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单界面的简要fragment
 * @date 15/11/27
 */
public class OrderSimpleListFragment extends BaseFragment implements
		AdapterView.OnItemClickListener,
		PullToRefreshBase.OnRefreshListener2<ListView> {
	private PullToRefreshListView lvOrderSimple;
	private OrderSimpleListAdapter mOrderSimpleListAdapter;
	private List<PackagePath> mPackagePathList;

	private final int PAGE_COUNT = 10;
	private int mPageIndex = 1;
	private boolean isloadingFinish = false;

	@Override
	protected void initData() {
		httpPostJson(new GetAllRoutesRequest(getActivity(), mPageIndex,
				PAGE_COUNT));
		mPackagePathList = new ArrayList<PackagePath>();
		mOrderSimpleListAdapter = new OrderSimpleListAdapter(getActivity(),
				mPackagePathList);
		lvOrderSimple.setAdapter(mOrderSimpleListAdapter);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order_simple_list;
	}

	@Override
	protected void initView(View _rootView) {
		lvOrderSimple = (PullToRefreshListView) _rootView
				.findViewById(R.id.lv_order_simple);
		lvOrderSimple.setMode(PullToRefreshBase.Mode.BOTH);
	}

	@Override
	protected void setListener() {
		lvOrderSimple.setOnItemClickListener(this);
		lvOrderSimple.setOnRefreshListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		NewPackageListActivity.invoke(getActivity(),
				mPackagePathList.get(position - 1));
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
		isloadingFinish = false;
		if (isSuccess) {
			int totalCount = response.totalCount;
			ArrayList<PackagePath> packagePathData = response.data;
			if (packagePathData != null) {
				mPackagePathList.addAll(packagePathData);
			}

			if (totalCount <= mPackagePathList.size()) {
				isloadingFinish = true;
			}
			mOrderSimpleListAdapter.notifyDataSetChanged();
			lvOrderSimple.onRefreshComplete();
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPackagePathList.clear();
		mOrderSimpleListAdapter.notifyDataSetChanged();
		mPageIndex = 1;
		httpPostJson(new GetAllRoutesRequest(getActivity(), mPageIndex,
				PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetAllRoutesRequest(getActivity(), mPageIndex,
					PAGE_COUNT));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvOrderSimple.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}
}
