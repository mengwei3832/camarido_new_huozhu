package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.StatisticalDataAdapter;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.request.GetBiaoLieBiaoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:统计数据页面
 * @ClassName: StatisticalDataActivity
 * @author: mengwei
 * @date: 2016-6-28 上午11:05:54
 * 
 */
public class StatisticalDataActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	/* 界面控件对象 */
	private LinearLayout llStatistocalEmpty;
	private PullToRefreshListView lvStatistocalView;
	private ProgressBar pbStatistocal;
	private ImageView ivStatistocal;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	/* 保存数据的集合 */
	private ArrayList<GetBiaoLieBiao> dataList = new ArrayList<GetBiaoLieBiao>();
	private StatisticalDataAdapter statisticalDataAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_statistical_data;
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
	 * @Create: 2016-6-28 上午11:30:35
	 * @Author : mengwei
	 */
	private void initFindView() {
		initActionBar();

		llStatistocalEmpty = obtainView(R.id.ll_statistocal_empty);
		lvStatistocalView = obtainView(R.id.lv_statistical_data);
		pbStatistocal = obtainView(R.id.pb_statistical);
		ivStatistocal = obtainView(R.id.iv_statistical);

		lvStatistocalView.setMode(PullToRefreshBase.Mode.BOTH);
		lvStatistocalView.setOnRefreshListener(this);

		statisticalDataAdapter = new StatisticalDataAdapter(dataList, mContext);
		lvStatistocalView.setAdapter(statisticalDataAdapter);
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_statistical_title));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatisticalDataActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 请求接口
		getStatisticalData();
	}

	/**
	 * @Description:请求数据接口
	 * @Title:getStatisticalData
	 * @return:void
	 * @throws
	 * @Create: 2016-6-28 下午1:31:25
	 * @Author : mengwei
	 */
	private void getStatisticalData() {
		statisticalDataAdapter.notifyDataSetChanged();
		pbStatistocal.setVisibility(View.VISIBLE);
		count = pageIndex * pageSize;
		httpPost(new GetBiaoLieBiaoRequest(mContext, pageSize, pageIndex));
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
		int totalCount = response.totalCount;

		if (isSuccess) {
			ArrayList<GetBiaoLieBiao> list = response.data;

			if (list.size() == 0) {
				showToast(message);
				llStatistocalEmpty.setVisibility(View.GONE);
				ivStatistocal.setVisibility(View.VISIBLE);
			} else {
				llStatistocalEmpty.setVisibility(View.VISIBLE);
				ivStatistocal.setVisibility(View.GONE);
			}

			if (list != null) {
				dataList.addAll(list);
			}

			if (totalCount <= count) {
				isEnd = true;
			}
			pbStatistocal.setVisibility(View.GONE);
			pbStatistocal.setVisibility(View.GONE);
			lvStatistocalView.onRefreshComplete();
			statisticalDataAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void setListener() {
		lvStatistocalView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 进入分享预览界面
		GetBiaoLieBiao getBiaoLieBiao = dataList.get(position - 1);
		String packageId = getBiaoLieBiao.Id;
		String beginCityName = getBiaoLieBiao.PackageBeginCityText;
		String beginCompanyName = getBiaoLieBiao.PackageBeginName;
		String endCityName = getBiaoLieBiao.PackageEndCityText;
		String endCompanyName = getBiaoLieBiao.PackageEndName;
		String packageDate = StringUtils
				.formatModify(getBiaoLieBiao.PackageStartTime);
		Log.d("TAG", "---beginCityName---" + beginCityName);
		Log.d("TAG", "---beginCompanyName---" + beginCompanyName);
		Log.d("TAG", "---endCityName---" + endCityName);
		Log.d("TAG", "---endCompanyName---" + endCompanyName);
		Log.d("TAG", "---packageDate---" + packageDate);
		SharePreviewActivity.invoke(mContext, packageId, beginCityName,
				beginCompanyName, endCityName, endCompanyName, packageDate);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		dataList.clear();
		statisticalDataAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getStatisticalData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getStatisticalData();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvStatistocalView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, StatisticalDataActivity.class);
		context.startActivity(intent);
	}

}
