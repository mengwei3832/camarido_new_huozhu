package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.HistoryDingDanAdapter;
import com.yunqi.clientandroid.employer.entity.HistoryDingDan;
import com.yunqi.clientandroid.employer.request.GetHistoryDingDanRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @Description:历史订单界面
 * @ClassName: HistoryYunDanActicity
 * @author: chengtao
 * @date: Aug 30, 2016 5:38:29 PM
 * 
 */
public class HistoryDingDanActicity extends BaseActivity implements
		OnRefreshListener2<ListView>, OnItemClickListener {
	// -----------------控件---------------
	private PullToRefreshListView lvHistory;
	private ProgressWheel progressBar;
	private ImageView ivBlank;

	// -----------------变量---------------
	private HistoryDingDanAdapter adapter;
	private List<HistoryDingDan> historyYunDanList;
	private int pageSize = 10;
	private int pageIndex = 1;
	private boolean isFinish = false;
	private int count;

	// -----------------请求---------------
	private GetHistoryDingDanRequest historyYunDanRequest;
	private static final int HISTORY_YUN_DAN_REQUEST = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_history_ding_dan;
	}

	@Override
	protected void initView() {
		// 获取控件
		lvHistory = obtainView(R.id.lv_history_ding_dan);
		progressBar = obtainView(R.id.progress_history_dingdan);
		ivBlank = obtainView(R.id.iv_history_dingdan);
		
		lvHistory.setMode(Mode.BOTH);
		// 初始化ActionBar
		initActionBar();
		// 初始化变量
		historyYunDanList = new ArrayList<HistoryDingDan>();
		adapter = new HistoryDingDanAdapter(mContext, historyYunDanList);
		lvHistory.setAdapter(adapter);
	}
	
	private void isShow(boolean isShow){
		if (isShow) {
			lvHistory.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			lvHistory.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: Aug 31, 2016 10:45:58 AM
	 * @Author : chengtao
	 */
	private void initActionBar() {
		setActionBarTitle("历史订单");
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
		isShow(true);
		getHistoryData();
	}

	/**
	 * 
	 * @Description:获取历史订单列表
	 * @Title:getHistoryData
	 * @return:void
	 * @throws
	 * @Create: Aug 30, 2016 7:56:34 PM
	 * @Author : chengtao
	 */
	private void getHistoryData() {
		count = pageIndex * pageSize;
		historyYunDanRequest = new GetHistoryDingDanRequest(mContext,
				pageIndex, pageSize);
		historyYunDanRequest.setRequestId(HISTORY_YUN_DAN_REQUEST);
		httpPost(historyYunDanRequest);
	}

	@Override
	protected void setListener() {
		lvHistory.setOnRefreshListener(this);
		lvHistory.setOnItemClickListener(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		int totalCount = response.totalCount;
		switch (requestId) {
		case HISTORY_YUN_DAN_REQUEST:// 历史运单
			if (isSuccess) {
				List<HistoryDingDan> list = response.data;
				if (list != null && list.size() > 0) {
					historyYunDanList.addAll(list);
					adapter.notifyDataSetChanged();
				}
				// 判断是否加载完成
				if (totalCount <= count) {
					isFinish = true;
				}
				
				if (list.size() == 0) {
					ivBlank.setVisibility(View.VISIBLE);
				} else {
					ivBlank.setVisibility(View.GONE);
				}
				
				lvHistory.onRefreshComplete();
			} else {
				showToast(message);
			}
			
			isShow(false);
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getResources().getString(R.string.net_error_toast));
		switch (requestId) {
		case HISTORY_YUN_DAN_REQUEST:
			lvHistory.onRefreshComplete();
			break;

		default:
			break;
		}
		
		isShow(false);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 初始化接口参数
		pageIndex = 1;
		isFinish = false;
		historyYunDanList.clear();
		// 发送请求
		getHistoryData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (isFinish) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					showToast("已经是最后一页了");
					lvHistory.onRefreshComplete();
				}
			}, 100);
		} else {// 上拉加载
			pageIndex++;
			// 发送请求
			getHistoryData();
		}
	}

	/**
	 * 
	 * @Description:本界跳转
	 * @Title:invoke
	 * @param context
	 *            上下文
	 * @return:void
	 * @throws
	 * @Create: Aug 30, 2016 5:42:33 PM
	 * @Author : chengtao
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, HistoryDingDanActicity.class);
		context.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String packageId = historyYunDanList.get(position - 1).Id + "";
		if (StringUtils.isStrNotNull(packageId)) {
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_history_item");
			BaoLieBiaoDetailActivity.invoke(mContext, packageId, 1);
		} else {
			showToast("此订单有问题");
		}
	}
}
