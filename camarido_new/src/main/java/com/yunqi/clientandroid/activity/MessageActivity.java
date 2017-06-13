package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.MessageAdapter;
import com.yunqi.clientandroid.entity.ShortMessage;
import com.yunqi.clientandroid.http.request.GetMessageListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 消息界面
 * @date 15/12/6
 */
public class MessageActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private PullToRefreshListView lvMsg;
	private List<ShortMessage> mMsgList;
	private MessageAdapter mMessageAdapter;

	private int mPageIndex = 1;// 起始页
	private final int PAGE_COUNT = 10;// 每页显示数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回

	// 广播接受者需要用的活动
	public static final String messageActivity = "messageActivity.broadcast.action";

	PreManager mPreManager;
	// 新增
	private ImageView ivBlank;// 白板提示
	private View pbBar;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		lvMsg = obtainView(R.id.lv_msg);
		pbBar = obtainView(R.id.pb_message_bar);
		lvMsg.setMode(PullToRefreshBase.Mode.BOTH);

		// 发送广播情况消息数量
		Intent intent = new Intent(messageActivity);
		sendBroadcast(intent);
		// 新增
		ivBlank = obtainView(R.id.iv_blank);
	}

	@Override
	protected void initData() {
		pbBar.setVisibility(View.VISIBLE);

		mPreManager = PreManager.instance(MessageActivity.this);
		httpPost(new GetMessageListRequest(MessageActivity.this, mPageIndex,
				PAGE_COUNT));
		mMsgList = new ArrayList<ShortMessage>();
		mMessageAdapter = new MessageAdapter(MessageActivity.this, mMsgList);
		lvMsg.setAdapter(mMessageAdapter);

	}

	@Override
	protected void setListener() {
		lvMsg.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mMsgList.clear();// 情况集合
		mMessageAdapter.notifyDataSetChanged();// 刷新界面
		mPageIndex = 1;
		httpPostJson(new GetMessageListRequest(MessageActivity.this,
				mPageIndex, PAGE_COUNT));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			httpPostJson(new GetMessageListRequest(MessageActivity.this,
					mPageIndex, PAGE_COUNT));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvMsg.onRefreshComplete();// 结束刷新
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
				R.string.tv_leftmenu_msg));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MessageActivity.this.finish();
				mPreManager.setMsgBubble(0);
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
			MessageActivity.this.finish();
			mPreManager.setMsgBubble(0);
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
			ArrayList<ShortMessage> data = response.data;
			if (data != null) {
				mMsgList.addAll(data);
			}
			mMessageAdapter.notifyDataSetChanged();// 刷新界面的方法
			if (totalCount <= mMsgList.size()) {
				isloadingFinish = true;
			}
			lvMsg.onRefreshComplete();

			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
		// 白板的隐藏显示
		if (mMsgList.size() > 0) {
			ivBlank.setVisibility(View.GONE);// 隐藏白板
		} else {
			ivBlank.setVisibility(View.VISIBLE);// 显示白板
		}
		pbBar.setVisibility(View.GONE);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
		lvMsg.onRefreshComplete();// 结束刷新
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, MessageActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 本界面增加flag跳转方式
	 * 
	 * @param context
	 */
	public static void invokeNewTask(Context context) {
		Intent intent = new Intent(context, MessageActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
