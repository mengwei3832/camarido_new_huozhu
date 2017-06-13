package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.adapter.GridViewPrivinceJianAdapter;
import com.yunqi.clientandroid.employer.adapter.VehicleListAdapter;
import com.yunqi.clientandroid.employer.adapter.VehicleListAdapter.OnCarListDelteListener;
import com.yunqi.clientandroid.employer.entity.VehicleList;
import com.yunqi.clientandroid.employer.request.AddVehicleRequest;
import com.yunqi.clientandroid.employer.request.GetVehicleRequest;
import com.yunqi.clientandroid.employer.request.RemoveTenantVehicleRequest;
import com.yunqi.clientandroid.entity.GetProvinceJian;
import com.yunqi.clientandroid.employer.request.GetPrivinceJianRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 
 * @Description:车辆管理
 * @ClassName: CarManagerAcitivity
 * @author: chengtao
 * @date: 2016年6月16日 下午11:49:33
 * 
 */
@SuppressLint("InflateParams")
public class CarManagerAcitivity extends BaseActivity implements
		OnRefreshListener2<ListView>, OnClickListener, OnCarListDelteListener {
	private Button btnProvence;
	private EditText etCarNum;
	private Button btnAddCar;
	private PullToRefreshListView lvCar;
	private List<VehicleList> mList;
	private VehicleListAdapter adapter;

	private View parentView;
	private GridView mGvChoose;
	private PopupWindow mPrivincePopup; // 省简称的PopupWindow
	private ArrayList<GetProvinceJian> mPrivinceJian = new ArrayList<GetProvinceJian>();// 存放简称
	private GridViewPrivinceJianAdapter mGridViewPrivinceJianAdapter;

	// 页面请求
	private GetVehicleRequest getVehicleRequest;
	private RemoveTenantVehicleRequest removeTenantVehicleRequest;
	private AddVehicleRequest addVehicleRequest;
	private GetPrivinceJianRequest mGetPrivinceJianRequest;
	// 请求ID
	private final static int GET_VEHICLE_REQUEST = 1;
	private final static int REMOVE_TENANT_VEHICLE_REQUEST = 2;
	private final static int ADD_VEHICLE_REQUEST = 3;
	private final int GET_SHENG_JIAN = 4;

	private int mPageIndex = 1;
	private final int PAGE_COUNT = 10;
	private boolean isloadingFinish = false;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_car_manager;
	}

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		parentView = LayoutInflater.from(mContext).inflate(
				R.layout.activity_startnewvehicle, null);

		btnProvence = obtainView(R.id.btn_provence);
		etCarNum = obtainView(R.id.et_car_num);
		btnAddCar = obtainView(R.id.btn_add_car);
		lvCar = obtainView(R.id.lv_car);
		mList = new ArrayList<VehicleList>();
		adapter = new VehicleListAdapter(mContext, mList, this);
		lvCar.setAdapter(adapter);

		initActionBar();
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午4:11:23
	 * @Author : chengtao
	 */
	private void initActionBar() {
		setActionBarTitle("");
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
		getProvinceJian();
		getCarList();
	}

	/**
	 * 
	 * @Description:获取省份简称
	 * @Title:getProvinceJian
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 上午11:35:26
	 * @Author : chengtao
	 */
	private void getProvinceJian() {
		mGetPrivinceJianRequest = new GetPrivinceJianRequest(
				CarManagerAcitivity.this);
		mGetPrivinceJianRequest.setRequestId(GET_SHENG_JIAN);
		httpPost(mGetPrivinceJianRequest);
	}

	/**
	 * 
	 * @Description:获取车辆列表
	 * @Title:getCarList
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 上午12:41:31
	 * @Author : chengtao
	 */
	private void getCarList() {
		mList.clear();
		adapter.notifyDataSetChanged();
		getVehicleRequest = new GetVehicleRequest(mContext, mPageIndex,
				PAGE_COUNT);
		getVehicleRequest.setRequestId(GET_VEHICLE_REQUEST);
		httpPost(getVehicleRequest);
	}

	@Override
	protected void setListener() {
		lvCar.setOnRefreshListener(this);
		btnProvence.setOnClickListener(this);
		btnAddCar.setOnClickListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex = 1;
		getCarList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			getCarList();
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lvCar.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		switch (requestId) {
		case GET_VEHICLE_REQUEST:// 获取车辆列表
			if (isSuccess) {
				int toatalCount = response.totalCount;
				ArrayList<VehicleList> carLists = response.data;
				if (carLists != null) {
					mList.addAll(carLists);
				}
				if (toatalCount <= mList.size()) {
					isloadingFinish = true;
				}
				adapter.notifyDataSetChanged();
				lvCar.onRefreshComplete();
			} else {
				if (message != null && !TextUtils.isEmpty(message)) {
					showToast(message);
				}
			}
			break;
		case ADD_VEHICLE_REQUEST:// 添加车辆
			showToast(message);
			break;
		case REMOVE_TENANT_VEHICLE_REQUEST:// 删除车辆
			showToast(message);
			if (isSuccess) {
				getCarList();
			}
			break;
		case GET_SHENG_JIAN:
			if (isSuccess) {
				ArrayList<GetProvinceJian> mPrivince = response.data;
				if (mPrivince.size() != 0) {
					mPrivinceJian.addAll(mPrivince);
				}

			} else {
				showToast(message);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_provence:// 设置车辆省份
			// 弹出选择省的简称
			showChoosePrivince();
			break;
		case R.id.btn_add_car:// 添加车辆
			AddCar();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:添加车辆
	 * @Title:AddCar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 上午11:46:50
	 * @Author : chengtao
	 */
	private void AddCar() {
		String mprivince = btnProvence.getText().toString().trim();
		// 验证车牌号码是否可添加
		String vehicleNo = etCarNum.getText().toString().trim();
		// 将字符串中的小写转换为大写
		vehicleNo = StringUtils.swapCase(vehicleNo);

		Log.e("TAG", "省份的简称-----" + mprivince);

		if (TextUtils.isEmpty(vehicleNo)) {
			showToast("请输入车牌号码");
			return;
		}

		// 正则校验车牌号码
		if (!vehicleNo.matches("[A-Z]{1}[A-Z_0-9]{5}")) {
			showToast("请输入正确的车牌号码");
			return;
		}

		String mVehicleCarNumber = mprivince + vehicleNo;

		Log.e("TAG", "--------车牌号-----" + mVehicleCarNumber);

		addVehicleRequest = new AddVehicleRequest(mContext, mVehicleCarNumber);
		addVehicleRequest.setRequestId(ADD_VEHICLE_REQUEST);
		httpPost(addVehicleRequest);
	}

	/**
	 * @Description:弹出选择省份的简称框
	 * @Title:showChoosePrivince
	 * @return:void
	 * @throws
	 * @Create: 2016-6-2 下午3:31:35
	 * @Author : chengTao
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

				btnProvence.setText(mPrivinceText);
				mPrivincePopup.dismiss();
			}
		});
	}

	/**
	 * 
	 * @Description:设置省份简称框
	 * @Title:settingPrivince
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 上午11:41:43
	 * @Author : chengtao
	 */
	@SuppressWarnings("deprecation")
	private void settingPrivince() {
		mPrivincePopup = new PopupWindow(CarManagerAcitivity.this);

		View privince_view = CarManagerAcitivity.this.getLayoutInflater()
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
				mPrivinceJian, CarManagerAcitivity.this);
		mGvChoose.setAdapter(mGridViewPrivinceJianAdapter);
	}

	/**
	 * 
	 * @Description:删除车辆
	 * @Title:deleteCar
	 * @param position
	 * @throws
	 * @Create: 2016年6月17日 上午11:44:57
	 * @Author : zhm
	 */
	@Override
	public void deleteCar(int position) {
		int vehicleTenantId = mList.get(position).Id;
		removeTenantVehicleRequest = new RemoveTenantVehicleRequest(mContext,
				vehicleTenantId);
		removeTenantVehicleRequest.setRequestId(REMOVE_TENANT_VEHICLE_REQUEST);
		httpGet(removeTenantVehicleRequest);
	}

	/**
	 * 
	 * @Description:车辆管理界面跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午3:28:22
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, CarManagerAcitivity.class);
		activity.startActivity(intent);
	}

}
