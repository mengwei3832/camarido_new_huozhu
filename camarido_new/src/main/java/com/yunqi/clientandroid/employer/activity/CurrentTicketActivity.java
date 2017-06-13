package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.CurrentTicketAdapter;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.employer.fragment.CurrentTicketFragment;
import com.yunqi.clientandroid.employer.request.PressCarRequest;
import com.yunqi.clientandroid.employer.request.TicketAllCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketCheckCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketHistoryCurrentRequest;
import com.yunqi.clientandroid.employer.request.TicketZhiXingCurrentRequest;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PayTimeUtils;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:class 当前运单列表界面
 * @ClassName: CurrentTicketActivity
 * @author: mengwei
 * @date: 2016-6-12 上午9:08:46
 * 
 */
public class CurrentTicketActivity extends BaseActivity implements
		View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>,
		OnItemClickListener {
	/* 界面控件对象 */
	private RelativeLayout rlAll;
	private RelativeLayout rlCurrentTicket;
	private TextView tvCurrentBianHao;
	private LinearLayout llCurrentChoose;
	private ImageView ivCurrentImg;
	private ImageView ivCurrentDelete;
	private RelativeLayout llCurrentSearch;
	private TextView tvCurrentStatusText;
	private LinearLayout llCurrentBianhao;
	private PullToRefreshListView lvCurrentListView;
	private ProgressWheel pbCurrentProgress;
	private ImageView ivCurrentBlank;
	private TextView tvBtSearch;
	private EditText etSearch;
	private LinearLayout llCurrentEmpty;

	/* 列表种类弹窗 */
	private PopupWindow mStatusChooseWindow;

	/* 发包方状态选择SP */
	private SpManager mSpManager;

	private CurrentTicketFragment mCurrentTicketFragment;

	/* 传递过来的数据 */
	private String packageCode;
	private String packageId;
	private int emptyId;
	private int checkId = -1;
	private String infoId;

	/* 列表适配 */
	private ArrayList<TicketCurrentBean> performListItem = new ArrayList<TicketCurrentBean>();
	private CurrentTicketAdapter mCurrentTicketAdapter;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	/* 请求类 */
	private TicketAllCurrentRequest mTicketAllCurrentRequest;
	private TicketZhiXingCurrentRequest mTicketZhiXingCurrentRequest;
	private TicketCheckCurrentRequest mTicketCheckCurrentRequest;
	private TicketHistoryCurrentRequest mTicketHistoryCurrentRequest;
	private PressCarRequest pressCarRequest;

	/* 请求ID */
	private final int GET_ALL_TICKET = 1;
	private final int GET_ZHIXING_TICKET = 2;
	private final int GET_CHECK_TICKET = 3;
	private final int GET_HISTORY_TICKET = 4;

	private int finishId = 0;

	// 是否需要刷新页面
	public static boolean isBack = false;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_current_ticket;
	}

	@Override
	protected void initView() {
		/* 初始化控件对象 */
		initFindView();

		/* 初始化列表种类弹窗 */
		createChooseStatusWindow();

		/* 适配器 */
		mCurrentTicketAdapter = new CurrentTicketAdapter(performListItem,
				CurrentTicketActivity.this);
		lvCurrentListView.setAdapter(mCurrentTicketAdapter);
	}

	/**
	 * 初始化列表种类弹窗
	 */
	private void createChooseStatusWindow() {
		View popupView = getLayoutInflater().inflate(
				R.layout.employer_current_ticket_popuwindow, null);
		mStatusChooseWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mStatusChooseWindow.setFocusable(true);
		mStatusChooseWindow.setOutsideTouchable(true);
		mStatusChooseWindow.setBackgroundDrawable(new BitmapDrawable());

		popupView.findViewById(R.id.tv_current_ticket_all).setOnClickListener(
				this);
		// popupView.findViewById(R.id.tv_current_ticket_zhixing)
		// .setOnClickListener(this);
		popupView.findViewById(R.id.tv_current_ticket_qianshou)
				.setOnClickListener(this);
		popupView.findViewById(R.id.tv_current_ticket_check)
				.setOnClickListener(this);
		popupView.findViewById(R.id.tv_current_ticket_history)
				.setOnClickListener(this);

	}

	/**
	 * 初始化控件对象
	 */
	private void initFindView() {
		initActionBar();

		mSpManager = SpManager.instance(CurrentTicketActivity.this);

		packageCode = getIntent().getStringExtra("ticketCode");
		checkId = getIntent().getIntExtra("checkId", -1);
		emptyId = getIntent().getIntExtra("emptyId", 0);
		infoId = getIntent().getStringExtra("infoId");
		// packageId = getIntent().getStringExtra("packageId");

		packageId = mSpManager.getPackageId();
		if (packageId == null || TextUtils.isEmpty(packageId)) {
			packageId = getIntent().getStringExtra("id");
		}

		Log.e("TAG", "-------packageCode------" + packageCode);
		Log.e("TAG", "-------packageId------" + packageId);

		rlAll = obtainView(R.id.rl_all);
		llCurrentBianhao = obtainView(R.id.ll_current_bianhao);
		rlCurrentTicket = obtainView(R.id.rl_current_ticket);
		tvCurrentBianHao = obtainView(R.id.tv_current_ticket_bianhao);
		llCurrentChoose = obtainView(R.id.ll_current_ticket_choose);
		ivCurrentImg = obtainView(R.id.iv_current_ticket_jiantou);
		tvCurrentStatusText = obtainView(R.id.tv_current_ticket_textStatus);
		lvCurrentListView = obtainView(R.id.lv_employer_current_ticket);
		pbCurrentProgress = obtainView(R.id.pb_current_ticket_progress);
		ivCurrentBlank = obtainView(R.id.iv_current_ticket_blank);
		etSearch = obtainView(R.id.et_search_input_employer);
		tvBtSearch = obtainView(R.id.tv_bt_search);
		llCurrentEmpty = obtainView(R.id.ll_current_ticket_empty);
		llCurrentSearch = obtainView(R.id.rl_search);

		lvCurrentListView.setMode(PullToRefreshBase.Mode.BOTH);
		lvCurrentListView.setOnRefreshListener(this);

		if (packageCode != null && !TextUtils.isEmpty(packageCode)) {
			llCurrentBianhao.setVisibility(View.VISIBLE);
			tvCurrentBianHao.setText(packageCode);
		} else {
			llCurrentBianhao.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_current_order));
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true, 0, "");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CurrentTicketActivity.this.finish();
				mSpManager.clearUp();
			}
		});

		setOnActionBarRightClickListener(false, null);
	}

	@Override
	protected void initData() {
		pbCurrentProgress.setVisibility(View.VISIBLE);

		// 设置初始列表类型
		setStatusText(checkId);
		// 请求服务器数据
		getCurrentTicketText(checkId, false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isBack) {
			performListItem.clear();
			packageId = mSpManager.getPackageId();

			// 设置初始列表类型
			setStatusText(checkId);
			// 请求服务器数据
			getCurrentTicketText(checkId, true);
			isBack = false;
		}
	}

	private void getCurrentTicketText(int status, boolean isClear) {
		if (isClear) {
			// 显示进度条
			pbCurrentProgress.setVisibility(View.VISIBLE);
			// 清空集合
			performListItem.clear();
			pageIndex = 1;
			count = 0;
		}

		// 判断包Id是否为空
		if (!StringUtils.isStrNotNull(packageId)) {
			packageId = "";
		}

		// 刷新界面
		mCurrentTicketAdapter.notifyDataSetChanged();
		// 获取搜索框的值
		String mKeyWord = etSearch.getText().toString().trim();

		count = pageIndex * pageSize;

		switch (status) {
		case -1:
			mTicketAllCurrentRequest = new TicketAllCurrentRequest(
					CurrentTicketActivity.this, pageSize, pageIndex, mKeyWord,
					packageId);
			mTicketAllCurrentRequest.setRequestId(GET_ALL_TICKET);
			httpPost(mTicketAllCurrentRequest);
			break;
		case 2:
			mTicketZhiXingCurrentRequest = new TicketZhiXingCurrentRequest(
					CurrentTicketActivity.this, pageSize, pageIndex, mKeyWord,
					packageId);
			mTicketZhiXingCurrentRequest.setRequestId(GET_ZHIXING_TICKET);
			httpPost(mTicketZhiXingCurrentRequest);
			break;
		case 3:
			mTicketCheckCurrentRequest = new TicketCheckCurrentRequest(
					CurrentTicketActivity.this, pageSize, pageIndex, mKeyWord,
					packageId);
			mTicketCheckCurrentRequest.setRequestId(GET_CHECK_TICKET);
			httpPost(mTicketCheckCurrentRequest);
			break;
		case 4:
			mTicketHistoryCurrentRequest = new TicketHistoryCurrentRequest(
					CurrentTicketActivity.this, pageSize, pageIndex, mKeyWord,
					packageId);
			mTicketHistoryCurrentRequest.setRequestId(GET_HISTORY_TICKET);
			httpPost(mTicketHistoryCurrentRequest);
			break;

		default:
			break;
		}
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
		case GET_ALL_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;
			if (isSuccess) {
				ArrayList<TicketCurrentBean> allList = response.data;

				if (allList.size() == 0) {
					showToast("暂时没有运单相关信息");
					if (emptyId == 0) {
						llCurrentEmpty.setVisibility(View.VISIBLE);
					} else {
						ivCurrentBlank.setVisibility(View.VISIBLE);
					}
				} else {
					ivCurrentBlank.setVisibility(View.GONE);
					llCurrentEmpty.setVisibility(View.GONE);
				}

				if (allList != null) {
					performListItem.addAll(allList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				Log.e("TAG",
						"-------allList的数据-------------" + allList.toString());

				mCurrentTicketAdapter.notifyDataSetChanged();
			} else {
				showToast(message);
			}
			break;
		case GET_ZHIXING_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				ArrayList<TicketCurrentBean> zhixingList = response.data;

				if (zhixingList.size() == 0) {
					showToast("暂时没有运单相关信息");
					if (emptyId == 0) {
						llCurrentEmpty.setVisibility(View.VISIBLE);
					} else {
						ivCurrentBlank.setVisibility(View.VISIBLE);
					}
				} else {
					ivCurrentBlank.setVisibility(View.GONE);
					llCurrentEmpty.setVisibility(View.GONE);
				}

				if (zhixingList != null) {
					performListItem.addAll(zhixingList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

			}

			break;
		case GET_CHECK_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				ArrayList<TicketCurrentBean> checkList = response.data;

				if (checkList.size() == 0) {
					showToast("暂时没有运单相关信息");
					if (emptyId == 0) {
						llCurrentEmpty.setVisibility(View.VISIBLE);
					} else {
						ivCurrentBlank.setVisibility(View.VISIBLE);
					}
				} else {
					ivCurrentBlank.setVisibility(View.GONE);
					llCurrentEmpty.setVisibility(View.GONE);
				}

				if (checkList != null) {
					performListItem.addAll(checkList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

			}

			break;
		case GET_HISTORY_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			totalCount = response.totalCount;

			if (isSuccess) {
				ArrayList<TicketCurrentBean> historyList = response.data;

				if (historyList.size() == 0) {
					showToast("暂时没有运单相关信息");
					if (emptyId == 0) {
						llCurrentEmpty.setVisibility(View.VISIBLE);
					} else {
						ivCurrentBlank.setVisibility(View.VISIBLE);
					}
				} else {
					ivCurrentBlank.setVisibility(View.GONE);
					llCurrentEmpty.setVisibility(View.GONE);
				}

				if (historyList != null) {
					performListItem.addAll(historyList);
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				mCurrentTicketAdapter.notifyDataSetChanged();

			}

			break;
		default:
			break;
		}
		pbCurrentProgress.setVisibility(View.GONE);
		lvCurrentListView.onRefreshComplete();
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		pbCurrentProgress.setVisibility(View.GONE);
		lvCurrentListView.onRefreshComplete();
		ivCurrentBlank.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置状态选择文字
	 * 
	 * @param status
	 */
	private void setStatusText(int status) {
		switch (status) {
		case -1:
			tvCurrentStatusText.setText(this.getResources().getString(
					R.string.employer_activity_current_ticket_all_text));
			break;
		case 2:
			tvCurrentStatusText.setText(this.getResources().getString(
					R.string.employer_activity_current_ticket_qianshou_text));
			break;
		case 3:
			tvCurrentStatusText.setText(this.getResources().getString(
					R.string.employer_activity_current_ticket_check_text));
			break;
		case 4:
			tvCurrentStatusText.setText(this.getResources().getString(
					R.string.employer_activity_current_ticket_history_text));
			break;
		}
	}

	/**
	 * 初始化监听事件
	 */
	private void initOnClick() {
		llCurrentChoose.setOnClickListener(this);
		llCurrentSearch.setOnClickListener(this);
		// ivCurrentDelete.setOnClickListener(this);
		lvCurrentListView.setOnItemClickListener(this);
		tvBtSearch.setOnClickListener(this);
	}

	@Override
	protected void setListener() {
		/* 初始化监听事件 */
		initOnClick();

		mStatusChooseWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						ivCurrentImg.setImageResource(R.drawable.xiajiantou);
					}
				});
	}

	/**
	 * 弹出框消失
	 */
	private void dismissPupWindows() {
		if (null != mStatusChooseWindow) {
			mStatusChooseWindow.dismiss();
			ivCurrentImg.setImageResource(R.drawable.xiajiantou);
		}
	}

	/**
	 * 显示弹出框
	 */
	private void showPupWindow() {
		// 设置弹窗的显示位置
		int width = rlAll.getMeasuredWidth();
		mStatusChooseWindow.showAsDropDown(rlAll, width, 0);
		ivCurrentImg.setImageResource(R.drawable.shangjiantou);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.iv_current_ticket_delete:
		//
		// break;
		// case R.id.bt_current_sendCar:
		// // TODO 调用通知接口
		// if (PayTimeUtils.isFastClickQuote()) {
		// showToast("正在处理，请不要频繁点击...");
		// return;
		// }
		// pressCarRequest = new PressCarRequest(mContext, infoId);
		// pressCarRequest.setRequestId(GET_SEND_CAR);
		// httpPost(pressCarRequest);
		// setViewEnable(false);
		// break;
		case R.id.ll_current_ticket_choose: // 选择状态
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_click_menu");

			if (mStatusChooseWindow.isShowing()) {
				dismissPupWindows();
			} else {
				showPupWindow();
			}
			break;
		case R.id.tv_bt_search: // 搜索车牌号
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_click_search");

			getCurrentTicketText(checkId, true);
			setStatusText(checkId);
			break;
		case R.id.tv_current_ticket_all: // 全部订单
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_select_menu");

			dismissPupWindows();
			checkId = -1;
			getCurrentTicketText(-1, true);
			setStatusText(-1);
			break;
		case R.id.tv_current_ticket_qianshou: // 待签收订单
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_select_menu");

			dismissPupWindows();
			checkId = 2;
			getCurrentTicketText(2, true);
			setStatusText(2);
			break;
		case R.id.tv_current_ticket_check: // 待审核订单
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_select_menu");

			dismissPupWindows();
			checkId = 3;
			getCurrentTicketText(3, true);
			setStatusText(3);
			break;
		case R.id.tv_current_ticket_history: // 历史订单
			//友盟统计首页
			mUmeng.setCalculateEvents("sign_up_select_menu");

			dismissPupWindows();
			checkId = 4;
			getCurrentTicketText(4, true);
			setStatusText(4);
			break;

		default:
			break;
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, String ticketCode, int emptyId,
			int checkId) {
		Intent intent = new Intent(context, CurrentTicketActivity.class);
		intent.putExtra("ticketCode", ticketCode);
		intent.putExtra("emptyId", emptyId);
		intent.putExtra("checkId", checkId);
		context.startActivity(intent);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invokeNew(Context context, int emptyId, String infoId,
			int finishId) {
		Intent intent = new Intent(context, CurrentTicketActivity.class);
		intent.putExtra("emptyId", emptyId);
		intent.putExtra("infoId", infoId);
		intent.putExtra("finishId", finishId);
		context.startActivity(intent);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invokeNewTask(Context context, String packageId) {
		Intent intent = new Intent(context, CurrentTicketActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			intent.putExtra("id", packageId);// 包的id
		}
		context.startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		packageCode = getIntent().getStringExtra("ticketCode");
		checkId = getIntent().getIntExtra("checkId", -1);
		emptyId = getIntent().getIntExtra("emptyId", 0);
		infoId = getIntent().getStringExtra("infoId");
		finishId = getIntent().getIntExtra("finishId", 0);
		packageId = getIntent().getStringExtra("id");
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (performListItem == null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCurrentListView.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (performListItem != null) {
			mCurrentTicketAdapter.notifyDataSetChanged();
		}

		// 清空存放当前订单列表的集合
		performListItem.clear();

		// 起始页置为1
		isEnd = false;
		// 请求服务器获取当前运单的数据列表
		getCurrentTicketText(checkId, true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			pageIndex++;
			getCurrentTicketText(checkId, false);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCurrentListView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	// 对返回键的监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mSpManager.clearUp();
			CurrentTicketActivity.this.finish();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TicketCurrentBean mTicketCurrentBean = performListItem
				.get(position - 1);

		// 获取订单ID
		String ticketId = String.valueOf(mTicketCurrentBean.id);

		// 跳转到订单详情
		// OrderDetailActivity.invoke(CurrentTicketActivity.this, ticketId);
		// WaybillDetailActivity.invoke(CurrentTicketActivity.this, ticketId);
	}

}
