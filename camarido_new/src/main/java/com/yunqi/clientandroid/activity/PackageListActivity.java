package com.yunqi.clientandroid.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.FocusonRouteAdapter;
import com.yunqi.clientandroid.entity.FocusCount;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.http.request.AddFocusPathRequest;
import com.yunqi.clientandroid.http.request.CancelFocusPathRequest;
import com.yunqi.clientandroid.http.request.GetFocusCountRequest;
import com.yunqi.clientandroid.http.request.GetPackageListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.MiPushUtil;

import java.util.ArrayList;

/**
 * @deprecated:所有路线点击进去的包列表
 */
public class PackageListActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView> {

	private String packageBeginCity;
	private String packageEndCity;
	private String packageBeginCityText;
	private String packageEndCityText;
	private String packageCount;
	private String orderCount;
	private String packageDistance;
	private TextView start_above;
	private TextView start_follow;
	private TextView end_above;
	private TextView end_follow;
	private TextView package_Count;
	private TextView package_order;
	private TextView package_distance;
	private String startabove;
	private String startfollow;
	private String endabove;
	private String endfollow;
	private ImageButton is_focuson;
	private TextView focusoncount;
	private PullToRefreshListView packageRefreshListView;
	private ProgressBar mProgress;
	private int pageIndex = 1;
	private int PageSize = 10;
	private boolean isEnd;
	private int totalCount;
	private int count;
	private ListView packageListView;
	private ArrayList<FocusonRoute> packageItemList = new ArrayList<FocusonRoute>();
	private FocusonRouteAdapter focusonRouteAdapter;
	private final String ISFOCUSON = "ISFOCUSON";
	private boolean focuson;

	private GetFocusCountRequest mGetFocusCountRequest;
	private GetPackageListRequest mGetFocusonRouteRequest;
	private AddFocusPathRequest mAddFocusPathRequest;
	private CancelFocusPathRequest mCancelFocusPathRequest;

	private final int GET_FOCUS_COUNT_REQUEST = 21;
	private final int GET_FOCUS_ROUTE_REQUEST = 22;
	private final int ADD_FOCUS_PATH_REQUEST = 23;
	private final int CANCEL_FOCUS_PATH_REQUEST = 24;

	// 获取关注人数和是否关注的方法
	private void getFocusonCount(String packageBeginCity, String packageEndCity) {
		mGetFocusCountRequest = new GetFocusCountRequest(this,
				packageBeginCity, packageEndCity);
		mGetFocusCountRequest.setRequestId(GET_FOCUS_COUNT_REQUEST);
		httpPost(mGetFocusCountRequest);
	}

	private void initPullToRefreshView() {

		packageRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		packageRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		packageRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		packageRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		packageListView = packageRefreshListView.getRefreshableView();
		packageListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		packageListView.setDividerHeight(20);
		packageListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		packageRefreshListView.setOnRefreshListener(this);
		// item的点击事件
		packageRefreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(PackageListActivity.this,
								PackageListDetailActivity.class);
						String packageId = packageItemList.get(position - 1).id;
						intent.putExtra("packageId", packageId);
						startActivity(intent);
					}
				});

		focusonRouteAdapter = new FocusonRouteAdapter(PackageListActivity.this,
				packageItemList);
		packageListView.setAdapter(focusonRouteAdapter);

	}

	// 访问服务器获取包列表数据的方法
	private void getPackageListDataFromServer(int pageIndex,
			String packageBeginCity, String packageEndCity) {
		// showLoading(true);
		count = pageIndex * PageSize;
		mGetFocusonRouteRequest = new GetPackageListRequest(this, pageIndex,
				PageSize, packageBeginCity, packageEndCity);
		mGetFocusonRouteRequest.setRequestId(GET_FOCUS_ROUTE_REQUEST);
		httpPostJson(mGetFocusonRouteRequest);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_packagelist;
	}

	// 初始化控件的方法
	@Override
	protected void initView() {
		initActionbar();
		packageItemList.clear();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("packageBeginCity")) {
			packageBeginCity = bundle.getString("packageBeginCity");
		}
		if (bundle != null && bundle.containsKey("packageBeginCityText")) {
			packageBeginCityText = bundle.getString("packageBeginCityText");
		}
		if (bundle != null && bundle.containsKey("packageEndCity")) {
			packageEndCity = bundle.getString("packageEndCity");
		}
		if (bundle != null && bundle.containsKey("packageEndCityText")) {
			packageEndCityText = bundle.getString("packageEndCityText");
		}
		if (bundle != null && bundle.containsKey("packageCount")) {
			packageCount = bundle.getString("packageCount");
		}
		if (bundle != null && bundle.containsKey("orderCount")) {
			orderCount = bundle.getString("orderCount");
		}
		if (bundle != null && bundle.containsKey("packageDistance")) {
			packageDistance = bundle.getString("packageDistance");
		}

		// 获取关注人数和关注状态
		if (!TextUtils.isEmpty(packageBeginCity) && packageBeginCity != null
				&& !TextUtils.isEmpty(packageEndCity) && packageEndCity != null) {
			getFocusonCount(packageBeginCity, packageEndCity);
		}

		start_above = (TextView) findViewById(R.id.tv_packagelist_startabove);
		start_follow = (TextView) findViewById(R.id.tv_packagelist_startfollow);
		end_above = (TextView) findViewById(R.id.tv_packagelist_endabove);
		end_follow = (TextView) findViewById(R.id.tv_packagelist_endfollow);
		package_Count = (TextView) findViewById(R.id.tv_packagelist_package);
		package_order = (TextView) findViewById(R.id.tv_packagelist_order);
		package_distance = (TextView) findViewById(R.id.tv_packagelist_distance);
		is_focuson = (ImageButton) findViewById(R.id.ib_packagelist_focuson);
		focusoncount = (TextView) findViewById(R.id.tv_packagelist_focusoncount);

		packageRefreshListView = (PullToRefreshListView) findViewById(R.id.packagelist_refreshlistview);
		mProgress = (ProgressBar) findViewById(R.id.pb_packagelist_progress);

		if (!TextUtils.isEmpty(packageBeginCityText)) {
			startabove = packageBeginCityText.split(" ")[0];
			startfollow = packageBeginCityText.split(" ")[1];
		}

		if (!TextUtils.isEmpty(packageEndCityText)) {
			endabove = packageEndCityText.split(" ")[0];
			endfollow = packageEndCityText.split(" ")[1];
		}

		if (!TextUtils.isEmpty(startabove)) {
			start_above.setText(startabove);
		}
		if (!TextUtils.isEmpty(startfollow)) {
			start_follow.setText(startfollow);
		}

		if (!TextUtils.isEmpty(endabove)) {
			end_above.setText(endabove);
		}
		if (!TextUtils.isEmpty(endfollow)) {
			end_follow.setText(endfollow);
		}

		if (!TextUtils.isEmpty(packageCount) && packageCount != null) {
			package_Count.setText(packageCount + "包/");
		}

		if (!TextUtils.isEmpty(orderCount) && orderCount != null) {
			package_order.setText(orderCount + "单");
		}

		if (!TextUtils.isEmpty(packageDistance) && packageDistance != null) {
			package_distance.setText("全程" + packageDistance + "公里");
		}

		focuson = CacheUtils.getBoolean(getApplicationContext(), ISFOCUSON,
				false);

		if (focuson) {
			is_focuson.setBackgroundResource(R.drawable.packagelist_focuson);
		} else {
			is_focuson.setBackgroundResource(R.drawable.packagelist_cancel_foc);
		}

		is_focuson.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击是否关注
				is_focuson.setEnabled(false);

				focuson = CacheUtils.getBoolean(getApplicationContext(),
						ISFOCUSON, false);
				if (!focuson) {
					// 点击后变为关注
					if (!TextUtils.isEmpty(packageBeginCity)
							&& packageBeginCity != null
							&& !TextUtils.isEmpty(packageEndCity)
							&& packageEndCity != null) {
						mAddFocusPathRequest = new AddFocusPathRequest(
								PackageListActivity.this, packageBeginCity,
								packageEndCity);
						mAddFocusPathRequest
								.setRequestId(ADD_FOCUS_PATH_REQUEST);
						httpPost(mAddFocusPathRequest);
					}

				} else {
					// 点击后变为取消关注
					if (!TextUtils.isEmpty(packageBeginCity)
							&& packageBeginCity != null
							&& !TextUtils.isEmpty(packageEndCity)
							&& packageEndCity != null) {
						mCancelFocusPathRequest = new CancelFocusPathRequest(
								PackageListActivity.this, packageBeginCity,
								packageEndCity);
						mCancelFocusPathRequest
								.setRequestId(CANCEL_FOCUS_PATH_REQUEST);
						httpPost(mCancelFocusPathRequest);
					}
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		packageItemList.clear();
		pageIndex = 1;
		// 访问服务器获取包列表数据
		if (!TextUtils.isEmpty(packageBeginCity) && packageBeginCity != null
				&& !TextUtils.isEmpty(packageEndCity) && packageEndCity != null) {
			getPackageListDataFromServer(pageIndex, packageBeginCity,
					packageEndCity);
		}
		// 初始化刷新view
		initPullToRefreshView();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {

	}

	// 初始化actionbar
	private void initActionbar() {
		setActionBarTitle("抢单");
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(false, 0, "");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PackageListActivity.this.finish();
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (packageItemList == null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					packageRefreshListView.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (packageItemList != null) {
			focusonRouteAdapter.notifyDataSetChanged();
		}

		packageItemList.clear();

		pageIndex = 1;
		if (!TextUtils.isEmpty(packageBeginCity) && packageBeginCity != null
				&& !TextUtils.isEmpty(packageEndCity) && packageEndCity != null) {
			getPackageListDataFromServer(pageIndex, packageBeginCity,
					packageEndCity);
		}

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			if (!TextUtils.isEmpty(packageBeginCity)
					&& packageBeginCity != null
					&& !TextUtils.isEmpty(packageEndCity)
					&& packageEndCity != null) {
				getPackageListDataFromServer(pageIndex, packageBeginCity,
						packageEndCity);
			}
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					packageRefreshListView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
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
		case GET_FOCUS_COUNT_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				FocusCount focusCountEntity = (FocusCount) response.singleData;
				int focusCount = focusCountEntity.focusCount;
				boolean isFocus = focusCountEntity.isFocus;

				if (isFocus) {
					CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
							true);
					is_focuson
							.setBackgroundResource(R.drawable.packagelist_focuson);

				} else {
					CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
							false);
					is_focuson
							.setBackgroundResource(R.drawable.packagelist_cancel_foc);

				}

				focusoncount.setText(focusCount + "人关注");

			}
			break;
		case GET_FOCUS_ROUTE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// showLoading(false);
			isEnd = false;
			if (isSuccess) {
				ArrayList<FocusonRoute> focusonData = response.data;
				totalCount = response.totalCount;
				if (focusonData != null) {
					packageItemList.addAll(focusonData);
				}

				if (packageItemList.size() == 0) {
					showToast("暂无该路线下的包信息");
				}

				if (totalCount <= count) {
					isEnd = true;
				}

				focusonRouteAdapter.notifyDataSetChanged();
				packageRefreshListView.onRefreshComplete();// 结束刷新的方法

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case ADD_FOCUS_PATH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast("谢谢您的关注");
				focuson = true;
				CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON, true);
				is_focuson
						.setBackgroundResource(R.drawable.packagelist_focuson);
				MiPushUtil.setMiPushTopic(PackageListActivity.this, "A"
						+ packageBeginCity + packageEndCity);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			is_focuson.setEnabled(true);
			break;
		case CANCEL_FOCUS_PATH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast("取消关注成功");
				focuson = false;
				CacheUtils
						.putBoolean(getApplicationContext(), ISFOCUSON, false);
				is_focuson
						.setBackgroundResource(R.drawable.packagelist_cancel_foc);
				MiPushUtil.setMiPushUnTopic(PackageListActivity.this, "A"
						+ packageBeginCity + packageEndCity);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			is_focuson.setEnabled(true);
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

}
