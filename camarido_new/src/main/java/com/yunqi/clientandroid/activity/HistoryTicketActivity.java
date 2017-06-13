package com.yunqi.clientandroid.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.HistoryTicketAdapter;
import com.yunqi.clientandroid.entity.CatoryInfo;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.GetCatoryRequest;
import com.yunqi.clientandroid.http.request.GetHistoryTicketRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.view.FlowRadioGroup;
import com.yunqi.clientandroid.view.wheel.ChangeAddressPopWin;
import com.yunqi.clientandroid.view.wheel.ChangeBirthDialog;
import com.yunqi.clientandroid.view.wheel.ChangeBirthDialog.OnBirthListener;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 历史订单页面
 * @date 15/12/22
 */
public class HistoryTicketActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener,
		RadioGroup.OnCheckedChangeListener {

	private boolean isEnd;// 是否服务器无数据返回
	private int currentYear;// 当前年
	private int currentMonth;// 当前月
	private int currentDay;// 当前日
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private int screeningType;// 筛选类型--1所有 2具体 3筛选
	private int mCatoryCheckId;// 货品种类选择的id
	private int mOrderType;// 订单类型--9已作废 8已完成
	// 起点和终点 城市省份id
	private int mPackageBeginProvinceId;
	private int mPackageBeginCityId;
	private int mPackageEndProvinceId;
	private int mPackageEndCityId;
	private int timeType;// 时间范围状态 1表示开始 2表示结束
	private int showPpwType = 1;// 显示或取消PPW 1为显示 2为取消
	private String timeStampBegin;// 选中的开始时间戳
	private String timeStampEnd;// 选中的结束时间戳
	private String ticketCode;// 订单号
	private String mStartProvince = "";// 开始省份
	private String mStartCity = "";// 开始城市
	private String mEndProvince = "";// 结束省份
	private String mEndCity = "";// 结束城市
	private String currentTime;// 存放当前时间
	private View parentView;
	private View mLine;
	private ProgressBar mProgress;
	private AutoCompleteTextView autoTvInputTicket;
	private ImageView mIvSearch;
	private ImageView mIvScreening;
	private PullToRefreshListView historyticketRefreshListView;
	private ArrayList<PerformListItem> performListItem = new ArrayList<PerformListItem>();// 存放历史订单列表的集合
	private Handler handler = new Handler();
	private ListView historyListView;
	private HistoryTicketAdapter historyTicketAdapter;
	// 筛选弹出框
	private PopupWindow mFilterPopuWindow = null;// 筛选弹出框
	// 筛选里面的view
	private FlowRadioGroup rgCatory;// 煤品种类
	private RadioGroup rgOrderType;// 抢单类型
	private RadioButton rbInvalid;// 已作废
	private RadioButton rbComplete;// 已完成
	private TextView tvStartProvince;
	private TextView tvStarCity;
	private TextView tvEndProvince;
	private TextView tvEndCity;
	private TextView tvTimeMin;// 时间范围前
	private TextView tvTimeMax;// 时间范围后
	private ImageButton ivReset;// 重置按钮
	private Button btnSure;// 确认筛选按钮
	private FrameLayout flBlank;

	private List<CatoryInfo> mCatoryList;// 存放煤种的集合
	private HashMap<String, RadioButton> mFilterCatoryMap = new HashMap<String, RadioButton>();

	/**
	 * 排序，筛选的状态
	 */
	private final int TEXTVIEW_CHANGE_NOMAL = 1;
	private final int TEXTVIEW_CHANGE_CHECKED = 2;
	private final int TEXTVIEW_CHANGE_NO = 3;

	// 本页请求
	private GetHistoryTicketRequest mGetHistoryTicketRequest;
	private GetCatoryRequest mGetCatoryRequest;// 获取筛选种类接口

	// 本页请求ID
	private final int GET_HISTORY_TICKETLIST_REQUEST = 1;
	private final int GET_HISTORY_TICKETCODE_REQUEST = 2;
	private final int GET_HISTORY_SCREENING_REQUEST = 4;
	private final int GET_CATORY_REQUEST = 3;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_history_ticket;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		// 初始化当前时间
		initTime();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_history_ticket, null);

		autoTvInputTicket = (AutoCompleteTextView) findViewById(R.id.autoTv_history_inputTicket);
		mIvSearch = (ImageView) findViewById(R.id.iv_history_search);
		mIvScreening = (ImageView) findViewById(R.id.ib_history_screening);
		mLine = findViewById(R.id.view_history_line);
		historyticketRefreshListView = (PullToRefreshListView) findViewById(R.id.historyticket_refreshlistview);
		mProgress = (ProgressBar) findViewById(R.id.pb_historyticket_progress);
		flBlank = obtainView(R.id.fl_historyticket_blank);

		// 初始化筛选对话框
		createFilterPopWindow();

		// 请求筛选的参数置为初始化
		filterReset();

	}

	@Override
	protected void initData() {
		// 显示加载
		mProgress.setVisibility(View.VISIBLE);
		// 请求服务器获取货品种类
		mCatoryList = new ArrayList<CatoryInfo>();
		mGetCatoryRequest = new GetCatoryRequest(HistoryTicketActivity.this);
		mGetCatoryRequest.setRequestId(GET_CATORY_REQUEST);
		httpPost(mGetCatoryRequest);

		// 清空集合
		performListItem.clear();
		// 访问服务器获取全部历史订单数据
		getDataFromeServiceHistoryList(pageIndex);
		// 刷新view
		initPullToRefreshView();

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.myticket_historyTicket));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HistoryTicketActivity.this.finish();
			}
		});

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mIvSearch.setOnClickListener(this);
		mIvScreening.setOnClickListener(this);

		// 设置筛选消失后动作
		mFilterPopuWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						// 设置打开筛选按钮的背景为灰色
						changeTextStyle(TEXTVIEW_CHANGE_NOMAL, mIvScreening);
					}
				});

		rgCatory.setOnCheckedChangeListener(this);
		rgOrderType.setOnCheckedChangeListener(this);

		tvStartProvince.setOnClickListener(this);
		tvStarCity.setOnClickListener(this);
		tvEndCity.setOnClickListener(this);
		tvEndProvince.setOnClickListener(this);
		ivReset.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		tvTimeMin.setOnClickListener(this);
		tvTimeMax.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_history_search:
			// 点击查询订单号
			ticketCode = autoTvInputTicket.getText().toString().trim();
			if (TextUtils.isEmpty(ticketCode)) {
				showToast("请输入需要查询的订单号");
				return;
			}

			// 清空集合
			performListItem.clear();
			// 显示加载
			mProgress.setVisibility(View.VISIBLE);
			// 起始页置为1
			pageIndex = 1;
			// 查询具体订单的方法
			getDataFromeServiceHistoryList(pageIndex, ticketCode);

			break;

		case R.id.ib_history_screening:
			// 点击弹出筛选界面,更换背景图片为蓝色
			changeTextStyle(TEXTVIEW_CHANGE_CHECKED, mIvScreening);

			if (showPpwType == 1) {
				// 显示
				showFilterPopuWindow();
			} else if (showPpwType == 2) {
				// 取消
				dismissFilterPopuWindow();
			}

			break;

		case R.id.tv_history_province_start_check: // 选择起点省份
		case R.id.tv_history_city_start_check: // 选择起点市区
			hideKeyboard();// 隐藏软键盘

			ChangeAddressPopWin changeAddressPopWin = new ChangeAddressPopWin(
					HistoryTicketActivity.this);
			changeAddressPopWin.setAddress("不限", "不限");
			changeAddressPopWin.showPopWin(HistoryTicketActivity.this);
			changeAddressPopWin
					.setAddresskListener(new ChangeAddressPopWin.OnAddressCListener() {
						@Override
						public void onClick(String province, String city,
								int provinceId, int cityId) {
							mPackageBeginProvinceId = provinceId;
							mPackageBeginCityId = cityId;
							mStartProvince = province;
							mStartCity = city;
							if (!TextUtils.isEmpty(province)
									&& province != null) {
								tvStartProvince.setText(province);
							}
							if (!TextUtils.isEmpty(city) && city != null) {
								tvStarCity.setText(city);
							}
						}
					});
			break;

		case R.id.tv_history_province_end_check: // 选择终点省份
		case R.id.tv_history_city_end_check: // 选择终点市区
			hideKeyboard();// 隐藏软键盘

			ChangeAddressPopWin changeAddressPopWiEnd = new ChangeAddressPopWin(
					HistoryTicketActivity.this);
			changeAddressPopWiEnd.setAddress("不限", "不限");
			changeAddressPopWiEnd.showPopWin(HistoryTicketActivity.this);
			changeAddressPopWiEnd
					.setAddresskListener(new ChangeAddressPopWin.OnAddressCListener() {
						@Override
						public void onClick(String province, String city,
								int provinceId, int cityId) {
							mPackageEndProvinceId = provinceId;
							mPackageEndCityId = cityId;
							mEndProvince = province;
							mEndCity = city;
							if (!TextUtils.isEmpty(province)
									&& province != null) {
								tvEndProvince.setText(province);
							}
							if (!TextUtils.isEmpty(city) && city != null) {
								tvEndCity.setText(city);
							}
						}
					});
			break;

		case R.id.tv_history_time_min:
			// 选择起始时间
			timeType = 1;
			showSelectorTime();
			break;

		case R.id.tv_history_time_max:
			// 选择结束时间
			timeType = 2;
			showSelectorTime();
			break;

		case R.id.iv_history_reset:// 重置
			filterReset();
			break;
		case R.id.btn_history_sure: // 确认
			// 清空集合
			performListItem.clear();
			// 显示加载
			mProgress.setVisibility(View.VISIBLE);
			// 起始页重置为1
			pageIndex = 1;
			// 筛选历史订单的方法
			getDataFromeServiceHistoryList(pageIndex, mCatoryCheckId,
					mOrderType, mPackageBeginProvinceId, mPackageBeginCityId,
					mPackageEndProvinceId, mPackageEndCityId, timeStampBegin,
					timeStampEnd);

			// 取消筛选弹窗
			dismissFilterPopuWindow();

			break;

		default:
			break;
		}
	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		historyticketRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		historyticketRefreshListView.getLoadingLayoutProxy(false, true)
				.setPullLabel(getString(R.string.pull_to_loadmore));
		historyticketRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		historyticketRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		historyListView = historyticketRefreshListView.getRefreshableView();
		historyListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		historyListView.setDividerHeight(25);
		historyListView.setSelector(android.R.color.transparent);// 隐藏listView默认的selector
		historyticketRefreshListView.setOnRefreshListener(this);
		// item的点击事件--点击跳转到历史订单详情界面
		historyticketRefreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 点击跳转到历史订单详情界面
						String ticketId = performListItem.get(position - 1).id;
						if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
							MyTicketDetailActivity.invoke(
									HistoryTicketActivity.this, ticketId);
						}
					}
				});

		historyTicketAdapter = new HistoryTicketAdapter(
				HistoryTicketActivity.this, performListItem);
		historyListView.setAdapter(historyTicketAdapter);

	}

	// 从服务器获取所有列表数据
	private void getDataFromeServiceHistoryList(int pageIndex) {
		count = pageIndex * pageSize;
		// 请求所有历史订单列表
		screeningType = 1;

		mGetHistoryTicketRequest = new GetHistoryTicketRequest(this, pageSize,
				pageIndex);
		mGetHistoryTicketRequest.setRequestId(GET_HISTORY_TICKETLIST_REQUEST);
		httpPost(mGetHistoryTicketRequest);
	}

	// 从服务器获取具体订单号数据
	private void getDataFromeServiceHistoryList(int pageIndex, String ticketCode) {
		count = pageIndex * pageSize;
		// 请求具体历史订单列表
		screeningType = 2;

		mGetHistoryTicketRequest = new GetHistoryTicketRequest(this, pageSize,
				pageIndex, ticketCode);
		mGetHistoryTicketRequest.setRequestId(GET_HISTORY_TICKETCODE_REQUEST);
		httpPost(mGetHistoryTicketRequest);
	}

	// 筛选请求服务器的方法
	private void getDataFromeServiceHistoryList(int pageIndex,
			int mCatoryCheckId, int mOrderType, int mPackageBeginProvinceId,
			int mPackageBeginCityId, int mPackageEndProvinceId,
			int mPackageEndCityId, String timeMin, String timeMax) {
		count = pageIndex * pageSize;
		// 请求筛选后订单列表
		screeningType = 3;

		mGetHistoryTicketRequest = new GetHistoryTicketRequest(this, pageSize,
				pageIndex, mCatoryCheckId, mOrderType, mPackageBeginProvinceId,
				mPackageBeginCityId, mPackageEndProvinceId, mPackageEndCityId,
				timeMin, timeMax);
		mGetHistoryTicketRequest.setRequestId(GET_HISTORY_SCREENING_REQUEST);
		httpPost(mGetHistoryTicketRequest);
	}

	// 上拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (performListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					historyticketRefreshListView.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (performListItem != null) {
			historyTicketAdapter.notifyDataSetChanged();
		}

		// 清空集合
		performListItem.clear();
		// 起始页置为1
		pageIndex = 1;

		if (screeningType == 1) {
			// 所有历史订单
			getDataFromeServiceHistoryList(pageIndex);
		} else if (screeningType == 2) {
			// 指定历史订单
			getDataFromeServiceHistoryList(pageIndex, ticketCode);
		} else if (screeningType == 3) {
			// 筛选历史订单
			getDataFromeServiceHistoryList(pageIndex, mCatoryCheckId,
					mOrderType, mPackageBeginProvinceId, mPackageBeginCityId,
					mPackageEndProvinceId, mPackageEndCityId, timeStampBegin,
					timeStampEnd);
		}

	}

	// 下啦刷新
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;

			if (screeningType == 1) {
				// 所有历史订单
				getDataFromeServiceHistoryList(pageIndex);
			} else if (screeningType == 2) {
				// 指定历史订单
				getDataFromeServiceHistoryList(pageIndex, ticketCode);
			} else if (screeningType == 3) {
				// 筛选历史订单
				getDataFromeServiceHistoryList(pageIndex, mCatoryCheckId,
						mOrderType, mPackageBeginProvinceId,
						mPackageBeginCityId, mPackageEndProvinceId,
						mPackageEndCityId, timeStampBegin, timeStampEnd);
			}

		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					historyticketRefreshListView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

	// 选择时间
	private void showSelectorTime() {
		ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(
				HistoryTicketActivity.this);
		if (!TextUtils.isEmpty(currentTime) && currentTime != null) {
			String[] currentTimeArray = currentTime.split("-");
			// 将获取到的当前日期截取年月日并转化为int类型
			currentYear = Integer.parseInt(currentTimeArray[0]);
			currentMonth = Integer.parseInt(currentTimeArray[1]);
			currentDay = Integer.parseInt(currentTimeArray[2]);

		}
		mChangeBirthDialog.setDate(currentYear, currentMonth, currentDay);
		mChangeBirthDialog.show();
		mChangeBirthDialog.setBirthdayListener(new OnBirthListener() {
			@Override
			public void onClick(String year, String month, String day) {
				String selectorTime = year + "-" + month + "-" + day;
				if (timeType == 1) {
					// 起点时间
					if (!TextUtils.isEmpty(selectorTime)) {
						tvTimeMin.setText(selectorTime);
					}
					// 将选中的时间转成时间戳
					timeStampBegin = conversionTime(selectorTime);
				} else if (timeType == 2) {
					// 结束时间
					if (!TextUtils.isEmpty(selectorTime)) {
						tvTimeMax.setText(selectorTime);
					}
					// 将选中的时间转成时间戳
					timeStampEnd = conversionTime(selectorTime);
				}

			}
		});

	}

	// 初始化时间的方法
	@SuppressLint("SimpleDateFormat")
	private void initTime() {
		// 1.创建一个Date对象.
		Date date = new Date();
		// 2.得到 例如2016.01.01
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		currentTime = sdf.format(date);

	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14"）返回时间戳
	 */
	public String conversionTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = format.parse(time);
			long longTime = date.getTime();

			times = String.valueOf(longTime / 1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return times;
	}

	/**
	 * 筛选状态重置
	 */
	private void filterReset() {
		// 起点重置
		tvStartProvince.setText("省份");
		mStartProvince = "";
		mPackageBeginProvinceId = -1;
		tvStarCity.setText("城市");
		mStartCity = "";
		mPackageBeginCityId = -1;

		// 终点重置
		tvEndProvince.setText("省份");
		mEndProvince = "";
		mPackageEndProvinceId = -1;
		tvEndCity.setText("城市");
		mEndCity = "";
		mPackageEndCityId = -1;
		// 时间重置
		tvTimeMin.setText("");
		timeStampBegin = "";
		tvTimeMax.setText("");
		timeStampEnd = "";
		// 抢单类型重置
		rbInvalid.setChecked(false);
		rbComplete.setChecked(false);
		rbInvalid.setBackgroundResource(R.drawable.order_check_background);
		rbComplete.setBackgroundResource(R.drawable.order_check_background);
		rbComplete.setTextColor(CamaridoApp.instance.getResources().getColor(
				R.color.order_from_color));
		rbInvalid.setTextColor(CamaridoApp.instance.getResources().getColor(
				R.color.order_from_color));

		// 抢单类型置为0意思为没选中
		mOrderType = -1;
		// 货品种类置为0
		mCatoryCheckId = -1;
		// 货品种类重置
		setCatoryStyle("");
	}

	/**
	 * 筛选的状态选择
	 */
	private void changeTextStyle(int checkedCode, ImageView imageView) {
		if (checkedCode == TEXTVIEW_CHANGE_NOMAL) {
			imageView.setImageResource(R.drawable.history_screening_black);
		} else if (checkedCode == TEXTVIEW_CHANGE_CHECKED) {
			imageView.setImageResource(R.drawable.history_screening_blue);
		} else if (checkedCode == TEXTVIEW_CHANGE_NO) {
			imageView.setImageResource(R.drawable.history_screening_black);
		}
	}

	/**
	 * 显示筛选弹窗
	 */
	private void showFilterPopuWindow() {
		if (mFilterPopuWindow == null) {
			createFilterPopWindow();
		}
		showPpwType = 2;// 置为下次取消
		mFilterPopuWindow.showAsDropDown(mLine, 0, 0);
	}

	/**
	 * 取消筛选弹窗
	 */
	private void dismissFilterPopuWindow() {
		if (mFilterPopuWindow != null && mFilterPopuWindow.isShowing()) {
			showPpwType = 1;// 置为下次显示
			mFilterPopuWindow.dismiss();
		}
	}

	/**
	 * 初始化筛选弹窗
	 */
	private void createFilterPopWindow() {
		View historyFilterView = getLayoutInflater().inflate(
				R.layout.historyticket_filter_dialog, null);

		mFilterPopuWindow = new PopupWindow(historyFilterView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		mFilterPopuWindow.setFocusable(false);
		mFilterPopuWindow.setOutsideTouchable(true);
		// mFilterPopuWindow.setBackgroundDrawable(new
		// ColorDrawable(0x00000000));
		mFilterPopuWindow.setAnimationStyle(android.R.style.Animation_Dialog);

		rgCatory = (FlowRadioGroup) historyFilterView
				.findViewById(R.id.rg_history_filter_catory);

		rgOrderType = (RadioGroup) historyFilterView
				.findViewById(R.id.rg_history_order_type);
		rbInvalid = (RadioButton) historyFilterView
				.findViewById(R.id.rb_history_invalid);
		rbComplete = (RadioButton) historyFilterView
				.findViewById(R.id.rb_history_complete);

		tvStartProvince = (TextView) historyFilterView
				.findViewById(R.id.tv_history_province_start_check);
		tvStarCity = (TextView) historyFilterView
				.findViewById(R.id.tv_history_city_start_check);
		tvEndProvince = (TextView) historyFilterView
				.findViewById(R.id.tv_history_province_end_check);
		tvEndCity = (TextView) historyFilterView
				.findViewById(R.id.tv_history_city_end_check);

		tvTimeMin = (TextView) historyFilterView
				.findViewById(R.id.tv_history_time_min);
		tvTimeMax = (TextView) historyFilterView
				.findViewById(R.id.tv_history_time_max);

		ivReset = (ImageButton) historyFilterView
				.findViewById(R.id.iv_history_reset);
		btnSure = (Button) historyFilterView
				.findViewById(R.id.btn_history_sure);

	}

	/**
	 * 改变种类状态
	 */
	private void setCatoryStyle(String key) {
		RadioButton radioButton = null;
		for (String catoryName : mFilterCatoryMap.keySet()) {
			if (catoryName.equals(key)) {
				radioButton = mFilterCatoryMap.get(catoryName);
				radioButton.setChecked(true);
				radioButton.setTextColor(Color.WHITE);
				radioButton.setBackgroundResource(R.drawable.order_checked);
			} else {
				radioButton = mFilterCatoryMap.get(catoryName);
				radioButton.setChecked(false);
				radioButton.setTextColor(CamaridoApp.instance.getResources()
						.getColor(R.color.order_from_color));
				radioButton
						.setBackgroundResource(R.drawable.order_check_background);
			}
		}
	}

	/**
	 * 筛选框 check
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_history_invalid: // 已作废
			mOrderType = 9;
			rbInvalid.setTextColor(Color.WHITE);
			rbComplete.setTextColor(CamaridoApp.instance.getResources()
					.getColor(R.color.order_from_color));
			rbInvalid.setBackgroundResource(R.drawable.order_checked);
			rbComplete.setBackgroundResource(R.drawable.order_check_background);
			break;
		case R.id.rb_history_complete: // 已完成
			mOrderType = 8;
			rbInvalid.setTextColor(CamaridoApp.instance.getResources()
					.getColor(R.color.order_from_color));
			rbComplete.setTextColor(Color.WHITE);
			rbInvalid.setBackgroundResource(R.drawable.order_check_background);
			rbComplete.setBackgroundResource(R.drawable.order_checked);
			break;
		default:
			break;
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		InputMethodManager im = (InputMethodManager) HistoryTicketActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(tvTimeMin.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		im.hideSoftInputFromWindow(tvTimeMax.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_HISTORY_TICKETLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			isEnd = false;// 服务器还有数据要返回
			// 获取所有历史订单
			if (isSuccess) {
				ArrayList<PerformListItem> historyData = response.data;
				totalCount = response.totalCount;
				// 将数据添加进集合
				if (historyData != null) {
					performListItem.addAll(historyData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无历史订单的信息");
					flBlank.setVisibility(View.VISIBLE);
				} else {
					flBlank.setVisibility(View.GONE);
				}

				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据要返回
				}

				historyTicketAdapter.notifyDataSetChanged();// 刷新界面
				historyticketRefreshListView.onRefreshComplete();// 结束刷新的方法
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 隐藏加载
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_HISTORY_TICKETCODE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			isEnd = false;// 服务器还有数据要返回
			// 获取指定历史订单
			if (isSuccess) {
				ArrayList<PerformListItem> historyData = response.data;
				totalCount = response.totalCount;
				// 将数据添加进集合
				if (historyData != null) {
					performListItem.addAll(historyData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无该订单号相关的信息");
					flBlank.setVisibility(View.VISIBLE);
				} else {
					flBlank.setVisibility(View.GONE);
				}
				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据要返回
				}

				historyTicketAdapter.notifyDataSetChanged();// 刷新界面
				historyticketRefreshListView.onRefreshComplete();// 结束刷新的方法

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

			}
			// 隐藏加载
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_HISTORY_SCREENING_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			isEnd = false;// 服务器还有数据要返回
			// 获取筛选历史订单
			if (isSuccess) {
				ArrayList<PerformListItem> historyData = response.data;
				totalCount = response.totalCount;
				// 将数据添加进集合
				if (historyData != null) {
					performListItem.addAll(historyData);
				}

				if (performListItem.size() == 0) {
					showToast("暂无该筛选条件相关的信息");
				}
				if (totalCount <= count) {
					isEnd = true;// 服务器没有数据要返回
				}

				historyTicketAdapter.notifyDataSetChanged();// 刷新界面
				historyticketRefreshListView.onRefreshComplete();// 结束刷新的方法

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

			}
			// 隐藏加载
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_CATORY_REQUEST: // 获取种类接口
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mCatoryList = response.data;
				if (mCatoryList.size() > 0) {
					for (final CatoryInfo catoryInfo : mCatoryList) {
						final RadioButton tempButton = new RadioButton(
								CamaridoApp.instance);
						tempButton
								.setBackgroundResource(R.drawable.order_filter_selector); // 设置RadioButton的背景图片
						tempButton.setButtonDrawable(new ColorDrawable(
								Color.TRANSPARENT)); // 设置按钮的样式
						tempButton.setGravity(Gravity.CENTER);
						tempButton.setText(catoryInfo.name);
						tempButton.setTextSize(15);
						tempButton.setTextColor(CamaridoApp.instance
								.getResources().getColor(
										R.color.order_from_color));
						rgCatory.addView(tempButton,
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						mFilterCatoryMap.put(catoryInfo.name, tempButton);

						tempButton
								.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(
											CompoundButton buttonView,
											boolean isChecked) {
										if (isChecked) {
											mCatoryCheckId = catoryInfo.id;
											setCatoryStyle(catoryInfo.name);
										}
									}
								});
					}
				}
			} else {

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, HistoryTicketActivity.class);
		activity.startActivity(intent);
	}

}
