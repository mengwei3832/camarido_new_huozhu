package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.ActiveAdapter;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.GetActiveListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 活动界面
 * @date 15/12/6
 */
public class ActiveActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private PullToRefreshListView activeRefreshListView;
	private ArrayList<Message> mActiveList = new ArrayList<Message>();
	private ActiveAdapter mActiveAdapter;
	private ListView activeListView;
	private Handler handler = new Handler();
	private View progress_activity;

	private int mPageIndex = 1;// 起始页
	private final int PAGE_COUNT = 10;// 每页显示数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回

	PreManager mPreManager;
	// 新增
	private ImageView ivBlank;// 白板提示

	@Override
	protected int getLayoutId() {
		return R.layout.activity_active;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		activeRefreshListView = obtainView(R.id.lv_refresh_active);

		// 初始化刷新view
		initPullToRefreshView();
		// 新增
		ivBlank = obtainView(R.id.iv_blank);
		progress_activity = obtainView(R.id.pb_activity_bar);

		progress_activity.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.discover_active));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActiveActivity.this.finish();
				mPreManager.setActiveBubble(0);
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	/**
	 * 
	 * @Description:class 对back键的监听
	 * @Title:onKeyDown
	 * @param keyCode
	 * @param event
	 * @return
	 * @throws
	 * @Create: 2016-5-7 下午4:40:05
	 * @Author : zhm
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActiveActivity.this.finish();
			mPreManager.setActiveBubble(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(ActiveActivity.this);
		mActiveList.clear();// 清空集合
		mPageIndex = 1;// 起始页置为1
		httpPostJson(new GetActiveListRequest(ActiveActivity.this, mPageIndex,
				PAGE_COUNT));
	}

	@Override
	protected void setListener() {

	}

	// 列表刷新的方法
	private void initPullToRefreshView() {
		activeRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		activeRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		activeRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		activeRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		activeListView = activeRefreshListView.getRefreshableView();
		activeListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		activeListView.setDividerHeight(25);
		activeListView.setSelector(android.R.color.transparent);// 隐藏listView默认的selector
		activeRefreshListView.setOnRefreshListener(this);
		// TODO--item的点击跳转到文章详情界面
		activeRefreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String messageId = mActiveList.get(position - 1).id;

						L.e("TAG", "huodeID------------------------"
								+ messageId);
						// 跳转到文章详情界面
						AttentionDetailActivity.invoke(ActiveActivity.this,
								messageId);
					}
				});
		mActiveAdapter = new ActiveAdapter(ActiveActivity.this, mActiveList);
		activeListView.setAdapter(mActiveAdapter);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (mActiveList == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					activeRefreshListView.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (mActiveList != null) {
			mActiveAdapter.notifyDataSetChanged();
		}
		mActiveList.clear();// 清空集合
		mPageIndex = 1; // 起始页置为1
		httpPostJson(new GetActiveListRequest(ActiveActivity.this, mPageIndex,
				PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetActiveListRequest(ActiveActivity.this,
					mPageIndex, PAGE_COUNT));
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					activeRefreshListView.onRefreshComplete();// 刷新界面完成
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
		isloadingFinish = false;// 服务器还有数据要返回
		if (isSuccess) {
			int totalCount = response.totalCount;
			ArrayList<Message> data = response.data;

			if (data != null) {
				mActiveList.addAll(data);
			}
			if (totalCount <= mActiveList.size()) {
				isloadingFinish = true;// 服务器没有数据要返回
			}

			mActiveAdapter.notifyDataSetChanged();// 刷新界面
			activeRefreshListView.onRefreshComplete();// 结束刷新的方法

			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
		// 白板的隐藏显示
		if (mActiveList.size() > 0) {
			ivBlank.setVisibility(View.GONE);// 隐藏白板
		} else {
			ivBlank.setVisibility(View.VISIBLE);// 显示白板
		}
		progress_activity.setVisibility(View.GONE);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isloadingFinish = false;// 服务器还有数据要返回
		activeRefreshListView.onRefreshComplete();// 结束刷新的方法
		showToast(this.getResources().getString(R.string.net_error_toast));
		progress_activity.setVisibility(View.GONE);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, ActiveActivity.class);
		context.startActivity(intent);
	}

}
