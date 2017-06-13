package com.yunqi.clientandroid.employer.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.R.id;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.ChengYunAdapter;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJi;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJiBottom;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJiTop;
import com.yunqi.clientandroid.employer.entity.TongJiDaoChu;
import com.yunqi.clientandroid.employer.request.ChengYunTongJiBottomRequest;
import com.yunqi.clientandroid.employer.request.ChengYunTongJiRequest;
import com.yunqi.clientandroid.employer.request.ChengYunTongJiTopRequest;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.request.TongJiDaoChuRequest;
import com.yunqi.clientandroid.employer.util.DownManagerUtil;
import com.yunqi.clientandroid.employer.util.FileDownloadUtils;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.MarqueeTextView;

/**
 * @Description:承运统计页面
 * @ClassName: ChengYunTongJiActivity
 * @author: mengwei
 * @date: 2016-8-13 上午11:10:27
 * 
 */
public class ChengYunTongJiActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
	/* 界面控件对象 */
	private TextView tvBegin;
	private TextView tvBeginCity;
	private TextView tvEnd;
	private TextView tvEndCity;
	private TextView tvVehicleNumber;
	private TextView tvDaiZhiXing;
	private TextView tvDaiJieSuan;
	private TextView tvYiJieSuan;
	private PullToRefreshListView lvTongJiView;
	private TextView tvSumNumber;
	private TextView tvKuangFa;
	private TextView tvQianShou;
	private MarqueeTextView tvSumYunfei;
	private LinearLayout llTongJi;
	private RelativeLayout rlEmpty;
	private ProgressWheel pbBar;
	private TextView tvYeShu;
	private ListView lvListView;
	private Button btDaoChu;
	private TextView tvZaituzhong;
	private Button btTuBiao;

	/* 传递过来的数据 */
	private String packageId;
	private String startTime = null;
	private String endTime = null;
	private String vehicleNo = null;
	private int departMentId = -1;
	private int ticketStatus = -1;
	private String begin;
	private String beginCity;
	private String beginCounty;
	private String end;
	private String endCity;
	private String endCounty;
	private int todayCount;

	/* 请求类 */
	private ChengYunTongJiRequest chengYunTongJiRequest;
	private ChengYunTongJiTopRequest chengYunTongJiTopRequest;
	private ChengYunTongJiBottomRequest chengYunTongJiBottomRequest;
	private TongJiDaoChuRequest tongJiDaoChuRequest;

	/* 请求ID */
	private final int GET_CHENGYUN_TOP = 1;
	private final int GET_CHENGYUN_LIEBIAO = 2;
	private final int GET_CHENGYUN_BOTTOM = 3;
	private final int GET_CHENGYUN_DAOCHU = 4;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isloadingFinish = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	private ArrayList<ChengYunTongJi> chengYunList = new ArrayList<ChengYunTongJi>();
	private ChengYunAdapter chengYunAdapter;

	// 下载
	private DownloadManager downloadManager;
	private DownLoadCompleteReceiver receiver;
	private String name;
	private String filePath;

	// 是否需要刷新页面
	public static boolean isBack = false;

	/* 开始地址和结束地址 */
	private String beginAddress;
	private String endAddress;
	private PreManager mPreManager;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_chengyun_tongji;
	}

	@Override
	protected void initView() {
		// 初始化标题栏
		initActionBar();
		// 初始化控件对象
		initFindView();
		mPreManager = PreManager.instance(mContext);
	}

	/**
	 * @Description:初始化控件对象
	 * @Title:initFindView
	 * @return:void
	 */
	private void initFindView() {
		tvBegin = obtainView(R.id.tv_tongji_begin);
		tvBeginCity = obtainView(R.id.tv_tongji_begin_city);
		tvEnd = obtainView(R.id.tv_tongji_end);
		tvEndCity = obtainView(R.id.tv_tongji_end_city);
		// tvVehicleNumber = obtainView(R.id.tv_tongji_vehicle_number);
		tvZaituzhong = obtainView(R.id.tv_tongji_zaituzhong_number);
		tvDaiZhiXing = obtainView(R.id.tv_tongji_daizhixing_number);
		tvDaiJieSuan = obtainView(R.id.tv_tongji_daijiesuan_number);
		tvYiJieSuan = obtainView(R.id.tv_tongji_yijiesuan_number);
		lvTongJiView = obtainView(R.id.lv_tongji_view);
		tvSumNumber = obtainView(R.id.tv_tongji_vehicle_sumnumber);
		tvKuangFa = obtainView(R.id.tv_tongji_kuangfa);
		tvQianShou = obtainView(R.id.tv_tongji_qianshou);
		tvSumYunfei = obtainView(R.id.tv_tongji_sunyunfei);
		llTongJi = obtainView(R.id.ll_chengyun_tongji);
		rlEmpty = obtainView(R.id.rl_tongji_empty);
		pbBar = obtainView(R.id.pb_tongji_bar);
		tvYeShu = obtainView(R.id.tv_tongji_yeshu);
		btDaoChu = obtainView(R.id.bt_tongji_daochu);
		btTuBiao = obtainView(id.bt_tongji_tubiao);

		tvYeShu.setText(Html
				.fromHtml("<font color='#606060'>第</font><font color='#00bbff'>"
						+ pageIndex
						+ "</font><font color='#606060'>页（每页10个运单）</font>"));

		lvTongJiView.setMode(PullToRefreshBase.Mode.BOTH);
		lvTongJiView.setOnRefreshListener(this);
		// initPullToRefreshView();
	}

	// private void initPullToRefreshView(){
	// lvTongJiView.setMode(PullToRefreshBase.Mode.BOTH);
	// lvTongJiView.getLoadingLayoutProxy(false, true)
	// .setPullLabel(getString(R.string.pull_to_loadmore));
	// lvTongJiView.getLoadingLayoutProxy(false, true)
	// .setRefreshingLabel(getString(R.string.pull_to_loading));
	// lvTongJiView.getLoadingLayoutProxy(false, true)
	// .setReleaseLabel(getString(R.string.pull_to_release));
	// lvListView = lvTongJiView.getRefreshableView();
	// lvListView.setDivider(new ColorDrawable(getResources().getColor(
	// R.color.carlistBackground)));
	// lvListView.setDividerHeight(25);
	// lvListView.setSelector(android.R.color.transparent);//
	// 隐藏listView默认的selector
	// lvTongJiView.setOnRefreshListener(this);
	// }

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_tongji_title));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
//		if (startTime == null || startTime.equals("946656000") || endTime == null || endTime.equals("32503651200") ||
//				StringUtils.isStrNotNull(vehicleNo) || ticketStatus == 0 || ticketStatus == -1 ||
//				departMentId ==0 || departMentId == -1){
//			setActionBarRight(true, 0,getString(R.string.employer_activity_tongji_right));
//		} else {
//			setActionBarRight(true, R.drawable.chengyuntongji_sousuo,"");
//		}

		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				ChengYunTongJiActivity.this.finish();
			}
		});

		setOnActionBarRightClickListener(true, new OnClickListener() {
			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("waybill_management_click_search");

				Log.e("TAG", "--------setOnActionBarRightClickListener--------");
				Log.e("TAG", "--------packageId--------" + packageId);
				Log.e("TAG", "--------startTime--------" + startTime);
				Log.e("TAG", "--------endTime--------" + endTime);
				Log.e("TAG", "--------vehicleNo--------" + vehicleNo);
				Log.e("TAG", "--------departMentId--------" + departMentId);
				Log.e("TAG", "--------ticketStatus--------" + ticketStatus);
				L.e("--------ticketStatus--------" + ticketStatus);
				// 进入搜索页面
				SearchOrderActivity.invoke(ChengYunTongJiActivity.this,
						packageId, startTime, endTime, vehicleNo, ticketStatus,
						departMentId);
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);

		packageId = getIntent().getStringExtra("packageId");
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");
		vehicleNo = getIntent().getStringExtra("vehicleNo");
		departMentId = getIntent().getIntExtra("departMentId", -1);
		ticketStatus = getIntent().getIntExtra("ticketStatus", -1);
		todayCount = getIntent().getIntExtra("todayCount", 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		packageId = getIntent().getStringExtra("packageId");
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");
		vehicleNo = getIntent().getStringExtra("vehicleNo");
		departMentId = getIntent().getIntExtra("departMentId", -1);
		ticketStatus = getIntent().getIntExtra("ticketStatus", -1);
		todayCount = getIntent().getIntExtra("todayCount", 0);

		Log.d("TAG", "--------packageId--------" + packageId);
		Log.d("TAG", "--------startTime--------" + startTime);
		Log.d("TAG", "--------endTime--------" + endTime);
		Log.d("TAG", "--------vehicleNo--------" + vehicleNo);
		Log.d("TAG", "--------departMentId--------" + departMentId);
		Log.d("TAG", "--------ticketStatus--------" + ticketStatus);
		Log.d("TAG", "--------isBack--------" + isBack);

		if (isBack) {
//			pbBar.setVisibility(View.VISIBLE);
//			llTongJi.setVisibility(View.GONE);
			showProgressDialog("正在搜索，请稍候...");
			chengYunList.clear();

			getBottomRequest();

			setEnabled(true);

			isBack = false;
		}

		if ((startTime == null || startTime.equals("946656000")) && (endTime == null || endTime.equals("32503651200")) &&
				!StringUtils.isStrNotNull(vehicleNo) && (ticketStatus == 0 || ticketStatus == -1) &&
				(departMentId ==0 || departMentId == -1)){
			setActionBarRight(true, R.drawable.chengyuntongji_sousuo_bai,"");
		} else {
			setActionBarRight(true, R.drawable.chengyuntongji_sousuo,"");
		}

	}

	private void getLeBiaoRequest() {
		chengYunAdapter.notifyDataSetChanged();
		count = pageIndex * pageSize;
		// 请求列表数据
		chengYunTongJiRequest = new ChengYunTongJiRequest(mContext, pageSize,
				pageIndex, packageId, startTime, endTime, vehicleNo,
				departMentId, ticketStatus, todayCount);
		chengYunTongJiRequest.setRequestId(GET_CHENGYUN_LIEBIAO);
		httpPost(chengYunTongJiRequest);
	}

	/**
	 * @Description:进行头部请求数据
	 * @Title:getShuJuRequest
	 */
	private void getShuJuRequest() {
		// 请求头部数据
		chengYunTongJiTopRequest = new ChengYunTongJiTopRequest(mContext,
				packageId);
		chengYunTongJiTopRequest.setRequestId(GET_CHENGYUN_TOP);
		httpPost(chengYunTongJiTopRequest);

	}

	/**
	 * 请求底部数据
	 */
	private void getBottomRequest() {
		// 请求底部数据
		chengYunTongJiBottomRequest = new ChengYunTongJiBottomRequest(mContext,
				packageId, startTime, endTime, vehicleNo, departMentId,
				ticketStatus, todayCount);
		chengYunTongJiBottomRequest.setRequestId(GET_CHENGYUN_BOTTOM);
		httpPost(chengYunTongJiBottomRequest);
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
		case GET_CHENGYUN_TOP:// 头部数据
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ChengYunTongJiTop chengTop = (ChengYunTongJiTop) response.singleData;

				int beforeExecuteCount = chengTop.BeforeExecuteCount; // 待执行数量
				int beforeSettlementCount = chengTop.BeforeSettlementCount; // 待结算数量
				int settledCount = chengTop.SettledCount; // 已结算数量
				int VehicleCount = chengTop.VehicleCount;
				String beginCity = chengTop.BeginCity;
				String beginRegion = chengTop.BeginRegion;
				beginAddress = chengTop.BeginAddress;
				String endCity = chengTop.EndCity;
				String endRegion = chengTop.EndRegion;
				endAddress = chengTop.EndAddress;
				String mCarRange = chengTop.CarRange;
				String mOnTheWayCount = chengTop.OnTheWayCount;

				if (StringUtils.isStrNotNull(beginAddress)) {
					tvBegin.setText(beginAddress);
				}

				if (StringUtils.isStrNotNull(beginCity)
						&& StringUtils.isStrNotNull(beginRegion)) {
					tvBeginCity.setText(beginCity + "·" + beginRegion);
				}

				if (StringUtils.isStrNotNull(mOnTheWayCount)) {
					tvZaituzhong.setText(mOnTheWayCount);
				} else {
					tvZaituzhong.setText("0");
				}

				if (StringUtils.isStrNotNull(endAddress)) {
					tvEnd.setText(endAddress);
				}

				if (StringUtils.isStrNotNull(endCity)
						&& StringUtils.isStrNotNull(endRegion)) {
					tvEndCity.setText(endCity + "·" + endRegion);
				}

				// if (StringUtils.isStrNotNull(mCarRange)) {
				// tvVehicleNumber.setText(mCarRange + "");
				// }

				getBottomRequest();

				tvDaiZhiXing.setText(beforeExecuteCount + "");
				tvDaiJieSuan.setText(beforeSettlementCount + "");
				tvYiJieSuan.setText(settledCount + "");
			}
			break;
		case GET_CHENGYUN_BOTTOM:// 底部数据
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ChengYunTongJiBottom chengBottom = (ChengYunTongJiBottom) response.singleData;

				int ticketCount = chengBottom.TicketCount;
				String initWeighSum = chengBottom.InitWeighSum;
				String reachWeighSum = chengBottom.ReachWeighSum;
				String shouldPayMoneySum = chengBottom.ShouldPayMoneySum;
				String truePayMoneySum = chengBottom.TruePayMoneySum;
				String intransitMoney = chengBottom.IntransitMoney;
				String waitPayMoney = chengBottom.WaitPayMoney;

				tvSumNumber.setText(Html.fromHtml("车次<font color='#ff4400'>"
						+ ticketCount + "</font>车"));
				if (StringUtils.isStrNotNull(initWeighSum)) {
					tvKuangFa.setText(Html.fromHtml("矿发<font color='#ff4400'>"
							+ initWeighSum + "</font>吨"));
				}
				if (StringUtils.isStrNotNull(reachWeighSum)) {
					tvQianShou.setText(Html.fromHtml("签收<font color='#ff4400'>"
							+ reachWeighSum + "</font>吨"));
				}

				Log.d("TAG", "----------在途运费(元)：-------------" + intransitMoney);
				Log.d("TAG", "----------待结运费(元)：-------------" + waitPayMoney);
				Log.d("TAG", "----------已结运费(元)：-------------"
						+ truePayMoneySum);

				// 跑马灯要加载的数据
				String sumYunFei = "<font color='#2299EE'>在途运费（元）：</font>"
						+ "<font color='#ff4400'>"
						+ intransitMoney
						+ "</font>"
						+ "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"
						+ "<font color='#2299EE'>待结运费（元）：</font><font color='#ff4400'>"
						+ waitPayMoney
						+ "</font>"
						+ "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"
						+ "<font color='#2299EE'>已结运费（元）：</font><font color='#ff4400'>"
						+ truePayMoneySum + "</font>"
						+ "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</sapn>";

				if (StringUtils.isStrNotNull(sumYunFei)) {
					tvSumYunfei.setText(Html.fromHtml(sumYunFei));
				}

				getLeBiaoRequest();

			}
			break;
		case GET_CHENGYUN_LIEBIAO:// 列表数据
			isSuccess = response.isSuccess;
			message = response.message;
			isloadingFinish = false;

			Log.e("TAG", "-----response.data------" + response.data);

			if (response.data == null) {
				showToast(message);
				rlEmpty.setVisibility(View.VISIBLE);
				lvTongJiView.setVisibility(View.GONE);
			} else {
				rlEmpty.setVisibility(View.GONE);
				lvTongJiView.setVisibility(View.VISIBLE);
			}
			if (isSuccess) {
				int totalCount = response.totalCount;
				ArrayList<ChengYunTongJi> tongjiList = response.data;

				if (tongjiList.size() == 0) {
					showToast(message);
					rlEmpty.setVisibility(View.VISIBLE);
				} else {
					rlEmpty.setVisibility(View.GONE);
				}

				if (tongjiList != null) {
					chengYunList.addAll(tongjiList);
				}

				if (totalCount <= chengYunList.size()) {
					isloadingFinish = true;
				}
			}

			chengYunAdapter.notifyDataSetChanged();
//			pbBar.setVisibility(View.GONE);
			lvTongJiView.onRefreshComplete();
//			llTongJi.setVisibility(View.VISIBLE);
			hideProgressDialog();
			break;

		case GET_CHENGYUN_DAOCHU:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				TongJiDaoChu tongJi = (TongJiDaoChu) response.singleData;

				filePath = tongJi.filePath;
				String beginTime = tongJi.beginTime;
				String endTime = tongJi.endTime;

				Log.d("TAG", "--------filePath-----------" + filePath);

				name = filePath.substring(filePath.lastIndexOf("/") + 1);

				if (StringUtils.isStrNotNull(filePath)) {
					// 下载
					Log.d("TAG", "--------进入-----------" + filePath);
					DownManagerUtil.downManagerEx(mContext, downloadManager,
							filePath);
//					FileDownloadUtils fileDownloadUtils = new FileDownloadUtils(filePath, mContext,btDaoChu);
//					fileDownloadUtils.downloadFile();
////					 FileDownloadUtils.getInstance(filePath, mContext).downloadFile();
				}

			} else {
				showToast(message);
				setEnabled(true);
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
		lvTongJiView.onRefreshComplete();
		pbBar.setVisibility(View.GONE);
		switch (requestId) {
		case GET_CHENGYUN_LIEBIAO:
			// getLeBiaoRequest();
			break;

		case GET_CHENGYUN_TOP:
			getShuJuRequest();
			break;

		case GET_CHENGYUN_DAOCHU:
			setEnabled(true);
			break;

		default:
			break;
		}

	}

	@Override
	protected void initData() {
		chengYunAdapter = new ChengYunAdapter(chengYunList, mContext);
		lvTongJiView.setAdapter(chengYunAdapter);

		packageId = getIntent().getStringExtra("packageId");
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");
		vehicleNo = getIntent().getStringExtra("vehicleNo");
		departMentId = getIntent().getIntExtra("departMentId", -1);
		ticketStatus = getIntent().getIntExtra("ticketStatus", -1);

//		pbBar.setVisibility(View.VISIBLE);
//		llTongJi.setVisibility(View.GONE);
		showProgressDialog("请稍候...");

		if (vehicleNo == null || TextUtils.isEmpty(vehicleNo)) {
			vehicleNo = "";
		}

		// 进行请求数据
		getShuJuRequest();

		downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		receiver = new DownLoadCompleteReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	protected void setListener() {
		btDaoChu.setOnClickListener(this);
		btTuBiao.setOnClickListener(this);
	}

	/**
	 * @Description:
	 * @Title:invoke
	 * @param context
	 * @param packageId
	 *            包ID
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param vehicleNo
	 *            车牌号
	 * @param departMentId
	 *            信息部ID
	 * @param ticketStatus
	 *            订单状态
	 * @return:void
	 * @throws
	 * @Create: 2016-8-13 下午1:44:52
	 * @Author : chengtao
	 */
	public static void invoke(Context context, String packageId,
			String startTime, String endTime, String vehicleNo,
			int departMentId, int ticketStatus, int todayCount) {
		Intent intent = new Intent(context, ChengYunTongJiActivity.class);
		intent.putExtra("packageId", packageId);
		intent.putExtra("startTime", startTime);
		intent.putExtra("endTime", endTime);
		intent.putExtra("vehicleNo", vehicleNo);
		intent.putExtra("departMentId", departMentId);
		intent.putExtra("ticketStatus", ticketStatus);
		intent.putExtra("todayCount", todayCount);
		context.startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		chengYunList.clear();
		chengYunAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getLeBiaoRequest();
		tvYeShu.setText(Html
				.fromHtml("<font color='#2299EE'>第</font><font color='#ff4400'>"
						+ pageIndex
						+ "</font><font color='#2299EE'>页（每页10个运单）</font>"));
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			pageIndex++;
			getLeBiaoRequest();
			tvYeShu.setText(Html
					.fromHtml("<font color='#2299EE'>第</font><font color='#ff4400'>"
							+ pageIndex
							+ "</font><font color='#2299EE'>页（每页10个运单）</font>"));
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvTongJiView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 再按一次退出程序
			ChengYunTongJiActivity.this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_tongji_daochu:
			//友盟统计首页
			mUmeng.setCalculateEvents("waybill_management_click_export_xls");

			// 请求电子报表地址
			tongJiDaoChuRequest = new TongJiDaoChuRequest(mContext, packageId,
					startTime, endTime, vehicleNo, departMentId, ticketStatus, todayCount);
			tongJiDaoChuRequest.setRequestId(GET_CHENGYUN_DAOCHU);
			httpPost(tongJiDaoChuRequest);

			setEnabled(false);
			break;

			case id.bt_tongji_tubiao://进入图表展示页面
				//友盟统计首页
				mUmeng.setCalculateEvents("waybill_management_click_chart_display");

//				WebTubiaoActivity.invoke(mContext,packageId);
				String cookies = mPreManager.getToken();
				Intent intent = new Intent(mContext,EmployerWebviewActivity.class);
				intent.putExtra("Url", HostPkgUtil.getApiHost()+"Owner/ChartOverview?packageId="+packageId+"&token="+cookies);
				intent.putExtra("Title",getString(R.string.employer_activity_web_title));
				startActivity(intent);
				break;

		default:
			break;
		}
	}

	private void setEnabled(boolean enabled) {
		if (enabled) {
			btDaoChu.setEnabled(enabled);
			btDaoChu.setBackgroundResource(R.drawable.sendbao_btn_background);
		} else {
			btDaoChu.setEnabled(enabled);
			btDaoChu.setBackgroundResource(R.drawable.btn_zhihui);
		}
	}

	private class DownLoadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
//			String name = filePath.substring(filePath.lastIndexOf("/") + 1);
//			L.e("---------------"+name);
//			File file = new File(Environment.DIRECTORY_DOWNLOADS+"/"+name);
//			OpenFileUrl(file);
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long id = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				showToast("任务下载已完成，请到手机文档查看!");
				setEnabled(true);
			} else if (intent.getAction().equals(
					DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
				// long id =
				// intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				// DownloadManager.Query query = new DownloadManager.Query();
				// query.setFilterById(id);
				// Cursor c = downloadManager.query(query);
				// if (c.moveToFirst()) {
				// int status =
				// c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
				// switch (status) {
				// case DownloadManager.STATUS_SUCCESSFUL:
				// //如果文件名不为空，说明已经存在了，然后获取uri，进行安装
				// File path = new
				// File(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)));
				// if(!path.exists()){
				// return;
				// }
				//
				// OpenFile(path);

				showxls();
				// break;
				//
				// default:
				// break;
				// }
				// }
			}
		}
	}

	public void showxls() {
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Download/" + name);// name here is the name of any
											// string you want to pass to the
											// method
			if (!file.isDirectory())
				file.mkdir();
			Intent testIntent = new Intent("com.adobe.reader");
			testIntent.setType("application/xls");
			testIntent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(file);
			testIntent.setDataAndType(uri, "application/xls");
			startActivity(testIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private NotificationManager manager;
	private NotificationCompat.Builder builder;

	private void OpenFile(File file) {
		MimeTypeMap myMime = MimeTypeMap.getSingleton();
		Intent newIntent = new Intent(Intent.ACTION_VIEW);
		String mimeType = myMime.getMimeTypeFromExtension(fileExt(file
				.getName()));
		newIntent.setDataAndType(Uri.fromFile(file), mimeType);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				ChengYunTongJiActivity.this, 0, newIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent).setAutoCancel(true);
		manager.notify(1, builder.build());
	}

	private String fileExt(String url) {
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf(".") + 1);
			if (ext.contains("%")) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.contains("/")) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
