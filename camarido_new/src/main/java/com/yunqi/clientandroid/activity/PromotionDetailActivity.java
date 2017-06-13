package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.CrashDetailAdapter;
import com.yunqi.clientandroid.entity.CrashDetail;
import com.yunqi.clientandroid.http.request.GetPromotionCrashDetailRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 推广明细界面
 * @date 15/12/6
 */
public class PromotionDetailActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private PullToRefreshListView lvCrashDetail;
	private List<CrashDetail> mCrashDetailList;
	private CrashDetailAdapter mCrashDetailAdapter;

	private int mPageIndex = 1;
	private final int PAGE_COUNT = 10;
	private boolean isloadingFinish = false;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_crashdetail;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		lvCrashDetail = obtainView(R.id.lv_crash_detail);
		lvCrashDetail.setMode(PullToRefreshBase.Mode.BOTH);

	}

	@Override
	protected void initData() {
		httpPost(new GetPromotionCrashDetailRequest(
				PromotionDetailActivity.this, mPageIndex, PAGE_COUNT));
		mCrashDetailList = new ArrayList<CrashDetail>();
		mCrashDetailAdapter = new CrashDetailAdapter(
				PromotionDetailActivity.this, mCrashDetailList);
		lvCrashDetail.setAdapter(mCrashDetailAdapter);

	}

	@Override
	protected void setListener() {
		lvCrashDetail.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mCrashDetailList.clear();
		mCrashDetailAdapter.notifyDataSetChanged();
		mPageIndex = 1;
		httpPostJson(new GetPromotionCrashDetailRequest(
				PromotionDetailActivity.this, mPageIndex, PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetPromotionCrashDetailRequest(
					PromotionDetailActivity.this, mPageIndex, PAGE_COUNT));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCrashDetail.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(getString(R.string.wallet_detail));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PromotionDetailActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
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
			ArrayList<CrashDetail> data = response.data;

			if (data != null) {
				mCrashDetailList.addAll(data);
			}

			if (totalCount <= mCrashDetailList.size()) {
				isloadingFinish = true;
			}

			mCrashDetailAdapter.notifyDataSetChanged();
			lvCrashDetail.onRefreshComplete();

			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		lvCrashDetail.onRefreshComplete();
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, PromotionDetailActivity.class);
		context.startActivity(intent);
	}

}
