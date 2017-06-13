package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.BankListAdapter;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.request.GetBankRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 选择银行
 * @date 15/12/6
 */
public class BankListActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		AdapterView.OnItemClickListener {

	private PullToRefreshListView lvBank;
	private List<VehicleType> mBankList;
	private BankListAdapter mBankAdapter;
	public final static int RESULT_CODE = 0x01;
	public final static String EXTRA_BANK = "bank";

	// private int mPageIndex = 1;
	// private final int PAGE_COUNT = 10;
	// private boolean isloadingFinish = false;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_bank_list;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		lvBank = obtainView(R.id.lv_bank);
		// lvBank.setMode(PullToRefreshBase.Mode.BOTH);

	}

	@Override
	protected void initData() {
		httpPost(new GetBankRequest(BankListActivity.this, "504"));

		mBankList = new ArrayList<VehicleType>();
		mBankAdapter = new BankListAdapter(BankListActivity.this, mBankList);
		lvBank.setAdapter(mBankAdapter);

	}

	@Override
	protected void setListener() {
		lvBank.setOnRefreshListener(this);
		lvBank.setOnItemClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mBankList.clear();// 清空集合
		mBankAdapter.notifyDataSetChanged();
		// mPageIndex = 1;
		httpPostJson(new GetBankRequest(BankListActivity.this, "504"));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// if (!isloadingFinish) {
		// mPageIndex++;
		// httpPostJson(new GetActiveListRequest(BankListActivity.this,
		// mPageIndex, PAGE_COUNT));
		// } else {
		// mHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// lvBank.onRefreshComplete();
		// showToast("已经是最后一页了");
		// }
		// }, 100);
		// }
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.bank));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BankListActivity.this.finish();
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
		// isloadingFinish = false;
		if (isSuccess) {
			// int totalCount = response.totalCount;
			ArrayList<VehicleType> data = response.data;
			if (data != null) {
				mBankList.addAll(data);
			}
			mBankAdapter.notifyDataSetChanged();// 刷新界面
			// if (totalCount <= mBankList.size()) {
			// isloadingFinish = true;
			// }
			lvBank.onRefreshComplete();// 结束刷新的方法

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
		lvBank.onRefreshComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent = new Intent(BankListActivity.this,
				WithdrawCashActivity.class);
		intent.putExtra(EXTRA_BANK, mBankAdapter.getItem(position - 1));
		setResult(RESULT_CODE, intent);
		BankListActivity.this.finish();
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Activity context) {
		Intent intent = new Intent(context, BankListActivity.class);
		context.startActivityForResult(intent, RESULT_CODE);
	}

}
