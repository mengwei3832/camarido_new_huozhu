package com.yunqi.clientandroid.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.activity.PackageListDetailActivity;
import com.yunqi.clientandroid.adapter.CurrentViewPagerAdapter;
import com.yunqi.clientandroid.adapter.HomeViewPagerAdapter;
import com.yunqi.clientandroid.adapter.OrderDetailListAdapter;
import com.yunqi.clientandroid.entity.BannerInfo;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.HostUtil.HostType;
import com.yunqi.clientandroid.http.request.CurrentOrderRequest;
import com.yunqi.clientandroid.http.request.GetBannerCheZhuRequest;
import com.yunqi.clientandroid.http.request.GetBannerRequest;
import com.yunqi.clientandroid.http.request.GetPackageListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.view.BasePager;
import com.yunqi.clientandroid.view.CircleIndicator;
import com.yunqi.clientandroid.view.HomeCurrentPager;
import com.yunqi.clientandroid.view.ViewPagerExpand;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 首页
 * @date 15/11/20
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener,
		AdapterView.OnItemClickListener {
	private ViewPagerExpand vpHome;
	private ViewPagerExpand vpCurrent;
	private ListView lvHotOrder;
	private View viewHeader;
	private RelativeLayout rlCurrentOrder;
	private CircleIndicator circleIndicator;
	private LinearLayout llCurrentPoint;
	private ImageView ivNoOrder;
	private TextView tvOrderMore;
	private MainActivity mainActivity;
	private PreManager mPreManager;
	private ArrayList<PerformListItem> performListItem = new ArrayList<PerformListItem>();
	private ArrayList<BasePager> pagers = new ArrayList<BasePager>();
	private List<BannerInfo> mPagerList = new ArrayList<BannerInfo>();
	private List<FocusonRoute> mHotOrderList = new ArrayList<FocusonRoute>();
	private List<ImageView> imageViewTips; // 装载订单轮播小圆点
	private ScheduledExecutorService scheduled; // 实例化线程池对象
	private ScheduledExecutorService timeScheduled; // 实例化线程池对象
	private TimerTask task; // 定时器任务
	private int oldPage = 0; // 前一页
	private int nextPage = 1; // 下一页
	private boolean isPause = false; // 是否触发暂停

	private CurrentViewPagerAdapter currentViewPagerAdapter;
	private HomeViewPagerAdapter mPagerAdapter;
	private OrderDetailListAdapter mHotOrderAdapter;

	// 热门订单显示条目数
	private final int HOME_HOT_ORDER_PAGE_COUNT = 3;
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量

	// 请求热门列表 接口
	private GetPackageListRequest mGetPackageListRequest;
	private final int GET_PACKAGE_SUBSRCIPTS_REQUEST = 1;

	// 获取当前订单接口
	private CurrentOrderRequest mCurrentOrderRequest;
	private final int CURRENT_ORDER_REQUEST = 2;

	// 获取首页banner接口
	private GetBannerCheZhuRequest mGetBannerRequest;
	private final int GET_BANNER_REQUEST = 3;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 接收到消息后，更新页面
			vpCurrent.setCurrentItem(msg.what);
		};
	};

	private class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			// 发送消息给UI线程
			if (!isPause) { // 不是暂停状态
				handler.sendEmptyMessage(nextPage);
			}

		}
	}

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(getActivity());
		mainActivity = (MainActivity) getActivity();

		mPreManager.clearHomeCache();

		// 获取热门订单列表
		mGetPackageListRequest = new GetPackageListRequest(mainActivity, 1,
				HOME_HOT_ORDER_PAGE_COUNT, 1);
		mGetPackageListRequest.setRequestId(GET_PACKAGE_SUBSRCIPTS_REQUEST);
		httpPostJson(mGetPackageListRequest);

		// 获取当前订单
		mCurrentOrderRequest = new CurrentOrderRequest(mainActivity, pageIndex,
				pageSize);
		mCurrentOrderRequest.setRequestId(CURRENT_ORDER_REQUEST);
		httpPostJson(mCurrentOrderRequest);

		if (mPreManager.isBannerCacheExist()) {
			// 首页轮播图
			String[] bannerImage = mPreManager.getBanner();
			// String[] bannerArtical =
			// mPreManager.getBannerArticalUrls().split(",");
			for (int i = 0; i < bannerImage.length; i++) {
				if (!bannerImage[i].equals("")) {
					mPagerList.add(new BannerInfo(bannerImage[i], ""));
				}
			}
		} else {
			// 获取首页banner
			mGetBannerRequest = new GetBannerCheZhuRequest(mainActivity);
			mGetBannerRequest.setRequestId(GET_BANNER_REQUEST);
			httpPost(mGetBannerRequest);
		}
		mPagerAdapter = new HomeViewPagerAdapter(mPagerList, mainActivity);
		vpHome.setAdapter(mPagerAdapter);
		circleIndicator.setIndicatorMode(CircleIndicator.Mode.INSIDE);
		circleIndicator.setViewPager(vpHome);

		// 轮播当前订单
		currentViewPagerAdapter = new CurrentViewPagerAdapter(pagers,
				mainActivity);
		vpCurrent.setAdapter(currentViewPagerAdapter);

		// 热门订单
		mHotOrderAdapter = new OrderDetailListAdapter(mainActivity,
				mHotOrderList);
		lvHotOrder.setAdapter(mHotOrderAdapter);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_home;
	}

	@Override
	protected void initView(View _rootView) {
		lvHotOrder = (ListView) _rootView.findViewById(R.id.lv_home_hot_order);

		viewHeader = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.fragment_home_header, null);

		vpHome = (ViewPagerExpand) viewHeader.findViewById(R.id.vp_home);
		circleIndicator = (CircleIndicator) viewHeader
				.findViewById(R.id.indicator);
		tvOrderMore = (TextView) viewHeader
				.findViewById(R.id.tv_home_order_more);
		rlCurrentOrder = (RelativeLayout) viewHeader
				.findViewById(R.id.rl_current_pagerOrder);
		vpCurrent = (ViewPagerExpand) viewHeader
				.findViewById(R.id.vp_current_order);
		llCurrentPoint = (LinearLayout) viewHeader
				.findViewById(R.id.ll_current_pagerPoint);
		ivNoOrder = (ImageView) viewHeader.findViewById(R.id.iv_no_order);

		// 监听ViewPager的选中事件
		vpCurrent.setOnPageChangeListener(new OnPageChangeListener() {
			// 某一页选择时，回调
			@Override
			public void onPageSelected(int position) {
				// 选中某一页，再去加载数据
				BasePager basePager = pagers.get(position);
				basePager.initData();

				if (position == pagers.size() - 1) {
					// 设置当前位置小红点的背景
					nextPage = 2;
					imageViewTips.get(1).setBackgroundResource(
							R.drawable.point_select);
					// 改变前一个位置小红点的背景
					imageViewTips.get(oldPage).setBackgroundResource(
							R.drawable.point_normal);
					oldPage = 1;
				} else if (position == 0) {
					nextPage = pagers.size() - 2;
					// 改变前一个位置小红点的背景
					imageViewTips.get(imageViewTips.size() - 2)
							.setBackgroundResource(R.drawable.point_select);
					// 改变前一个位置小红点的背景
					imageViewTips.get(oldPage).setBackgroundResource(
							R.drawable.point_normal);
					oldPage = imageViewTips.size() - 2;
				} else {
					// 改变前一个位置小红点的背景
					imageViewTips.get(position).setBackgroundResource(
							R.drawable.point_select);
					if (position != oldPage) {
						imageViewTips.get(oldPage).setBackgroundResource(
								R.drawable.point_normal);
					}
					nextPage = position + 1;
					oldPage = position;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (position == pagers.size() - 1 && positionOffset == 0.0f) {
					vpCurrent.setCurrentItem(1, false);

				} else if (position == 0 && positionOffset == 0.0f) {
					vpCurrent.setCurrentItem(pagers.size() - 2, false);
				}
			}

			@Override
			public void onPageScrollStateChanged(int position) {
				switch (position) {
				case 1: // 1表示手动触摸
					isPause = true;
					if (!timeScheduled.isTerminated()) {
						timeScheduled.schedule(task, 6, TimeUnit.SECONDS); // 让自动循环暂停6秒
					}
					break;
				}
			}
		});

		lvHotOrder.addHeaderView(viewHeader);

	}

	@Override
	protected void setListener() {
		lvHotOrder.setOnItemClickListener(this);
		tvOrderMore.setOnClickListener(this);
		ivNoOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_home_order_more: // 更多
			mainActivity.changeTab(2);
			break;
		case R.id.iv_no_order: // 没有当前订单
			mainActivity.changeTab(2);
			break;
		}
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	/**
	 * 初始化圆点
	 */
	private void initImageViewTips() {

		imageViewTips = new ArrayList<ImageView>();

		for (int i = 0; i < pagers.size(); i++) {
			ImageView imageViewTip = new ImageView(CamaridoApp.instance);
			imageViewTip.setLayoutParams(new LayoutParams(13, 13)); // 设置圆点宽高
			imageViewTips.add(imageViewTip);
			if (i == 0 || i == pagers.size() - 1) {
				imageViewTip.setVisibility(View.GONE);
			} else if (i == 1) {
				imageViewTip.setBackgroundResource(R.drawable.point_select);
			} else {
				imageViewTip.setBackgroundResource(R.drawable.point_normal);
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					new LayoutParams(13, 13));
			params.leftMargin = 5;
			params.rightMargin = 5;
			llCurrentPoint.addView(imageViewTip, params);
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		int totalCount;
		switch (requestId) {
		case GET_PACKAGE_SUBSRCIPTS_REQUEST: // 热门订单回调
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mHotOrderList = response.data;
				// 判断是否为null
				if (mHotOrderList != null) {
					mHotOrderAdapter.addAll(mHotOrderList);
				}
			}
			break;

		case CURRENT_ORDER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				totalCount = response.totalCount;
				ArrayList<PerformListItem> performListItemData = response.data;
				if (performListItemData != null
						&& performListItemData.size() > 0) {
					performListItem.clear();// 清空数据集合
					pagers.clear();// 清空viewPager集合
					rlCurrentOrder.setVisibility(View.VISIBLE);
					ivNoOrder.setVisibility(View.GONE);
					performListItem.addAll(performListItemData);

					for (int i = 0; i < performListItem.size(); i++) {
						pagers.add(new HomeCurrentPager(mainActivity, i,
								performListItem));
					}

					// 初始化圆点
					initImageViewTips();
					// 设置默认从1开始
					vpCurrent.setCurrentItem(1);

					// 初始化第一页数据
					if (pagers != null && pagers.size() > 0) {
						pagers.get(0).initData();
					}

					// 开启定时器，每隔2秒自动播放下一张（通过调用线程实现）（与Timer类似，可使用Timer代替）
					scheduled = Executors.newSingleThreadScheduledExecutor();
					// 设置一个线程，该线程用于通知UI线程变换图片
					ViewPagerTask pagerTask = new ViewPagerTask();
					scheduled.scheduleAtFixedRate(pagerTask, 2, 4,
							TimeUnit.SECONDS);

					timeScheduled = Executors
							.newSingleThreadScheduledExecutor();
					task = new TimerTask() {
						@Override
						public void run() {
							isPause = false;
						}
					};

					// 刷新界面
					currentViewPagerAdapter.notifyDataSetChanged();

				} else {
					rlCurrentOrder.setVisibility(View.GONE);
					ivNoOrder.setVisibility(View.VISIBLE);
				}

			} else {
				rlCurrentOrder.setVisibility(View.GONE);
				ivNoOrder.setVisibility(View.VISIBLE);
			}
			break;
		case GET_BANNER_REQUEST:
			totalCount = response.totalCount;
			mPagerList = response.data;
			View view;
			StringBuilder sbImage = new StringBuilder();
			// llpagePoint.removeAllViews();
			for (int i = 0; i < totalCount; i++) {
				// 每循环一次, 向LinearLayout布局中添加一个View.
				view = new View(CamaridoApp.instance);
				view.setBackgroundResource(R.drawable.point_normal);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						13, 13);
				if (i != 0) {
					params.leftMargin = 20;
				}
				view.setLayoutParams(params);
				// llpagePoint.addView(view);

				sbImage.append(i == (totalCount - 1) ? mPagerList.get(i).ImageUrl
						: mPagerList.get(i).ImageUrl + ",");
			}
			mPagerAdapter.setList(mPagerList);
			mPagerAdapter.notifyDataSetChanged();
			circleIndicator.setViewPager(vpHome);
			// 设置首页轮播缓存
			mPreManager.setCarrBannerInfo(sbImage.toString());
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);

		switch (requestId) {
		case GET_BANNER_REQUEST:// 获取首页图片
			showToast("连接超时,请检查网络");
			break;

		case CURRENT_ORDER_REQUEST:// 获取热门订单
			rlCurrentOrder.setVisibility(View.GONE);
			ivNoOrder.setVisibility(View.VISIBLE);
			break;
		}

	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		MainActivity activity = (MainActivity) getActivity();
		activity.getActionBar().show();
		activity.setActionBarTitle(getString(R.string.app_name));
		// String showTestname;
		// activity.setActionBarLeft(R.drawable.home_order_status);
		// if (HostUtil.type == HostType.DEBUG_HOST) {
		// showTestname=getString(R.string.ceshihuanjing_bug);
		// activity.setActionBarRight(true,0,showTestname);
		// } else if (HostUtil.type == HostType.DEMO_HOST) {
		// showTestname=getString(R.string.ceshihuanjing_demo);
		// activity.setActionBarRight(true,0,showTestname);
		// }
		int type = CacheUtils.getInt(getActivity(), "type", 0);

		if (type == 1) {
			activity.setActionBarLeft(R.drawable.test);
		} else if (type == 2) {
			activity.setActionBarLeft(R.drawable.demo);
		}

		if (mPreManager.isLogin()) {
			activity.setActionBarRight(false, 0, "");
		} else {
			activity.setActionBarRight(true, 0,
					getActivity().getString(R.string.login));

		}
		activity.setOnActionBarLeftClickListener(null);
		activity.setOnActionBarRightClickListener(false,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 登录界面
						LoginActicity.invoke(getActivity());
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		// 初始化titileBar
		initActionBar();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0)
			return;
		FocusonRoute focusonRoute = mHotOrderAdapter.getItem(position - 1);
		String focusonId = focusonRoute.id;// 包的id
		if (!TextUtils.isEmpty(focusonId) && focusonId != null) {
			// 跳转到包详情界面
			PackageListDetailActivity.invoke(mainActivity, focusonId);
		}
	}

	public interface IChangeMain {
		void changeTab(int tabIndex);
	}

}
