package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.QuoteYijiaAdapter;
import com.yunqi.clientandroid.employer.entity.YiJiaHistory;
import com.yunqi.clientandroid.employer.request.SetYiJiaPrice;
import com.yunqi.clientandroid.employer.request.YiJiaHistoryRequest;
import com.yunqi.clientandroid.employer.util.SoftHideKeyBoardUtil;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:报价单下的议价页面
 * @ClassName: QuoteYijiaActivity
 * @author: mengwei
 * @date: 2016-9-22 下午2:47:35
 * 
 */
public class QuoteYijiaActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
	/* 控件对象 */
	private PullToRefreshListView lvYijiaView;
	private EditText etPrice;
	private EditText etVehicle;
	private Button btYijia;
	private View pbBar;
	private LinearLayout llBottomInput;
	private LinearLayout llTopView;

	// 适配器
	private ArrayList<YiJiaHistory> yijiaList = new ArrayList<YiJiaHistory>();
	private QuoteYijiaAdapter quoteYijiaAdapter;

	private String packageId;
	private String infoId;
	private String departId;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	/* 请求类 */
	private YiJiaHistoryRequest yiJiaHistoryRequest;
	private SetYiJiaPrice yiJiaRequest;

	/* 请求Id */
	private final int YIJIA_HISTORY = 1;
	private final int YIJIA_PRICE = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_quote_yijia;
	}

	@Override
	protected void initView() {
		initActionBar();
		// 获取报价单ID
		packageId = getIntent().getStringExtra("packageId");
		infoId = getIntent().getStringExtra("infoId");
		departId = getIntent().getStringExtra("departId");

		Log.e("TAG", "包Id----------" + packageId);

		lvYijiaView = obtainView(R.id.lv_yijia_view);
		etPrice = obtainView(R.id.et_quote_yijia_money);
		etVehicle = obtainView(R.id.et_quote_yijia_car);
		btYijia = obtainView(R.id.bt_quote_yijia);
		pbBar = obtainView(R.id.pb_quote_yijia);
		llBottomInput = obtainView(R.id.ll_yijia_input);
		llTopView = obtainView(R.id.ll_yijia_history);

//		SoftHideKeyBoardUtil.assistActivity(QuoteYijiaActivity.this);

		lvYijiaView.setMode(PullToRefreshBase.Mode.BOTH);
		lvYijiaView.setOnRefreshListener(this);

		quoteYijiaAdapter = new QuoteYijiaAdapter(mContext, yijiaList);
		lvYijiaView.setAdapter(quoteYijiaAdapter);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_quote_yijia));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(false, 0, null);

		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				QuoteYijiaActivity.this.finish();
			}
		});

		setOnActionBarRightClickListener(false, null);

	}

	@Override
	protected void initData() {
		pbBar.setVisibility(View.VISIBLE);
		llTopView.setVisibility(View.GONE);
		llBottomInput.setVisibility(View.GONE);
		// 请求历史数据
		yiJiaHistoryCall();
	}

	/**
	 * 议价历史列表的请求
	 */
	private void yiJiaHistoryCall() {
		count = pageIndex * pageSize;
		yiJiaHistoryRequest = new YiJiaHistoryRequest(mContext, packageId,
				departId, pageIndex, pageSize);
		yiJiaHistoryRequest.setRequestId(YIJIA_HISTORY);
		httpPost(yiJiaHistoryRequest);
	}

	/**
	 * 议价的请求
	 */
	private void yiJiaCall(String price, String vehicleCount) {
		yiJiaRequest = new SetYiJiaPrice(mContext, infoId, price, vehicleCount);
		yiJiaRequest.setRequestId(YIJIA_PRICE);
		httpPost(yiJiaRequest);
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
		case YIJIA_PRICE:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);
				finish();
			} else {
				showToast(message);
			}
//			llTopView.setVisibility(View.VISIBLE);
//			llBottomInput.setVisibility(View.VISIBLE);
//			pbBar.setVisibility(View.GONE);
			hideProgressDialog();
			break;
		case YIJIA_HISTORY:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;
			if (isSuccess) {
				ArrayList<YiJiaHistory> yiList = response.data;

				if (yiList != null) {
					yijiaList.addAll(yiList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

			} else {
				showToast(message);
			}
			llTopView.setVisibility(View.VISIBLE);
			llBottomInput.setVisibility(View.VISIBLE);
			pbBar.setVisibility(View.GONE);
			quoteYijiaAdapter.notifyDataSetChanged();
			lvYijiaView.onRefreshComplete();
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		switch (requestId) {
		case YIJIA_PRICE:
//			llTopView.setVisibility(View.VISIBLE);
//			llBottomInput.setVisibility(View.VISIBLE);
//			pbBar.setVisibility(View.GONE);
			hideProgressDialog();
			break;
		case YIJIA_HISTORY:
			llTopView.setVisibility(View.VISIBLE);
			llBottomInput.setVisibility(View.VISIBLE);
			lvYijiaView.onRefreshComplete();
			pbBar.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void setListener() {
		btYijia.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_quote_yijia:// 点击议价按钮
			// 获取价格、车数
			String price = etPrice.getText().toString().trim();
			String vehicle = etVehicle.getText().toString().trim();

			if (!StringUtils.isStrNotNull(price)) {
				showToast("请输入金额");
				return;
			}
			if (!StringUtils.isStrNotNull(vehicle)) {
				showToast("请输入车数");
				return;
			}

			double mPrice = Double.valueOf(price);
			int mVehicle = Integer.valueOf(vehicle);

			if (mPrice == 0) {
				showToast("金额不可为0");
				return;
			}
			if (mVehicle == 0) {
				showToast("车数不可为0");
				return;
			}

			//友盟统计首页
			mUmeng.setCalculateEvents("quote_click_item_bargain_finish");

			// 调用议价的接口
			yiJiaCall(price, vehicle);
//			llTopView.setVisibility(View.GONE);
//			llBottomInput.setVisibility(View.GONE);
//			pbBar.setVisibility(View.VISIBLE);
			showProgressDialog("正在提交，请稍候...");
			break;

		default:
			break;
		}
	}

	/**
	 * @Description:本界面跳转方法
	 * @Title:invoke
	 * @param context
	 * @return:void
	 * @throws
	 * @Create: 2016-9-22 下午3:32:42
	 * @Author : chengtao
	 */
	public static void invoke(Context context, String packageId, String infoId,
			String departId) {
		Intent intent = new Intent(context, QuoteYijiaActivity.class);
		intent.putExtra("packageId", packageId);
		intent.putExtra("infoId", infoId);
		intent.putExtra("departId", departId);
		context.startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		yijiaList.clear();
		quoteYijiaAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		yiJiaHistoryCall();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			yiJiaHistoryCall();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvYijiaView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

}
