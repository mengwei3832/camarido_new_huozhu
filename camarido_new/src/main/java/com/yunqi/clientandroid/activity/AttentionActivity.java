package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.AttentionAdapter;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.GetAttentionListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的关注
 * @date 15/12/6
 */
public class AttentionActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		AdapterView.OnItemClickListener {

	private PullToRefreshListView lvAttention;
	private List<Message> mAttentionlist = new ArrayList<Message>();
	private AttentionAdapter mAttentionAdapter;

	private int mPageIndex = 1;// 起始页置为1
	private final int PAGE_COUNT = 10;// 每页显示数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回

	PreManager mPreManager;
	// 新增
	private ImageView ivBlank;// 白板提示

	@Override
	protected int getLayoutId() {
		return R.layout.activity_attention;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();
		lvAttention = obtainView(R.id.lv_attention);
		lvAttention.setMode(PullToRefreshBase.Mode.BOTH);
		// 新增
		ivBlank = obtainView(R.id.iv_blank);
	}

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(AttentionActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		httpPost(new GetAttentionListRequest(AttentionActivity.this,
				mPageIndex, PAGE_COUNT));

		// onPullDownToRefresh(lvAttention);
		mAttentionlist.clear();// 清空集合

		mAttentionAdapter = new AttentionAdapter(AttentionActivity.this,
				mAttentionlist);
		lvAttention.setAdapter(mAttentionAdapter);
	}

	@Override
	protected void setListener() {
		lvAttention.setOnRefreshListener(this);
		lvAttention.setOnItemClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mAttentionlist.clear();// 清空集合
		mAttentionAdapter.notifyDataSetChanged();// 刷新界面
		mPageIndex = 1; // 起始页置为1
		httpPostJson(new GetAttentionListRequest(AttentionActivity.this,
				mPageIndex, PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetAttentionListRequest(AttentionActivity.this,
					mPageIndex, PAGE_COUNT));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvAttention.onRefreshComplete();// 结束界面刷新
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.tv_leftmenu_focus));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPreManager.setAttentionBubble(0);
				AttentionActivity.this.finish();
			}
		});
		setActionBarRight(true, 0, "管理");
		setOnActionBarRightClickListener(false, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TagManagerActivity.invoke(AttentionActivity.this);
			}
		});
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
			mPreManager.setAttentionBubble(0);
			AttentionActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
				mAttentionlist.addAll(data);
			}
			if (totalCount <= mAttentionlist.size()) {
				isloadingFinish = true;// 服务器没有数据要返回
			}

			mAttentionAdapter.notifyDataSetChanged();// 刷新界面
			lvAttention.onRefreshComplete();// 结束刷新的方法

			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
		// 白板的隐藏显示
		if (mAttentionlist.size() > 0) {
			ivBlank.setVisibility(View.GONE);// 隐藏白板
		} else {
			ivBlank.setVisibility(View.VISIBLE);// 显示白板
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		isloadingFinish = false;// 服务器还有数据要返回
		lvAttention.onRefreshComplete();// 结束刷新的方法
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String attentionId = mAttentionAdapter.getItem(position - 1).id;
		if (!TextUtils.isEmpty(attentionId) && attentionId != null) {
			AttentionDetailActivity.invoke(AttentionActivity.this, attentionId);
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, AttentionActivity.class);
		context.startActivity(intent);
	}
}
