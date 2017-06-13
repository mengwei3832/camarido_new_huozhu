package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.AttentionDetailActivity;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.SearchListActivity;
import com.yunqi.clientandroid.adapter.SearchAdapter;
import com.yunqi.clientandroid.employer.request.HangTagRequest;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.DiscoverSearchRequest;
import com.yunqi.clientandroid.http.request.DiscoverTagRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:资讯列表界面
 * @ClassName: ZiXunLieBiaoActivity
 * @author: mengwei
 * @date: 2016-6-21 下午2:57:41
 * 
 */
public class ZiXunLieBiaoActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	// 界面控件对象
	private PullToRefreshListView lvZiXun;
	private ProgressBar pbZixun;
	private ImageView ivZiXunBlank;

	// 分页请求参数
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	// 传递过来的数据
	private static final String EXTRA_CLICK_FROM = "CLICK_FROM";
	private static final String EXTRA_KEYWORD = "KEYWORD";
	private static final String EXTRA_TITLE = "TITLE";
	private static final String EXTRA_TAGID = "TAGID";
	public static final int FROM_SEARCH = 1;
	public static final int FROM_TAG = 2;

	private int mClickFrom;
	private String mKeyWord;
	private String title;
	private int tagId;

	// 本界面接口
	private DiscoverSearchRequest mDiscoverSearchRequest;
	private HangTagRequest mDiscoverTagRequest;

	private final int SEARCH_REQUEST = 1;
	private final int TAG_REQUEST = 2;

	// 存储资讯数据集合
	private ArrayList<Message> ziXunList = new ArrayList<Message>();
	private SearchAdapter searchAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_zixun;
	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		mClickFrom = intent.getIntExtra(EXTRA_CLICK_FROM, 0);
		mKeyWord = intent.getStringExtra(EXTRA_KEYWORD);
		title = intent.getStringExtra(EXTRA_TITLE);
		tagId = intent.getIntExtra(EXTRA_TAGID, 0);

		initActionBar();

		lvZiXun = obtainView(R.id.lv_employer_zixun);
		pbZixun = obtainView(R.id.pb_employer_zixun);
		ivZiXunBlank = obtainView(R.id.iv_employer_zixun_blank);

		lvZiXun.setMode(PullToRefreshBase.Mode.BOTH);
		lvZiXun.setOnRefreshListener(this);
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(title);
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ZiXunLieBiaoActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 清空集合
		ziXunList.clear();
		pbZixun.setVisibility(View.VISIBLE);
		count = pageIndex * pageSize;

		// 请求数据
		switch (mClickFrom) {
		case FROM_SEARCH: // 搜索框的请求
			mDiscoverSearchRequest = new DiscoverSearchRequest(
					ZiXunLieBiaoActivity.this, pageIndex, pageSize, mKeyWord);
			mDiscoverSearchRequest.setRequestId(SEARCH_REQUEST);
			httpPost(mDiscoverSearchRequest);
			break;

		case FROM_TAG: // 下方Tag请求
			mDiscoverTagRequest = new HangTagRequest(ZiXunLieBiaoActivity.this,
					pageIndex, pageSize, tagId);
			mDiscoverTagRequest.setRequestId(TAG_REQUEST);
			httpPost(mDiscoverTagRequest);
			break;

		default:
			break;
		}

		// 适配器
		searchAdapter = new SearchAdapter(ZiXunLieBiaoActivity.this, ziXunList);
		lvZiXun.setAdapter(searchAdapter);
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
		case SEARCH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			int totalCount = response.totalCount;

			if (response.data.size() == 0) {
				showToast(message);
				ivZiXunBlank.setVisibility(View.VISIBLE);
			} else {
				ivZiXunBlank.setVisibility(View.GONE);
			}

			if (isSuccess) {
				ArrayList<Message> searchList = response.data;

				if (searchList != null) {
					ziXunList.addAll(searchList);
				}

				if (ziXunList.size() == 0) {
					showToast(message);
					ivZiXunBlank.setVisibility(View.VISIBLE);
				} else {
					ivZiXunBlank.setVisibility(View.GONE);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				searchAdapter.notifyDataSetChanged();
				lvZiXun.onRefreshComplete();
			} else {
				showToast(message);
			}
			pbZixun.setVisibility(View.GONE);
			break;

		case TAG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			int totalCount1 = response.totalCount;

			if (isSuccess) {
				ArrayList<Message> tagList = response.data;

				if (tagList.size() == 0) {
					showToast(message);
					ivZiXunBlank.setVisibility(View.VISIBLE);
					// setBaiBan(true);
				} else {
					ivZiXunBlank.setVisibility(View.GONE);
				}

				if (tagList != null) {
					ziXunList.addAll(tagList);
				}

				if (totalCount1 <= count) {
					isEnd = true;
				}

				searchAdapter.notifyDataSetChanged();
				lvZiXun.onRefreshComplete();
			} else {
				showToast(message);
				setBaiBan(true);
			}
			pbZixun.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		lvZiXun.onRefreshComplete();
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		lvZiXun.setOnItemClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		ziXunList.clear();
		searchAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		onResume();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			onResume();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvZiXun.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message message = ziXunList.get(position);

		String messageId = message.id;

		AttentionDetailActivity.invoke(ZiXunLieBiaoActivity.this, messageId);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, int clickFrom, String keyWord,
			String title, int tagId) {
		Intent intent = new Intent(context, ZiXunLieBiaoActivity.class);
		intent.putExtra(EXTRA_CLICK_FROM, clickFrom);
		intent.putExtra(EXTRA_KEYWORD, keyWord);
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_TAGID, tagId);
		context.startActivity(intent);
	}

}
