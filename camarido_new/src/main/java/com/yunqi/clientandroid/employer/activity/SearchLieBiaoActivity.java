package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.HangYeAdapter;
import com.yunqi.clientandroid.employer.entity.HangYeZiXun;
import com.yunqi.clientandroid.employer.request.HangYeRequest;
import com.yunqi.clientandroid.http.request.DiscoverSearchRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:行业资讯列表数据
 * @ClassName: HangYeLieBiaoActivity
 * @author: chengtao
 * @date: 2016-7-7 下午2:56:42
 * 
 */
public class SearchLieBiaoActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	private PullToRefreshListView lvHangYeView;
	private ProgressBar pbHangYeBar;
	private ImageView ivHangYeBlank;
	private String keyWord;
	private String title;

	private ArrayList<HangYeZiXun> hangList = new ArrayList<HangYeZiXun>();
	private HangYeRequest hangYeRequest;
	private HangYeAdapter hangYeAdapter;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_hangye;
	}

	@Override
	protected void initView() {
		keyWord = getIntent().getStringExtra("keyWord");
		title = getIntent().getStringExtra("title");

		initActionBar();

		lvHangYeView = obtainView(R.id.lv_employer_hangye);
		pbHangYeBar = obtainView(R.id.pb_hangye_bar);
		ivHangYeBlank = obtainView(R.id.iv_hangye_blank);

		lvHangYeView.setMode(PullToRefreshBase.Mode.BOTH);
		lvHangYeView.setOnRefreshListener(this);

		hangYeAdapter = new HangYeAdapter(hangList, mContext);
		lvHangYeView.setAdapter(hangYeAdapter);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(title);
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true, 0, "");

		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				SearchLieBiaoActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 请求列表
		getZiXunLieBiao();
	}

	private void getZiXunLieBiao() {
		hangList.clear();
		count = pageIndex * pageSize;
		hangYeAdapter.notifyDataSetChanged();
		pbHangYeBar.setVisibility(View.VISIBLE);
		httpPost(new DiscoverSearchRequest(mContext, pageSize, pageIndex,
				keyWord));
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
		totalCount = response.totalCount;

		if (isSuccess) {
			ArrayList<HangYeZiXun> yelist = response.data;

			if (yelist != null) {
				hangList.addAll(yelist);
			}

			if (hangList.size() == 0) {
				showToast(message);
				ivHangYeBlank.setVisibility(View.VISIBLE);
			} else {
				ivHangYeBlank.setVisibility(View.GONE);
			}

			if (totalCount <= count) {
				isEnd = true;
			}
		}

		lvHangYeView.onRefreshComplete();
		hangYeAdapter.notifyDataSetChanged();
		pbHangYeBar.setVisibility(View.GONE);
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
	}

	@Override
	protected void setListener() {
		lvHangYeView.setOnItemClickListener(this);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, String title, String keyWord) {
		Intent intent = new Intent(context, SearchLieBiaoActivity.class);
		intent.putExtra("keyWord", keyWord);
		intent.putExtra("title", title);
		context.startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		hangList.clear();
		hangYeAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getZiXunLieBiao();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getZiXunLieBiao();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvHangYeView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HangYeZiXun hangYeZiXun = hangList.get(position - 1);

		String tagId = String.valueOf(hangYeZiXun.id);
		String title = hangYeZiXun.MessageTitle;

		GetMessageActivity.invoke(mContext, tagId, 0);
	}

}
