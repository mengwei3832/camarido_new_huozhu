package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.PackageListDetailActivity;
import com.yunqi.clientandroid.adapter.OrderDetailListAdapter;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.http.request.GetPackageListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单界面的详情界面
 * @date 15/11/27
 */
public class OrderDetailListFragment extends BaseFragment implements
		AdapterView.OnItemClickListener, OrderFragment.IChangeDetailList,
		PullToRefreshBase.OnRefreshListener2<ListView> {
	private PullToRefreshListView lvOrderDetail;
	private OrderDetailListAdapter mOrderDetailListAdapter;
	private FrameLayout flBlank;
	private List<FocusonRoute> mOrderList = new ArrayList<FocusonRoute>();

	private final int PAGE_COUNT = 10;
	private int mPageIndex = 1;
	private boolean isloadingFinish = false;
	private PreManager mPreManager;
	private FilterManager mFilterManager;

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(getActivity());
		mFilterManager = FilterManager.instance(getActivity());

		// 清空集合
		mOrderList.clear();
		httpPostJson(new GetPackageListRequest(getActivity(), mPageIndex,
				PAGE_COUNT, mPreManager.getOrderSortType(),
				Integer.parseInt(StringUtils.getOrderCity(mFilterManager
						.getStartProvince())[1]), Integer.parseInt(StringUtils
						.getOrderCity(mFilterManager.getStartCity())[1]),
				Integer.parseInt(StringUtils.getOrderCity(mFilterManager
						.getEndProvince())[1]), Integer.parseInt(StringUtils
						.getOrderCity(mFilterManager.getEndCity())[1]),
				mFilterManager.getOrderType(), mFilterManager.getCatoryType(),
				mFilterManager.getStartPrice(), mFilterManager.getEndPrice()));
		mOrderDetailListAdapter = new OrderDetailListAdapter(getActivity(),
				mOrderList);
		lvOrderDetail.setAdapter(mOrderDetailListAdapter);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order_detail_list;
	}

	@Override
	protected void initView(View _rootView) {
		lvOrderDetail = (PullToRefreshListView) _rootView
				.findViewById(R.id.lv_order_detail);
		lvOrderDetail.setMode(PullToRefreshBase.Mode.BOTH);
		flBlank = (FrameLayout) _rootView
				.findViewById(R.id.fl_order_detail_blank);
	}

	@Override
	protected void setListener() {
		lvOrderDetail.setOnItemClickListener(this);
		lvOrderDetail.setOnRefreshListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		FocusonRoute focusonRoute = mOrderDetailListAdapter
				.getItem(position - 1);
		String ticketId = focusonRoute.id;
		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 跳转到包的详情
			PackageListDetailActivity.invoke(getActivity(), ticketId);
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
		isloadingFinish = false;
		if (isSuccess) {
			// 获取列表成功
			int totalCount = response.totalCount;
			ArrayList<FocusonRoute> orderListData = response.data;
			if (orderListData.size() == 0) {
				showToast("暂无相关包信息");
				flBlank.setVisibility(View.VISIBLE);
			} else {
				flBlank.setVisibility(View.GONE);
			}

			if (orderListData != null) {
				mOrderList.addAll(orderListData);
			}

			if (totalCount <= mOrderList.size()) {
				isloadingFinish = true;
			}
			mOrderDetailListAdapter.notifyDataSetChanged();
			lvOrderDetail.onRefreshComplete();
			lvOrderDetail.getmHeaderLoadingView().setVisibility(View.GONE);
		} else {
			// 获取列表失败
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	@Override
	public void changeSort(int sortType) {
		lvOrderDetail.setState(State.REFRESHING, true);
		lvOrderDetail.getmHeaderLoadingView().setVisibility(View.VISIBLE);
		switch (sortType) {
		case OrderFragment.SORT_ATTENTION: // 关注排序
			onPullDownToRefresh(lvOrderDetail);
			break;
		case OrderFragment.SORT_SAME_CITY: // 同城排序
			onPullDownToRefresh(lvOrderDetail);
			break;
		case OrderFragment.SORT_PRICE: // 价格排序
			onPullDownToRefresh(lvOrderDetail);
			break;
		}
	}

	@Override
	public void requestFilter(int catoryCheckId, int orderType,
			int packageBeginProvinceId, int packageBeginCityId,
			int packageEndProvinceId, int packageEndCityId, long beginPrice,
			long endPrice) {
		// 清空集合
		mOrderList.clear();
		mOrderDetailListAdapter.notifyDataSetChanged();
		httpPostJson(new GetPackageListRequest(getActivity(), mPageIndex,
				PAGE_COUNT, mPreManager.getOrderSortType(),
				packageBeginProvinceId, packageBeginCityId,
				packageEndProvinceId, packageEndCityId, orderType,
				catoryCheckId, beginPrice, endPrice));
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

		// 清空集合
		mOrderList.clear();
		mOrderDetailListAdapter.notifyDataSetChanged();
		mPageIndex = 1;
		httpPostJson(new GetPackageListRequest(getActivity(), mPageIndex,
				PAGE_COUNT, mPreManager.getOrderSortType(),
				Integer.parseInt(StringUtils.getOrderCity(mFilterManager
						.getStartProvince())[1]), Integer.parseInt(StringUtils
						.getOrderCity(mFilterManager.getStartCity())[1]),
				Integer.parseInt(StringUtils.getOrderCity(mFilterManager
						.getEndProvince())[1]), Integer.parseInt(StringUtils
						.getOrderCity(mFilterManager.getEndCity())[1]),
				mFilterManager.getOrderType(), mFilterManager.getCatoryType(),
				mFilterManager.getStartPrice(), mFilterManager.getEndPrice()));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetPackageListRequest(getActivity(), mPageIndex,
					PAGE_COUNT, mPreManager.getOrderSortType(),
					Integer.parseInt(StringUtils.getOrderCity(mFilterManager
							.getStartProvince())[1]),
					Integer.parseInt(StringUtils.getOrderCity(mFilterManager
							.getStartCity())[1]), Integer.parseInt(StringUtils
							.getOrderCity(mFilterManager.getEndProvince())[1]),
					Integer.parseInt(StringUtils.getOrderCity(mFilterManager
							.getEndCity())[1]), mFilterManager.getOrderType(),
					mFilterManager.getCatoryType(),
					mFilterManager.getStartPrice(),
					mFilterManager.getEndPrice()));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvOrderDetail.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}
}
