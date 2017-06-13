package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.ActiveActivity;
import com.yunqi.clientandroid.activity.AttentionActivity;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.activity.MessageActivity;
import com.yunqi.clientandroid.activity.SearchListActivity;
import com.yunqi.clientandroid.adapter.DiscoverListAdapter;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.http.request.GetDiscoverListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发现
 * @date 15/11/20
 */
public class DiscoverFragment extends BaseFragment implements
		AdapterView.OnItemClickListener, View.OnClickListener,
		View.OnKeyListener {
	private ListView lvDiscover;
	private View viewDiscoverHeader;
	// 搜索
	private EditText etSearch;
	// 消息
	private TextView tvMsg;
	// 关注
	private TextView tvNotice;
	// 活动
	private TextView tvActive;
	// 搜索
	private ImageButton ibSearch;
	// 三个气泡数
	private TextView tvMsgBubble;
	private TextView tvAttentionBubble;
	private TextView tvActiveBubble;

	private DiscoverListAdapter mDiscoverListAdapter;
	private List<DiscoverListItem> mDiscoverListItemList = new ArrayList<DiscoverListItem>();

	PreManager mPreManager;

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(getActivity());
		httpPost(new GetDiscoverListRequest(getActivity()));

		mDiscoverListAdapter = new DiscoverListAdapter(this.getActivity(),
				mDiscoverListItemList);
		lvDiscover.setAdapter(mDiscoverListAdapter);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_discover;
	}

	@Override
	protected void initView(View _rootView) {
		lvDiscover = (ListView) _rootView.findViewById(R.id.lv_discover);
		viewDiscoverHeader = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.fragment_discover_header, null);

		etSearch = (EditText) viewDiscoverHeader.findViewById(R.id.et_search);
		tvMsg = (TextView) viewDiscoverHeader.findViewById(R.id.tv_msg);
		tvNotice = (TextView) viewDiscoverHeader.findViewById(R.id.tv_notice);
		tvActive = (TextView) viewDiscoverHeader.findViewById(R.id.tv_active);
		tvMsgBubble = (TextView) viewDiscoverHeader
				.findViewById(R.id.tv_msg_bubble);
		tvAttentionBubble = (TextView) viewDiscoverHeader
				.findViewById(R.id.tv_attention_bubble);
		tvActiveBubble = (TextView) viewDiscoverHeader
				.findViewById(R.id.tv_active_bubble);
		ibSearch = (ImageButton) viewDiscoverHeader
				.findViewById(R.id.ib_search);

		lvDiscover.addHeaderView(viewDiscoverHeader);
	}

	@Override
	protected void setListener() {
		lvDiscover.setOnItemClickListener(this);
		tvMsg.setOnClickListener(this);
		tvNotice.setOnClickListener(this);
		tvActive.setOnClickListener(this);
		ibSearch.setOnClickListener(this);
		etSearch.setOnKeyListener(this);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String hint;
				if (hasFocus) {
					hint = etSearch.getHint().toString();
					etSearch.setTag(hint);
					etSearch.setHint("");
				} else {
					hint = etSearch.getTag().toString();
					etSearch.setHint(hint);
				}
			}
		});

	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		MainActivity activity = (MainActivity) getActivity();
		activity.getActionBar().show();
		activity.setActionBarTitle(getString(R.string.main_discovery));
		activity.setActionBarLeft(0);
		activity.setActionBarRight(false, 0, "");
		activity.setOnActionBarLeftClickListener(null);
		activity.setOnActionBarRightClickListener(true, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		// 初始化titileBar
		initActionBar();

		// 显示未读数量
		showBubble();

		// 注册广播接收者--接收情况消息提示
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageActivity.messageActivity);
		getActivity().registerReceiver(messgeDetail, filter);
	}

	// 广播接收者--接收情况消息提示
	BroadcastReceiver messgeDetail = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MessageActivity.messageActivity)) {
				tvMsgBubble.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_msg: // 消息
			mPreManager.setMsgBubble(0);
			MessageActivity.invoke(getActivity());
			break;
		case R.id.tv_notice: // 关注
			mPreManager.setAttentionBubble(0);
			AttentionActivity.invoke(getActivity());
			break;
		case R.id.tv_active: // 活动
			mPreManager.setActiveBubble(0);
			ActiveActivity.invoke(getActivity());
			break;
		case R.id.ib_search: // 搜索
			SearchListActivity.invoke(getActivity(),
					SearchListActivity.FROM_SEARCH, etSearch.getText()
							.toString(), null);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0)
			return;

		DiscoverListItem discoverListItem = mDiscoverListAdapter
				.getItem(position - 1);
		if (discoverListItem != null) {
			SearchListActivity.invoke(getActivity(),
					SearchListActivity.FROM_TAG, null, discoverListItem);
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

		if (isSuccess) {
			ArrayList<DiscoverListItem> discoverData = response.data;
			if (discoverData != null) {
				mDiscoverListItemList.addAll(discoverData);
			}
			mDiscoverListAdapter.notifyDataSetChanged();
		} else {

		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				SearchListActivity.invoke(getActivity(),
						SearchListActivity.FROM_SEARCH, etSearch.getText()
								.toString(), null);
			}
			return true;
		}
		return false;
	}

	/**
	 * 显示气泡
	 */
	private void showBubble() {
		// 消息气泡
		if (mPreManager.getMsgBubble() > 0) {
			tvMsgBubble.setVisibility(View.VISIBLE);
			tvMsgBubble.setText(mPreManager.getMsgBubble() + "");
		} else {
			tvMsgBubble.setVisibility(View.GONE);
		}

		// 关注气泡
		if (mPreManager.getAttentionBubble() > 0) {
			tvAttentionBubble.setVisibility(View.VISIBLE);
			tvAttentionBubble.setText(mPreManager.getAttentionBubble() + "");
		} else {
			tvAttentionBubble.setVisibility(View.GONE);
		}

		// 活动气泡
		if (mPreManager.getActiveBubble() > 0) {
			tvActiveBubble.setVisibility(View.VISIBLE);
			tvActiveBubble.setText(mPreManager.getActiveBubble() + "");
		} else {
			tvActiveBubble.setVisibility(View.GONE);
		}
	}
}
