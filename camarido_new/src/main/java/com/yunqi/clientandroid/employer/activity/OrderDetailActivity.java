package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.MyTicketDetailActivity;
import com.yunqi.clientandroid.employer.request.EmployerCancelTicketRequest;
import com.yunqi.clientandroid.employer.request.GetOrderDetailRequest;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.entity.GetVTicketByIdInfo;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方订单详情页
 * @date 16/1/19
 */
public class OrderDetailActivity extends BaseActivity implements
		View.OnClickListener, OnCheckedChangeListener {

	private String ticketId;// 订单的id
	private String priceType;// 价格的类型
	private String packageBeginName;
	private String packageBeginAddress;
	private String packageEndName;
	private String packageEndAddress;

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
	private TextView tvDriver;
	private TextView tvVehicleNo;
	private TextView tvOrderNum;

	private TextView tvLoadingFee;
	private TextView tvUnloadingFee;
	private TextView tvGoodsPrice;
	private TextView tvCompanyName;
	private TextView tvTicketBaoXian;
	private TextView tvGoodsTextView;
	private TextView tvYunFeiTextView;
	private TextView tvXieZhuangTextView;
	private CheckBox btTicketDetailTel;
	private Button btTicketDetermine;
	private RelativeLayout rlTicketDingDan;
	private RelativeLayout rlTicketDingDanInfo;
	private RelativeLayout rlTicketGoods;
	private RelativeLayout rlTicketGoodsInfo;
	private RelativeLayout rlTicketYunFei;
	private RelativeLayout rlTicketYunFeiInfo;
	private RelativeLayout rlTicketTiShi;
	private RelativeLayout rlTicketTiShiInfo;
	private ImageView ivDingDanIco;
	private ImageView ivGoodsIco;
	private ImageView ivYunFeiIco;
	private ImageView ivTiShiIco;

	private String mVehicleContacts;// 跟车电话
	private String mUserPhone; // 执行人电话

	private PopupWindow selectTelPpw;
	private AlertDialog alertDialog;
	private int selectTelPpwHeight;

	private ArrayList<DriverDetailInfo> driverData;// 存放司机列表
	private ArrayList<Object> listDriverIsOnWay = new ArrayList<Object>();// 存司机是否在途
	private ArrayList<String> listDriverName = new ArrayList<String>();// 存放司机的名字
	private ArrayList<String> listId = new ArrayList<String>();// 存放车辆司机Id

	// 本页请求
	private GetOrderDetailRequest mGetVTicketByIdRequest;
	private EmployerCancelTicketRequest mEmployerCancelTicketRequest;

	// 本页面请求id
	private final int GET_TICKET_DETAIL_REQUEST = 1;
	private final int GET_TICKET_CANCEL = 2;

	// 初始化titleBar
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.order_detail));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderDetailActivity.this.finish();
			}
		});
	}

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_ticketdetail;
	}

	@Override
	protected void initView() {
		// 初始化titleBar
		initActionBar();

		// 获取从列表页面传过来的订单id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getString("ticketId");
		}

		// 初始化控件
		tvProvenance = (TextView) findViewById(R.id.tv_ticketdetail_provenance_employer);
		tvProAddress = (TextView) findViewById(R.id.tv_ticketdetail_proaddress_employer);
		tvDestination = (TextView) findViewById(R.id.tv_ticketdetail_destination_employer);
		tvDestaddress = (TextView) findViewById(R.id.tv_ticketdetail_destaddress_employer);
		tvDriver = (TextView) findViewById(R.id.tv_ticketdetail_drive_employer);
		tvVehicleNo = (TextView) findViewById(R.id.tv_ticketdetail_vehicleNo_employer);
		tvOrderNum = (TextView) findViewById(R.id.tv_ticketdetail_orderNumber_employer);
		tvFreight = (TextView) findViewById(R.id.tv_ticketdetail_freight_employer);
		tvSubsidy = (TextView) findViewById(R.id.tv_ticketdetail_subsidy_employer);
		tvCoaltype = (TextView) findViewById(R.id.tv_ticketdetail_coaltype_employer);
		tvCalorific = (TextView) findViewById(R.id.tv_ticketdetail_calorific_employer);
		tvMileage = (TextView) findViewById(R.id.tv_ticketdetail_mileage_employer);
		tvTolls = (TextView) findViewById(R.id.tv_ticketdetail_tolls_employer);
		tvCreateTime = (TextView) findViewById(R.id.tv_ticketdetail_createTime_employer);
		tvRecommended = (TextView) findViewById(R.id.tv_ticketdetail_recommended_employer);
		tvNote = (TextView) findViewById(R.id.tv_ticketdetail_note_employer);
		mRlGlobal = (RelativeLayout) findViewById(R.id.rl_ticketdetail_global_employer);
		mProgress = (ProgressBar) findViewById(R.id.pb_ticketdetail_progress_employer);

		tvCompanyName = obtainView(R.id.tv_ticket_detail_company_employer);
		tvTicketBaoXian = obtainView(R.id.tv_detail_baoxian);
		tvGoodsTextView = obtainView(R.id.tv_goods_employer);
		tvYunFeiTextView = obtainView(R.id.tv_yunfei_employer);
		tvXieZhuangTextView = obtainView(R.id.tv_xiezhuang_employer);
		btTicketDetailTel = obtainView(R.id.bt_packagedetail_tel);
		btTicketDetermine = obtainView(R.id.bt_ticketdetail_determine);
		tvLoadingFee = (TextView) findViewById(R.id.tv_ticketdetail_loadingFee_employer);
		tvUnloadingFee = (TextView) findViewById(R.id.tv_ticketdetail_unloadingFee_employer);
		tvGoodsPrice = (TextView) findViewById(R.id.tv_ticketdetail_goodsPrice_employer);

		rlTicketDingDan = obtainView(R.id.rl_ticket_dingdan);
		rlTicketDingDanInfo = obtainView(R.id.rl_ticket_dingdan_info);
		rlTicketGoods = obtainView(R.id.rl_ticket_goods);
		rlTicketGoodsInfo = obtainView(R.id.rl_ticket_goods_info);
		rlTicketYunFei = obtainView(R.id.rl_ticket_yunfei);
		rlTicketYunFeiInfo = obtainView(R.id.rl_ticket_yunfei_info);
		rlTicketTiShi = obtainView(R.id.rl_ticket_tishi);
		rlTicketTiShiInfo = obtainView(R.id.rl_ticket_tishi_info);
		ivDingDanIco = obtainView(R.id.iv_ticket_dingdan_icon);
		ivGoodsIco = obtainView(R.id.iv_ticket_goods_icon);
		ivYunFeiIco = obtainView(R.id.iv_ticket_yunfei_icon);
		ivTiShiIco = obtainView(R.id.iv_ticket_tishi_icon);

		rlTicketDingDan.setTag(true);
		rlTicketGoods.setTag(true);
		rlTicketYunFei.setTag(true);
		rlTicketTiShi.setTag(true);
	}

	@Override
	protected void initData() {

		// mProgress.setVisibility(View.VISIBLE);

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
		btTicketDetailTel.setOnCheckedChangeListener(this);
		btTicketDetermine.setOnClickListener(this);
		rlTicketDingDan.setOnClickListener(this);
		rlTicketGoods.setOnClickListener(this);
		rlTicketYunFei.setOnClickListener(this);
		rlTicketTiShi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.rl_ticket_dingdan: //
			if ((Boolean) rlTicketDingDan.getTag()) {
				rlTicketDingDanInfo.setVisibility(View.VISIBLE);
				rlTicketDingDan.setTag(false);
				shunShiZhen(ivDingDanIco);
			} else {
				rlTicketDingDanInfo.setVisibility(View.GONE);
				rlTicketDingDan.setTag(true);
				niShiZhen(ivDingDanIco);
			}
			break;

		case R.id.rl_ticket_goods:
			if ((Boolean) rlTicketGoods.getTag()) {
				rlTicketGoodsInfo.setVisibility(View.VISIBLE);
				rlTicketGoods.setTag(false);
				shunShiZhen(ivGoodsIco);
			} else {
				rlTicketGoodsInfo.setVisibility(View.GONE);
				rlTicketGoods.setTag(true);
				niShiZhen(ivGoodsIco);
			}
			break;

		case R.id.rl_ticket_yunfei:
			if ((Boolean) rlTicketYunFei.getTag()) {
				rlTicketYunFeiInfo.setVisibility(View.VISIBLE);
				rlTicketYunFei.setTag(false);
				shunShiZhen(ivYunFeiIco);
			} else {
				rlTicketYunFeiInfo.setVisibility(View.GONE);
				rlTicketYunFei.setTag(true);
				niShiZhen(ivYunFeiIco);
			}
			break;

		case R.id.rl_ticket_tishi:
			if ((Boolean) rlTicketTiShi.getTag()) {
				rlTicketTiShiInfo.setVisibility(View.VISIBLE);
				rlTicketTiShi.setTag(false);
				shunShiZhen(ivTiShiIco);
			} else {
				rlTicketTiShiInfo.setVisibility(View.GONE);
				rlTicketTiShi.setTag(true);
				niShiZhen(ivTiShiIco);
			}
			break;

		case R.id.bt_ticketdetail_determine:
			showAffirm(ticketId);
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

	// TODO显示确认执行的对话框
	protected void showAffirm(final String ticketId) {
		AlertDialog.Builder builder = new Builder(OrderDetailActivity.this);
		// 设置对话框不能被取消
		builder.setCancelable(false);

		View view = View.inflate(OrderDetailActivity.this,
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
		btTicketDetermine.setEnabled(false);

	}

	/**
	 * @Description:取消订单
	 * @Title:cancleTicket
	 * @param ticketId
	 * @param memo
	 * @return:void
	 * @throws
	 * @Create: 2016-6-14 下午8:05:53
	 * @Author : mengwei
	 */
	private void cancleTicket(String ticketId, String memo) {
		mEmployerCancelTicketRequest = new EmployerCancelTicketRequest(
				OrderDetailActivity.this, ticketId, memo);
		mEmployerCancelTicketRequest.setRequestId(GET_TICKET_CANCEL);
		httpPost(mEmployerCancelTicketRequest);
	}

	// 从服务器获取我的订单模块的订单详情信息
	private void getDataFromServiceTicketDetail(String ticketId) {
		// mRlGlobal.setVisibility(View.INVISIBLE);

		mGetVTicketByIdRequest = new GetOrderDetailRequest(this, ticketId);
		mGetVTicketByIdRequest.setRequestId(GET_TICKET_DETAIL_REQUEST);
		httpPost(mGetVTicketByIdRequest);

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_TICKET_CANCEL:
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
				OrderDetailActivity.this.finish();
			} else {
				// 取消订单失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置删除按钮可点击
				btTicketDetermine.setEnabled(true);
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
				String ticketCode = getVTicketByIdInfo.ticketCode;// 订单号
				String packagePrice = getVTicketByIdInfo.packagePrice;// 运价
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
				String subsidy = getVTicketByIdInfo.subsidy;// 补助
				String packageGoodsPrice = getVTicketByIdInfo.packageGoodsPrice;// 货值价格
				String tmcPhone = getVTicketByIdInfo.tmcPhone;// 驻矿联系电话
				String stcPhone = getVTicketByIdInfo.stcPhone;// 签收联系电话
				String pcsPhone = getVTicketByIdInfo.pcsPhone;// 平台客服电话
				String loadingFee = getVTicketByIdInfo.loadingFee;// 装车费
				String unloadingFee = getVTicketByIdInfo.unloadingFee;// 卸车费
				String companyShortName = getVTicketByIdInfo.tenantShortname;// 公司简称
				int insuranceType = getVTicketByIdInfo.insuranceType;// 0：无保险
																		// 1：平台送保险
																		// 2：自己购买保险
				mVehicleContacts = getVTicketByIdInfo.vehicleContacts;
				mUserPhone = getVTicketByIdInfo.userPhone;

				if (insuranceType == 0) {
					tvTicketBaoXian.setVisibility(View.GONE);
				} else {
					tvTicketBaoXian.setVisibility(View.VISIBLE);
				}

				if (!TextUtils.isEmpty(companyShortName)
						&& companyShortName != null) {
					tvCompanyName.setText(companyShortName);
				}

				if (ticketStatus != null && ticketStatus.equals("0")
						&& ticketStatus.equals("1")) {
					btTicketDetermine.setVisibility(View.VISIBLE);
				} else {
					btTicketDetermine.setVisibility(View.GONE);
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

				// 设置装车费
				if (!TextUtils.isEmpty(loadingFee) && loadingFee != null) {
					tvXieZhuangTextView.setText("装车" + loadingFee + "元");
				}

				// 设置卸车费
				if (!TextUtils.isEmpty(unloadingFee) && unloadingFee != null) {
					tvXieZhuangTextView.setText(tvXieZhuangTextView.getText()
							.toString().trim()
							+ ",卸车" + unloadingFee + "元");
				}

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

				// 运费
				if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
					tvFreight.setText(packagePrice + "元" + priceType);
					tvYunFeiTextView.setText(packagePrice + "元" + priceType);
				}

				// 货品分类名称
				if (!TextUtils.isEmpty(categoryName) && categoryName != null) {
					tvCoaltype.setText(categoryName);
					tvGoodsTextView.setText(categoryName);
				}

				// 设置货品价格
				if (!TextUtils.isEmpty(packageGoodsPrice)
						&& packageGoodsPrice != null) {
					tvGoodsPrice.setText(packageGoodsPrice + "元" + priceType);
					tvGoodsTextView.setText(tvGoodsTextView.getText()
							.toString().trim()
							+ "," + packageGoodsPrice + "元" + priceType);
				}

				// 补助
				if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
					tvSubsidy.setText(subsidy + "元");
					tvYunFeiTextView.setText(tvYunFeiTextView.getText()
							.toString().trim()
							+ ",补助" + subsidy + "元");
				}

				// 货品信息
				if (!TextUtils.isEmpty(packageGoodsCalorific)
						&& packageGoodsCalorific != null) {
					tvCalorific.setText(packageGoodsCalorific);
				}

				// 距离
				if (!TextUtils.isEmpty(packageDistance)
						&& packageDistance != null) {
					tvMileage.setText(packageDistance + "公里");
				}

				// 过路费
				if (!TextUtils.isEmpty(packageRoadToll)
						&& packageRoadToll != null) {
					tvTolls.setText(packageRoadToll + "元");
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
				}

				// 物流备注
				if (!TextUtils.isEmpty(ticketMemo) && ticketMemo != null) {
					tvNote.setText(ticketMemo);
				}

			} else {
				// 获取订单数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 显示界面
			// mRlGlobal.setVisibility(View.VISIBLE);
			// mProgress.setVisibility(View.INVISIBLE);

			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// 本页面的跳转方法
	public static void invoke(Context activity, String ticketId) {
		Intent intent = new Intent();
		intent.setClass(activity, OrderDetailActivity.class);
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
		intent.setClass(activity, OrderDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ticketId", ticketId);
		activity.startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		createselectTelPoupWindow(mVehicleContacts, mUserPhone);
		switch (buttonView.getId()) {
		case R.id.bt_packagedetail_tel:
			if (isChecked) {
				int[] location = new int[2];
				btTicketDetailTel.getLocationOnScreen(location);
				selectTelPpw.showAtLocation(btTicketDetailTel,
						Gravity.NO_GRAVITY, location[0], location[1]
								- selectTelPpwHeight);
				btTicketDetailTel.setButtonDrawable(R.drawable.btn_tel_normal);
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
	 * @Description:联系电话
	 * @Title:createselectTelPoupWindow
	 * @param vehicleContacts
	 * @param userPhone
	 * @return:void
	 * @throws
	 * @Create: 2016-6-14 下午7:24:20
	 * @Author : mengwei
	 */
	private void createselectTelPoupWindow(final String vehicleContacts,
			final String userPhone) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.employer_ticket_detail_phone_poupwindow, null);
		selectTelPpw = new PopupWindow(view,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		selectTelPpw.setBackgroundDrawable(new BitmapDrawable());
		selectTelPpw.setOutsideTouchable(true);// 设置PopupWindow外部区域可触摸
		selectTelPpw.setFocusable(true); // 设置PopupWindow可获取焦点
		selectTelPpw.setTouchable(true); // 设置PopupWindow可触摸
		selectTelPpw.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#03000000")));

		Button btUserPhone = (Button) view
				.findViewById(R.id.bt_ticket_detail_userphone);
		Button btVehicleContacts = (Button) view
				.findViewById(R.id.bt_ticket_detail_vehicleContacts);

		if (vehicleContacts == null && TextUtils.isEmpty(vehicleContacts)) {
			btVehicleContacts.setVisibility(View.GONE);
		}

		if (userPhone == null && TextUtils.isEmpty(userPhone)) {
			btUserPhone.setVisibility(View.GONE);
		}

		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		selectTelPpwHeight = view.getMeasuredHeight();

		btUserPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonUtil.callPhoneIndirect(mContext, userPhone);
			}
		});

		btVehicleContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonUtil.callPhoneIndirect(mContext, vehicleContacts);
			}
		});

		selectTelPpw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				btTicketDetailTel.setChecked(false);

			}
		});
	}

}
