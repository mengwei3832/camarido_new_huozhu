package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.entity.SendPackageEntity;
import com.yunqi.clientandroid.employer.fragment.EmploterBaoBiaoFragment;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.request.SendNeedAuditPackageRequest;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:发包预览界面
 * @ClassName: EmployerPreViewActivity
 * @author: mengwei
 * @date: 2016-6-23 下午6:34:48
 * 
 */
public class EmployerPreViewActivity extends BaseActivity implements
		View.OnClickListener {
	// 界面控件对象
	private TextView tvTime, tvShip, tvDisCharge, tvVehicle, tvGoods,
			tvBilling, tvInvoice, tvRemark;
	private LinearLayout llRemark;
	private Button btNext;
	private SendPackageEntity entity;// 发包信息对象
	private TextView tvTusun;
	// 传递过来的值
	private long startTime; // 发包开始时间
	private long endTime; // 发包结束时间
	/*
	 * private int zhuangProvinceId;// 装货省地址ID private int zhuangCityId;//
	 * 装货城市地址ID private int zhuangAreaId;// 装货地区地址ID
	 */private String zhuangCutomName;// 装货地址别名
	/*
	 * private String shipAddress; // 装货地址 private int xieProvinceId;// 卸货省地址ID
	 * private int xieCityId;// 卸货城市地址ID private int xieAreaId;// 卸货地区地址ID
	 */private String xieCutomName;// 卸货地址别名
	// private String desChargeAddress; // 卸货
	private float unitPrice; // 货值单价
	private String coalName; // 煤品名称
	// private int coalNameId; // 煤品名称ID
	private int vehicleNumber; // 所需车数
	// private int carTypeId[];// 车型ID数组
	private String carTypeStr;// 车型字符串
	private int packageSettlementTy;// 结算方式
	// private int insuranceType;// 保险
	private int needInvoice; // 索要发票
	private float loading; // 装车费
	private float unLoading; // 卸车费
	private String remark; // 备注
	private String tuSunlv;// 途损率
	private String SumDun;// 货品总吨
	private String vehicleNumberText;// 需求车次
	public static String FROM_PRE_VIEW = "FROM_PRE_VIEW";
	private int shortFallType;
	private String mTuSunDun;

	// 页面请求
	private SendNeedAuditPackageRequest sendNeedAuditPackageRequest;
	// 请求ID
	private final static int SEND_NEED_AUDIT_PACKAGE_REQUEST = 1;

	// loading加载框
	private PopupWindow pbPopupWindow;
	private View parentView;
	private TextView tvTextView;

	//友盟统计
	private UmengStatisticsUtils mUmeng;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_preview;
	}

	@Override
	protected void initView() {
		// 获得传递过来的值
		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		entity = (SendPackageEntity) bundle.getSerializable("packageInfo");

		parentView = EmployerPreViewActivity.this.getLayoutInflater().inflate(
				R.layout.employer_activity_preview, null);

		//友盟统计
		mUmeng = UmengStatisticsUtils.instance(mContext);

		// 初始化控件对象
		initFindView();
		// 给对应控件赋值
		setControls();
	}

	/**
	 * @Description:给对应控件赋值
	 * @Title:setControls
	 * @return:void
	 * @throws
	 * @Create: 2016-6-23 下午7:56:43
	 * @Author : chengtao
	 */
	private void setControls() {
		if (entity != null) {
			startTime = entity.getStartTime();
			endTime = entity.getEndTime();
			/*
			 * zhuangProvinceId = entity.getZhuangProvinceId(); zhuangCityId =
			 * entity.getZhuangCityId(); zhuangAreaId =
			 * entity.getZhuangAreaId();
			 */
			zhuangCutomName = entity.getZhuangCutomName();
			// shipAddress = entity.getShipAddress();

			/*
			 * xieProvinceId = entity.getXieProvinceId(); xieCityId =
			 * entity.getXieCityId(); xieAreaId = entity.getXieAreaId();
			 */
			xieCutomName = entity.getXieCutomName();
			// desChargeAddress = entity.getDesChargeAddress();

			unitPrice = entity.getUnitPrice();
			coalName = entity.getCoalName();
			// coalNameId = entity.getCoalNameId();

			vehicleNumber = entity.getVehicleNumberId();
			// carTypeId = entity.getCarList();
			carTypeStr = entity.getCarTypeStr();
			packageSettlementTy = entity.getPackageSettlementType();
			// insuranceType = entity.getInsuranceType();

			needInvoice = entity.getNeedInvoice();
			loading = entity.getLoading();
			unLoading = entity.getUnLoading();
			remark = entity.getRemark();
			tuSunlv = entity.getTuSunLv();
			SumDun = entity.getSumDun();
			vehicleNumberText = entity.getVehicleNumberText();
			shortFallType = entity.getShortFallType();
			mTuSunDun = entity.getTuSunDun();
		}
		for (int i = 0; i < entity.getCarList().length; i++) {
			Log.v("TAG", "entity.getCarList()--------" + entity.getCarList()[i]);
		}
		// 发货时限
		if (startTime > 0 && endTime > 0) {
			tvTime.setText(StringUtils.formatModify(startTime + "") + " ~ "
					+ StringUtils.formatModify(endTime + ""));
		}
		// 装货地址
		if (zhuangCutomName != null && !TextUtils.isEmpty(zhuangCutomName)) {
			tvShip.setText(zhuangCutomName);
		}
		// 卸货地址
		if (xieCutomName != null && !TextUtils.isEmpty(xieCutomName)) {
			tvDisCharge.setText(xieCutomName);
		}
		// 用车信息
		if (carTypeStr != null && !TextUtils.isEmpty(carTypeStr)) {
			tvVehicle.setText(carTypeStr + "  ");
		}
		if (StringUtils.isStrNotNull(vehicleNumberText)) {
			tvVehicle.setText(tvVehicle.getText().toString()
					+ vehicleNumberText);
		}

		// 货物信息
		if ((coalName != null && !TextUtils.isEmpty(coalName))) {
			tvGoods.setText(coalName + "  ");
		}

		if (unitPrice > 0) {
			String mUnitPrice = String.valueOf(unitPrice);
			tvGoods.setText(tvGoods.getText().toString()
					+ StringUtils.saveTwoNumber(mUnitPrice) + "元/吨");
		}

		if (StringUtils.isStrNotNull(SumDun)) {
			tvGoods.setText(tvGoods.getText().toString() + "    共"
					+ StringUtils.saveTwoNumber(SumDun) + "吨");
		}

		Log.e("TAG", "++++++++++++++++packageSettlementTy-----------"
				+ packageSettlementTy);

		// 结算方式
		if (packageSettlementTy == 0) {
			tvBilling.setText("线上结算");
		} else if (packageSettlementTy == 1) {
			tvBilling.setText("线下结算");
		}

		// 发票
		if (needInvoice == 0) {
			tvInvoice.setText("不要发票");
		} else if (needInvoice == 1) {
			tvInvoice.setText("要发票");
		}

		// 备注
		if (loading > 0 || unLoading > 0
				|| (remark != null && !TextUtils.isEmpty(remark))) {
			llRemark.setVisibility(View.VISIBLE);
			if (loading > 0) {
				String mLoading = String.valueOf(loading);
				tvRemark.setText("装车费:" + StringUtils.saveTwoNumber(mLoading)
						+ "元  ");
			}
			if (unLoading > 0) {
				String mUnLoading = String.valueOf(unLoading);
				tvRemark.setText(tvRemark.getText().toString() + "卸车费:"
						+ StringUtils.saveTwoNumber(mUnLoading) + "元  ");
			}
			if (remark != null && !TextUtils.isEmpty(remark)) {
				tvRemark.setText(tvRemark.getText().toString() + remark);
			}
		}

		// 途损率
		if (shortFallType == 10){
			if (StringUtils.isStrNotNull(tuSunlv)) {
				double mTuSun = Double.valueOf(tuSunlv);
				tvTusun.setText(StringUtils.sanToQianFenHao(mTuSun));

			} else {
				tvTusun.setText("0‰");
			}
		} else if (shortFallType == 20){
			if (StringUtils.isStrNotNull(mTuSunDun)){
				tvTusun.setText(mTuSunDun+"吨/车");
			} else {
				tvTusun.setText("0吨/车");
			}
		}

	}

	/**
	 * @Description:初始化控件对象
	 * @Title:initFindView
	 * @return:void
	 * @throws
	 * @Create: 2016-6-23 下午7:26:40
	 * @Author : mengwei
	 */
	private void initFindView() {
		initActionBar();

		tvTime = obtainView(R.id.tv_preview_time);
		tvShip = obtainView(R.id.tv_preview_ship);
		tvDisCharge = obtainView(R.id.tv_preview_discharge);
		tvVehicle = obtainView(R.id.tv_preview_vehicle_message);
		tvGoods = obtainView(R.id.tv_preview_goods_message);
		tvBilling = obtainView(R.id.tv_preview_billing_message);
		tvInvoice = obtainView(R.id.tv_preview_invoice_message);
		tvRemark = obtainView(R.id.tv_preview_remark_message);
		llRemark = obtainView(R.id.ll_remark);
		btNext = obtainView(R.id.bt_preview_next);
		tvTusun = obtainView(R.id.tv_preview_tusun_message);
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月21日 下午2:37:45
	 * @Author : mengwei
	 */
	private void initActionBar() {
		setActionBarTitle("预览信息");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmployerPreViewActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		btNext.setOnClickListener(this);
	}

	/**
	 * 
	 * @Description:
	 * @Title:invoke
	 * @param context
	 * @param entity
	 *            发包信息对象 SendPackageEntity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月25日 下午5:59:42
	 * @Author : chengtao
	 */
	public static void invoke(Context context, SendPackageEntity entity) {
		Intent intent = new Intent(context, EmployerPreViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("packageInfo", entity);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_preview_next:
			// 请求接口
			sendNeedAuditPackageRequest = new SendNeedAuditPackageRequest(
					mContext, entity);
			sendNeedAuditPackageRequest
					.setRequestId(SEND_NEED_AUDIT_PACKAGE_REQUEST);
			httpPostJson(sendNeedAuditPackageRequest);

			setViewEnable(false);
//			showProgressBarPop();
			showProgressDialog("发布中...");
			//友盟统计
			mUmeng.setCalculateEvents("ship_click_release");
			break;

		default:
			break;
		}
	}

	private void settingProgressBar() {
		pbPopupWindow = new PopupWindow(mContext);

		View pb_view = EmployerPreViewActivity.this.getLayoutInflater()
				.inflate(R.layout.employer_popupwindow_progressbar, null);

		pbPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		pbPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		pbPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		pbPopupWindow.setContentView(pb_view);
		pbPopupWindow.setOutsideTouchable(true);
		pbPopupWindow.setFocusable(true);
		pbPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		pbPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		tvTextView = (TextView) pb_view.findViewById(R.id.tv_pb_text);
		tvTextView.setText("发布中...");
	}

	private void showProgressBarPop() {
		if (pbPopupWindow == null) {
			// 设置进度条弹出框
			settingProgressBar();
		}

		// 弹出框出来的方式
		pbPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);

	}

	/**
	 * @Description:按钮不可点击
	 * @Title:setViewEnable
	 * @return:void
	 * @throws
	 * @Create: 2016-7-6 上午9:39:50
	 * @Author : chengtao
	 */
	private void setViewEnable(boolean enabled) {
		if (enabled) {
			btNext.setEnabled(enabled);
			btNext.setBackgroundResource(R.drawable.sendbao_btn_background);
		} else {
			btNext.setEnabled(enabled);
			btNext.setBackgroundResource(R.drawable.btn_zhihui);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSucess = response.isSuccess;
		String message = response.message;
		switch (requestId) {
		case SEND_NEED_AUDIT_PACKAGE_REQUEST:
			showToast(message);
			if (isSucess) {
				EmploterBaoBiaoFragment.isBack = true;
				EmployerMainActivity.newInvoke(mContext, FROM_PRE_VIEW);
				//友盟统计
				mUmeng.setCalculateEvents("ship_click_release_finish");
				finish();
//				pbPopupWindow.dismiss();
			} else {
				setViewEnable(true);
//				pbPopupWindow.dismiss();
			}
			hideProgressDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		setViewEnable(true);
		hideProgressDialog();
//		pbPopupWindow.dismiss();
	}
}
