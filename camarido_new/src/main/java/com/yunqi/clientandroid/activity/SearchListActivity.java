package com.yunqi.clientandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.SearchAdapter;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.DiscoverSearchRequest;
import com.yunqi.clientandroid.http.request.DiscoverTagRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 搜索界面
 * @date 15/12/6
 */
public class SearchListActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		AdapterView.OnItemClickListener {

	private PullToRefreshListView lvSearch;
	private List<Message> mSearchList;
	private SearchAdapter mSearchAdapter;

	private int mPageIndex = 1;// 起始页
	private final int PAGE_COUNT = 10;// 每页显示数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回

	private int mClickFrom;
	private String mKeyWord;
	private String mTitle;
	private DiscoverListItem mDiscoverItem;

	// 本界面接口
	private DiscoverSearchRequest mDiscoverSearchRequest;
	private DiscoverTagRequest mDiscoverTagRequest;

	private final int SEARCH_REQUEST = 1;
	private final int TAG_REQUEST = 2;

	// 跳转数据
	private static final String EXTRA_CLICK_FROM = "CLICK_FROM";
	private static final String EXTRA_KEYWORD = "KEYWORD";
	private static final String EXTRA_ITEM = "DISCOVER_ITEM";
	public static final int FROM_SEARCH = 1;
	public static final int FROM_TAG = 2;

	// 新增
	private ImageView ivBlank;// 白板提示

	@Override
	protected int getLayoutId() {
		return R.layout.activity_search;
	}

	@Override
	protected void initView() {
		lvSearch = obtainView(R.id.lv_attention);
		lvSearch.setMode(PullToRefreshBase.Mode.BOTH);
		// 新增
		ivBlank = obtainView(R.id.iv_blank);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mClickFrom = intent.getIntExtra(EXTRA_CLICK_FROM, 0);
		mKeyWord = intent.getStringExtra(EXTRA_KEYWORD);
		mDiscoverItem = (DiscoverListItem) intent
				.getSerializableExtra(EXTRA_ITEM);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mClickFrom == FROM_SEARCH) {
			mTitle = mKeyWord;
			mDiscoverSearchRequest = new DiscoverSearchRequest(this,
					mPageIndex, PAGE_COUNT, mKeyWord);
			mDiscoverSearchRequest.setRequestId(SEARCH_REQUEST);
			httpPostJson(mDiscoverSearchRequest);
		} else if (mClickFrom == FROM_TAG) {
			mTitle = mDiscoverItem.tagName;
			mDiscoverTagRequest = new DiscoverTagRequest(this, mPageIndex,
					PAGE_COUNT, mDiscoverItem.id);
			mDiscoverTagRequest.setRequestId(TAG_REQUEST);
			httpPostJson(mDiscoverTagRequest);

		}

		// 初始化titileBar
		initActionBar();
		mSearchList = new ArrayList<Message>();
		mSearchAdapter = new SearchAdapter(SearchListActivity.this, mSearchList);
		lvSearch.setAdapter(mSearchAdapter);
	}

	@Override
	protected void setListener() {
		lvSearch.setOnRefreshListener(this);
		lvSearch.setOnItemClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mSearchList.clear();
		mSearchAdapter.notifyDataSetChanged();
		mPageIndex = 1;
		if (mClickFrom == FROM_SEARCH) {
			mDiscoverSearchRequest = new DiscoverSearchRequest(this,
					mPageIndex, PAGE_COUNT, mKeyWord);
			mDiscoverSearchRequest.setRequestId(SEARCH_REQUEST);
			httpPostJson(mDiscoverSearchRequest);
		} else if (mClickFrom == FROM_TAG) {
			mDiscoverTagRequest = new DiscoverTagRequest(this, mPageIndex,
					PAGE_COUNT, mDiscoverItem.id);
			mDiscoverTagRequest.setRequestId(TAG_REQUEST);
			httpPostJson(mDiscoverTagRequest);

		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			if (mClickFrom == FROM_SEARCH) {
				mDiscoverSearchRequest = new DiscoverSearchRequest(this,
						mPageIndex, PAGE_COUNT, mKeyWord);
				mDiscoverSearchRequest.setRequestId(SEARCH_REQUEST);
				httpPostJson(mDiscoverSearchRequest);
			} else if (mClickFrom == FROM_TAG) {
				mDiscoverTagRequest = new DiscoverTagRequest(this, mPageIndex,
						PAGE_COUNT, mDiscoverItem.id);
				mDiscoverTagRequest.setRequestId(TAG_REQUEST);
				httpPostJson(mDiscoverTagRequest);

			}
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvSearch.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(mTitle);
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchListActivity.this.finish();
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
			ArrayList<Message> data = response.data;
			if (data != null) {
				mSearchList.addAll(data);
			}
			mSearchAdapter.notifyDataSetChanged();
			if (totalCount <= mSearchList.size()) {
				isloadingFinish = true;
			}
			lvSearch.onRefreshComplete();
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		} else {
			if (!TextUtils.isEmpty(message) && message != null) {
				showToast(message);
			}
		}
		// 白板的隐藏显示
		if (mSearchList.size() > 0) {
			ivBlank.setVisibility(View.GONE);// 隐藏白板
		} else {
			ivBlank.setVisibility(View.VISIBLE);// 显示白板
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		lvSearch.onRefreshComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message message = mSearchAdapter.getItem(position - 1);
		AttentionDetailActivity.invoke(SearchListActivity.this, message.id);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, int clickFrom, String keyWord,
			DiscoverListItem item) {
		Intent intent = new Intent(context, SearchListActivity.class);
		intent.putExtra(EXTRA_CLICK_FROM, clickFrom);
		intent.putExtra(EXTRA_KEYWORD, keyWord);
		intent.putExtra(EXTRA_ITEM, item);

		context.startActivity(intent);
	}

}
