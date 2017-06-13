package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.MingXiAdapter;
import com.yunqi.clientandroid.employer.entity.MingXiList;
import com.yunqi.clientandroid.employer.request.GetAccountRecordsRequest;
import com.yunqi.clientandroid.http.response.Response;

public class MingXiActivity extends BaseActivity implements
		OnRefreshListener2<ListView> {
	private PullToRefreshListView lvMingXi;
	private List<MingXiList> list;
	private MingXiAdapter adapter;

	private int mPageIndex = 1;
	private final int PAGE_COUNT = 10;
	private boolean isloadingFinish = false;

	// 页面请求
	GetAccountRecordsRequest getAccountRecordsRequest;
	// 请求ID
	private final static int GET_ACCOUNT_RECORDS_REQUEST = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activty_ming_xi;
	}

	@Override
	protected void initView() {
		lvMingXi = obtainView(R.id.lv_ming_xi);
		list = new ArrayList<MingXiList>();
		initActionBar();
		lvMingXi.setMode(PullToRefreshBase.Mode.BOTH);
		adapter = new MingXiAdapter(mContext, list);
		lvMingXi.setAdapter(adapter);
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月15日 下午5:11:47
	 * @Author : chengtao
	 */
	private void initActionBar() {
		setActionBarTitle("明细");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		// 添加数据
		getRefreshList();
	}

	private void getRefreshList() {
		list.clear();
		adapter.notifyDataSetChanged();
		getAccountRecordsRequest = new GetAccountRecordsRequest(mContext,
				mPageIndex, PAGE_COUNT);
		getAccountRecordsRequest.setRequestId(GET_ACCOUNT_RECORDS_REQUEST);
		httpPostJson(getAccountRecordsRequest);
	}

	@Override
	protected void setListener() {
		lvMingXi.setOnRefreshListener(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String messsage = response.message;
		isloadingFinish = false;
		if (isSuccess) {
			int totalCount = response.totalCount;
			ArrayList<MingXiList> lists = response.data;
			if (lists != null) {
				list.addAll(lists);
				if (lists.size() > 0) {
					setBaiBan(false);
				} else {
					setBaiBan(true);
				}
			}
			if (totalCount <= list.size()) {
				isloadingFinish = true;
			}
			adapter.notifyDataSetChanged();
			lvMingXi.onRefreshComplete();
		} else {
			setBaiBan(true);
		}
		if (messsage != null && !TextUtils.isEmpty(messsage)) {
			showToast(messsage);
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
		lvMingXi.onRefreshComplete();
		setBaiBan(true);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex = 1;
		getRefreshList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			getRefreshList();
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lvMingXi.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

	/**
	 * 
	 * @Description:明细界面跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月16日 下午9:19:32
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, MingXiActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * 
	 * @Description: 消息推送跳转
	 * @Title:invokeNewTask
	 * @param context
	 *            上下文
	 * @return:void
	 * @throws
	 * @Create: Aug 30, 2016 5:44:13 PM
	 * @Author : chengtao
	 */
	public static void invokeNewTask(Context context) {
		Intent intent = new Intent(context, MingXiActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
