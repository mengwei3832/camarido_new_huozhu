package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.ShipAddressAdapter;
import com.yunqi.clientandroid.employer.entity.AddressBeginAnd;
import com.yunqi.clientandroid.employer.entity.ShipHeighWay;
import com.yunqi.clientandroid.employer.request.ShipHighWayRequest;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:装货地址页面
 * @ClassName: ShipAddressActivity
 * @author: mengwei
 * @date: 2016-6-22 下午3:58:58
 * 
 */
public class ShipAddressActivity extends BaseActivity implements
		View.OnClickListener, AdapterView.OnItemClickListener,
		PullToRefreshBase.OnRefreshListener2<ListView> {
	// 界面控件对象
	private RelativeLayout rlShipCommon;
	private RelativeLayout rlShipDiQu;
	private TextView tvShengShiQu;
	private EditText etShipDetail;
	private EditText etShipCompany;
	private PullToRefreshListView lvShipView;
	private Button btShipFinish;
	private ProgressBar pbShiProgress;
	private TextView tvShipRed;

	// 验证码
	private final int COMMON_ADDRESS = 1;
	public static int requestCode;
	private String detailAddress;
	private String companyName;

	private FilterManager filterManager;
	private SpManager mSpManager;

	// 保存的省市区数据
	private String provinceName, cityName, countyName;
	private int provinceId, cityId, countyId;
	private int mRequestCode;

	/** 判断是装货还是卸货 */
	private boolean mIsChoose = true;

	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int totalCount;// 返回数据的总数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();

	// 保存公共点的集合
	private ArrayList<ShipHeighWay> shipHighWayList = new ArrayList<ShipHeighWay>();
	private ShipAddressAdapter shipAddressAdapter;

	private boolean isCopyPackage = false;
	private boolean isFirstIn = true;
	private boolean etEmptyId;

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_ship_address;
	}

	@Override
	protected void initView() {

		Log.e("TAG", requestCode + "");
		filterManager = FilterManager.instance(mContext);
		mSpManager = SpManager.instance(mContext);
		mUmeng = UmengStatisticsUtils.instance(mContext);
		filterManager.clearSp();
		mIsChoose = filterManager.getZhuangXieAddress();
		initActionBar();

		// 获取传递过来的值
		mRequestCode = getIntent().getIntExtra("mRequestCode",0);
		provinceId = getIntent().getIntExtra("provinceId",0);
		provinceName = getIntent().getStringExtra("provinceName");
		cityId = getIntent().getIntExtra("cityId",0);
		cityName = getIntent().getStringExtra("cityName");
		countyId = getIntent().getIntExtra("countyId",0);
		countyName = getIntent().getStringExtra("countyName");
		detailAddress = getIntent().getStringExtra("address");
		companyName = getIntent().getStringExtra("company");
		isCopyPackage = getIntent().getBooleanExtra("isCopyPackage", false);


		rlShipCommon = obtainView(R.id.rl_ship_common);
		rlShipDiQu = obtainView(R.id.rl_ship_choose);
		tvShengShiQu = obtainView(R.id.tv_sheng_shi_qu);
		etShipDetail = obtainView(R.id.et_ship_input_detail);
		etShipCompany = obtainView(R.id.et_ship_input_company);
		lvShipView = obtainView(R.id.lv_ship_highway);
		btShipFinish = obtainView(R.id.bt_ship_finish);
		pbShiProgress = obtainView(R.id.pb_ship_progress);
		tvShipRed = obtainView(R.id.tv_ship_redtext);

		lvShipView.setMode(PullToRefreshBase.Mode.BOTH);
		lvShipView.setOnRefreshListener(this);

		// 适配器
		shipAddressAdapter = new ShipAddressAdapter(shipHighWayList, mContext);
		lvShipView.setAdapter(shipAddressAdapter);
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {

		L.e("-----mIsChoose---------"+mIsChoose);

		if (mIsChoose){
			setActionBarTitle(this.getResources().getString(
					R.string.employer_activity_title));
		} else {
			setActionBarTitle(this.getResources().getString(
					R.string.employer_activity_title1));
		}
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShipAddressActivity.this.finish();
				filterManager.clearSp();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("TAG", "onResume");
		etEmptyId = filterManager.getEtEmpty();
		if (!isFirstIn) {
			// 获取保存的省市区
			provinceName = filterManager.getProvince();
			cityName = filterManager.getCity();
			countyName = filterManager.getAreas();
			provinceId = filterManager.getProvinceId();
			cityId = filterManager.getCityId();
			countyId = filterManager.getAreasId();
			detailAddress = filterManager.getAddressDetail();
			companyName = filterManager.getCompanyCustomName();
		}

		Log.e("TAG", "详细地址-----" + detailAddress);
		Log.e("TAG", "单位名称-----" + companyName);

		Log.e("TAG", "provinceName-----" + provinceName);
		Log.e("TAG", "cityName-----" + cityName);
		Log.e("TAG", "countyName-----" + countyName);
		Log.e("TAG", "provinceId-----" + provinceId);
		Log.e("TAG", "cityId-----" + cityId);
		Log.e("TAG", "countyId-----" + countyId);
		Log.e("TAG", "detailAddress-----" + detailAddress);
		Log.e("TAG", "companyName-----" + companyName);
		if ((provinceName != null && !TextUtils.isEmpty(provinceName))
				&& (cityName != null && !TextUtils.isEmpty(cityName))
				&& (countyName != null && !TextUtils.isEmpty(countyName))) {
			tvShengShiQu.setText(Html.fromHtml("<font color='#666666'>"
					+ provinceName + cityName + countyName + "</font>"));
		}

		Log.v("TAG", "--------etEmptyId---------" + etEmptyId);

		if (etEmptyId) {
			etShipCompany.setText("");
			etShipDetail.setText("");
		} else {
			if (detailAddress != null && !TextUtils.isEmpty(detailAddress)) {
				etShipDetail.setText(detailAddress);
			}
			if (companyName != null && !TextUtils.isEmpty(companyName)) {
				etShipCompany.setText(companyName);
			}

			// 判断是否复制发包
			if (isCopyPackage) {
				if (detailAddress != null && !TextUtils.isEmpty(detailAddress)) {
					etShipDetail.setText(detailAddress);
				}

				if (companyName != null && !TextUtils.isEmpty(companyName)) {
					etShipCompany.setText(companyName);
				}
			}

			// 判断传过来的数据并给输入框赋值
			if (detailAddress != null && !TextUtils.isEmpty(detailAddress)) {
				etShipDetail.setText(detailAddress);
			}
			if (companyName != null && !TextUtils.isEmpty(companyName)) {
				etShipCompany.setText(companyName);
			}
		}
		// 通过获得的省市区请求公路指数
		getShipHighWay();
		isFirstIn = false;
	}

	/**
	 * @Description:通过获得的省市区请求公路指数
	 * @Title:getShipHighWay
	 * @return:void
	 * @throws
	 * @Create: 2016-6-22 下午6:08:45
	 * @Author : mengwei
	 */
	private void getShipHighWay() {
		if (provinceId != 0 && cityId != 0 && countyId != 0) {
			shipAddressAdapter.notifyDataSetChanged();
			// 清空集合
			shipHighWayList.clear();
			// 显示进度条
			pbShiProgress.setVisibility(View.VISIBLE);
			httpPost(new ShipHighWayRequest(mContext, pageIndex, pageSize,
					provinceId, cityId, countyId));
		}

	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		if (isSuccess) {
			ArrayList<ShipHeighWay> shiplist = response.data;

			if (shiplist != null) {
				shipHighWayList.addAll(shiplist);
			}

			if (shiplist.size() == 0) {
				tvShipRed.setVisibility(View.GONE);
			} else {
				tvShipRed.setVisibility(View.VISIBLE);
			}

			shipAddressAdapter.notifyDataSetChanged();
			lvShipView.onRefreshComplete();

			pbShiProgress.setVisibility(View.GONE);
		} else {
			showToast(message);
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void initData() {
		rlShipCommon.setOnClickListener(this);
		rlShipDiQu.setOnClickListener(this);
		lvShipView.setOnItemClickListener(this);
		btShipFinish.setOnClickListener(this);
	}

	@Override
	protected void setListener() {
		lvShipView.setOnItemClickListener(this);
		btShipFinish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ship_common:// 选择常用地址
			//友盟统计常用地址
			if (mIsChoose){
				mUmeng.setCalculateEvents("ship_begin_address_often");
			} else {
				mUmeng.setCalculateEvents("ship_end_address_often");
			}

			filterManager.clearSp();
			CommonAddressActivity.invoke(ShipAddressActivity.this);
			break;
		case R.id.rl_ship_choose:// 选择地区
			//友盟统计省市区选择
			if (mIsChoose){
				mUmeng.setCalculateEvents("ship_begin_address_province");
			} else {
				mUmeng.setCalculateEvents("ship_end_address_province");
			}

			filterManager.clearSp();
			EmployerAddProvinceActivity.invoke(ShipAddressActivity.this);
			break;
		case R.id.bt_ship_finish:// 完成
			// 获取输入框的值
			detailAddress = etShipDetail.getText().toString().trim();
			companyName = etShipCompany.getText().toString().trim();

			Log.v("TAG", "detailAddress" + detailAddress);
			Log.v("TAG", "companyName" + companyName);
			L.e("------------mRequestCode------------"+mRequestCode);

			if (provinceId == 0 || cityId == 0
					|| countyId == 0 ) {
				showToast("请填写地区信息");
				return;
			}

			if (detailAddress == null || TextUtils.isEmpty(detailAddress)) {
				showToast("请填写详细地址");
				return;
			}

			if (!StringUtils.isStrNotNull(companyName)) {
				showToast("请填写单位名称");
				return;
			}

			//友盟统计
			if (mIsChoose){
				mUmeng.setCalculateEvents("ship_begin_address_finish");
			} else {
				mUmeng.setCalculateEvents("ship_end_address_finish");
			}

			// 将地址和单位名称带回去
			Intent intent = new Intent(mContext,NewSendPackageActivity.class);
			Bundle bundle = new Bundle();
			AddressBeginAnd addressBeginAnd = new AddressBeginAnd();
			addressBeginAnd.setDetailAddress(detailAddress);
			addressBeginAnd.setCompanyName(companyName);
			addressBeginAnd.setProvinceId(provinceId);
			addressBeginAnd.setCityId(cityId);
			addressBeginAnd.setCountyId(countyId);
			addressBeginAnd.setProvinceName(provinceName);
			addressBeginAnd.setCityName(cityName);
			addressBeginAnd.setCountyName(countyName);
			addressBeginAnd.setType(mRequestCode);
//			intent.putExtra("detailAddress", detailAddress);
//			intent.putExtra("companyName", companyName);
//			intent.putExtra("provinceId", provinceId);
//			intent.putExtra("cityId", cityId);
//			intent.putExtra("countyId", countyId);
//			intent.putExtra("provinceName", provinceName);
//			intent.putExtra("cityName", cityName);
//			intent.putExtra("countyName", countyName);
			bundle.putSerializable("ADDRESS",addressBeginAnd);
			intent.putExtras(bundle);
//			setResult(requestCode, intent);
			startActivity(intent);
			ShipAddressActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position--;
		ShipHeighWay shipHeighWay = shipHighWayList.get(position);
		// 点击获取公共点的详细地址
		detailAddress = shipHeighWay.PublicPointDetail;
		companyName = shipHeighWay.PublicPointName;
		// 将详细地址赋给输入框
		if (detailAddress != null && !TextUtils.isEmpty(detailAddress)) {
			etShipDetail.setText(detailAddress + "");
		} else {
			etShipDetail.setText("");
		}
		if (companyName != null && !TextUtils.isEmpty(companyName)) {
			etShipCompany.setText(companyName + "");
		} else {
			etShipCompany.setText("");
		}
		//友盟统计公共点点击
		if (mIsChoose){
			mUmeng.setCalculateEvents("ship_begin_address_public");
		} else {
			mUmeng.setCalculateEvents("ship_end_address_public");
		}
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data); Log.v("TAG", "onActivityResult"); if (data != null) {
	 * switch (requestCode) { case COMMON_ADDRESS: // 获取返回的值 detailAddress =
	 * data.getStringExtra("detailAddress"); provinceId =
	 * data.getStringExtra("provinceId"); cityId =
	 * data.getStringExtra("cityId"); countyId =
	 * data.getStringExtra("countyId"); companyName =
	 * data.getStringExtra("companyName"); String provinveName =
	 * data.getStringExtra("provinveName"); String cityName =
	 * data.getStringExtra("cityName"); String areaName =
	 * data.getStringExtra("areaName"); Log.v("TAG", "provinveName---------" +
	 * provinveName); Log.v("TAG", "cityName---------" + cityName); Log.v("TAG",
	 * "areaName---------" + areaName); Log.v("TAG", "provinceId---------" +
	 * provinceId); Log.v("TAG", "cityId---------" + cityId); Log.v("TAG",
	 * "countyId---------" + countyId); if (provinveName != null &&
	 * !TextUtils.isEmpty(provinveName)) {
	 * tvShengShiQu.setText(Html.fromHtml("<font color='#666666'>" +
	 * provinceName + "</font>")); } if (cityName != null &&
	 * !TextUtils.isEmpty(cityName)) {
	 * tvShengShiQu.setText(Html.fromHtml("<font color='#666666'>" +
	 * tvShengShiQu.getText().toString() + cityName + "</font>")); } if
	 * (areaName != null && !TextUtils.isEmpty(areaName)) {
	 * tvShengShiQu.setText(Html.fromHtml("<font color='#666666'>" +
	 * tvShengShiQu.getText().toString() + countyName + "</font>")); } break;
	 * 
	 * default: break; } } }
	 */

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Activity activity, int mRequestCode,
			String address, int provinceId, int cityId, int countyId,
			String company, String provinceName, String cityName,
			String countyName, boolean isCopyPackage) {
		Intent intent = new Intent(activity, ShipAddressActivity.class);
		intent.putExtra("mRequestCode", mRequestCode);
		intent.putExtra("provinceId", provinceId);
		intent.putExtra("cityId", cityId);
		intent.putExtra("countyId", countyId);
		intent.putExtra("provinceName", provinceName);
		intent.putExtra("cityName", cityName);
		intent.putExtra("countyName", countyName);
		intent.putExtra("company", company);
		intent.putExtra("address", address);
		intent.putExtra("isCopyPackage", isCopyPackage);
//		activity.startActivityForResult(intent, mRequestCode);
		activity.startActivity(intent);
	}

	/**
	 * 本界面invokeNew跳转方法
	 */
	public static void invokeNew(Activity activity) {
		Intent intent = new Intent(activity, ShipAddressActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		shipHighWayList.clear();
		shipAddressAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getShipHighWay();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getShipHighWay();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvShipView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}

	}

}
