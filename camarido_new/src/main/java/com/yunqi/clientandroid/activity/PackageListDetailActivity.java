package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.entity.GetVehicleListInfo;
import com.yunqi.clientandroid.entity.PackageBiddingInfo;
import com.yunqi.clientandroid.entity.PackageDetailInfo;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.request.BiddingTicketRequest;
import com.yunqi.clientandroid.http.request.GetPackDetailRequest;
import com.yunqi.clientandroid.http.request.GetPackageBiddingInfoRequest;
import com.yunqi.clientandroid.http.request.GrabATicketRequest;
import com.yunqi.clientandroid.http.request.PackDriverListRequest;
import com.yunqi.clientandroid.http.request.PackVehicleListRequest;
import com.yunqi.clientandroid.http.request.SharePackageRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.umeng.share.ShareUtil;
import com.yunqi.clientandroid.umeng.share.SharelistHelper;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单详情界面
 * @date 15/11/28
 */
public class PackageListDetailActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	private String packageId;// 包Id
	private String vehicleId;// 车辆Id
	private String packageType;// 包类型 0：普通包，1：竞价包
	private String hisCount;// 成交单数
	private int maxsize = 24;// 字体大小
	private int minsize = 14;// 字体大小
	private int grabCount;
	private String priceType;// 价格类型
	private String andId;// 车辆司机Id
	private String selectVehicle;// 选中的车辆牌号
	private String selectDriver;// 选中的司机
	private String vehicleListMessage;// 获取可抢单车辆成功与否的提示信息
	private String packageBeginName;
	private String packageBeginAddress;
	private String packageEndName;
	private String packageEndAddress;
	private String packageBeginCityText;
	private String packageEndCityText;
	private boolean isNeedInviteCode;// 是否锁定（即需要推荐码）
	private boolean isGrabSuccess;// 是否抢单成功
	private boolean vehicleListIsSuccess;// 获取可抢单车辆成功与否
	private String packageBeginLatitude;
	private String packageBeginLongitude;
	private String packageEndLatitude;
	private String packageEndLongitude;
	private String avgPrice;// 均价
	private String estimate;// 估计
	private Share mShare;

	private TextView tvProvenance;
	private TextView tvProAddress;
	private TextView tvDestination;
	private TextView tvDestaddress;
	private TextView tvWaybillcount;
	private TextView tvFreight;
	private TextView tvSubsidy;
	private TextView tvCoaltype;
	private TextView tvCalorific;
	private TextView tvMileage;
	private TextView tvTolls;
	// private TextView tvStartTime;
	// private TextView tvEndTime;
	private TextView tvRecommended;
	private TextView tvNote;
	private TextView tvVehicleNo;
	// private TextView tvDriver;
	private TextView tvEstimate;
	private TextView tvHisCount;
	private TextView tvAvgPrice;

	/*
	 * private TextView tvTmcPhoneOne; private TextView tvTmcPhoneTwo; private
	 * TextView tvTmcPhoneThree; private TextView tvStcPhoneOne; private
	 * TextView tvStcPhoneTwo; private TextView tvStcPhoneThree; private
	 * TextView tvPcsPhoneOne; private TextView tvPcsPhoneTwo; private TextView
	 * tvPcsPhoneThree;
	 */
	// private TextView tvLoadingFee;
	// private TextView tvUnloadingFee;
	private TextView tvGoodsPrice;

	private PopupWindow selectorMsgPpw;
	private PopupWindow selectorVehiclePpw;
	private PopupWindow selectorDriverPpw;
	private PopupWindow binddingMsgPpw;
	private View ivLine;
	private View parentView;
	private RelativeLayout rlVehicle;
	private RelativeLayout rlDriver;
	private RelativeLayout mRlGlobal;
	private ProgressBar mProgress;
	private LinearLayout llInputCode;
	private LinearLayout llInputPrice;
	private LinearLayout llProvenance;
	private LinearLayout llDestination;
	private EditText etInputPrice;
	private EditText etInputCode;
	private Button btnGrab;
	private Button btnDetermine;
	private TextView tv_detail_company;// 公司的名称
	private LinearLayout ll_detail_baoxian;
	private ImageView iv_packagedetail_xing;// 三星的标志

	// 新增
	private LinearLayout llPlace;
	private RelativeLayout rlYunFei;
	private ImageView ivYunFeiIcon;
	private RelativeLayout rlGoods;
	private ImageView ivGoodsIcon;
	private RelativeLayout rlTiShi;
	private ImageView ivTiShiIcon;
	private RelativeLayout rlYunFeiInfo;
	private RelativeLayout rlGoodsInfo;
	private RelativeLayout rlTiShiInfo;
	private TextView tvTime;
	private TextView tvYunFei;
	private TextView tvGoods;
	private TextView tvBaoXian;
	private TextView tvDianFu;
	private TextView tvXieZhuang;
	private CheckBox cbPackageTel;
	private PopupWindow selectTelPpw;
	private int selectTelPpwHeight;

	private ArrayList<GetVehicleListInfo> vehicleData;// 存放车辆列表
	private ArrayList<String> listVehicleNo = new ArrayList<String>();// 存车辆的车牌号码
	private ArrayList<String> listName = new ArrayList<String>();// 存默认司机
	private ArrayList<String> listVehicleId = new ArrayList<String>();// 存车辆ID
	private ArrayList<Object> listVehicleOnWay = new ArrayList<Object>();// 存车辆是否在途
	private ArrayList<Integer> listVehicleXing = new ArrayList<Integer>();// 存放星级的集合
	private ArrayList<Object> listDriverIsOnWay = new ArrayList<Object>();// 存司机是否在途
	private ArrayList<String> listDriverName = new ArrayList<String>();// 存放司机的名字
	private ArrayList<String> listId = new ArrayList<String>();// 存放车辆司机Id
	private ArrayList<DriverDetailInfo> driverData;// 存放司机列表
	private ArrayList<DriverDetailInfo> oneDriverData;// 存放第一辆车的司机列表

	// 本页面请求
	private GetPackDetailRequest mGetPackDetailRequest;
	private PackVehicleListRequest mPackVehicleListRequest;
	private PackDriverListRequest mPackDriverListRequest;
	private GrabATicketRequest mGrabATicketRequest;
	private BiddingTicketRequest mBiddingATicketRequest;
	private GetPackageBiddingInfoRequest mGetPackageBiddingInfoRequest;
	private SharePackageRequest mSharePackageRequest;

	// 本页面请求id
	private final int GET_PACK_DETAIL_REQUEST = 1;
	private final int GET_PACK_VEHICLELIST_REQUEST = 2;
	private final int GET_PACK_ONEDRIVER_REQUEST = 3;
	private final int GET_PACK_GRABATICKET_REQUEST = 4;
	private final int GET_PACK_DRIVERLIST_REQUEST = 5;
	private final int GET_PACK_BIDDINGATICKET_REQUEST = 6;
	private final int GET_PACK_BIDDINGINFO_REQUEST = 7;
	private final int SHARE_PACKAGE_REQUEST = 8;
	private RelativeLayout ll_packdetail_gongsi;
	private RelativeLayout ll_packdetail_pingtai;

	// 初始化标题栏
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.order_detail));
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true, 0, this.getResources()
				.getString(R.string.share));
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PackageListDetailActivity.this.finish();
			}
		});
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View V) {
				if (mShare != null) {
					ShareUtil.share(PackageListDetailActivity.this,
							SharelistHelper.FROM_TYPE_GRAB_ORDER, mShare);
				} else {
					showToast("无法获取分享内容");
				}
			}
		});
	}

	@Override
	protected int getLayoutId() {
		return R.layout.new_activity_packagelist_detail;
	}

	@Override
	protected void initView() {
		// 初始化标题栏
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.new_activity_packagelist_detail, null);
		// 接收从包列表页跳转传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("packageId")) {
			packageId = bundle.getString("packageId");
		}

		// 初始化控件
		tvProvenance = (TextView) findViewById(R.id.tv_packdetail_provenance);
		tvProAddress = (TextView) findViewById(R.id.tv_packdetail_pro_address);
		tvDestination = (TextView) findViewById(R.id.tv_packdetail_destination);
		tvDestaddress = (TextView) findViewById(R.id.tv_packdetail_destaddress);
		tvWaybillcount = (TextView) findViewById(R.id.tv_packdetail_waybillcount);
		tvFreight = (TextView) findViewById(R.id.tv_packdetail_freight);
		tvSubsidy = (TextView) findViewById(R.id.tv_packdetail_subsidy);
		tvCoaltype = (TextView) findViewById(R.id.tv_packdetail_coaltype);
		tvCalorific = (TextView) findViewById(R.id.tv_packdetail_calorific);
		tvMileage = (TextView) findViewById(R.id.tv_packdetail_mileage);
		tvTolls = (TextView) findViewById(R.id.tv_packdetail_tolls);
		// tvStartTime = (TextView) findViewById(R.id.tv_packdetail_starttime);
		// tvEndTime = (TextView) findViewById(R.id.tv_packdetail_endtime);
		tvRecommended = (TextView) findViewById(R.id.tv_packdetail_recommended);
		tvNote = (TextView) findViewById(R.id.tv_packdetail_note);
		btnDetermine = (Button) findViewById(R.id.bt_packagedetail_determine);
		llProvenance = (LinearLayout) findViewById(R.id.ll_packdetail_provenance);
		llDestination = (LinearLayout) findViewById(R.id.ll_packdetail_destination);
		mRlGlobal = (RelativeLayout) findViewById(R.id.rl_packdetail_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_packdetail_progress);
		/*
		 * tvTmcPhoneOne = (TextView)
		 * findViewById(R.id.tv_packdetail_tmcPhoneOne); tvTmcPhoneTwo =
		 * (TextView) findViewById(R.id.tv_packdetail_tmcPhoneTwo);
		 * tvTmcPhoneThree = (TextView)
		 * findViewById(R.id.tv_packdetail_tmcPhoneThree); tvStcPhoneOne =
		 * (TextView) findViewById(R.id.tv_packdetail_stcPhoneOne);
		 * tvStcPhoneTwo = (TextView)
		 * findViewById(R.id.tv_packdetail_stcPhoneTwo); tvStcPhoneThree =
		 * (TextView) findViewById(R.id.tv_packdetail_stcPhoneThree);
		 * tvPcsPhoneOne = (TextView)
		 * findViewById(R.id.tv_packdetail_pcsPhoneOne); tvPcsPhoneTwo =
		 * (TextView) findViewById(R.id.tv_packdetail_pcsPhoneTwo);
		 * tvPcsPhoneThree = (TextView)
		 * findViewById(R.id.tv_packdetail_pcsPhoneThree);
		 */
		// tvLoadingFee = (TextView)
		// findViewById(R.id.tv_packdetail_loadingFee);
		// tvUnloadingFee = (TextView)
		// findViewById(R.id.tv_packdetail_unloadingFee);
		tvGoodsPrice = (TextView) findViewById(R.id.tv_packdetail_goodsPrice);
		tv_detail_company = (TextView) findViewById(R.id.tv_detail_company);
		ll_packdetail_gongsi = (RelativeLayout) findViewById(R.id.ll_packdetail_gongsi);
		ll_packdetail_pingtai = (RelativeLayout) findViewById(R.id.ll_packdetail_pingtai);
		ll_detail_baoxian = (LinearLayout) findViewById(R.id.ll_detail_baoxian);
		iv_packagedetail_xing = (ImageView) findViewById(R.id.iv_packagedetail_xing);
		// 新增
		llPlace = obtainView(R.id.ll_place);
		rlYunFei = obtainView(R.id.rl_yunfei);
		rlYunFei.setTag(true);
		ivYunFeiIcon = obtainView(R.id.iv_yunfei_icon);
		rlGoods = obtainView(R.id.rl_goods);
		rlGoods.setTag(true);
		ivGoodsIcon = obtainView(R.id.iv_goods_icon);
		rlTiShi = obtainView(R.id.rl_tishi);
		rlTiShi.setTag(true);
		ivTiShiIcon = obtainView(R.id.iv_tishi_icon);
		rlYunFeiInfo = obtainView(R.id.rl_yunfei_info);
		rlGoodsInfo = obtainView(R.id.rl_goods_info);
		rlTiShiInfo = obtainView(R.id.rl_tishi_info);
		tvTime = obtainView(R.id.tv_time);
		tvYunFei = obtainView(R.id.tv_yunfei);
		tvGoods = obtainView(R.id.tv_goods);
		tvBaoXian = obtainView(R.id.tv_baoxian);
		tvDianFu = obtainView(R.id.tv_dianfu);
		tvXieZhuang = obtainView(R.id.tv_xiezhuang);
		cbPackageTel = obtainView(R.id.bt_packagedetail_tel);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注册广播接收者--接收地图页传过来的抢单成功与否状态
		IntentFilter filter = new IntentFilter();
//		filter.addAction(StartingPointActivity.startingpoint);
		PackageListDetailActivity.this.registerReceiver(packageDetail, filter);
	}

	// 广播接收者--接收地图页传过来的抢单成功与否状态
	BroadcastReceiver packageDetail = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			if (action.equals(StartingPointActivity.startingpoint)) {
//				boolean grabSuccess = intent.getExtras().getBoolean(
//						"grabSuccess");
//				// 如果地图页已抢单,订单详情不可抢单
//				if (grabSuccess) {
//					setViewEnable(false);
//				}
//			}
		}
	};

	@Override
	protected void initData() {
		// 从服务器获取订单详情数据
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			getDataFromServicePackDetail(packageId);
		}

		// 从服务器获取默认车辆和司机
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			getDataFromServiceVehicle(packageId);
		}
		// 获取分享内容
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			getShareContent(packageId);
		}
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		btnDetermine.setOnClickListener(this);
		// llProvenance.setOnClickListener(this);
		// llDestination.setOnClickListener(this);
		ll_packdetail_gongsi.setOnClickListener(this);// 点击进入公司详情
		// ll_packdetail_pingtai.setOnClickListener(this);//

		/*
		 * tvTmcPhoneOne.setOnClickListener(this);
		 * tvTmcPhoneTwo.setOnClickListener(this);
		 * tvTmcPhoneThree.setOnClickListener(this);
		 * tvStcPhoneOne.setOnClickListener(this);
		 * tvStcPhoneTwo.setOnClickListener(this);
		 * tvStcPhoneThree.setOnClickListener(this);
		 * tvPcsPhoneOne.setOnClickListener(this);
		 * tvPcsPhoneTwo.setOnClickListener(this);
		 * tvPcsPhoneThree.setOnClickListener(this);
		 */

		// ll_detail_baoxian.setOnClickListener(this);
		// 新增
		llPlace.setOnClickListener(this);
		rlYunFei.setOnClickListener(this);
		rlGoods.setOnClickListener(this);
		rlTiShi.setOnClickListener(this);
		cbPackageTel.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_packagedetail_determine:
			L.e("TAG", "-----------点击1----------");

			// TODO--点击跳转到选择车辆和司机界面
			if (vehicleListIsSuccess) {
				Log.e("TAG", "-----------点击2----------");
				Log.e("TAG", "-----------packageType----------" + packageType);
				// 如果有可抢单车辆弹出
				if (packageType != null && packageType.equals("0")) {
					Log.e("TAG", "-----------点击3----------");
					// 抢单弹窗
					showSelectorMsgPpw();
				} else if (packageType != null && packageType.equals("1")) {
					// 竞价弹窗
					showBiddingMsgPpw();
				}
			} else {
				// 如果无可抢单车辆提示
				showToast(vehicleListMessage);
				// 跳转到快速认证界面
				// FastAddVehicleActivity.invoke(PackageListDetailActivity.this);

			}

			break;

		/*
		 * case R.id.ll_packdetail_provenance: // 跳转到起始点地图
		 * StartingPointActivity.invoke(this, packageId, isNeedInviteCode,
		 * packageType, packageBeginName, packageBeginAddress, packageEndName,
		 * packageEndAddress, packageBeginCityText, packageEndCityText,
		 * packageBeginLatitude, packageBeginLongitude, packageEndLatitude,
		 * packageEndLongitude, isGrabSuccess);
		 * 
		 * break;
		 * 
		 * case R.id.ll_packdetail_destination: // 跳转到起始点地图
		 * StartingPointActivity.invoke(this, packageId, isNeedInviteCode,
		 * packageType, packageBeginName, packageBeginAddress, packageEndName,
		 * packageEndAddress, packageBeginCityText, packageEndCityText,
		 * packageBeginLatitude, packageBeginLongitude, packageEndLatitude,
		 * packageEndLongitude, isGrabSuccess); break;
		 */
		case R.id.ll_place:
			// 跳转到起始点地图
			/*
			 * StartingPointActivity.invoke(this, packageId, isNeedInviteCode,
			 * packageType, packageBeginName, packageBeginAddress,
			 * packageEndName, packageEndAddress, packageBeginCityText,
			 * packageEndCityText, packageBeginLatitude, packageBeginLongitude,
			 * packageEndLatitude, packageEndLongitude, isGrabSuccess);
			 */
			break;
		case R.id.ll_packdetail_gongsi: // 点击进入公司详情
			Log.e("TAG", "包的ID:-------------------------" + packageId);

			// CompanyDetailActivity.invoke(PackageListDetailActivity.this,
			// packageId);
			Intent intent = new Intent(PackageListDetailActivity.this,
					CompanyDetailActivity.class);
			// intent.putExtra("packageId", packageId);

			CompanyDetailActivity.invoke_intent(PackageListDetailActivity.this,
					packageId, intent);

			// startActivity(intent);

			break;

		/*
		 * case R.id.tv_packdetail_tmcPhoneOne: // 驻矿电话1 String tmcPhoneOne =
		 * tvTmcPhoneOne.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(tmcPhoneOne)) {
		 * CommonUtil.callPhoneIndirect(this, tmcPhoneOne); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_tmcPhoneTwo: // 驻矿电话2 String tmcPhoneTwo =
		 * tvTmcPhoneTwo.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(tmcPhoneTwo)) {
		 * CommonUtil.callPhoneIndirect(this, tmcPhoneTwo); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_tmcPhoneThree: // 驻矿电话3 String tmcPhoneThree
		 * = tvTmcPhoneThree.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(tmcPhoneThree)) {
		 * CommonUtil.callPhoneIndirect(this, tmcPhoneThree); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_stcPhoneOne: // 签收电话1 String stcPhoneOne =
		 * tvStcPhoneOne.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(stcPhoneOne)) {
		 * CommonUtil.callPhoneIndirect(this, stcPhoneOne); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_stcPhoneTwo: // 签收电话2 String stcPhoneTwo =
		 * tvStcPhoneTwo.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(stcPhoneTwo)) {
		 * CommonUtil.callPhoneIndirect(this, stcPhoneTwo); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_stcPhoneThree: // 签收电话3 String stcPhoneThree
		 * = tvStcPhoneThree.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(stcPhoneThree)) {
		 * CommonUtil.callPhoneIndirect(this, stcPhoneThree); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_pcsPhoneOne: // 客服电话1 String pcsPhoneOne =
		 * tvPcsPhoneOne.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(pcsPhoneOne)) {
		 * CommonUtil.callPhoneIndirect(this, pcsPhoneOne); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_pcsPhoneTwo: // 客服电话2 String pcsPhoneTwo =
		 * tvPcsPhoneTwo.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(pcsPhoneTwo)) {
		 * CommonUtil.callPhoneIndirect(this, pcsPhoneTwo); }
		 * 
		 * break;
		 * 
		 * case R.id.tv_packdetail_pcsPhoneThree: // 客服电话3 String pcsPhoneThree
		 * = tvPcsPhoneThree.getText().toString().trim();
		 * 
		 * if (!TextUtils.isEmpty(pcsPhoneThree)) {
		 * CommonUtil.callPhoneIndirect(this, pcsPhoneThree); }
		 * 
		 * break;
		 */

		case R.id.ll_detail_baoxian:// 进入保证金页面

			Log.e("TAG", "------------Jinru保险----------------");

			// Intent it = new Intent(PackageListDetailActivity.this,
			// PleDgeActivity.class);g
			// startActivity(it);
			HelpActivity.invoke(PackageListDetailActivity.this, "insurance");
			break;
		case R.id.rl_yunfei:
			if ((Boolean) rlYunFei.getTag()) {
				rlYunFeiInfo.setVisibility(View.VISIBLE);
				rlYunFei.setTag(false);
				shunShiZhen(ivYunFeiIcon);
			} else {
				rlYunFeiInfo.setVisibility(View.GONE);
				rlYunFei.setTag(true);
				niShiZhen(ivYunFeiIcon);
			}
			break;
		case R.id.rl_goods:
			if ((Boolean) rlGoods.getTag()) {
				rlGoodsInfo.setVisibility(View.VISIBLE);
				rlGoods.setTag(false);
				shunShiZhen(ivGoodsIcon);
			} else {
				rlGoodsInfo.setVisibility(View.GONE);
				rlGoods.setTag(true);
				niShiZhen(ivGoodsIcon);
			}
			break;
		case R.id.rl_tishi:
			if ((Boolean) rlTiShi.getTag()) {
				rlTiShiInfo.setVisibility(View.VISIBLE);
				rlTiShi.setTag(false);
				shunShiZhen(ivTiShiIcon);
			} else {
				rlTiShiInfo.setVisibility(View.GONE);
				rlTiShi.setTag(true);
				niShiZhen(ivTiShiIcon);
			}
			break;
		}

	}

	/**
	 * 
	 * @Description:图标逆时针
	 * @Title:niShiZhen
//	 * @param ivYunFeiIcon2
	 * @return:void
	 * @throws
	 * @Create: 2016年5月31日 下午2:53:01
	 * @Author : chengtao
	 */
	private void niShiZhen(ImageView iv) {
		RotateAnimation ra = new RotateAnimation(90f, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(200);
		ra.setFillAfter(true);
		iv.startAnimation(ra);
	}

	/**
	 * 
	 * @Description:图标顺时针
	 * @Title:shunShiZhen
//	 * @param ivYunFeiIcon2
	 * @return:void
	 * @throws
	 * @Create: 2016年5月31日 下午2:53:20
	 * @Author : chengtao
	 */
	private void shunShiZhen(ImageView iv) {
		RotateAnimation ra = new RotateAnimation(0f, 90f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(200);
		ra.setFillAfter(true);
		iv.startAnimation(ra);
	}

	// 主界面抢单按钮的变化
	private void setViewEnable(boolean bEnable) {
		btnDetermine.setEnabled(bEnable);
	}

	// 弹窗抢单按钮的变化
	private void setPpwViewEnable(boolean bEnable) {
		btnGrab.setEnabled(bEnable);

	}

	// 从服务器获取包竞价历史统计信息
	private void getDataFromServicePackageBiddingInfo(String packageId) {
		mGetPackageBiddingInfoRequest = new GetPackageBiddingInfoRequest(this,
				packageId);
		mGetPackageBiddingInfoRequest
				.setRequestId(GET_PACK_BIDDINGINFO_REQUEST);
		httpPost(mGetPackageBiddingInfoRequest);
	}

	// 从服务器获取包详情数据的方法
	private void getDataFromServicePackDetail(String packageId) {
		mRlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetPackDetailRequest = new GetPackDetailRequest(this, packageId);
		mGetPackDetailRequest.setRequestId(GET_PACK_DETAIL_REQUEST);
		httpPost(mGetPackDetailRequest);
	}

	// 没有锁定进行抢单
	protected void getDataFromSeriverGrabATicket(String packageId, String Id) {
		mGrabATicketRequest = new GrabATicketRequest(this, packageId, Id);
		mGrabATicketRequest.setRequestId(GET_PACK_GRABATICKET_REQUEST);
		httpPost(mGrabATicketRequest);
	}

	// 锁定进行抢单
	protected void getDataFromSeriverGrabATicket(String packageId, String Id,
			String inputCode) {
		mGrabATicketRequest = new GrabATicketRequest(this, packageId, Id,
				inputCode);
		mGrabATicketRequest.setRequestId(GET_PACK_GRABATICKET_REQUEST);
		httpPost(mGrabATicketRequest);
	}

	// 竞价进行抢单
	protected void getDataFromSeriverBiddingATicket(String packageId,
			String Id, String biddingPrice) {
		mBiddingATicketRequest = new BiddingTicketRequest(this, packageId, Id,
				biddingPrice);
		mBiddingATicketRequest.setRequestId(GET_PACK_BIDDINGATICKET_REQUEST);
		httpPost(mBiddingATicketRequest);
	}

	// 从服务器获取第一辆车的司机
	protected void getDataFromSeriverOneDriver(String vehicleId) {
		// 清除存默认司机集合
		listName.clear();

		mPackDriverListRequest = new PackDriverListRequest(this, vehicleId);
		mPackDriverListRequest.setRequestId(GET_PACK_ONEDRIVER_REQUEST);
		httpPost(mPackDriverListRequest);

	}

	// 从服务器获取车的司机
	protected void getDataFromSeriverDriverList(String vehicleId) {
		// 清空存放司机姓名、司机是否在途、车辆司机id的集合
		listDriverName.clear();
		listDriverIsOnWay.clear();
		listId.clear();
		rlDriver.setEnabled(false);

		mPackDriverListRequest = new PackDriverListRequest(this, vehicleId);
		mPackDriverListRequest.setRequestId(GET_PACK_DRIVERLIST_REQUEST);
		httpPost(mPackDriverListRequest);

	}

	// 从服务器获取可抢单车辆列表
	protected void getDataFromServiceVehicle(String packageId) {
		// 清空存放车牌号、车辆id、车辆是否在途状态
		listVehicleNo.clear();
		listVehicleId.clear();
		listVehicleOnWay.clear();

		mPackVehicleListRequest = new PackVehicleListRequest(this, packageId);
		mPackVehicleListRequest.setRequestId(GET_PACK_VEHICLELIST_REQUEST);
		httpPost(mPackVehicleListRequest);

	}

	// 获取分析那个内容
	protected void getShareContent(String packageId) {
		mSharePackageRequest = new SharePackageRequest(this, packageId);
		mSharePackageRequest.setRequestId(SHARE_PACKAGE_REQUEST);
		httpPost(mSharePackageRequest);

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_PACK_DETAIL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取包详细数据成功
				PackageDetailInfo packageDetailInfo = (PackageDetailInfo) response.singleData;
				packageType = packageDetailInfo.packageType;// 包类型 0：普通包，1：竞价包
				isNeedInviteCode = packageDetailInfo.isNeedInviteCode;// 是否锁定（即需要推荐码）
				packageBeginName = packageDetailInfo.packageBeginName;// 出发地名称
				packageBeginAddress = packageDetailInfo.packageBeginAddress;// 出发地详细地址
				packageEndName = packageDetailInfo.packageEndName;// 目的地名称
				packageEndAddress = packageDetailInfo.packageEndAddress;// 目的地详细地址
				packageBeginCityText = packageDetailInfo.packageBeginCityText;// 出发地市描述
				packageEndCityText = packageDetailInfo.packageEndCityText;// 目的地市描述
				packageBeginLatitude = packageDetailInfo.packageBeginLatitude;// 出发地维度
				packageBeginLongitude = packageDetailInfo.packageBeginLongitude;// 出发地经度
				packageEndLatitude = packageDetailInfo.packageEndLatitude;// 目的地维度
				packageEndLongitude = packageDetailInfo.packageEndLongitude;// 目的地经度
				String packageCount = packageDetailInfo.packageCount;// 包的运单量
				String packagePrice = packageDetailInfo.packagePrice;// 运价
				String packagePriceType = packageDetailInfo.packagePriceType;// 运价方式
																				// 0：/吨，1：/吨*公里，2：/车数
				String categoryName = packageDetailInfo.categoryName;// 货品分类名称
				String packageGoodsCalorific = packageDetailInfo.packageGoodsCalorific;// 货品信息
				String packageStartTime = packageDetailInfo.packageStartTime;// 包有效期开始时间
				String packageEndTime = packageDetailInfo.packageEndTime;// 包有效期结束时间
				String packageMemo = packageDetailInfo.packageMemo;// 物流备注
				String packageRoadToll = packageDetailInfo.packageRoadToll;// 过路费
				String packageRecommendPath = packageDetailInfo.packageRecommendPath;// 推荐路线
				String packageDistance = packageDetailInfo.packageDistance;// 距离
				String orderCount = packageDetailInfo.orderCount;// 已运运单量
				String subsidy = packageDetailInfo.subsidy;// 补助
				if (!TextUtils.isEmpty(packageCount) && packageCount != null
						&& !TextUtils.isEmpty(orderCount) && orderCount != null) {
					grabCount = Integer.parseInt(packageCount)
							- Integer.parseInt(orderCount);
				}
				String packageGoodsPrice = packageDetailInfo.packageGoodsPrice;// 货值价格
				String tmcPhone = packageDetailInfo.tmcPhone;// 驻矿联系电话
				String stcPhone = packageDetailInfo.stcPhone;// 签收联系电话
				String tdscPhone = packageDetailInfo.tdscPhone;// 发包方电话
				String pcsPhone = packageDetailInfo.pcsPhone;// 平台客服电话
				String loadingFee = packageDetailInfo.loadingFee;// 装车费
				String unloadingFee = packageDetailInfo.unloadingFee;// 卸车费
				boolean isPayFor = packageDetailInfo.isPayFor;// 是否付款
				boolean isPayIn = packageDetailInfo.isPayIn;// 是否缴纳保证金
				String tenantName = packageDetailInfo.tenantName;// 公司全称
				String tenantShortName = packageDetailInfo.tenantShortName;// 公司简称
				int pInsuranceType = packageDetailInfo.insuranceType;// 0：无保险
																		// 1：平台送保险
																		// 2：自己购买保险
				boolean pNeedInvoice = packageDetailInfo.needInvoice;// 是否需要开票

				int orderCountByCurrentUser = packageDetailInfo.orderCountByCurrentUser;
				// 根据是否开票显示三星标记
				if (pNeedInvoice) {
					iv_packagedetail_xing.setVisibility(View.VISIBLE);
				}

				// 运价方式 0：/吨，1：/吨*公里，2：/车数
				if (packagePriceType != null && packagePriceType.equals("0")) {
					priceType = "/吨";
				} else if (packagePriceType != null
						&& packagePriceType.equals("1")) {
					priceType = "/吨*公里";
				} else if (packagePriceType != null
						&& packagePriceType.equals("2")) {
					priceType = "/车数";
				}

				// 公司的名称
				if (!TextUtils.isEmpty(tenantShortName)
						&& tenantShortName != null) {
					tv_detail_company.setText(tenantShortName);
				}

				// 平台保险
				if (pInsuranceType == 1) {
					tvBaoXian.setVisibility(View.VISIBLE);
				}

				/*
				 * //显示保证金的图标 if (pInsuranceType == 0) {
				 * ll_detail_baoxian.setVisibility(View.GONE); } else if
				 * (pInsuranceType == 1) {
				 * ll_detail_baoxian.setVisibility(View.VISIBLE); }
				 */

				// 出发地名称
				if (!TextUtils.isEmpty(packageBeginName)
						&& packageBeginName != null) {
					tvProvenance.setText(packageBeginName);
				}

				// 出发地详细地址
				if (!TextUtils.isEmpty(packageBeginAddress)
						&& packageBeginAddress != null) {
					tvProAddress.setText(packageBeginAddress);
				}

				// 目的地名称
				if (!TextUtils.isEmpty(packageEndName)
						&& packageEndName != null) {
					tvDestination.setText(packageEndName);
				}

				// 目的地详细地址
				if (!TextUtils.isEmpty(packageEndAddress)
						&& packageEndAddress != null) {
					tvDestaddress.setText(packageEndAddress);
				}

				/*
				 * // 设置驻矿电话 if (!TextUtils.isEmpty(tmcPhone) && tmcPhone !=
				 * null) { if (tmcPhone.contains(",")) { // 包含逗号 String[]
				 * tmcPhoneArray = tmcPhone.split(","); if (tmcPhoneArray.length
				 * >= 1 && tmcPhoneArray.length < 2) { // 设置驻矿电话1 String
				 * tmcPhoneOne = tmcPhoneArray[0];
				 * tvTmcPhoneOne.setText(Html.fromHtml("<u>" + tmcPhoneOne +
				 * "</u>")); } else if (tmcPhoneArray.length >= 2 &&
				 * tmcPhoneArray.length < 3) { // 设置驻矿电话1 String tmcPhoneOne =
				 * tmcPhoneArray[0]; tvTmcPhoneOne.setText(Html.fromHtml("<u>" +
				 * tmcPhoneOne + "</u>")); // 设置驻矿电2 String tmcPhoneTwo =
				 * tmcPhoneArray[1]; tvTmcPhoneTwo.setText(Html.fromHtml("<u>" +
				 * tmcPhoneTwo + "</u>"));
				 * tvTmcPhoneTwo.setVisibility(View.VISIBLE); } else if
				 * (tmcPhoneArray.length >= 3) { // 设置驻矿电话1 String tmcPhoneOne =
				 * tmcPhoneArray[0]; tvTmcPhoneOne.setText(Html.fromHtml("<u>" +
				 * tmcPhoneOne + "</u>")); // 设置驻矿电2 String tmcPhoneTwo =
				 * tmcPhoneArray[1]; tvTmcPhoneTwo.setText(Html.fromHtml("<u>" +
				 * tmcPhoneTwo + "</u>"));
				 * tvTmcPhoneTwo.setVisibility(View.VISIBLE); // 设置驻矿电话3 String
				 * tmcPhoneThree = tmcPhoneArray[2];
				 * tvTmcPhoneThree.setText(Html.fromHtml("<u>" + tmcPhoneThree +
				 * "</u>")); tvTmcPhoneThree.setVisibility(View.VISIBLE); } }
				 * else { // 不包含逗号--设置驻矿电话1
				 * tvTmcPhoneOne.setText(Html.fromHtml("<u>" + tmcPhone +
				 * "</u>")); } } // 设置签收电话 if (!TextUtils.isEmpty(stcPhone) &&
				 * stcPhone != null) { if (stcPhone.contains(",")) { // 包含逗号
				 * String[] stcPhoneArray = stcPhone.split(","); if
				 * (stcPhoneArray.length >= 1 && stcPhoneArray.length < 2) { //
				 * 设置签收电话1 String stcPhoneOne = stcPhoneArray[0];
				 * tvStcPhoneOne.setText(Html.fromHtml("<u>" + stcPhoneOne +
				 * "</u>")); } else if (stcPhoneArray.length >= 2 &&
				 * stcPhoneArray.length < 3) { // 设置签收电话1 String stcPhoneOne =
				 * stcPhoneArray[0]; tvStcPhoneOne.setText(Html.fromHtml("<u>" +
				 * stcPhoneOne + "</u>")); // 设置签收电2 String stcPhoneTwo =
				 * stcPhoneArray[1]; tvStcPhoneTwo.setText(Html.fromHtml("<u>" +
				 * stcPhoneTwo + "</u>"));
				 * tvStcPhoneTwo.setVisibility(View.VISIBLE); } else if
				 * (stcPhoneArray.length >= 3) { // 设置签收电话1 String stcPhoneOne =
				 * stcPhoneArray[0]; tvStcPhoneOne.setText(Html.fromHtml("<u>" +
				 * stcPhoneOne + "</u>")); // 设置签收电2 String stcPhoneTwo =
				 * stcPhoneArray[1]; tvStcPhoneTwo.setText(Html.fromHtml("<u>" +
				 * stcPhoneTwo + "</u>"));
				 * tvStcPhoneTwo.setVisibility(View.VISIBLE); // 设置签收电话3 String
				 * stcPhoneThree = stcPhoneArray[2];
				 * tvStcPhoneThree.setText(Html.fromHtml("<u>" + stcPhoneThree +
				 * "</u>")); tvStcPhoneThree.setVisibility(View.VISIBLE); } }
				 * else { // 不包含逗号--设置签收电话1
				 * tvStcPhoneOne.setText(Html.fromHtml("<u>" + stcPhone +
				 * "</u>")); } }
				 * 
				 * // 设置客服电话 if (!TextUtils.isEmpty(pcsPhone) && pcsPhone !=
				 * null) { if (pcsPhone.contains(",")) { // 包含逗号 String[]
				 * pcsPhoneArray = pcsPhone.split(","); if (pcsPhoneArray.length
				 * >= 1 && pcsPhoneArray.length < 2) { // 设置客服电话1 String
				 * pcsPhoneOne = pcsPhoneArray[0];
				 * tvPcsPhoneOne.setText(Html.fromHtml("<u>" + pcsPhoneOne +
				 * "</u>")); } else if (pcsPhoneArray.length >= 2 &&
				 * pcsPhoneArray.length < 3) { // 设置客服电话1 String pcsPhoneOne =
				 * pcsPhoneArray[0]; tvPcsPhoneOne.setText(Html.fromHtml("<u>" +
				 * pcsPhoneOne + "</u>")); // 设置客服电2 String pcsPhoneTwo =
				 * pcsPhoneArray[1]; tvPcsPhoneTwo.setText(Html.fromHtml("<u>" +
				 * pcsPhoneTwo + "</u>"));
				 * tvPcsPhoneTwo.setVisibility(View.VISIBLE); } else if
				 * (pcsPhoneArray.length >= 3) { // 设置客服电话1 String pcsPhoneOne =
				 * pcsPhoneArray[0]; tvPcsPhoneOne.setText(Html.fromHtml("<u>" +
				 * pcsPhoneOne + "</u>")); // 设置客服电2 String pcsPhoneTwo =
				 * pcsPhoneArray[1]; tvPcsPhoneTwo.setText(Html.fromHtml("<u>" +
				 * pcsPhoneTwo + "</u>"));
				 * tvPcsPhoneTwo.setVisibility(View.VISIBLE); // 设置客服电话3 String
				 * pcsPhoneThree = pcsPhoneArray[2];
				 * tvPcsPhoneThree.setText(Html.fromHtml("<u>" + pcsPhoneThree +
				 * "</u>")); tvPcsPhoneThree.setVisibility(View.VISIBLE); } }
				 * else { // 不包含逗号--设置客服电话1
				 * tvPcsPhoneOne.setText(Html.fromHtml("<u>" + pcsPhone +
				 * "</u>")); } }
				 */

				/*
				 * // 设置装车费 if (!TextUtils.isEmpty(loadingFee) && loadingFee !=
				 * null && !loadingFee.equals("0.00")) {
				 * tvLoadingFee.setText(loadingFee + "元"); } else {
				 * tvLoadingFee.setText(this.getResources().getString(
				 * R.string.message_notString)); }
				 * 
				 * // 设置卸车费 if (!TextUtils.isEmpty(unloadingFee) && unloadingFee
				 * != null && !unloadingFee.equals("0.00")) {
				 * tvUnloadingFee.setText(unloadingFee + "元"); } else {
				 * tvUnloadingFee.setText(this.getResources().getString(
				 * R.string.message_notString)); }
				 */

				// 装卸
				if (!TextUtils.isEmpty(loadingFee) && loadingFee != null
						&& !loadingFee.equals("0.00")) {
					tvXieZhuang.setText("装" + loadingFee + "元/车,");
				} else if (!TextUtils.isEmpty(unloadingFee)
						&& unloadingFee != null && !unloadingFee.equals("0.00")) {
					tvXieZhuang.setText(tvXieZhuang.getText().toString() + "卸"
							+ unloadingFee + "元/车");
				} else {
					tvXieZhuang.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 过路费
				if (!TextUtils.isEmpty(packageRoadToll)
						&& packageRoadToll != null
						&& !packageRoadToll.equals("0.00")) {
					tvTolls.setText(packageRoadToll + "元");
				} else {
					tvTolls.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 可抢运单
				/*
				 * if (!TextUtils.isEmpty(grabCount + "") && grabCount > 0) {
				 * tvWaybillcount.setText(grabCount + "单"); } else { //
				 * TODO--没有可抢订单--设置主界面按钮为不可点击 setViewEnable(false);
				 * Toast.makeText(PackageListDetailActivity.this, "没有可抢订单不能抢单",
				 * Toast.LENGTH_LONG).show(); }
				 */

				// 运费
				if (!TextUtils.isEmpty(packagePrice) && packagePrice != null
						&& !packagePrice.equals("0.00")) {
					tvFreight.setText(packagePrice + "元" + priceType);
					tvYunFei.setText(packagePrice + "元" + priceType);
				} else {
					tvFreight.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 补助
				if (!TextUtils.isEmpty(subsidy) && subsidy != null
						&& !subsidy.equals("0.00")) {
					tvSubsidy.setText(subsidy + "元");
					tvYunFei.setText(tvYunFei.getText().toString() + ",补助"
							+ subsidy + "元/车");
				} else {
					tvSubsidy.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 运费提示信息
				if (TextUtils.isEmpty(tvYunFei.getText().toString())) {
					tvYunFei.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 货品分类名称
				if (!TextUtils.isEmpty(categoryName) && categoryName != null) {
					tvCoaltype.setText(categoryName);
					tvGoods.setText(categoryName + ",");
				} else {
					tvCoaltype.setText(this.getResources().getString(
							R.string.message_notString));
				}
				// 设置货品价格
				if (!TextUtils.isEmpty(packageGoodsPrice)
						&& packageGoodsPrice != null
						&& !packageGoodsPrice.equals("0.00")) {
					tvGoodsPrice.setText(packageGoodsPrice + "元" + priceType);
					tvGoods.setText(tvGoods.getText().toString()
							+ packageGoodsPrice + "元" + priceType);
				} else {
					tvGoodsPrice.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 货品信息
				if (!TextUtils.isEmpty(packageGoodsCalorific)
						&& packageGoodsCalorific != null) {
					tvCalorific.setText(packageGoodsCalorific);
				} else {
					tvCalorific.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 设置货物提示信息
				if (TextUtils.isEmpty(tvGoods.getText().toString())) {
					tvGoods.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 距离
				if (!TextUtils.isEmpty(packageDistance)
						&& packageDistance != null
						&& !packageDistance.equals("0.00")) {
					tvMileage.setText(packageDistance + "公里");
				} else {
					if (!TextUtils.isEmpty(packageBeginLatitude)
							&& packageBeginLatitude != null
							&& !TextUtils.isEmpty(packageBeginLongitude)
							&& packageBeginLongitude != null
							&& !TextUtils.isEmpty(packageEndLatitude)
							&& packageEndLatitude != null
							&& !TextUtils.isEmpty(packageEndLongitude)
							&& packageEndLongitude != null) {
						float[] resultDistance = new float[1];
						Location.distanceBetween(
								Double.parseDouble(packageBeginLatitude),
								Double.parseDouble(packageBeginLongitude),
								Double.parseDouble(packageEndLatitude),
								Double.parseDouble(packageEndLongitude),
								resultDistance);
						tvMileage.setText(Math.rint(resultDistance[0] / 100)
								/ 10 + "公里");
					} else {
						tvMileage.setText(this.getResources().getString(
								R.string.message_notString));
					}
				}

				/*
				 * // 包有效期开始时间 if (!TextUtils.isEmpty(packageStartTime) &&
				 * packageStartTime != null) { String formatTicket = StringUtils
				 * .formatModify(packageStartTime);
				 * tvStartTime.setText(formatTicket); } else {
				 * tvStartTime.setText(this.getResources().getString(
				 * R.string.message_notString)); }
				 * 
				 * // 包有效期结束时间 if (!TextUtils.isEmpty(packageEndTime) &&
				 * packageEndTime != null) { String formatTicket = StringUtils
				 * .formatModify(packageEndTime);
				 * tvEndTime.setText(formatTicket); } else {
				 * tvEndTime.setText(this.getResources().getString(
				 * R.string.message_notString)); }
				 */

				// 设置时间
				if (!TextUtils.isEmpty(packageStartTime)
						&& packageStartTime != null
						&& !TextUtils.isEmpty(packageEndTime)
						&& packageEndTime != null) {
					String formatTicket = StringUtils
							.formatModify(packageStartTime);
					String formatTicket2 = StringUtils
							.formatModify(packageEndTime);
					tvTime.setText(formatTicket + "~" + formatTicket2);
				} else {
					tvTime.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 推荐路线
				if (!TextUtils.isEmpty(packageRecommendPath)
						&& packageRecommendPath != null) {
					tvRecommended.setText(packageRecommendPath);
				} else {
					tvRecommended.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 物流备注
				if (!TextUtils.isEmpty(packageMemo) && packageMemo != null) {
					tvNote.setText(packageMemo);
				} else {
					tvNote.setText(this.getResources().getString(
							R.string.message_notString));
				}

				// 根据包类型设置按钮显示字样
				if (packageType != null && packageType.equals("0")) {
					// 抢单
					btnDetermine.setBackgroundResource(R.drawable.btn_qiangdan);
				} else if (packageType != null && packageType.equals("1")) {
					// 竞价
					btnDetermine.setBackgroundResource(R.drawable.btn_jingjia);
					// 从服务器获取包竞价历史统计信息
					if (!TextUtils.isEmpty(packageId) && packageId != null) {
						getDataFromServicePackageBiddingInfo(packageId);
					}
				}
				if (String.valueOf(orderCountByCurrentUser) == null
						&& TextUtils.isEmpty(String
								.valueOf(orderCountByCurrentUser))) {
					cbPackageTel.setButtonDrawable(R.drawable.btn_tel_zhihui);
					cbPackageTel.setOnCheckedChangeListener(null);
				} else {
					createselectTelPoupWindow(tdscPhone, tmcPhone, stcPhone);
					cbPackageTel.setButtonDrawable(R.drawable.btn_tel_normal);
				}

			} else {
				// 获取数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 显示界面
			mRlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_PACK_VEHICLELIST_REQUEST:
			vehicleListIsSuccess = response.isSuccess;
			vehicleListMessage = response.message;
			// 从服务器获取可抢单车辆列表
			if (vehicleListIsSuccess) {
				vehicleData = response.data;
				// 将车牌号码、车辆id、车辆是否在途分别添加进不同的集合
				for (int i = 0; i < vehicleData.size(); i++) {
					listVehicleNo.add(vehicleData.get(i).vehicleNo);// 车牌号
					listVehicleId.add(vehicleData.get(i).vehicleId);// 车辆Id
					listVehicleOnWay.add(vehicleData.get(i).vehicleIsOnWay);// 车辆是否在途
					listVehicleXing.add(vehicleData.get(i).StarsLevel);// 车辆的星级
				}

				// 获取第一辆车的车辆Id
				if (vehicleData.size() > 0) {
					vehicleId = vehicleData.get(0).vehicleId;
				}
				// 获取第一辆车的vehicleId去请求司机
				if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
					getDataFromSeriverOneDriver(vehicleId);
				}
			}

			break;

		case GET_PACK_ONEDRIVER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 从服务器获取第一辆车的司机列表
			if (isSuccess) {
				// 获取第一辆车的司机列表成功
				oneDriverData = response.data;
				// 获取第一辆车的车辆司机Id
				if (oneDriverData.size() > 0) {
					andId = oneDriverData.get(0).id;
				}

				// 将默认司机添加进集合
				for (int i = 0; i < oneDriverData.size(); i++) {
					Boolean isDefaultDriver = oneDriverData.get(i).isDefaultDriver;// 是否是默认司机
					if (isDefaultDriver) {
						listName.add(oneDriverData.get(i).name);// 司机真实姓名
					}
				}

			}

			break;
		case GET_PACK_DRIVERLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 从服务器获取车辆的司机列表
			if (isSuccess) {
				// 获取车的司机列表成功
				driverData = response.data;
				// 显示选择司机的弹窗
				// showSelectorDriverPpw();

			} else {
				// 获取信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			rlDriver.setEnabled(true);

			break;

		case GET_PACK_GRABATICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 抢单成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 设置主界面按钮为不可点击
				setViewEnable(false);
				isGrabSuccess = true;// 状态为抢单成功
				// 取消弹窗
				selectorMsgPpw.dismiss();
				// 退出当前页面
				PackageListDetailActivity.this.finish();
			} else {
				// 抢单失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 设置弹窗界面按钮为可点击
				setPpwViewEnable(true);
			}

			break;

		case GET_PACK_BIDDINGATICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 竞价成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 设置主界面按钮为灰色
				setViewEnable(false);
				isGrabSuccess = true;// 状态为竞价成功
				// 取消弹窗
				binddingMsgPpw.dismiss();
				// 退出当前页面
				PackageListDetailActivity.this.finish();
			} else {
				// 竞价失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 设置弹窗界面按钮为可点击
				setPpwViewEnable(true);
			}
			break;

		case GET_PACK_BIDDINGINFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取包竞价历史统计信息成功
				PackageBiddingInfo packageBiddingInfo = (PackageBiddingInfo) response.singleData;
				avgPrice = packageBiddingInfo.avgPrice;// 均价
				estimate = packageBiddingInfo.estimate;// 估计
				hisCount = packageBiddingInfo.hisCount;// 成交单数
			}
			break;

		case SHARE_PACKAGE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mShare = (Share) response.singleData;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:创建包详情电话PoupWindow
	 * @Title:createselectTelPoupWindow
	 * @param tdscPhone
	 *            发货方联系电话
	 * @param tmcPhone
	 *            驻矿联系电话
	 * @param stcPhone
	 *            签收联系电话
	 * @return:void
	 * @throws
	 * @Create: 2016年5月31日 下午3:43:18
	 * @Author : zhm
	 */
	private void createselectTelPoupWindow(final String tdscPhone,
			final String tmcPhone, final String stcPhone) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.package_detail_phone_poupwindow, null);
		selectTelPpw = new PopupWindow(view,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		selectTelPpw.setBackgroundDrawable(new BitmapDrawable());
		selectTelPpw.setOutsideTouchable(true);// 设置PopupWindow外部区域可触摸
		selectTelPpw.setFocusable(true); // 设置PopupWindow可获取焦点
		selectTelPpw.setTouchable(true); // 设置PopupWindow可触摸
		selectTelPpw.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#03000000")));
		Button btnFaBao = (Button) view.findViewById(R.id.phone_fabao);
		Button btnKuangFa = (Button) view.findViewById(R.id.phone_kuangfa);
		Button btnQianShou = (Button) view.findViewById(R.id.phone_qianshou);

		if (TextUtils.isEmpty(tdscPhone)) {
			btnFaBao.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(tmcPhone)) {
			btnKuangFa.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(stcPhone)) {
			btnQianShou.setVisibility(View.GONE);
		}
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		selectTelPpwHeight = view.getMeasuredHeight();
		btnFaBao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.callPhoneIndirect(mContext, tdscPhone);
			}
		});
		btnKuangFa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.callPhoneIndirect(mContext, tmcPhone);
			}
		});

		btnQianShou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.callPhoneIndirect(mContext, stcPhone);
			}
		});

		selectTelPpw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				cbPackageTel.setChecked(false);
				cbPackageTel.setButtonDrawable(R.drawable.btn_tel_normal);
			}
		});
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		showToast(this.getResources().getString(R.string.net_error_toast));
		switch (requestId) {
		case GET_PACK_GRABATICKET_REQUEST:
			// 设置弹窗界面按钮为可点击
			setPpwViewEnable(true);
			break;

		case GET_PACK_BIDDINGATICKET_REQUEST:
			// 设置弹窗界面按钮为可点击
			setPpwViewEnable(true);
			break;

		case GET_PACK_DRIVERLIST_REQUEST:
			// 设置选中司机的按钮为可点击
			rlDriver.setEnabled(true);
			break;

		}
	}

	// 选择司机的PopupWindow
	// protected void showSelectorDriverPpw() {
	// selectorDriverPpw = new PopupWindow(PackageListDetailActivity.this);
	// View seldriver_view = getLayoutInflater().inflate(
	// R.layout.packdetail_selvehicle_ppw, null);
	//
	// TextView tvCancle = (TextView) seldriver_view
	// .findViewById(R.id.tv_selvehicle_cancle);
	// TextView tvSure = (TextView) seldriver_view
	// .findViewById(R.id.tv_selvehicle_sure);
	// // 点击取消
	// tvCancle.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// selectorDriverPpw.dismiss();
	// }
	// });
	//
	// // 点击确定
	// tvSure.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 显示司机姓名
	// if (selectDriver != null && !TextUtils.isEmpty(selectDriver)) {
	// tvDriver.setText(selectDriver);
	// }
	// // 取消弹窗
	// selectorDriverPpw.dismiss();
	// }
	// });
	//
	// WheelView mvDriverList = (WheelView) seldriver_view
	// .findViewById(R.id.wv_selvehicle_show);
	//
	// // 循环获取司机列表中每个司机的个人信息
	// for (int i = 0; i < driverData.size(); i++) {
	// listDriverName.add(driverData.get(i).name);// 司机姓名
	// listDriverIsOnWay.add(driverData.get(i).driverIsOnWay);// 司机是否在途
	// listId.add(driverData.get(i).id);// 车辆司机ID
	// }
	//
	// final DriverTextAdapter driverTextAdapter = new DriverTextAdapter(this,
	// listDriverName, 0, maxsize, minsize);
	//
	// mvDriverList.setVisibleItems(5);
	// mvDriverList.setViewAdapter(driverTextAdapter);
	// mvDriverList.setCurrentItem(0);
	// mvDriverList.addChangingListener(new OnWheelChangedListener() {
	// @Override
	// public void onChanged(WheelView wheel, int oldValue, int newValue) {
	// String currentText = (String) driverTextAdapter
	// .getItemText(wheel.getCurrentItem());
	// // 获取每一车辆的车辆司机id
	// andId = listId.get(wheel.getCurrentItem());
	// selectDriver = currentText;
	// setTextviewSizeTwo(currentText, driverTextAdapter);
	//
	// }
	// });
	// mvDriverList.addScrollingListener(new OnWheelScrollListener() {
	// @Override
	// public void onScrollingStarted(WheelView wheel) {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onScrollingFinished(WheelView wheel) {
	// String currentText = (String) driverTextAdapter
	// .getItemText(wheel.getCurrentItem());
	// setTextviewSizeTwo(currentText, driverTextAdapter);
	// }
	// });
	//
	// selectorDriverPpw.setWidth(LayoutParams.MATCH_PARENT);
	// selectorDriverPpw.setHeight(LayoutParams.WRAP_CONTENT);
	// selectorDriverPpw.setBackgroundDrawable(new BitmapDrawable());
	// selectorDriverPpw.setContentView(seldriver_view);
	// selectorDriverPpw.setOutsideTouchable(true);
	// selectorDriverPpw.setFocusable(true);
	// selectorDriverPpw.setTouchable(true); // 设置PopupWindow可触摸
	// selectorDriverPpw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	//
	// }

	class DriverTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected DriverTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;

			// 判断集合是否有数据
			if (listDriverIsOnWay.size() > 0 && list.size() > 0) {
				boolean onWay = (Boolean) listDriverIsOnWay.get(0);// 判断第一个司机是否在途
				if (onWay) {
					selectDriver = list.get(0) + "  承运中";// 初始化为第一个司机
				} else {
					selectDriver = list.get(0) + "  空闲中";// 初始化为第一个司机
				}

			}

			// 判断集合是否有数据
			if (listId.size() > 0) {
				andId = listId.get(0);// 初始化为第1个司机的车辆司机ID
			}

			setItemTextResource(R.id.tempValue);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			// 判断司机是否在途
			boolean onWay = (Boolean) listDriverIsOnWay.get(index);
			if (onWay) {
				return list.get(index) + "  承运中";
			} else {
				return list.get(index) + "  空闲中";
			}
		}

	}

	// 选择车辆的PopupWindow
	protected void showSelectorVehiclePpw() {
		selectorVehiclePpw = new PopupWindow(PackageListDetailActivity.this);
		View selvehicle_view = getLayoutInflater().inflate(
				R.layout.packdetail_selvehicle_ppw, null);

		TextView tvCancle = (TextView) selvehicle_view
				.findViewById(R.id.tv_selvehicle_cancle);
		TextView tvSure = (TextView) selvehicle_view
				.findViewById(R.id.tv_selvehicle_sure);

		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectorVehiclePpw.dismiss();
			}
		});
		// 点击确定
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示车牌号码
				if (selectVehicle != null && !TextUtils.isEmpty(selectVehicle)) {
					tvVehicleNo.setText(selectVehicle);
				}
				// 取消弹窗
				selectorVehiclePpw.dismiss();
			}
		});

		WheelView mvVehicleList = (WheelView) selvehicle_view
				.findViewById(R.id.wv_selvehicle_show);

		final VehicleTextAdapter vehicleTextAdapter = new VehicleTextAdapter(
				this, listVehicleNo, 0, maxsize, minsize);
		mvVehicleList.setVisibleItems(5);
		mvVehicleList.setViewAdapter(vehicleTextAdapter);
		mvVehicleList.setCurrentItem(0);
		mvVehicleList.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) vehicleTextAdapter
						.getItemText(wheel.getCurrentItem());
				// 获取车辆的id
				vehicleId = listVehicleId.get(wheel.getCurrentItem());
				selectVehicle = currentText;
				setTextviewSize(currentText, vehicleTextAdapter);

			}
		});
		mvVehicleList.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) vehicleTextAdapter
						.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, vehicleTextAdapter);
			}
		});

		selectorVehiclePpw.setWidth(LayoutParams.MATCH_PARENT);
		selectorVehiclePpw.setHeight(LayoutParams.WRAP_CONTENT);
		selectorVehiclePpw.setBackgroundDrawable(new BitmapDrawable());
		selectorVehiclePpw.setContentView(selvehicle_view);
		selectorVehiclePpw.setOutsideTouchable(true);// 设置PopupWindow外部区域可触摸
		selectorVehiclePpw.setFocusable(true); // 设置PopupWindow可获取焦点
		selectorVehiclePpw.setTouchable(true); // 设置PopupWindow可触摸
		selectorVehiclePpw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

	}

	class VehicleTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected VehicleTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			// 判断集合是否有数据
			if (listVehicleOnWay.size() > 0 && list.size() > 0) {
				boolean onWay = (Boolean) listVehicleOnWay.get(0);// 判断第一辆车是否在途
				if (onWay) {
					selectVehicle = list.get(0) + "  承运中";// 初始化为第一辆车
				} else {
					selectVehicle = list.get(0) + "  空闲中";// 初始化为第一辆车
				}

			}
			// 判断集合是否有数据
			if (listVehicleId.size() > 0) {
				vehicleId = listVehicleId.get(0);// 初始化为第一辆车的id
			}
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			// 判断车辆是否在途
			boolean onWay = (Boolean) listVehicleOnWay.get(index);
			// 根据获取的星级进行赋值
			int xStarsLevel = listVehicleXing.get(index);

			Log.e("TAG", "获取的星级-----" + xStarsLevel);

			String xingText = "";
			if (xStarsLevel == 1) {
				xingText = "(一星)";
			} else if (xStarsLevel == 2) {
				xingText = "(二星)";
			} else if (xStarsLevel == 3) {
				xingText = "(三星)";
			}
			// 判断车辆是否在途
			if (onWay) {
				return list.get(index) + xingText + "  承运中";
			} else {
				return list.get(index) + xingText + "  空闲中";
			}
		}
	}

	// 竞价PopupWindow
	private void showBiddingMsgPpw() {
		binddingMsgPpw = new PopupWindow(PackageListDetailActivity.this);
		View binddingMsgPpw_view = getLayoutInflater().inflate(
				R.layout.packdetail_bidding_item, null);
		binddingMsgPpw.setWidth(LayoutParams.MATCH_PARENT);
		binddingMsgPpw.setHeight(LayoutParams.WRAP_CONTENT);
		// binddingMsgPpw.setBackgroundDrawable(new ColorDrawable(0x00000000));
		binddingMsgPpw.setBackgroundDrawable(new BitmapDrawable());
		// binddingMsgPpw.setBackgroundDrawable(null);
		binddingMsgPpw.setContentView(binddingMsgPpw_view);

		binddingMsgPpw.setOutsideTouchable(true);// 设置非PopupWindow区域可触摸
		binddingMsgPpw.setTouchable(true); // 设置PopupWindow可触摸
		binddingMsgPpw.setFocusable(true);// 设置PopupWindow可获取焦点
		binddingMsgPpw
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		binddingMsgPpw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		TextView tvEstimateUp = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_estimateUp);
		TextView tvEstimateDown = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_estimateDown);
		TextView tvHisCountUp = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_hisCountUp);
		TextView tvHisCountDown = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_hisCountDown);
		TextView tvAvgPriceUp = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_avgPriceUp);
		TextView tvAvgPriceDown = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_avgPriceDown);

		tvEstimate = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_estimate);
		tvHisCount = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_hisCount);
		tvAvgPrice = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_avgPrice);
		llInputPrice = (LinearLayout) binddingMsgPpw_view
				.findViewById(R.id.ll_bidding_inputPrice);
		rlVehicle = (RelativeLayout) binddingMsgPpw_view
				.findViewById(R.id.rl_bidding_vehicle);
		// rlDriver = (RelativeLayout) binddingMsgPpw_view
		// .findViewById(R.id.rl_bidding_driver);
		etInputPrice = (EditText) binddingMsgPpw_view
				.findViewById(R.id.et_bidding_price);
		tvVehicleNo = (TextView) binddingMsgPpw_view
				.findViewById(R.id.tv_bidding_VehicleNo);
		// tvDriver = (TextView) binddingMsgPpw_view
		// .findViewById(R.id.tv_bidding_driver);
		btnGrab = (Button) binddingMsgPpw_view
				.findViewById(R.id.bt_bidding_determine);

		// 设置估计
		if (!TextUtils.isEmpty(estimate + "") && estimate != null) {
			tvEstimate.setText(estimate + "");
		} else {
			tvEstimateUp.setVisibility(View.INVISIBLE);
			tvEstimateDown.setVisibility(View.INVISIBLE);
		}

		// 设置成交单数
		if (!TextUtils.isEmpty(hisCount) && hisCount != null) {
			tvHisCount.setText(hisCount);
		} else {
			tvHisCountUp.setVisibility(View.INVISIBLE);
			tvHisCountDown.setVisibility(View.INVISIBLE);
		}

		// 设置均价
		if (!TextUtils.isEmpty(avgPrice) && avgPrice != null) {
			tvAvgPrice.setText(avgPrice);
		} else {
			tvAvgPriceUp.setVisibility(View.INVISIBLE);
			tvAvgPriceDown.setVisibility(View.INVISIBLE);
		}

		// /判断存放车牌号的集合是否有数据
		if (listVehicleNo.size() > 0 && listVehicleOnWay.size() > 0
				&& !TextUtils.isEmpty(listVehicleNo.get(0))
				&& !TextUtils.isEmpty(listVehicleOnWay.get(0) + "")) {
			boolean onWay = (Boolean) listVehicleOnWay.get(0);
			if (onWay) {
				tvVehicleNo.setText(listVehicleNo.get(0) + "  承运中");
			} else {
				tvVehicleNo.setText(listVehicleNo.get(0) + "  空闲中");
			}
		}

		// 判断存放第一辆车的默认司机的集合是否有数据
		// if (listName.size() > 0 && !TextUtils.isEmpty(listName.get(0))) {
		// tvDriver.setText(listName.get(0));
		// }

		// 删除弹窗
		ImageView ivDelete = (ImageView) binddingMsgPpw_view
				.findViewById(R.id.iv_bidding_delete);
		ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 将车辆和司机置为空
				// tvDriver.setText("");
				tvVehicleNo.setText("");
				binddingMsgPpw.dismiss();
			}
		});

		// 选择车辆
		rlVehicle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 换车将司机置为空
				// tvDriver.setText("");
				showSelectorVehiclePpw();
			}
		});

		// 选择司机
		// rlDriver.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// String VehicleNo = tvVehicleNo.getText().toString().trim();
		// if (TextUtils.isEmpty(VehicleNo)) {
		// showToast("请先选择车辆");
		// return;
		// }
		// // 根据车辆id获取司机列表
		// if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
		// getDataFromSeriverDriverList(vehicleId);
		// }
		// }
		// });

		// 竞价
		btnGrab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// String driver = tvDriver.getText().toString().trim();
				// if (TextUtils.isEmpty(driver)) {
				// showToast("请先选择司机");
				// return;
				// }
				if (vehicleId == null) {
					showToast("请先选择车辆");
					return;
				}

				if (TextUtils.isEmpty(vehicleId)) {
					showToast("请先选择车辆");
					return;
				}

				String biddingPrice = etInputPrice.getText().toString().trim();
				if (TextUtils.isEmpty(biddingPrice)) {
					showToast("请输入竞价价格");
					return;
				}

				// 调用竞价接口
				if (!TextUtils.isEmpty(packageId) && packageId != null) {
					getDataFromSeriverBiddingATicket(packageId, vehicleId,
							biddingPrice);
				}
				// 将竞价按钮设置为不可点击
				setPpwViewEnable(false);
			}
		});

	}

	// 抢单的PopupWindow
	private void showSelectorMsgPpw() {
		Log.e("TAG", "-----------点击4----------");
		selectorMsgPpw = new PopupWindow(PackageListDetailActivity.this);
		View selectormsg_view = getLayoutInflater().inflate(
				R.layout.packdetail_selectormsg_item, null);

		selectorMsgPpw.setWidth(LayoutParams.MATCH_PARENT);
		selectorMsgPpw.setHeight(LayoutParams.WRAP_CONTENT);
		// selectorMsgPpw.setBackgroundDrawable(new ColorDrawable(0x00000000));
		selectorMsgPpw.setBackgroundDrawable(new BitmapDrawable());
		// selectorMsgPpw.setBackgroundDrawable(null);
		selectorMsgPpw.setContentView(selectormsg_view);

		selectorMsgPpw.setOutsideTouchable(true);// 设置非PopupWindow区域可触摸
		selectorMsgPpw.setTouchable(true); // 设置PopupWindow可触摸
		selectorMsgPpw.setFocusable(true);// 设置PopupWindow可获取焦点
		selectorMsgPpw
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 设置软键盘可顶起
		/*
		 * //获取弹窗高度 selectormsg_view.measure(MeasureSpec.UNSPECIFIED,
		 * MeasureSpec.UNSPECIFIED); int height =
		 * selectormsg_view.getMeasuredHeight(); int[] location = new int[2];
		 * btnDetermine.getLocationOnScreen(location);
		 * selectorMsgPpw.showAtLocation(btnDetermine, Gravity.NO_GRAVITY, 0,
		 * location[1] - height);// 设置弹出位置
		 */selectorMsgPpw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);// 设置弹出位置

		llInputCode = (LinearLayout) selectormsg_view
				.findViewById(R.id.rl_selectormsg_inputCode);
		rlVehicle = (RelativeLayout) selectormsg_view
				.findViewById(R.id.rl_selectormsg_vehicle);
		// rlDriver = (RelativeLayout) selectormsg_view
		// .findViewById(R.id.rl_selectormsg_driver);
		etInputCode = (EditText) selectormsg_view
				.findViewById(R.id.et_selectormsg_code);
		tvVehicleNo = (TextView) selectormsg_view
				.findViewById(R.id.tv_selector_VehicleNo);
		// tvDriver = (TextView) selectormsg_view
		// .findViewById(R.id.tv_selector_driver);
		btnGrab = (Button) selectormsg_view
				.findViewById(R.id.bt_selector_determine);
		ivLine = (View) selectormsg_view.findViewById(R.id.iv_selectormsg_line);

		// 判断存放车牌号的集合是否有数据--并展示第一辆车的数据
		if (listVehicleNo.size() > 0 && listVehicleOnWay.size() > 0
				&& !TextUtils.isEmpty(listVehicleNo.get(0))
				&& !TextUtils.isEmpty(listVehicleOnWay.get(0) + "")) {
			// 判断第一辆车是否在承运中
			boolean onWay = (Boolean) listVehicleOnWay.get(0);
			if (onWay) {
				tvVehicleNo.setText(listVehicleNo.get(0) + "  承运中");
			} else {
				tvVehicleNo.setText(listVehicleNo.get(0) + "  空闲中");
			}
		}

		// 判断存放第一辆车的默认司机的集合是否有数据
		// if (listName.size() > 0 && !TextUtils.isEmpty(listName.get(0))) {
		// tvDriver.setText(listName.get(0));
		// }

		// 判断是否需要验证码
		if (isNeedInviteCode) {
			// 锁定
			llInputCode.setVisibility(View.VISIBLE);
			ivLine.setVisibility(View.VISIBLE);
		} else {
			// 不锁定
			llInputCode.setVisibility(View.GONE);
			ivLine.setVisibility(View.GONE);
		}

		// 删除按钮
		ImageView ivDelete = (ImageView) selectormsg_view
				.findViewById(R.id.iv_selectormsg_delete);
		// 取消弹窗
		ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectorMsgPpw.dismiss();
				// 显示车辆、司机置为空
				// tvDriver.setText("");
				tvVehicleNo.setText("");
			}
		});
		// 选择车辆
		rlVehicle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出选择车辆弹窗
				showSelectorVehiclePpw();
				// 重新选车辆要清空原有司机
				// tvDriver.setText("");
			}
		});

		// 选择司机
		// rlDriver.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// String VehicleNo = tvVehicleNo.getText().toString().trim();
		// if (TextUtils.isEmpty(VehicleNo)) {
		// showToast("请先选择车辆");
		// return;
		// }
		// // 根据车辆ID从服务器获取司机列表
		// if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
		// getDataFromSeriverDriverList(vehicleId);
		// }
		// }
		// });

		// 抢单
		btnGrab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// String driver = tvDriver.getText().toString().trim();
				// if (TextUtils.isEmpty(driver)) {
				// showToast("请先选择司机");
				// return;
				// }
				if (vehicleId == null) {
					showToast("请先选择车辆");
					return;
				}

				if (TextUtils.isEmpty(vehicleId)) {
					showToast("请先选择车辆");
					return;
				}

				if (isNeedInviteCode) {
					// 锁定
					String inputCode = etInputCode.getText().toString().trim();

					if (TextUtils.isEmpty(inputCode)) {
						showToast("请输入验证码");
					}
					// 调用锁定接口
					if (!TextUtils.isEmpty(packageId) && packageId != null) {
						getDataFromSeriverGrabATicket(packageId, vehicleId,
								inputCode);
					}

				} else {
					// 调用没有锁定接口
					if (!TextUtils.isEmpty(packageId) && packageId != null) {
						getDataFromSeriverGrabATicket(packageId, vehicleId);
					}
				}

				// 设置弹窗界面抢单按钮不可点击
				setPpwViewEnable(false);
			}
		});

	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			VehicleTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSizeTwo(String curriteItemText,
			DriverTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	// 本界面的跳转方法
	public static void invoke(Context activity, String packageId) {
		Intent intent = new Intent();
		intent.setClass(activity, PackageListDetailActivity.class);
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			intent.putExtra("packageId", packageId);// 包的id
		}
		activity.startActivity(intent);
	}

	/**
	 * 本Activity跳转增加flag方式
	 * 
	 * @param activity
	 * @param packageId
	 */
	public static void invokeNewTask(Context activity, String packageId) {
		Intent intent = new Intent();
		intent.setClass(activity, PackageListDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			intent.putExtra("packageId", packageId);// 包的id
		}
		activity.startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.bt_packagedetail_tel:
			if (isChecked) {
				int[] location = new int[2];
				cbPackageTel.getLocationOnScreen(location);
				selectTelPpw.showAtLocation(cbPackageTel, Gravity.NO_GRAVITY,
						location[0], location[1] - selectTelPpwHeight);
				cbPackageTel.setButtonDrawable(R.drawable.btn_tel_xia);
				System.out.println("getLocationOnScreen:" + location[0] + ","
						+ location[1]);
			} else {
				selectTelPpw.dismiss();
				cbPackageTel.setButtonDrawable(R.drawable.btn_tel_normal);
			}
			break;

		default:
			break;
		}
	}

}
