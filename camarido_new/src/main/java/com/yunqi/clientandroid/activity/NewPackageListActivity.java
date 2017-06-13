package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.OrderDetailListAdapter;
import com.yunqi.clientandroid.entity.FocusCount;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.http.request.AddFocusPathRequest;
import com.yunqi.clientandroid.http.request.CancelFocusPathRequest;
import com.yunqi.clientandroid.http.request.GetFocusCountRequest;
import com.yunqi.clientandroid.http.request.GetPackageListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.MiPushUtil;

/**
 * @deprecated:所有路线点击进去的包列表
 */
public class NewPackageListActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>,
		AdapterView.OnItemClickListener {

	private TextView tvStartProvince;
	private TextView tvStartCity;
	private TextView tvEndProvince;
	private TextView tvEndCity;
	private TextView tvOrder;
	private TextView tvDistance;
	private ImageButton is_focuson;
	private TextView tvFocusonCount;
	private PullToRefreshListView packageRefreshListView;
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private boolean isEnd;// 是否服务器无数据返回
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private ListView packageListView;
	private ArrayList<FocusonRoute> packageItemList = new ArrayList<FocusonRoute>();
	private OrderDetailListAdapter mOrderDetailListAdapter;
	private final String ISFOCUSON = "ISFOCUSON";
	private boolean focuson;
	private PackagePath mPackagePath;
	private int mFocusCount;

	private static final String EXTRA_PACKAGE_PATH = "PACKAGE_PATH";

	private GetFocusCountRequest mGetFocusCountRequest;
	private GetPackageListRequest mGetFocusonRouteRequest;
	private AddFocusPathRequest mAddFocusPathRequest;
	private CancelFocusPathRequest mCancelFocusPathRequest;

	private final int GET_FOCUS_COUNT_REQUEST = 21;
	private final int GET_FOCUS_ROUTE_REQUEST = 22;
	private final int ADD_FOCUS_PATH_REQUEST = 23;
	private final int CANCEL_FOCUS_PATH_REQUEST = 24;
	private String packageBeginCity;
	private String packageEndCity;
	private String orderCount;
	private String packageCount;
	private String packageDistance;
	private String packageBeginProvinceText;
	private String packageEndProvinceText;
	private String packageBeginCityText;
	private String packageEndCityText;

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
						String packageId = packageItemList.get(position - 1).id;
						if (!TextUtils.isEmpty(packageId) && packageId != null) {
							PackageListDetailActivity.invoke(
									NewPackageListActivity.this, packageId);
						}
					}
				});

		mOrderDetailListAdapter = new OrderDetailListAdapter(
				NewPackageListActivity.this, packageItemList);
		packageListView.setAdapter(mOrderDetailListAdapter);

	}

	// 访问服务器获取包列表数据的方法
	private void getPackageListDataFromServer(int pageIndex,
			String packageBeginCity, String packageEndCity) {
		count = pageIndex * pageSize;
		mGetFocusonRouteRequest = new GetPackageListRequest(this, pageIndex,
				pageSize, packageBeginCity, packageEndCity);
		mGetFocusonRouteRequest.setRequestId(GET_FOCUS_ROUTE_REQUEST);
		httpPostJson(mGetFocusonRouteRequest);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_new_packagelist;
	}

	// 初始化控件的方法
	@Override
	protected void initView() {
		// 初始化titileBar
		initActionbar();
		// 清空集合
		packageItemList.clear();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey(EXTRA_PACKAGE_PATH)) {
			mPackagePath = (PackagePath) bundle
					.getSerializable(EXTRA_PACKAGE_PATH);
		}

		if (mPackagePath != null) {
			packageBeginCity = mPackagePath.packageBeginCity;
			packageEndCity = mPackagePath.packageEndCity;
			orderCount = mPackagePath.orderCount;
			packageCount = mPackagePath.packageCount;
			packageDistance = mPackagePath.packageDistance;
			packageBeginProvinceText = mPackagePath.packageBeginProvinceText;
			packageEndProvinceText = mPackagePath.packageEndProvinceText;
			packageBeginCityText = mPackagePath.packageBeginCityText;
			packageEndCityText = mPackagePath.packageEndCityText;
		}

		// 获取关注人数和关注状态
		if (!TextUtils.isEmpty(packageBeginCity) && packageBeginCity != null
				&& !TextUtils.isEmpty(packageEndCity) && packageEndCity != null) {
			getFocusonCount(packageBeginCity, packageEndCity);
		}

		tvStartProvince = (TextView) findViewById(R.id.tv_packagelist_startabove);
		tvStartCity = (TextView) findViewById(R.id.tv_packagelist_startfollow);
		tvEndProvince = (TextView) findViewById(R.id.tv_packagelist_endabove);
		tvEndCity = (TextView) findViewById(R.id.tv_packagelist_endfollow);
		tvOrder = (TextView) findViewById(R.id.tv_count);
		tvDistance = (TextView) findViewById(R.id.tv_packagelist_distance);
		is_focuson = (ImageButton) findViewById(R.id.ib_packagelist_focuson);
		tvFocusonCount = (TextView) findViewById(R.id.tv_packagelist_focusoncount);

		packageRefreshListView = (PullToRefreshListView) findViewById(R.id.packagelist_refreshlistview);

		if (!TextUtils.isEmpty(orderCount) && orderCount != null
				&& !TextUtils.isEmpty(packageCount) && packageCount != null) {
			tvOrder.setText(Html.fromHtml("<font color='#ff4400'>" + orderCount
					+ "/" + packageCount + "</font>" + "单"));
		}

		if (!TextUtils.isEmpty(packageDistance) && packageDistance != null) {
			tvDistance.setText("全程" + packageDistance + "公里");
		}

		if (!TextUtils.isEmpty(packageBeginProvinceText)
				&& packageBeginProvinceText != null) {
			tvStartProvince.setText(packageBeginProvinceText);
		}

		if (!TextUtils.isEmpty(packageEndProvinceText)
				&& packageEndProvinceText != null) {
			tvEndProvince.setText(packageEndProvinceText);
		}

		if (!TextUtils.isEmpty(packageBeginCityText)
				&& packageBeginCityText != null) {
			tvStartCity.setText(packageBeginCityText);
		}

		if (!TextUtils.isEmpty(packageEndCityText)
				&& packageEndCityText != null) {
			tvEndCity.setText(packageEndCityText);
		}

		// focuson = CacheUtils.getBoolean(getApplicationContext(), ISFOCUSON,
		// false);
		//
		// if (focuson) {
		// is_focuson.setBackgroundResource(R.drawable.packagelist_focuson);
		// } else {
		// is_focuson.setBackgroundResource(R.drawable.packagelist_cancel_foc);
		// }

		is_focuson.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击是否关注
				// is_focuson.setEnabled(false);

				// focuson = CacheUtils.getBoolean(getApplicationContext(),
				// ISFOCUSON, false);
				if (!focuson) {
					// 点击后变为关注
					if (!TextUtils.isEmpty(packageBeginCity)
							&& packageBeginCity != null
							&& !TextUtils.isEmpty(packageEndCity)
							&& packageEndCity != null) {

						mAddFocusPathRequest = new AddFocusPathRequest(
								NewPackageListActivity.this, packageBeginCity,
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
								NewPackageListActivity.this, packageBeginCity,
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
		packageRefreshListView.setOnItemClickListener(this);
	}

	// 初始化titileBar
	private void initActionbar() {
		setActionBarTitle("订单详情");
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(false, 0, "");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NewPackageListActivity.this.finish();
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

		packageItemList.clear();
		if (packageItemList != null) {
			mOrderDetailListAdapter.notifyDataSetChanged();
		}

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
				mFocusCount = focusCountEntity.focusCount;
				boolean isFocus = focusCountEntity.isFocus;
				focuson = isFocus;

				if (isFocus) {
					// CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
					// true);
					is_focuson
							.setBackgroundResource(R.drawable.packagelist_focuson);

				} else {
					// CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
					// false);
					is_focuson
							.setBackgroundResource(R.drawable.packagelist_cancel_foc);

				}

				if (!TextUtils.isEmpty(mFocusCount + "") && mFocusCount >= 0) {
					tvFocusonCount.setText(mFocusCount + "人关注");
				}

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

				mOrderDetailListAdapter.notifyDataSetChanged();
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
				// CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
				// true);
				is_focuson
						.setBackgroundResource(R.drawable.packagelist_focuson);
				MiPushUtil.setMiPushTopic(NewPackageListActivity.this, "A"
						+ mPackagePath.packageBeginCity
						+ mPackagePath.packageEndCity);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			is_focuson.setEnabled(true);
			mFocusCount = mFocusCount + 1;
			tvFocusonCount.setText((mFocusCount) + "人关注");
			break;
		case CANCEL_FOCUS_PATH_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast("取消关注成功");
				focuson = false;
				// CacheUtils.putBoolean(getApplicationContext(), ISFOCUSON,
				// false);
				is_focuson
						.setBackgroundResource(R.drawable.packagelist_cancel_foc);
				MiPushUtil.setMiPushUnTopic(NewPackageListActivity.this, "A"
						+ mPackagePath.packageBeginCity
						+ mPackagePath.packageEndCity);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			is_focuson.setEnabled(true);
			mFocusCount = mFocusCount - 1;
			tvFocusonCount.setText((mFocusCount) + "人关注");
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		packageRefreshListView.onRefreshComplete();
		switch (requestId) {
		case GET_FOCUS_ROUTE_REQUEST:
			showToast(getString(R.string.net_error_toast));
			break;
		case ADD_FOCUS_PATH_REQUEST:
			showToast(getString(R.string.net_error_toast));
			break;
		case CANCEL_FOCUS_PATH_REQUEST:
			showToast(getString(R.string.net_error_toast));
			break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FocusonRoute focusonRoute = mOrderDetailListAdapter
				.getItem(position - 1);
		String focusonId = focusonRoute.id;// 关注路线下的包id
		if (!TextUtils.isEmpty(focusonId) && focusonId != null) {
			PackageListDetailActivity.invoke(NewPackageListActivity.this,
					focusonId);
		}
	}

	/**
	 * 本界面跳转的方法
	 */
	public static void invoke(Context context, PackagePath packagePath) {
		Intent intent = new Intent(context, NewPackageListActivity.class);
		intent.putExtra(EXTRA_PACKAGE_PATH, packagePath);
		context.startActivity(intent);
	}

}
