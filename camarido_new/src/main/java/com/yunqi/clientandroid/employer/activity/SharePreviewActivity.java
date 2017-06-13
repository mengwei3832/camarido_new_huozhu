package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.PackageListDetailActivity;
import com.yunqi.clientandroid.employer.adapter.SharePreViewAdapter;
import com.yunqi.clientandroid.employer.entity.ShareContext;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.employer.request.GetShareContextRequest;
import com.yunqi.clientandroid.employer.request.TicketAllCurrentRequest;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.umeng.share.ShareUtil;
import com.yunqi.clientandroid.umeng.share.SharelistHelper;

/**
 * @Description:分享预览界面
 * @ClassName: SharePreviewActivity
 * @author: mengwei
 * @date: 2016-6-28 下午2:16:13
 * 
 */
public class SharePreviewActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnClickListener {
	/* 界面控件对象 */
	private LinearLayout llShareEmpty;
	private TextView tvShareBeginCity;
	private TextView tvShareBeginCompany;
	private TextView tvShareEndCity;
	private TextView tvShareEndCompany;
	private TextView tvShareTime;
	private PullToRefreshListView lvShareView;
	private Button btShareButton;
	private ProgressBar pbShareBar;
	private ImageView ivShareBlank;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	private ArrayList<TicketCurrentBean> shareList = new ArrayList<TicketCurrentBean>();
	private SharePreViewAdapter sharePreViewAdapter;

	private TicketAllCurrentRequest ticketAllCurrentRequest;
	private GetShareContextRequest getShareContextRequest;

	private final int GET_LIEBIAO = 1;
	private final int GET_SHARE = 2;

	private String packageId;
	private Share shareContext;
	private String beginCityName;
	private String beginCompanyName;
	private String endCityName;
	private String endCompanyName;
	private String packageDate;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_share_preview;
	}

	@Override
	protected void initView() {
		// 初始化控件对象
		initFindView();
	}

	/**
	 * @Description:初始化控件对象
	 * @Title:initFindView
	 * @return:void
	 * @throws
	 * @Create: 2016-6-28 下午3:07:44
	 * @Author : mengwei
	 */
	private void initFindView() {
		initActionBar();

		packageId = getIntent().getStringExtra("packageId");
		beginCityName = getIntent().getStringExtra("beginCityName");
		beginCompanyName = getIntent().getStringExtra("beginCompanyName");
		endCityName = getIntent().getStringExtra("endCityName");
		endCompanyName = getIntent().getStringExtra("endCompanyName");
		packageDate = getIntent().getStringExtra("packageDate");

		llShareEmpty = obtainView(R.id.ll_share_empty);
		tvShareBeginCity = obtainView(R.id.tv_share_beginCity);
		tvShareBeginCompany = obtainView(R.id.tv_share_beginCompany);
		tvShareEndCity = obtainView(R.id.tv_share_endCity);
		tvShareEndCompany = obtainView(R.id.tv_share_endCompany);
		tvShareTime = obtainView(R.id.tv_share_date);
		lvShareView = obtainView(R.id.lv_share_preview);
		btShareButton = obtainView(R.id.bt_share_preview);
		pbShareBar = obtainView(R.id.pb_share);
		ivShareBlank = obtainView(R.id.iv_blank_share);

		lvShareView.setMode(PullToRefreshBase.Mode.BOTH);
		lvShareView.setOnRefreshListener(this);

		setFindView();

		sharePreViewAdapter = new SharePreViewAdapter(shareList, mContext);
		lvShareView.setAdapter(sharePreViewAdapter);
	}

	/**
	 * @Description:给控件赋值
	 * @Title:setFindView
	 * @return:void
	 * @throws
	 * @Create: 2016-7-5 下午5:44:38
	 * @Author : chengtao
	 */
	private void setFindView() {
		tvShareBeginCity.setText(beginCityName);
		tvShareBeginCompany.setText(beginCompanyName);
		tvShareEndCity.setText(endCityName);
		tvShareEndCompany.setText(endCompanyName);
		tvShareTime.setText(packageDate);
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_share_title));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharePreviewActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 请求接口获得数据
		getSharePreViewContext();
	}

	/**
	 * @Description:请求分享的数据
	 * @Title:getShareContext
	 * @return:void
	 * @throws
	 * @Create: 2016-6-28 下午5:32:43
	 * @Author : mengwei
	 */
	private void getShareContext() {
		getShareContextRequest = new GetShareContextRequest(mContext, packageId);
		getShareContextRequest.setRequestId(GET_SHARE);
		httpPost(getShareContextRequest);
	}

	/**
	 * @Description:请求接口获得数据
	 * @Title:getSharePreViewContext
	 * @return:void
	 * @throws
	 * @Create: 2016-6-28 下午3:17:54
	 * @Author : mengwei
	 */
	private void getSharePreViewContext() {
		sharePreViewAdapter.notifyDataSetChanged();
		count = pageIndex * pageSize;
		pbShareBar.setVisibility(View.VISIBLE);
		ticketAllCurrentRequest = new TicketAllCurrentRequest(mContext,
				pageSize, pageIndex, "", packageId);
		ticketAllCurrentRequest.setRequestId(GET_LIEBIAO);
		httpPost(ticketAllCurrentRequest);
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
		case GET_LIEBIAO:
			isSuccess = response.isSuccess;
			message = response.message;
			int totalCount = response.totalCount;
			if (isSuccess) {
				ArrayList<TicketCurrentBean> sList = response.data;

				// packageId = sList.get(0).packageId;

				if (sList.size() == 0) {
					showToast(message);
					llShareEmpty.setVisibility(View.GONE);
					ivShareBlank.setVisibility(View.VISIBLE);
				} else {
					llShareEmpty.setVisibility(View.VISIBLE);
					ivShareBlank.setVisibility(View.GONE);
				}

				if (sList != null) {
					shareList.addAll(sList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				pbShareBar.setVisibility(View.GONE);
				lvShareView.onRefreshComplete();
				sharePreViewAdapter.notifyDataSetChanged();
				// 请求分享的数据
				getShareContext();

			}
			break;

		case GET_SHARE:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				shareContext = (Share) response.singleData;

			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void setListener() {
		btShareButton.setOnClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		shareList.clear();
		sharePreViewAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getSharePreViewContext();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getSharePreViewContext();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvShareView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onClick(View v) {
		if (shareContext != null) {
			ShareUtil.share(SharePreviewActivity.this,
					SharelistHelper.FROM_TYPE_GRAB_ORDER, shareContext);
		} else {
			showToast("无法获取分享内容");
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, String packageId,
			String beginCityName, String beginCompanyName, String endCityName,
			String endCompanyName, String packageDate) {
		Intent intent = new Intent(context, SharePreviewActivity.class);
		intent.putExtra("packageId", packageId);
		intent.putExtra("beginCityName", beginCityName);
		intent.putExtra("beginCompanyName", beginCompanyName);
		intent.putExtra("endCityName", endCityName);
		intent.putExtra("endCompanyName", endCompanyName);
		intent.putExtra("packageDate", packageDate);
		context.startActivity(intent);
	}

}
