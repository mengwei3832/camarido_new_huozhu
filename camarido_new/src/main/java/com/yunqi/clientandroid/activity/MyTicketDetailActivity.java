package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.entity.GetVTicketByIdInfo;
import com.yunqi.clientandroid.http.request.CancelTicketRequest;
import com.yunqi.clientandroid.http.request.ChangeTicketDriverRequest;
import com.yunqi.clientandroid.http.request.GetVTicketByIdRequest;
import com.yunqi.clientandroid.http.request.TicketDriverListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的订单模块下的详情界面
 * @date 15/12/2
 */
public class MyTicketDetailActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	private String ticketId;// 订单的id
	private int maxsize = 24;// 设置字体的大小
	private int minsize = 14;// 设置字体的大小
	private boolean isChange;// 是否需要更改司机
	private String priceType;// 价格的类型
	private String vehicleId;// 车辆Id
	private String andId;// 车辆司机Id
	private String driverName;// 选中的司机
	private String packageBeginName;
	private String packageBeginAddress;
	private String packageEndName;
	private String packageEndAddress;
	private String packageBeginCityText;
	private String packageEndCityText;
	private String packageBeginLatitude;
	private String packageBeginLongitude;
	private String packageEndLatitude;
	private String packageEndLongitude;
	private int dPackageId;// 包ID
	private String tmcPhone;
	private String stcPhone;
	private String pcsPhone;

	private View parentView;
	private PopupWindow selectorDriverPpw;
	private RelativeLayout mRlGlobal;
	private ProgressBar mProgress;
	private TextView tvProvenance;
	private TextView tvProAddress;
	private TextView tvDestination;
	private TextView tvDestaddress;
	private TextView tvFreight;
	private TextView tvSubsidy;
	private TextView tvCoaltype;
	private TextView tvCalorific;
	private TextView tvMileage;
	private TextView tvTolls;
	private TextView tvCreateTime;
	private TextView tvRecommended;
	private TextView tvNote;
	private Button btnDelete;
	private TextView tvDriver;
	private TextView tvChange;
	private TextView tvVehicleNo;
	private TextView tvOrderNum;
	private TextView tv_ticket_detail_company;// 公司名称
	private RelativeLayout ll_detail_gongsi, ll_detail_pingtai;
	private TextView ll_detail_it;// 缴纳保险
	private RelativeLayout rlDingDan;
	private RelativeLayout rlDingdanInfo;
	private RelativeLayout rlGoodsInfo;
	private RelativeLayout rlGoods;
	private RelativeLayout rlYunfei;
	private RelativeLayout rlYunfeiInfo;
	private RelativeLayout rlTishi;
	private RelativeLayout rlTishiInfo;
	private CheckBox btPackagedetailTel;
	private ImageView ivDingdanIcon;
	private ImageView ivGoodsIcon;
	private ImageView ivYunfeiIcon;
	private ImageView ivTishiIcon;
	private TextView tvGoods;
	private TextView tvYunFei;
	private LinearLayout llTicketVisver;

	private PopupWindow selectTelPpw;
	private int selectTelPpwHeight;

	private TextView tvTmcPhoneOne;
	private TextView tvTmcPhoneTwo;
	private TextView tvTmcPhoneThree;
	private TextView tvStcPhoneOne;
	private TextView tvStcPhoneTwo;
	private TextView tvStcPhoneThree;
	private TextView tvPcsPhoneOne;
	private TextView tvPcsPhoneTwo;
	private TextView tvPcsPhoneThree;
	private TextView tvLoadingFee;
	private TextView tvUnloadingFee;
	private TextView tvGoodsPrice;
	private AlertDialog alertDialog;

	private ArrayList<DriverDetailInfo> driverData;// 存放司机列表
	private ArrayList<Object> listDriverIsOnWay = new ArrayList<Object>();// 存司机是否在途
	private ArrayList<String> listDriverName = new ArrayList<String>();// 存放司机的名字
	private ArrayList<String> listId = new ArrayList<String>();// 存放车辆司机Id

	// 本页请求
	private GetVTicketByIdRequest mGetVTicketByIdRequest;
	private ChangeTicketDriverRequest mChangeTicketDriverRequest;
	private TicketDriverListRequest mTicketDriverListRequest;
	private CancelTicketRequest mCancelTicketRequest;

	// 本页面请求id
	private final int GET_TICKET_DETAIL_REQUEST = 1;
	private final int CHANGE_TICKET_DRIVER_REQUEST = 2;
	private final int GET_TICKET_DRIVERLIST_REQUEST = 3;
	private final int CANCEL_TICKET_REQUEST = 4;

	// 初始化titileBar
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.order_detail));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyTicketDetailActivity.this.finish();
			}
		});
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_myticketdetail;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_myticketdetail, null);

		// 获取从列表页面传过来的订单id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getString("ticketId");
		}

		// 初始化控件
		tvProvenance = (TextView) findViewById(R.id.tv_ticketdetail_provenance);
		tvProAddress = (TextView) findViewById(R.id.tv_ticketdetail_proaddress);
		tvDestination = (TextView) findViewById(R.id.tv_ticketdetail_destination);
		tvDestaddress = (TextView) findViewById(R.id.tv_ticketdetail_destaddress);
		tvDriver = (TextView) findViewById(R.id.tv_packdetail_calorific_ding);
		// tvChange = (TextView) findViewById(R.id.tv_ticketdetail_change);
		tvVehicleNo = (TextView) findViewById(R.id.tv_packdetail_goodsPrice_ding);
		tvOrderNum = (TextView) findViewById(R.id.tv_packdetail_coaltype_ding);
		tvFreight = (TextView) findViewById(R.id.tv_packdetail_freight);
		tvSubsidy = (TextView) findViewById(R.id.tv_packdetail_subsidy);
		tvCoaltype = (TextView) findViewById(R.id.tv_packdetail_coaltype);
		tvCalorific = (TextView) findViewById(R.id.tv_packdetail_calorific);
		tvMileage = (TextView) findViewById(R.id.tv_packdetail_mileage);
		tvTolls = (TextView) findViewById(R.id.tv_packdetail_tolls);
		tvCreateTime = (TextView) findViewById(R.id.tv_time);
		tvRecommended = (TextView) findViewById(R.id.tv_packdetail_recommended);
		tvNote = (TextView) findViewById(R.id.tv_packdetail_note);
		btnDelete = (Button) findViewById(R.id.bt_packagedetail_determine);
		mRlGlobal = (RelativeLayout) findViewById(R.id.rl_ticketdetail_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_packdetail_progress);
		llTicketVisver = obtainView(R.id.ll_ticket_visver);

		// tvTmcPhoneOne = (TextView)
		// findViewById(R.id.tv_ticketdetail_tmcPhoneOne);
		// tvTmcPhoneTwo = (TextView)
		// findViewById(R.id.tv_ticketdetail_tmcPhoneTwo);
		// tvTmcPhoneThree = (TextView)
		// findViewById(R.id.tv_ticketdetail_tmcPhoneThree);
		// tvStcPhoneOne = (TextView)
		// findViewById(R.id.tv_ticketdetail_stcPhoneOne);
		// tvStcPhoneTwo = (TextView)
		// findViewById(R.id.tv_ticketdetail_stcPhoneTwo);
		// tvStcPhoneThree = (TextView)
		// findViewById(R.id.tv_ticketdetail_stcPhoneThree);
		// tvPcsPhoneOne = (TextView)
		// findViewById(R.id.tv_ticketdetail_pcsPhoneOne);
		// tvPcsPhoneTwo = (TextView)
		// findViewById(R.id.tv_ticketdetail_pcsPhoneTwo);
		// tvPcsPhoneThree = (TextView)
		// findViewById(R.id.tv_ticketdetail_pcsPhoneThree);
		tvLoadingFee = (TextView) findViewById(R.id.tv_xiezhuang);
		// tvUnloadingFee = (TextView)
		// findViewById(R.id.tv_ticketdetail_unloadingFee);
		tvGoodsPrice = (TextView) findViewById(R.id.tv_packdetail_goodsPrice);

		// 新增字段的变量名
		tv_ticket_detail_company = (TextView) findViewById(R.id.tv_ticket_detail_company);
		ll_detail_gongsi = (RelativeLayout) findViewById(R.id.ll_detail_gongsi);
		// ll_detail_pingtai = (RelativeLayout)
		// findViewById(R.id.ll_detail_pingtai);
		ll_detail_it = (TextView) findViewById(R.id.ll_detail_it);
		rlDingDan = obtainView(R.id.rl_dingdan);
		rlDingdanInfo = obtainView(R.id.rl_dingdan_info);
		rlGoodsInfo = obtainView(R.id.rl_goods_info);
		rlGoods = obtainView(R.id.rl_goods);
		rlYunfei = obtainView(R.id.rl_yunfei);
		rlYunfeiInfo = obtainView(R.id.rl_yunfei_info);
		rlTishi = obtainView(R.id.rl_tishi);
		rlTishiInfo = obtainView(R.id.rl_tishi_info);
		btPackagedetailTel = obtainView(R.id.bt_packagedetail_tel);

		ivDingdanIcon = obtainView(R.id.iv_dingdan_icon_ding);
		ivGoodsIcon = obtainView(R.id.iv_goods_icon);
		ivTishiIcon = obtainView(R.id.iv_tishi_icon);
		ivYunfeiIcon = obtainView(R.id.iv_yunfei_icon);

		tvGoods = obtainView(R.id.tv_goods);
		tvYunFei = obtainView(R.id.tv_yunfei);

		rlDingDan.setTag(true);
		rlGoods.setTag(true);
		rlTishi.setTag(true);
		rlYunfei.setTag(true);

	}

	@Override
	protected void initData() {
		// 从服务器获取我的订单模块的订单详情信息
		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			getDataFromServiceTicketDetail(ticketId);
		}

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		// tvChange.setOnClickListener(this);

		btnDelete.setOnClickListener(this);
		// tvTmcPhoneOne.setOnClickListener(this);
		// tvTmcPhoneTwo.setOnClickListener(this);
		// tvTmcPhoneThree.setOnClickListener(this);
		// tvStcPhoneOne.setOnClickListener(this);
		// tvStcPhoneTwo.setOnClickListener(this);
		// tvStcPhoneThree.setOnClickListener(this);
		// tvPcsPhoneOne.setOnClickListener(this);
		// tvPcsPhoneTwo.setOnClickListener(this);
		// tvPcsPhoneThree.setOnClickListener(this);

		ll_detail_gongsi.setOnClickListener(this);
		// ll_detail_pingtai.setOnClickListener(this);

		// 保证金的监听
		ll_detail_it.setOnClickListener(this);

		rlDingDan.setOnClickListener(this);
		rlGoods.setOnClickListener(this);
		rlYunfei.setOnClickListener(this);
		rlTishi.setOnClickListener(this);
		btPackagedetailTel.setOnCheckedChangeListener(this);

	}

	// private boolean flag = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.rl_dingdan: //
			if ((Boolean) rlDingDan.getTag()) {
				rlDingdanInfo.setVisibility(View.VISIBLE);
				rlDingDan.setTag(false);
				shunShiZhen(ivDingdanIcon);
			} else {
				rlDingdanInfo.setVisibility(View.GONE);
				rlDingDan.setTag(true);
				niShiZhen(ivDingdanIcon);
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

		case R.id.rl_yunfei:
			if ((Boolean) rlYunfei.getTag()) {
				rlYunfeiInfo.setVisibility(View.VISIBLE);
				rlYunfei.setTag(false);
				shunShiZhen(ivYunfeiIcon);
			} else {
				rlYunfeiInfo.setVisibility(View.GONE);
				rlYunfei.setTag(true);
				niShiZhen(ivYunfeiIcon);
			}
			break;

		case R.id.rl_tishi:
			if ((Boolean) rlTishi.getTag()) {
				rlTishiInfo.setVisibility(View.VISIBLE);
				rlTishi.setTag(false);
				shunShiZhen(ivTishiIcon);
			} else {
				rlTishiInfo.setVisibility(View.GONE);
				rlTishi.setTag(true);
				niShiZhen(ivTishiIcon);
			}
			break;

		// case R.id.tv_ticketdetail_change:
		// // 选择司机的PPW
		// showSelectorDriverPpw();
		//
		// break;

		case R.id.bt_packagedetail_determine:
			// 取消订单
			showAffirm(ticketId);

			break;

		// case R.id.tv_ticketdetail_tmcPhoneOne:
		// // 驻矿电话1
		// String tmcPhoneOne = tvTmcPhoneOne.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(tmcPhoneOne) && tmcPhoneOne != null) {
		// CommonUtil.callPhoneIndirect(this, tmcPhoneOne);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_tmcPhoneTwo:
		// // 驻矿电话2
		// String tmcPhoneTwo = tvTmcPhoneTwo.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(tmcPhoneTwo) && tmcPhoneTwo != null) {
		// CommonUtil.callPhoneIndirect(this, tmcPhoneTwo);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_tmcPhoneThree:
		// // 驻矿电话3
		// String tmcPhoneThree = tvTmcPhoneThree.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(tmcPhoneThree) && tmcPhoneThree != null) {
		// CommonUtil.callPhoneIndirect(this, tmcPhoneThree);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_stcPhoneOne:
		// // 签收电话1
		// String stcPhoneOne = tvStcPhoneOne.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(stcPhoneOne) && stcPhoneOne != null) {
		// CommonUtil.callPhoneIndirect(this, stcPhoneOne);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_stcPhoneTwo:
		// // 签收电话2
		// String stcPhoneTwo = tvStcPhoneTwo.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(stcPhoneTwo) && stcPhoneTwo != null) {
		// CommonUtil.callPhoneIndirect(this, stcPhoneTwo);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_stcPhoneThree:
		// // 签收电话3
		// String stcPhoneThree = tvStcPhoneThree.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(stcPhoneThree) && stcPhoneThree != null) {
		// CommonUtil.callPhoneIndirect(this, stcPhoneThree);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_pcsPhoneOne:
		// // 客服电话1
		// String pcsPhoneOne = tvPcsPhoneOne.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(pcsPhoneOne) && pcsPhoneOne != null) {
		// CommonUtil.callPhoneIndirect(this, pcsPhoneOne);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_pcsPhoneTwo:
		// // 客服电话2
		// String pcsPhoneTwo = tvPcsPhoneTwo.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(pcsPhoneTwo) && pcsPhoneTwo != null) {
		// CommonUtil.callPhoneIndirect(this, pcsPhoneTwo);
		// }
		//
		// break;
		//
		// case R.id.tv_ticketdetail_pcsPhoneThree:
		// // 客服电话3
		// String pcsPhoneThree = tvPcsPhoneThree.getText().toString().trim();
		//
		// if (!TextUtils.isEmpty(pcsPhoneThree) && pcsPhoneThree != null) {
		// CommonUtil.callPhoneIndirect(this, pcsPhoneThree);
		// }
		//
		// break;

		case R.id.ll_detail_gongsi:// 点击进入企业详情
			Log.e("TAG", "包的ID——————————————————————————" + dPackageId);

			String dePackageID = String.valueOf(dPackageId);

			Intent intent = new Intent(MyTicketDetailActivity.this,
					CompanyDetailActivity.class);
			CompanyDetailActivity.invoke_intent(MyTicketDetailActivity.this,
					dePackageID, intent);
			break;

		case R.id.ll_detail_it:// 点击进入保证金页面
			// Intent it = new Intent(MyTicketDetailActivity.this,
			// HelpActivity.class);g
			// startActivity(it);
			HelpActivity.invoke(MyTicketDetailActivity.this, "insurance");
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:图标逆时针
	 * @Title:niShiZhen
	 * @param ivYunFeiIcon2
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
		ra.startNow();
	}

	/**
	 * 
	 * @Description:图标顺时针
	 * @Title:shunShiZhen
	 * @param ivYunFeiIcon2
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
		ra.startNow();
	}

	// 从服务器获取更改司机列表接口
	// private void getDataFromSeriverDriverList(String vehicleId) {
	// // 清空存放司机姓名、车辆司机id、司机是否在途集合
	// listDriverName.clear();
	// listDriverIsOnWay.clear();
	// listId.clear();
	//
	// mTicketDriverListRequest = new TicketDriverListRequest(this, vehicleId);
	// mTicketDriverListRequest.setRequestId(GET_TICKET_DRIVERLIST_REQUEST);
	// httpPost(mTicketDriverListRequest);
	// }

	// 点击更改司机的接口
	// private void changeTicketDriver(String ticketId, String vehicleDriverId)
	// {
	// mChangeTicketDriverRequest = new ChangeTicketDriverRequest(this,
	// ticketId, vehicleDriverId);
	// mChangeTicketDriverRequest.setRequestId(CHANGE_TICKET_DRIVER_REQUEST);
	// httpPost(mChangeTicketDriverRequest);
	// }

	// 从服务器获取我的订单模块的订单详情信息
	private void getDataFromServiceTicketDetail(String ticketId) {
		mRlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetVTicketByIdRequest = new GetVTicketByIdRequest(this, ticketId);
		mGetVTicketByIdRequest.setRequestId(GET_TICKET_DETAIL_REQUEST);
		httpPost(mGetVTicketByIdRequest);

	}

	// 取消订单
	protected void cancleTicket(String ticketId, String memo) {
		mCancelTicketRequest = new CancelTicketRequest(
				MyTicketDetailActivity.this, ticketId, memo);
		mCancelTicketRequest.setRequestId(CANCEL_TICKET_REQUEST);
		httpPost(mCancelTicketRequest);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case CANCEL_TICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 取消订单成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭弹窗
				alertDialog.dismiss();

				// 关闭当前页面
				MyTicketDetailActivity.this.finish();
			} else {
				// 取消订单失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置删除按钮可点击
				btnDelete.setEnabled(true);
			}
			break;

		case GET_TICKET_DETAIL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取订单详情成功
				GetVTicketByIdInfo getVTicketByIdInfo = (GetVTicketByIdInfo) response.singleData;

				String ticketStatus = getVTicketByIdInfo.ticketStatus;// 执行状态：0：待生效；1：待执行；2：待换票；3：待装运；4：待收货；5：待审核；6：待结算；7：可领取，8：已结算；9：禁用；10：取消
				packageBeginName = getVTicketByIdInfo.packageBeginName;// 出发地名称
				packageBeginAddress = getVTicketByIdInfo.packageBeginAddress;// 出发地详细地址
				packageEndName = getVTicketByIdInfo.packageEndName;// 目的地名称
				packageEndAddress = getVTicketByIdInfo.packageEndAddress;// 目的地详细地址
				packageBeginCityText = getVTicketByIdInfo.packageBeginCityText;// 出发地市描述
				packageEndCityText = getVTicketByIdInfo.packageEndCityText;// 目的地市描述
				packageBeginLatitude = getVTicketByIdInfo.packageBeginLatitude;// 出发地维度
				packageBeginLongitude = getVTicketByIdInfo.packageBeginLongitude;// 出发地经度
				packageEndLatitude = getVTicketByIdInfo.packageEndLatitude;// 目的地维度
				packageEndLongitude = getVTicketByIdInfo.packageEndLongitude;// 目的地经度
				String ticketCode = getVTicketByIdInfo.ticketCode;// 订单号
				String packagePrice = getVTicketByIdInfo.packagePrice;// 运价
				String freightPayable = getVTicketByIdInfo.freightPayable;// 运费
				String packagePriceType = getVTicketByIdInfo.packagePriceType;// 运价方式
				String categoryName = getVTicketByIdInfo.categoryName;// 货品分类名称
				String packageGoodsCalorific = getVTicketByIdInfo.packageGoodsCalorific;// 货品信息
				String ticketMemo = getVTicketByIdInfo.ticketMemo;// 物流备注
				String packageRoadToll = getVTicketByIdInfo.packageRoadToll;// 过路费
				String packageRecommendPath = getVTicketByIdInfo.packageRecommendPath;// 推荐路线
				String packageDistance = getVTicketByIdInfo.packageDistance;// 距离
				String createTime = getVTicketByIdInfo.createTime;// 下单时间
				String name = getVTicketByIdInfo.name;// 执行人姓名
				String vehicleNo = getVTicketByIdInfo.vehicleNo;// 车牌号
				vehicleId = getVTicketByIdInfo.vehicleId;// 车辆Id
				String subsidy = getVTicketByIdInfo.subsidy;// 补助
				String packageGoodsPrice = getVTicketByIdInfo.packageGoodsPrice;// 货值价格
				tmcPhone = getVTicketByIdInfo.tmcPhone;// 驻矿联系电话
				stcPhone = getVTicketByIdInfo.stcPhone;// 签收联系电话
				pcsPhone = getVTicketByIdInfo.pcsPhone;// 平台客服电话
				String loadingFee = getVTicketByIdInfo.loadingFee;// 装车费
				String unloadingFee = getVTicketByIdInfo.unloadingFee;// 卸车费

				// 新增的字段
				String cTenantName = getVTicketByIdInfo.tenantName;// 公司名称
				String cTenantShortName = getVTicketByIdInfo.tenantShortname;// 公司简称
				boolean pIsPayFor = getVTicketByIdInfo.isPayFor;// 是否垫付
				boolean pIsPayIn = getVTicketByIdInfo.isPayIn;// 是否缴纳保证金
				dPackageId = getVTicketByIdInfo.packageId;// 包ID
				int pInsuranceType = getVTicketByIdInfo.insuranceType;// 0：无保险
																		// 1：平台送保险
																		// 2：自己购买保险

				Log.e("TAG", "---------公司的简称---------" + cTenantShortName);

				// 公司的名称
				if (!TextUtils.isEmpty(cTenantShortName)
						&& cTenantShortName != null) {
					tv_ticket_detail_company.setText(cTenantShortName);
				}

				// 显示保证金的图标
				if (pInsuranceType == 0) {
					ll_detail_it.setVisibility(View.GONE);
				} else if (pInsuranceType == 1) {
					ll_detail_it.setVisibility(View.VISIBLE);
				}
				// 根据执行状态显示删除订单按钮
				if (ticketStatus != null && ticketStatus.equals("0")) {
					btnDelete.setVisibility(View.VISIBLE);
				} else if (ticketStatus != null && ticketStatus.equals("1")) {
					btnDelete.setVisibility(View.VISIBLE);
				}

				// 根据执行状态隐藏电话
				if ((ticketStatus != null && ticketStatus.equals("8"))
						|| (ticketStatus != null && ticketStatus.equals("9"))
						|| (ticketStatus != null && ticketStatus.equals("10"))) {
					llTicketVisver.setVisibility(View.GONE);
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

				// 设置驻矿电话
				// if (!TextUtils.isEmpty(tmcPhone) && tmcPhone != null) {
				// if (tmcPhone.contains(",")) {
				// // 包含逗号
				// String[] tmcPhoneArray = tmcPhone.split(",");
				// if (tmcPhoneArray.length >= 1
				// && tmcPhoneArray.length < 2) {
				// // 设置驻矿电话1
				// String tmcPhoneOne = tmcPhoneArray[0];
				// tvTmcPhoneOne.setText(Html.fromHtml("<u>"
				// + tmcPhoneOne + "</u>"));
				// } else if (tmcPhoneArray.length >= 2
				// && tmcPhoneArray.length < 3) {
				// // 设置驻矿电话1
				// String tmcPhoneOne = tmcPhoneArray[0];
				// tvTmcPhoneOne.setText(Html.fromHtml("<u>"
				// + tmcPhoneOne + "</u>"));
				// // 设置驻矿电2
				// String tmcPhoneTwo = tmcPhoneArray[1];
				// tvTmcPhoneTwo.setText(Html.fromHtml("<u>"
				// + tmcPhoneTwo + "</u>"));
				// tvTmcPhoneTwo.setVisibility(View.VISIBLE);
				// } else if (tmcPhoneArray.length >= 3) {
				// // 设置驻矿电话1
				// String tmcPhoneOne = tmcPhoneArray[0];
				// tvTmcPhoneOne.setText(Html.fromHtml("<u>"
				// + tmcPhoneOne + "</u>"));
				// // 设置驻矿电2
				// String tmcPhoneTwo = tmcPhoneArray[1];
				// tvTmcPhoneTwo.setText(Html.fromHtml("<u>"
				// + tmcPhoneTwo + "</u>"));
				// tvTmcPhoneTwo.setVisibility(View.VISIBLE);
				// // 设置驻矿电话3
				// String tmcPhoneThree = tmcPhoneArray[2];
				// tvTmcPhoneThree.setText(Html.fromHtml("<u>"
				// + tmcPhoneThree + "</u>"));
				// tvTmcPhoneThree.setVisibility(View.VISIBLE);
				// }
				// } else {
				// // 不包含逗号--设置驻矿电话1
				// tvTmcPhoneOne.setText(Html.fromHtml("<u>" + tmcPhone
				// + "</u>"));
				// }
				// }
				// // 设置签收电话
				// if (!TextUtils.isEmpty(stcPhone) && stcPhone != null) {
				// if (stcPhone.contains(",")) {
				// // 包含逗号
				// String[] stcPhoneArray = stcPhone.split(",");
				// if (stcPhoneArray.length >= 1
				// && stcPhoneArray.length < 2) {
				// // 设置签收电话1
				// String stcPhoneOne = stcPhoneArray[0];
				// tvStcPhoneOne.setText(Html.fromHtml("<u>"
				// + stcPhoneOne + "</u>"));
				// } else if (stcPhoneArray.length >= 2
				// && stcPhoneArray.length < 3) {
				// // 设置签收电话1
				// String stcPhoneOne = stcPhoneArray[0];
				// tvStcPhoneOne.setText(Html.fromHtml("<u>"
				// + stcPhoneOne + "</u>"));
				// // 设置签收电2
				// String stcPhoneTwo = stcPhoneArray[1];
				// tvStcPhoneTwo.setText(Html.fromHtml("<u>"
				// + stcPhoneTwo + "</u>"));
				// tvStcPhoneTwo.setVisibility(View.VISIBLE);
				// } else if (stcPhoneArray.length >= 3) {
				// // 设置签收电话1
				// String stcPhoneOne = stcPhoneArray[0];
				// tvStcPhoneOne.setText(Html.fromHtml("<u>"
				// + stcPhoneOne + "</u>"));
				// // 设置签收电2
				// String stcPhoneTwo = stcPhoneArray[1];
				// tvStcPhoneTwo.setText(Html.fromHtml("<u>"
				// + stcPhoneTwo + "</u>"));
				// tvStcPhoneTwo.setVisibility(View.VISIBLE);
				// // 设置签收电话3
				// String stcPhoneThree = stcPhoneArray[2];
				// tvStcPhoneThree.setText(Html.fromHtml("<u>"
				// + stcPhoneThree + "</u>"));
				// tvStcPhoneThree.setVisibility(View.VISIBLE);
				// }
				// } else {
				// // 不包含逗号--设置签收电话1
				// tvStcPhoneOne.setText(Html.fromHtml("<u>" + stcPhone
				// + "</u>"));
				// }
				// }
				//
				// // 设置客服电话
				// if (!TextUtils.isEmpty(pcsPhone) && pcsPhone != null) {
				// if (pcsPhone.contains(",")) {
				// // 包含逗号
				// String[] pcsPhoneArray = pcsPhone.split(",");
				// if (pcsPhoneArray.length >= 1
				// && pcsPhoneArray.length < 2) {
				// // 设置客服电话1
				// String pcsPhoneOne = pcsPhoneArray[0];
				// tvPcsPhoneOne.setText(Html.fromHtml("<u>"
				// + pcsPhoneOne + "</u>"));
				// } else if (pcsPhoneArray.length >= 2
				// && pcsPhoneArray.length < 3) {
				// // 设置客服电话1
				// String pcsPhoneOne = pcsPhoneArray[0];
				// tvPcsPhoneOne.setText(Html.fromHtml("<u>"
				// + pcsPhoneOne + "</u>"));
				// // 设置客服电2
				// String pcsPhoneTwo = pcsPhoneArray[1];
				// tvPcsPhoneTwo.setText(Html.fromHtml("<u>"
				// + pcsPhoneTwo + "</u>"));
				// tvPcsPhoneTwo.setVisibility(View.VISIBLE);
				// } else if (pcsPhoneArray.length >= 3) {
				// // 设置客服电话1
				// String pcsPhoneOne = pcsPhoneArray[0];
				// tvPcsPhoneOne.setText(Html.fromHtml("<u>"
				// + pcsPhoneOne + "</u>"));
				// // 设置客服电2
				// String pcsPhoneTwo = pcsPhoneArray[1];
				// tvPcsPhoneTwo.setText(Html.fromHtml("<u>"
				// + pcsPhoneTwo + "</u>"));
				// tvPcsPhoneTwo.setVisibility(View.VISIBLE);
				// // 设置客服电话3
				// String pcsPhoneThree = pcsPhoneArray[2];
				// tvPcsPhoneThree.setText(Html.fromHtml("<u>"
				// + pcsPhoneThree + "</u>"));
				// tvPcsPhoneThree.setVisibility(View.VISIBLE);
				// }
				// } else {
				// // 不包含逗号--设置客服电话1
				// tvPcsPhoneOne.setText(Html.fromHtml("<u>" + pcsPhone
				// + "</u>"));
				// }
				// }

				// 设置装车费
				if ((!TextUtils.isEmpty(loadingFee) && loadingFee != null)
						|| (!TextUtils.isEmpty(unloadingFee) && unloadingFee != null)) {
					tvLoadingFee.setText("装" + loadingFee + "元/车,卸"
							+ unloadingFee + "元/车");
				}

				// 设置卸车费
				// if (!TextUtils.isEmpty(unloadingFee) && unloadingFee != null)
				// {
				// tvUnloadingFee.setText(unloadingFee + "元");
				// }

				// 司机
				if (!TextUtils.isEmpty(name) && name != null) {
					tvDriver.setText(name);
				}

				// 车牌号
				if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
					tvVehicleNo.setText(vehicleNo);
				}

				// 订单号
				if (!TextUtils.isEmpty(ticketCode) && ticketCode != null) {
					tvOrderNum.setText(ticketCode);
				}

				if ((!TextUtils.isEmpty(packagePrice) && packagePrice != null)
						&& (!TextUtils.isEmpty(subsidy) && subsidy != null)) {
					tvYunFei.setText(packagePrice + "元" + priceType + ",补助"
							+ subsidy + "元/车");
				} else {
					tvYunFei.setText("---");
				}

				// 运费
				if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
					tvFreight.setText(packagePrice + "元" + priceType);
				}

				if ((!TextUtils.isEmpty(packageGoodsPrice) && packageGoodsPrice != null)
						&& (!TextUtils.isEmpty(categoryName) && categoryName != null)) {
					tvGoods.setText(categoryName + "," + packageGoodsPrice
							+ "元" + priceType);
				} else {
					tvGoods.setText("---");
				}

				// 设置货品价格
				if (!TextUtils.isEmpty(packageGoodsPrice)
						&& packageGoodsPrice != null) {
					tvGoodsPrice.setText(packageGoodsPrice + "元" + priceType);
				}

				// 补助
				if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
					tvSubsidy.setText(subsidy + "元/车");
				} else {
					tvSubsidy.setText("---");
				}

				// 货品分类名称
				if (!TextUtils.isEmpty(categoryName) && categoryName != null) {
					tvCoaltype.setText(categoryName);
				}

				// 货品信息
				if (!TextUtils.isEmpty(packageGoodsCalorific)
						&& packageGoodsCalorific != null) {
					tvCalorific.setText(packageGoodsCalorific);
				} else {
					tvCalorific.setText("---");
				}

				// 距离
				if (!TextUtils.isEmpty(packageDistance)
						&& packageDistance != null) {
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

				// 过路费
				if (!TextUtils.isEmpty(packageRoadToll)
						&& packageRoadToll != null) {
					tvTolls.setText(packageRoadToll + "元");
				} else {
					tvTolls.setText("---");
				}

				// 下单时间
				if (!TextUtils.isEmpty(createTime) && createTime != null) {
					String formatTicket = StringUtils.formatModify(createTime);
					tvCreateTime.setText(formatTicket);
				}

				// 推荐路线
				if (!TextUtils.isEmpty(packageRecommendPath)
						&& packageRecommendPath != null) {
					tvRecommended.setText(packageRecommendPath);
				} else {
					tvRecommended.setText("---");
				}

				// 物流备注
				if (!TextUtils.isEmpty(ticketMemo) && ticketMemo != null) {
					tvNote.setText(ticketMemo);
				}

				// 只有带执行列表的订单详情需要更改司机
				// if (ticketStatus != null && ticketStatus.equals("0")) {
				// isChange = true;// 需要更改司机
				// } else if (ticketStatus != null && ticketStatus.equals("1"))
				// {
				// isChange = true;// 需要更改司机
				// } else {
				// isChange = false;// 不需要更改司机
				// }

				// // 判断是否要显示更改司机
				// if (isChange) {
				// // 需要更改司机
				// tvChange.setVisibility(View.VISIBLE);
				// } else {
				// // 不需要更改司机
				// tvChange.setVisibility(View.INVISIBLE);
				// }

				// // 从服务器获取更改司机列表
				// if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
				// getDataFromSeriverDriverList(vehicleId);
				// }

			} else {
				// 获取订单数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 显示界面
			mRlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;

		// case CHANGE_TICKET_DRIVER_REQUEST:
		// isSuccess = response.isSuccess;
		// message = response.message;
		//
		// if (isSuccess) {
		// // 更换司机成功
		// if (!TextUtils.isEmpty(driverName) && driverName != null) {
		// tvDriver.setText(driverName);
		// }
		//
		// if (!TextUtils.isEmpty(message) && message != null) {
		// showToast(message);
		// }
		//
		// } else {
		// // 更换司机失败
		// if (!TextUtils.isEmpty(message) && message != null) {
		// showToast(message);
		// }
		// }
		// break;

		// case GET_TICKET_DRIVERLIST_REQUEST:
		// isSuccess = response.isSuccess;
		// message = response.message;
		//
		// if (isSuccess) {
		// // 获取车的司机列表成功
		// driverData = response.data;
		// // 循环获取司机列表中每个司机的个人信息
		// for (int i = 0; i < driverData.size(); i++) {
		// String name = driverData.get(i).name;// 司机姓名
		// boolean driverIsOnWay = driverData.get(i).driverIsOnWay;// 司机是否在途
		// String id = driverData.get(i).id;// 车辆司机ID
		//
		// if (name != null && !TextUtils.isEmpty(name)) {
		// listDriverName.add(name);
		// }
		//
		// if (!TextUtils.isEmpty(driverIsOnWay + "")) {
		// listDriverIsOnWay.add(driverIsOnWay);
		// }
		//
		// if (id != null && !TextUtils.isEmpty(id)) {
		// listId.add(id);
		// }
		// }
		//
		// } else {
		// // 获取车的司机列表失败
		// }
		//
		// break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		showToast(this.getResources().getString(R.string.net_error_toast));
		// 设置删除按钮可点击
		btnDelete.setEnabled(true);
	}

	// TODO显示确认执行的对话框
	protected void showAffirm(final String ticketId) {
		AlertDialog.Builder builder = new Builder(MyTicketDetailActivity.this);
		// 设置对话框不能被取消
		builder.setCancelable(false);

		View view = View.inflate(MyTicketDetailActivity.this,
				R.layout.dialog_delete_ticket, null);
		final EditText etMemo = (EditText) view
				.findViewById(R.id.et_ticketdetail_memo);
		TextView tvCancle = (TextView) view
				.findViewById(R.id.tv_ticketdetail_cancle);
		TextView tvConfirm = (TextView) view
				.findViewById(R.id.tv_ticketdetail_confirm);

		// 取消按钮
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 消除对话框
				alertDialog.dismiss();
			}
		});

		// 确定按钮
		tvConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					// 确认不再执行订单
					String memo = etMemo.getText().toString().trim();

					cancleTicket(ticketId, memo);

				}
			}
		});

		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();

		// 设置删除按钮不可点击
		btnDelete.setEnabled(false);

	}

	// 选择司机的PopupWindow
	// protected void showSelectorDriverPpw() {
	// selectorDriverPpw = new PopupWindow(MyTicketDetailActivity.this);
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
	// // 点击更改司机
	// if (!TextUtils.isEmpty(andId) && andId != null) {
	// changeTicketDriver(ticketId, andId);
	// } else {
	// showToast("操作失败,请重试");
	// }
	// // 取消弹窗
	// selectorDriverPpw.dismiss();
	// }
	// });
	//
	// WheelView mvDriverList = (WheelView) seldriver_view
	// .findViewById(R.id.wv_selvehicle_show);
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
	// driverName = currentText.split(" ")[0];
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
					driverName = list.get(0) + "";// 初始化为第一个司机
				} else {
					driverName = list.get(0) + "";// 初始化为第一个司机
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
	public static void invoke(Context activity, String ticketId) {
		Intent intent = new Intent();
		intent.setClass(activity, MyTicketDetailActivity.class);
		intent.putExtra("ticketId", ticketId);
		activity.startActivity(intent);
	}

	/**
	 * 本界面增加flag跳转方式
	 * 
	 * @param activity
	 * @param ticketId
	 */
	public static void invokeNewTask(Context activity, String ticketId) {
		Intent intent = new Intent();
		intent.setClass(activity, MyTicketDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ticketId", ticketId);
		activity.startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		createselectTelPoupWindow(pcsPhone, tmcPhone, stcPhone);
		switch (buttonView.getId()) {
		case R.id.bt_packagedetail_tel:
			if (isChecked) {
				int[] location = new int[2];
				btPackagedetailTel.getLocationOnScreen(location);
				selectTelPpw.showAtLocation(btPackagedetailTel,
						Gravity.NO_GRAVITY, location[0], location[1]
								- selectTelPpwHeight);
				btPackagedetailTel.setButtonDrawable(R.drawable.btn_tel_xia);
				System.out.println("getLocationOnScreen:" + location[0] + ","
						+ location[1]);
			} else {
				selectTelPpw.dismiss();
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

		btnFaBao.setText("客服电话");

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
				btPackagedetailTel.setChecked(false);

			}
		});
	}

}
