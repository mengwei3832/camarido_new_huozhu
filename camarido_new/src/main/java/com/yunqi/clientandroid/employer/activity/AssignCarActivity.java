package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.baidu.pano.platform.http.s;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.StartNewVehicleActivity;
import com.yunqi.clientandroid.adapter.GridViewPrivinceJianAdapter;
import com.yunqi.clientandroid.employer.adapter.AssignCarAdapter;
import com.yunqi.clientandroid.employer.adapter.AssignCarAdapter.ViewHolder;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;
import com.yunqi.clientandroid.employer.entity.GetSunCar;
import com.yunqi.clientandroid.employer.request.CarPaiAddRequest;
import com.yunqi.clientandroid.employer.request.GetAddCarListRequest;
import com.yunqi.clientandroid.employer.request.GetSumCarRequest;
import com.yunqi.clientandroid.employer.request.TijiaoZhipaiCarRequest;
import com.yunqi.clientandroid.entity.GetProvinceJian;
import com.yunqi.clientandroid.http.request.GetPrivinceJianRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:class 指派车辆页面
 * @ClassName: AssignCarActivity
 * @author: zhm
 * @date: 2016-5-17 上午9:52:14
 * 
 */
public class AssignCarActivity extends BaseActivity implements
		View.OnClickListener, OnItemClickListener,
		PullToRefreshBase.OnRefreshListener2<ListView> {
	/* 页面上的控件对象 */
	private TextView tvAssignNumberAdd;// 指派车辆的数量
	private EditText etAssignAddCar;// 添加车辆输入框
	private Button btAssignAddCar; // 添加车辆按钮
	private PullToRefreshListView lvAssignCarList;// listview列表
	private Button btAssignZhipai; // 指派车辆按钮
	private AssignCarAdapter mAssignCarAdapter;
	private int checkNum; // 记录选中的条目数量
	private ImageView ivAssignBlank;// 无列表时显示
	private ProgressBar mProgress;
	private TextView tvAssignProvince;
	private GridView mGvChoose;

	// 分页请求的参数
	private final int PAGE_COUNT = 10;
	private int mPageIndex = 1;
	private boolean isLoadingFinish = false;
	private int count;

	private int mPackageCount, mOrderCount;
	private int tenantId, packageId; // 公司ID，包ID

	private PopupWindow mPrivincePopup; // 省简称的PopupWindow
	private View parentView;
	private ArrayList<GetProvinceJian> mPrivinceJian = new ArrayList<GetProvinceJian>();// 存放简称
	private GridViewPrivinceJianAdapter mGridViewPrivinceJianAdapter;

	private ArrayList<CarListZhipai> mZPlist = new ArrayList<CarListZhipai>();// 存储指派车辆列表
	// 存储车辆ID的集合
	private ArrayList<String> mVehicleID = new ArrayList<String>();
	// 存储车主姓名的集合
	private ArrayList<String> mVehicleName = new ArrayList<String>();
	// 存储车牌号的集合
	private ArrayList<String> mVehicleNo = new ArrayList<String>();

	/* 页面请求类 */
	private GetAddCarListRequest mGetAddCarListRequest;
	private CarPaiAddRequest mCarPaiAddRequest;
	private TijiaoZhipaiCarRequest mTijiaoZhipaiCarRequest;
	private GetSumCarRequest mGetSumCarRequest;
	private GetPrivinceJianRequest mGetPrivinceJianRequest;

	/* 页面请求ID */
	private final int GET_ADD_CARLIST = 1;
	private final int GET_ADD_REQUEST = 2;
	private final int GET_TIJIAO_CARCHECK = 3;
	private final int GET_SUMCAR_NUMBER = 4;
	private final int GET_SHENG_JIAN = 5;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_assigncar;
	}

	@Override
	protected void initView() {
		initActionBar();

		parentView = LayoutInflater.from(AssignCarActivity.this).inflate(
				R.layout.employer_activity_assigncar, null);

		tvAssignNumberAdd = obtainView(R.id.tv_assign_carNumberAdd);
		etAssignAddCar = obtainView(R.id.et_assign_addCar);
		btAssignAddCar = obtainView(R.id.bt_assign_addCar);
		lvAssignCarList = obtainView(R.id.lv_assign_carList);
		btAssignZhipai = obtainView(R.id.bt_assign_zhipai);
		ivAssignBlank = obtainView(R.id.iv_assign_blank);
		mProgress = obtainView(R.id.pb_assign_employer);
		tvAssignProvince = obtainView(R.id.tv_assign_province);
		mGvChoose = obtainView(R.id.gv_assign_sheng);

		lvAssignCarList.setMode(PullToRefreshBase.Mode.BOTH);
		lvAssignCarList.setOnRefreshListener(this);

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_assign_carNumber));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignCarActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {
		// 获取传过来的数据
		packageId = Integer.parseInt(getIntent().getStringExtra("packageId"));
		tenantId = Integer.parseInt(getIntent().getStringExtra("tenantId"));

		// 请求车辆列表的数据
		// getAddCarListDetail(PAGE_COUNT,mPageIndex);

		// 请求签收车数和在途车数
		mGetSumCarRequest = new GetSumCarRequest(AssignCarActivity.this,
				packageId);
		mGetSumCarRequest.setRequestId(GET_SUMCAR_NUMBER);
		httpPost(mGetSumCarRequest);

		// 获取省份简称的请求
		getProvinceJian();

	}

	// 获取省份简称的请求
	private void getProvinceJian() {
		mGetPrivinceJianRequest = new GetPrivinceJianRequest(
				AssignCarActivity.this);
		mGetPrivinceJianRequest.setRequestId(GET_SHENG_JIAN);
		httpPost(mGetPrivinceJianRequest);
	}

	// 请求车辆列表的数据
	private void getAddCarListDetail(int pageSize, int pageIndex) {
		mGetAddCarListRequest = new GetAddCarListRequest(AssignCarActivity.this);
		mGetAddCarListRequest.setRequestId(GET_ADD_CARLIST);
		httpPost(mGetAddCarListRequest);
	}

	@Override
	protected void setListener() {
		btAssignAddCar.setOnClickListener(this);
		btAssignZhipai.setOnClickListener(this);
		lvAssignCarList.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAddCarListDetail(PAGE_COUNT, mPageIndex);

		// 显示加载进度条
		mProgress.setVisibility(View.VISIBLE);

		mAssignCarAdapter = new AssignCarAdapter(AssignCarActivity.this,
				mZPlist);
		lvAssignCarList.setAdapter(mAssignCarAdapter);

		mAssignCarAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:下拉刷新
	 * @Title:onPullDownToRefresh
	 * @param refreshView
	 * @throws
	 * @Create: 2016年5月19日 下午4:27:02
	 * @Author : zhm
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex = 1;
		getAddCarListDetail(PAGE_COUNT, mPageIndex);
	}

	/**
	 * 
	 * @Description:上拉加载
	 * @Title:onPullUpToRefresh
	 * @param refreshView
	 * @throws
	 * @Create: 2016年5月19日 下午4:27:07
	 * @Author : zhm
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex++;
		if (!isLoadingFinish) {
			getAddCarListDetail(PAGE_COUNT, mPageIndex);
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lvAssignCarList.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 200);
		}
	}

	// 设置省份简称框
	private void settingPrivince() {
		mPrivincePopup = new PopupWindow(AssignCarActivity.this);

		View privince_view = AssignCarActivity.this.getLayoutInflater()
				.inflate(R.layout.popupwindow_privince_gridview, null);

		mPrivincePopup.setWidth(LayoutParams.MATCH_PARENT);
		mPrivincePopup.setHeight(LayoutParams.WRAP_CONTENT);
		mPrivincePopup.setBackgroundDrawable(new BitmapDrawable());
		mPrivincePopup.setContentView(privince_view);
		mPrivincePopup.setOutsideTouchable(true);
		mPrivincePopup.setFocusable(true);
		mPrivincePopup.setTouchable(true); // 设置PopupWindow可触摸
		mPrivincePopup
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		mGvChoose = (GridView) privince_view
				.findViewById(R.id.gv_popup_GridView);

		// 给适配器适配数据
		mGridViewPrivinceJianAdapter = new GridViewPrivinceJianAdapter(
				mPrivinceJian, AssignCarActivity.this);
		mGvChoose.setAdapter(mGridViewPrivinceJianAdapter);
	}

	/**
	 * @Description:弹出选择省份的简称框
	 * @Title:showChoosePrivince
	 * @return:void
	 * @throws
	 * @Create: 2016-6-2 下午3:31:35
	 * @Author : zhm
	 */
	private void showChoosePrivince() {
		if (mPrivincePopup == null) {
			settingPrivince();
		}

		mPrivincePopup.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		mGvChoose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GetProvinceJian mGetProvinceJian = mPrivinceJian.get(position);

				String mPrivinceText = mGetProvinceJian.ShortName;

				showToast(mPrivinceText);

				tvAssignProvince.setText(mPrivinceText);

				// mGvChoose.setVisibility(View.GONE);
				// mBtnNextStep.setVisibility(View.VISIBLE);
				mPrivincePopup.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_assign_province:
			// 弹出选择省的简称
			showChoosePrivince();
			break;

		case R.id.bt_assign_addCar: // 添加车辆
			// 获取输入框的车牌号
			String mCarPai = etAssignAddCar.getText().toString().trim();
			String province = tvAssignProvince.getText().toString().trim();

			String vehicleNo = province + mCarPai;

			if (TextUtils.isEmpty(mCarPai)) {
				showToast("请输入车牌号码");
				return;
			}

			// 正则校验车牌号码
			if (!vehicleNo.matches("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}")) {
				showToast("请输入正确的车牌号码");
				return;
			}

			// 将车牌号上传到服务器
			mCarPaiAddRequest = new CarPaiAddRequest(AssignCarActivity.this,
					vehicleNo);
			mCarPaiAddRequest.setRequestId(GET_ADD_REQUEST);
			httpPost(mCarPaiAddRequest);

			// 将添加按钮设为不可点击
			setViewAddEnable(false);
			break;

		case R.id.bt_assign_zhipai: // 指派车辆
			if (mVehicleID.size() != 0 && mVehicleName.size() != 0
					&& mVehicleNo.size() != 0) {
				int sVehicleIDSize = mVehicleID.size();
				int sVehicleNameIDSize = mVehicleName.size();
				int sVehicleNoSize = mVehicleNo.size();

				Log.e("TAG", "车ID-------从Adapter获取的集合的数量----------"
						+ sVehicleIDSize);
				Log.e("TAG", "车主ID-------从Adapter获取的集合的数量----------"
						+ sVehicleNameIDSize);
				Log.e("TAG", "车牌号-------从Adapter获取的集合的数量----------"
						+ sVehicleNoSize);

				String[] mVehicleIDArr = new String[sVehicleIDSize];
				String[] mVehicleNameArr = new String[sVehicleNameIDSize];
				String[] mVehicleNoArr = new String[sVehicleNoSize];

				String[] sVehicleIDArray = mVehicleID.toArray(mVehicleIDArr);
				String[] sVehicleNameIDArray = mVehicleName
						.toArray(mVehicleNameArr);
				String[] sVehicleNoArray = mVehicleNo.toArray(mVehicleNoArr);

				Log.e("TAG", "车ID-------从Adapter获取的数组----------"
						+ sVehicleIDArray.toString());
				Log.e("TAG", "车主ID-------从Adapter获取的数组----------"
						+ sVehicleNameIDArray.toString());
				Log.e("TAG", "车牌号-------从Adapter获取的数组----------"
						+ sVehicleNoArray.toString());

				mTijiaoZhipaiCarRequest = new TijiaoZhipaiCarRequest(
						AssignCarActivity.this, tenantId, packageId,
						sVehicleIDArray, sVehicleNameIDArray, sVehicleNoArray);
				mTijiaoZhipaiCarRequest.setRequestId(GET_TIJIAO_CARCHECK);
				httpPost(mTijiaoZhipaiCarRequest);
			} else {
				showToast("请指派车辆");
			}

			setViewZhiEnable(false);

			break;

		default:
			break;
		}
	}

	// 设置按钮不可重复点击的方法
	private void setViewAddEnable(boolean bEnable) {
		btAssignAddCar.setEnabled(bEnable);
	}

	// 设置按钮不可重复点击的方法
	private void setViewZhiEnable(boolean bEnable) {
		btAssignZhipai.setEnabled(bEnable);
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
		case GET_ADD_CARLIST: // 获取指派车辆列表
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ArrayList<CarListZhipai> mZhipaiList = response.data;

				if (mZhipaiList.size() != 0) {
					mZPlist.addAll(mZhipaiList);
					btAssignZhipai.setVisibility(View.VISIBLE);
					ivAssignBlank.setVisibility(View.GONE);
				} else {
					showToast("你还没有添加车辆");
					btAssignZhipai.setVisibility(View.GONE);
					ivAssignBlank.setVisibility(View.VISIBLE);
				}

				if (isSuccess) {

				}

				mAssignCarAdapter.notifyDataSetChanged();
			}
			// 隐藏加载进度条
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_ADD_REQUEST: // 添加车辆
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);
				setViewAddEnable(true);

				// 刷新页面
				mAssignCarAdapter.notifyDataSetChanged();
				onPullDownToRefresh(lvAssignCarList);

			} else {
				showToast(message);
				setViewAddEnable(true);
			}
			break;

		case GET_TIJIAO_CARCHECK: // 提交指派的车辆
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);
				setViewZhiEnable(true);
			} else {
				showToast(message);
				setViewZhiEnable(true);
			}
			break;

		case GET_SUMCAR_NUMBER: // 请求签收车数和在途车数
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				GetSunCar mGetSunCar = (GetSunCar) response.singleData;

				mPackageCount = mGetSunCar.PackageCount; // 总车数
				mOrderCount = mGetSunCar.OrderCount; // 已经报名车数

				if (mPackageCount != 0) {
					// 对指派车辆的总数赋值
					tvAssignNumberAdd
							.setText(mOrderCount + "/" + mPackageCount);
				}
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
		switch (requestId) {
		case GET_ADD_REQUEST:
			setViewAddEnable(true);
			break;

		case GET_TIJIAO_CARCHECK:
			setViewZhiEnable(true);
			break;

		default:
			break;
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, String packageId, String tenantId) {
		Intent intent = new Intent(context, AssignCarActivity.class);
		intent.putExtra("packageId", packageId);
		intent.putExtra("tenantId", tenantId);
		context.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder holder = (ViewHolder) view.getTag();

		CarListZhipai mCarListZhipai = mZPlist.get(position);

		String vehicleIdString = String.valueOf(mCarListZhipai.VehicleId);
		String vehicleNameString = String
				.valueOf(mCarListZhipai.VehicleOwnerId);
		String vehicleNoString = mCarListZhipai.VehicleNo;

		holder.cbAssignCheck.toggle();

		mAssignCarAdapter.getIsSelected().put(position,
				holder.cbAssignCheck.isChecked());

		if (holder.cbAssignCheck.isChecked() == true) {
			mVehicleID.add(position, vehicleIdString);
			mVehicleName.add(position, vehicleNameString);
			mVehicleNo.add(position, vehicleNoString);

			checkNum++;
		} else {
			mVehicleID.remove(position);
			mVehicleName.remove(position);
			mVehicleNo.remove(position);

			checkNum--;
		}

		int mNumber = mOrderCount + checkNum;

		if (mNumber > mPackageCount) {
			showToast("指派车辆总额已满，再次指派无效");
			return;
		}

		// 显示指派的车辆数量
		tvAssignNumberAdd.setText(mNumber + "/" + mPackageCount);

		mAssignCarAdapter.notifyDataSetChanged();

	}

}
