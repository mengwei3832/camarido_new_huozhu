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
import com.yunqi.clientandroid.http.request.GetCrashDetailRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的钱包明细界面
 * @date 15/12/6
 */
public class CrashDetailActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private PullToRefreshListView lvCrashDetail;
	private List<CrashDetail> mCrashDetailList;
	private CrashDetailAdapter mCrashDetailAdapter;

	private int mPageIndex = 1;// 起始页
	private final int PAGE_COUNT = 10;// 每页显示数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回

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
		httpPost(new GetCrashDetailRequest(CrashDetailActivity.this,
				mPageIndex, PAGE_COUNT));
		mCrashDetailList = new ArrayList<CrashDetail>();
		mCrashDetailAdapter = new CrashDetailAdapter(CrashDetailActivity.this,
				mCrashDetailList);
		lvCrashDetail.setAdapter(mCrashDetailAdapter);

	}

	@Override
	protected void setListener() {
		lvCrashDetail.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mCrashDetailList.clear();// 清空集合
		mCrashDetailAdapter.notifyDataSetChanged();// 刷新界面
		mPageIndex = 1;
		httpPostJson(new GetCrashDetailRequest(CrashDetailActivity.this,
				mPageIndex, PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetCrashDetailRequest(CrashDetailActivity.this,
					mPageIndex, PAGE_COUNT));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCrashDetail.onRefreshComplete();// 结束刷新的方法
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.wallet_detail));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CrashDetailActivity.this.finish();
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
		isloadingFinish = false;// 服务器还有数据要返回
		if (isSuccess) {
			int totalCount = response.totalCount;
			ArrayList<CrashDetail> data = response.data;
			if (data != null) {
				mCrashDetailList.addAll(data);
			}
			mCrashDetailAdapter.notifyDataSetChanged();// 刷新界面
			if (totalCount <= mCrashDetailList.size()) {
				isloadingFinish = true;// 服务器没有数据要返回
			}
			lvCrashDetail.onRefreshComplete();// 结束刷新界面

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
		showToast(this.getResources().getString(R.string.net_error_toast));
		lvCrashDetail.onRefreshComplete();
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, CrashDetailActivity.class);
		context.startActivity(intent);
	}

}
