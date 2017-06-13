package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.OrderSimpleFoucusListAdapter;
import com.yunqi.clientandroid.entity.FocusPath;
import com.yunqi.clientandroid.http.request.CancelFocusPathRequest;
import com.yunqi.clientandroid.http.request.GetFocusPathRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.MiPushUtil;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 标签管理关注fragment
 * @date 15/11/27
 */
public class OrderSimpleFoucusListFragment extends BaseFragment implements
		AdapterView.OnItemClickListener,
		PullToRefreshBase.OnRefreshListener2<ListView>,
		OrderSimpleFoucusListAdapter.IFocusonClick {
	private PullToRefreshListView lvOrderSimple;
	private OrderSimpleFoucusListAdapter mOrderSimpleFoucusListAdapter;
	private List<FocusPath> mPackagePathList;
	private FocusPath mFocusPath;
	private FrameLayout flBlank;

	private final int PAGE_COUNT = 10;
	private int mPageIndex = 1;
	private boolean isloadingFinish = false;

	private GetFocusPathRequest mGetFocusPathRequest;
	private CancelFocusPathRequest mCancelFocusPathRequest;

	private final int GET_FOCUS_PATH_REQUEST = 1;
	private final int CANCEL_FOCUS_PATH_REQUEST = 2;

	@Override
	protected void initData() {
		mGetFocusPathRequest = new GetFocusPathRequest(getActivity(),
				mPageIndex, PAGE_COUNT);
		mGetFocusPathRequest.setRequestId(GET_FOCUS_PATH_REQUEST);
		httpPostJson(mGetFocusPathRequest);

		mPackagePathList = new ArrayList<FocusPath>();
		mOrderSimpleFoucusListAdapter = new OrderSimpleFoucusListAdapter(
				getActivity(), mPackagePathList, this);
		lvOrderSimple.setAdapter(mOrderSimpleFoucusListAdapter);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order_simple_list;
	}

	@Override
	protected void initView(View _rootView) {
		lvOrderSimple = (PullToRefreshListView) _rootView
				.findViewById(R.id.lv_order_simple);
		flBlank = (FrameLayout) _rootView
				.findViewById(R.id.fl_order_simple_blank);
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
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_FOCUS_PATH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			isloadingFinish = false;
			if (isSuccess) {
				int totalCount = response.totalCount;
				ArrayList<FocusPath> data = response.data;
				if (data != null) {
					mPackagePathList.addAll(data);
				}

				if (data.size() == 0) {
					showToast("暂无相关路线信息");
					flBlank.setVisibility(View.VISIBLE);
				} else {
					flBlank.setVisibility(View.GONE);
				}

				mOrderSimpleFoucusListAdapter.notifyDataSetChanged();
				if (totalCount <= mPackagePathList.size()) {
					isloadingFinish = true;
				}
				lvOrderSimple.onRefreshComplete();
			}
			break;
		case CANCEL_FOCUS_PATH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				MiPushUtil.setMiPushUnTopic(getActivity(), "A"
						+ mFocusPath.beginCity + mFocusPath.endCity);
				mPackagePathList.remove(mFocusPath);
				mOrderSimpleFoucusListAdapter.notifyDataSetChanged();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		switch (requestId) {
		case GET_FOCUS_PATH_REQUEST:
			showToast("连接超时,请检查网络");
			break;

		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPackagePathList.clear();
		mOrderSimpleFoucusListAdapter.notifyDataSetChanged();
		mPageIndex = 1;
		mGetFocusPathRequest = new GetFocusPathRequest(getActivity(),
				mPageIndex, PAGE_COUNT);
		mGetFocusPathRequest.setRequestId(GET_FOCUS_PATH_REQUEST);
		httpPostJson(mGetFocusPathRequest);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			mGetFocusPathRequest = new GetFocusPathRequest(getActivity(),
					mPageIndex, PAGE_COUNT);
			mGetFocusPathRequest.setRequestId(GET_FOCUS_PATH_REQUEST);
			httpPostJson(mGetFocusPathRequest);
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

	@Override
	public void cancelFocuson(FocusPath packagePath) {
		this.mFocusPath = packagePath;
		String beginCity = packagePath.beginCity;
		String endCity = packagePath.endCity;
		if (!TextUtils.isEmpty(beginCity) && beginCity != null
				&& !TextUtils.isEmpty(endCity) && endCity != null) {
			mCancelFocusPathRequest = new CancelFocusPathRequest(getActivity(),
					beginCity, endCity);
			mCancelFocusPathRequest.setRequestId(CANCEL_FOCUS_PATH_REQUEST);
			httpPost(mCancelFocusPathRequest);
		}
	}
}
