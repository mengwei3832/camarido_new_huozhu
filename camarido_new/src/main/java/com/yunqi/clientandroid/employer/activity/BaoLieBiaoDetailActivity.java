package com.yunqi.clientandroid.employer.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard.Key;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//import com.baidu.pano.platform.http.s;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.PingTaiKeFu;
import com.yunqi.clientandroid.employer.fragment.EmploterBaoBiaoFragment;
import com.yunqi.clientandroid.employer.request.CanclePackageRequest;
import com.yunqi.clientandroid.employer.request.GetBaoDetailContentRequest;
import com.yunqi.clientandroid.employer.request.PingTaiKeFuRequest;
import com.yunqi.clientandroid.employer.request.StopApplyRequest;
import com.yunqi.clientandroid.employer.request.StopPackageRequest;
import com.yunqi.clientandroid.employer.request.TicketHistoryCurrentRequest;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.UserUtil;

/**
 * @Description:包的详情界面
 * @ClassName: BaoLieBiaoDetailActivity
 * @author: zhm
 * @date: 2016-5-23 下午4:29:22
 * 
 */
public class BaoLieBiaoDetailActivity extends BaseActivity implements
		View.OnClickListener {
	/* 界面控件对象 */
	// private TextView tvBaoDetailType; // 包类型：0：一口价；1：竞价；2：定向指派
	// private TextView tvBaoDetailDate; // 包开始时间和结束时间
	// private TextView tvBaoDetailCategoryName; // 品类名称
	// private TextView tvBaoDetailBeginAdress; // 开始地址
	// private TextView tvBaoDetailAndAdress; // 结束地址
	// private TextView tvBaoDetailSubsidy; // 补贴
	// private TextView tvBaoDetailBaoXianZi; // 保险的字
	// private ImageView ivBaoDetailBaoXianTu; // 保险的图
	// private TextView tvBaoDetailCarSum; // 总车数和已报名车数
	// private TextView tvBaoDetailStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成
	// private TextView tvBaoDetailPrice; // 发包方价格
	// private TextView tvBaoDetailUnitPrice; // 单价
	// private TextView tvBaoDetailCategoryNameText;// 品类名称
	// private TextView tvBaoDetailPackageWeight; // 总吨数
	// private TextView tvBaoDetailQiang; // 已抢订单
	// private TextView tvBaoDetailExecute; // 执行中订单
	// private TextView tvBaoDetailAudit; // 待审核订单
	// private TextView tvBaoDetailHistory; // 历史订单
	// private Button btBaoDetailStop; // 停止抢单
	// private Button btBaoDetailCopy; // 复制发包
	// private RelativeLayout rlBaoDetailQing;
	// private RelativeLayout rlBaoDetailExecute;
	// private RelativeLayout rlBaoDetailAudit;
	// private RelativeLayout rlBaoDetailHistory;
	private TextView tvOrderDetailBegin;// 开始地址
	private TextView tvOrderDetailBeginCity;// 开始二、三级
	private TextView tvOrderDetailEnd;// 结束地址
	private TextView tvOrderDetailEndCity;// 结束二、三级
	private TextView tvOrderDetailDate;// 时间
	private TextView tvOrderDetailVehicleNumber;// 车数
	private TextView tvOrderDetailUnitPrice;// 单价
	private TextView tvOrderDetailCocal;// 品种
	private TextView tvOrderDetailTransactionsNumber;// 已成交报价单
	private TextView tvOrderDetailSendCarNumber;// 已派车
	private TextView tvOrderDetailBillingNumber;// 待结算运单
	private TextView tvOrderDetailFinishNumber;// 已完成运单
	private RelativeLayout rlOrderDetailTransactions;
	private RelativeLayout rlOrderDetailSendCar;
	private RelativeLayout rlOrderDetailBilling;
	private RelativeLayout rlOrderDetailFinish;
	private Button btOrderDetailResend;// 再发一单
	private LinearLayout llOrderDetailInsurance;// 保险
	private Button btOrderDetailPcs;// 联系客服
	private ScrollView slOrderDetail;
	private LinearLayout llOrderDetailBottom;
	private  ProgressWheel pbOrderDetailBar;
	private TextView tvOrderDetailTusun;// 途损比率
	private ImageView ivOrderDetailBaoxian;// 保险
	private TextView tvSumDun;

	/* 请求的包详情信息 */
	private String mId; // 包ID
	private String mTenantId; // 公司ID
	private int mPackageType; // 包类型：0：一口价；1：竞价；2：定向指派
	private String mPackageStartTime; // 包开始时间
	private String mPackageEndTime; // 包结束时间
	private String mCategoryName; // 品类名称
	private String mPackageBeginAddress; // 开始地址
	private String mPackageEndAddress; // 结束地址
	private double mSubsidy; // 补贴
	private int mInsuranceType; // 保险：0：无保险；1：平台送保险；2：自己购买保险
	private String mPackageCount; // 总车数
	private int mOrderCount; // 已报名车数
	private int orderInfoCount;// 已下单车数
	private int mPackageStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成
	private double mPackagePriceOrigin; // 发包方价格
	private double mPackageGoodsPrice; // 货值单价
	private String mPackageWeight; // 总吨数
	private String mPakcageCode;
	private int mOrderAllCount; // 已抢订单数量
	private int mOrderExecutingCount; // 执行中订单数量
	private int mOrderPendingCount; // 审核中订单数量
	private int mOrderExecutedCount; // 已完成订单数量
	private String mPackageBeginName;// 始发地
	private String mPackageEndName; // 目的地
	private String mQuotationcount;// 执行状况
	private String mConfirmQuotationcount;// 已派车
	private String mOrderSettledCount;// 待结算
	private String mOrderBeforeSettleCount;// 已完成
	public String mPackageBeginProvinceText; // 开始省份名称
	public String mPackageBeginCityText; // 开始城市名称
	public String mPackageBeginCountryText; // 开始区县名称
	public String mPackageEndProvinceText; // 结束省份名称
	public String mPackageEndCityText; // 结束城市名称
	public String mPackageEndCountryText; // 结束区县名称
	private String mPcsPhone;// 平台客服电话
	private String mTicketArrangeCount;
	private String mPrice;
	private String mCarRange;
	private String mBeforeExcute;
	private String mOnTheWayCount;

	/* 请求ID */
	private final int GET_BAO_DETAIL = 1;
	private final int STOP_QIANG_TICKET = 2;
	private final int STOP_PACKAGE = 3;

	/* 请求类 */
	private GetBaoDetailContentRequest mGetBaoDetailContentRequest; // 包的详细信息
	private StopApplyRequest mStopApplyRequest; // 停止抢单
	private PingTaiKeFuRequest pingTaiKeFuRequest;
	private CanclePackageRequest canclePackageRequest;
	private StopPackageRequest stopPackageRequest;

	/* 传过来的数据 */
	private String packageID; // 包ID

	/* 弹出框 */
	private AlertDialog alertDialog;

	private SpManager mSpManager;

	private static Class className;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_order_manager_detail;
	}

	@Override
	protected void initView() {
		initActionBar();

		mSpManager = SpManager.instance(BaoLieBiaoDetailActivity.this);

		// tvBaoDetailType = obtainView(R.id.tv_employer_biao_detail_type);
		// tvBaoDetailDate = obtainView(R.id.tv_employer_biao_detail_date);
		// tvBaoDetailCategoryName =
		// obtainView(R.id.tv_employer_biao_detail_meizhong);
		// tvBaoDetailBeginAdress =
		// obtainView(R.id.tv_employer_biao_detail_qidian);
		// tvBaoDetailAndAdress =
		// obtainView(R.id.tv_employer_biao_detail_zhongdian);
		// tvBaoDetailSubsidy = obtainView(R.id.tv_employer_biao_detail_buzhu);
		// tvBaoDetailBaoXianZi =
		// obtainView(R.id.tv_employer_biao_detail_baoxian);
		// ivBaoDetailBaoXianTu =
		// obtainView(R.id.iv_employer_biao_detail_baoxian);
		// tvBaoDetailCarSum = obtainView(R.id.tv_employer_biao_detail_carSum);
		// tvBaoDetailStatus =
		// obtainView(R.id.tv_employer_biao_detail_zhuangtai);
		// tvBaoDetailPrice = obtainView(R.id.tv_employer_biao_detail_danjia);
		// tvBaoDetailUnitPrice = obtainView(R.id.tv_employer_bao_detail_price);
		// tvBaoDetailCategoryNameText =
		// obtainView(R.id.tv_employer_bao_detail_pinzhong);
		// tvBaoDetailPackageWeight =
		// obtainView(R.id.tv_employer_bao_detail_number);
		// tvBaoDetailQiang = obtainView(R.id.tv_foot_bao_detail_qiang);
		// tvBaoDetailExecute = obtainView(R.id.tv_foot_bao_detail_zhixing);
		// tvBaoDetailAudit = obtainView(R.id.tv_foot_bao_detail_shenhe);
		// tvBaoDetailHistory = obtainView(R.id.tv_foot_bao_detail_history);
		// rlBaoDetailQing = obtainView(R.id.rl_foot_bao_detail_qiang);
		// rlBaoDetailExecute = obtainView(R.id.rl_foot_bao_detail_zhixing);
		// rlBaoDetailAudit = obtainView(R.id.rl_foot_bao_detail_shenhe);
		// rlBaoDetailHistory = obtainView(R.id.rl_foot_bao_detail_history);
		// btBaoDetailCopy = obtainView(R.id.bt_foot_bao_detail_copy);
		// btBaoDetailStop = obtainView(R.id.bt_foot_bao_detail_stop);
		initFindView();

		// Calendar calendar = Calendar.getInstance();
		//
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// calendar.add(Calendar.DAY_OF_MONTH, -2); //得到前一天
		// int day = calendar.get(Calendar.DAY_OF_MONTH);
		//
		//
		// DatePickerDialog dialog = new
		// DatePickerDialog(BaoLieBiaoDetailActivity.this, new
		// DatePickerDialog.OnDateSetListener() {
		//
		// @Override
		// public void onDateSet(DatePicker view, int year, int monthOfYear,
		// int dayOfMonth) {
		// showToast(year+"--"+(monthOfYear+1)+"---"+dayOfMonth);
		// }
		// }, year, month, day);
		//
		// dialog.getDatePicker().setMaxDate(new Date().getTime());
		// dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "取消", dialog);
		// dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "确定", dialog);
		// dialog.show();
	}

	/**
	 * @Description:初始化控件对象
	 * @Title:initFindView
	 * @return:void
	 * @throws
	 * @Create: 2016-6-25 下午2:26:23
	 * @Author : chengtao
	 */
	private void initFindView() {
		tvOrderDetailBegin = obtainView(R.id.tv_package_detail_begin);
		tvOrderDetailBeginCity = obtainView(R.id.tv_package_detail_begin_city);
		tvOrderDetailEnd = obtainView(R.id.tv_package_detail_end);
		tvOrderDetailEndCity = obtainView(R.id.tv_package_detail_end_city);
		tvOrderDetailDate = obtainView(R.id.tv_package_detail_date);
		tvOrderDetailVehicleNumber = obtainView(R.id.tv_package_detail_vehicle_number);
		tvOrderDetailUnitPrice = obtainView(R.id.tv_order_detail_unitprice);
		tvOrderDetailCocal = obtainView(R.id.tv_order_detail_cocal);
		tvOrderDetailTransactionsNumber = obtainView(R.id.tv_order_detail_transactions_number);
		tvOrderDetailSendCarNumber = obtainView(R.id.tv_order_detail_sendcar_number);
		tvOrderDetailBillingNumber = obtainView(R.id.tv_order_detail_billing_number);
		tvOrderDetailFinishNumber = obtainView(R.id.tv_order_detail_finish_number);
		rlOrderDetailTransactions = obtainView(R.id.rl_order_detail_transactions);
		rlOrderDetailSendCar = obtainView(R.id.rl_order_detail_sendcar);
		rlOrderDetailBilling = obtainView(R.id.rl_order_detail_billing);
		rlOrderDetailFinish = obtainView(R.id.rl_order_detail_finish);
		btOrderDetailResend = obtainView(R.id.bt_order_detail_resend);
		btOrderDetailPcs = obtainView(R.id.bt_order_detail_pcs);
		slOrderDetail = obtainView(R.id.sl_gone);
		llOrderDetailBottom = obtainView(R.id.ll_bottom);
		pbOrderDetailBar = obtainView(R.id.pb_order_detail_bar);
		tvOrderDetailTusun = obtainView(R.id.tv_package_detail_tusun);
		ivOrderDetailBaoxian = obtainView(R.id.iv_package_detail_baoxian);
		tvSumDun = obtainView(R.id.tv_order_detail_sumdun);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.employer_fragment_order_detail_title));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);

		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				EmploterBaoBiaoFragment.isBack = false;
				BaoLieBiaoDetailActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 获取传过来的数据
		packageID = getIntent().getStringExtra("id");
		int mAnNiuHui = getIntent().getIntExtra("anNiuHui", 0);

		if (mAnNiuHui != 0) {
			setStopPackageEnabled(false);
		}

		Log.e("TAG", "------------传过来的包ID----------------" + packageID);

		// 请求获取包的详细信息
		getBaoDetailContent();

	}

	// 请求获取包的详细信息
	private void getBaoDetailContent() {
		// 加载进度条
		pbOrderDetailBar.setVisibility(View.VISIBLE);
		llOrderDetailBottom.setVisibility(View.GONE);
		slOrderDetail.setVisibility(View.GONE);

		mGetBaoDetailContentRequest = new GetBaoDetailContentRequest(
				BaoLieBiaoDetailActivity.this, packageID);
		mGetBaoDetailContentRequest.setRequestId(GET_BAO_DETAIL);
		httpPost(mGetBaoDetailContentRequest);

	}

	@Override
	protected void setListener() {
		// btBaoDetailStop.setOnClickListener(this);
		// btBaoDetailCopy.setOnClickListener(this);
		//
		// rlBaoDetailQing.setOnClickListener(this);
		// rlBaoDetailExecute.setOnClickListener(this);
		// rlBaoDetailAudit.setOnClickListener(this);
		// rlBaoDetailHistory.setOnClickListener(this);
		// 初始化监听事件
		initOnClick();
	}

	/**
	 * @Description:初始化监听事件
	 * @Title:initOnClick
	 * @return:void
	 * @throws
	 * @Create: 2016-6-25 下午4:58:06
	 * @Author : chengtao
	 */
	private void initOnClick() {
		rlOrderDetailTransactions.setOnClickListener(this);
		rlOrderDetailSendCar.setOnClickListener(this);
		rlOrderDetailBilling.setOnClickListener(this);
		rlOrderDetailFinish.setOnClickListener(this);
		btOrderDetailResend.setOnClickListener(this);
		btOrderDetailPcs.setOnClickListener(this);
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
		// case GET_PINGTAI_KEFU:
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// PingTaiKeFu pingTaiKeFu = (PingTaiKeFu) response.singleData;
		//
		// mPcsPhone = pingTaiKeFu.PlatServicePhoneNum;
		// }
		// break;

		case GET_BAO_DETAIL: // 获取包详情信息
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				GetBiaoLieBiao mGetBiaoLieBiao = (GetBiaoLieBiao) response.singleData;

				// 获取包的详细信息
				mId = String.valueOf(mGetBiaoLieBiao.Id);
				mTenantId = String.valueOf(mGetBiaoLieBiao.TenantId);
				mPackageType = mGetBiaoLieBiao.PackageType; // 包类型：0：一口价；1：竞价；2：定向指派
				mPackageStartTime = StringUtils
						.formatSimpleDate(mGetBiaoLieBiao.PackageStartTime); // 包开始时间
				mPackageEndTime = StringUtils
						.formatSimpleDate(mGetBiaoLieBiao.PackageEndTime); // 包结束时间
				mCategoryName = mGetBiaoLieBiao.CategoryName; // 品类名称
				mPackageBeginAddress = mGetBiaoLieBiao.PackageBeginAddress; // 开始地址
				mPackageEndAddress = mGetBiaoLieBiao.PackageEndAddress; // 结束地址
				mSubsidy = mGetBiaoLieBiao.Subsidy; // 补贴
				mInsuranceType = mGetBiaoLieBiao.InsuranceType; // 保险：0：无保险；1：平台送保险；2：自己购买保险
				mPackageCount = mGetBiaoLieBiao.PackageCount; // 总车数
				mOrderCount = mGetBiaoLieBiao.OrderCount; // 已报名车数
				mPackageStatus = mGetBiaoLieBiao.PackageStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成
				mPackagePriceOrigin = mGetBiaoLieBiao.PackagePriceOrigin; // 发包方价格
				mPackageGoodsPrice = mGetBiaoLieBiao.PackageGoodsPrice; // 货值单价
				mPackageWeight = mGetBiaoLieBiao.PackageWeight; // 总吨数
				mPakcageCode = mGetBiaoLieBiao.PakcageCode;
				mOrderAllCount = mGetBiaoLieBiao.OrderAllCount;
				mOrderExecutingCount = mGetBiaoLieBiao.OrderExecutingCount;
				mOrderPendingCount = mGetBiaoLieBiao.OrderPendingCount;
				mOrderExecutedCount = mGetBiaoLieBiao.OrderExecutedCount;

				mPackageBeginName = mGetBiaoLieBiao.PackageBeginName;// 始发地
				mPackageEndName = mGetBiaoLieBiao.PackageEndName;// 目的地

				mQuotationcount = mGetBiaoLieBiao.Quotationcount;// 已报价
				mConfirmQuotationcount = mGetBiaoLieBiao.ConfirmQuotationcount;// 已成交
				mOrderSettledCount = mGetBiaoLieBiao.OrderSettledCount;// 已完成
				mOrderBeforeSettleCount = mGetBiaoLieBiao.OrderBeforeSettleCount;// 待结算
				mPackageBeginProvinceText = mGetBiaoLieBiao.PackageBeginProvinceText;
				mPackageBeginCityText = mGetBiaoLieBiao.PackageBeginCityText;
				mPackageBeginCountryText = mGetBiaoLieBiao.PackageBeginCountryText;
				mPackageEndProvinceText = mGetBiaoLieBiao.PackageEndProvinceText;
				mPackageEndCityText = mGetBiaoLieBiao.PackageEndCityText;
				mPackageEndCountryText = mGetBiaoLieBiao.PackageEndCountryText;
				orderInfoCount = mGetBiaoLieBiao.OrderInfoCount;
				mTicketArrangeCount = mGetBiaoLieBiao.ticketArrangeCount;
				mPrice = mGetBiaoLieBiao.PackagePrice;
				mCarRange = mGetBiaoLieBiao.CarRange;
				String mPackageLoseRate = mGetBiaoLieBiao.PackageLoseRate;
				mBeforeExcute = mGetBiaoLieBiao.BeforeExecute;
				mOnTheWayCount = mGetBiaoLieBiao.onTheWayCount;
				int mShortFallType = mGetBiaoLieBiao.ShortFallType;

				// 给订单数量赋值
				tvOrderDetailTransactionsNumber.setText(mQuotationcount
						+ "家已报价,已成交" + mConfirmQuotationcount + "家");
				tvOrderDetailSendCarNumber.setText(mTicketArrangeCount + "");
				tvOrderDetailBillingNumber
						.setText(mOrderBeforeSettleCount + "");
				tvOrderDetailFinishNumber.setText(mOrderSettledCount + "");

				// 单价
				tvOrderDetailUnitPrice.setText(mPackageGoodsPrice + "元");

				if (StringUtils.isStrNotNull(mPackageLoseRate)) {
					if(mShortFallType == 10){
						double mTuSun = Double.valueOf(mPackageLoseRate);
						tvOrderDetailTusun.setText(StringUtils
								.sanToQianFenHao(mTuSun));
					} else if (mShortFallType == 20){
						tvOrderDetailTusun.setText(mPackageLoseRate + "吨/车");
					}
				}

				if (!TextUtils.isEmpty(mPackageStartTime)
						&& mPackageStartTime != null
						&& !TextUtils.isEmpty(mPackageEndTime)
						&& mPackageEndTime != null) {
					tvOrderDetailDate.setText(mPackageStartTime + "~"
							+ mPackageEndTime);
				}

				if (StringUtils.isStrNotNull(mPackageWeight)) {
					tvSumDun.setText(mPackageWeight + "吨");
				}

				if (!TextUtils.isEmpty(mCategoryName) && mCategoryName != null) {
					tvOrderDetailCocal.setText(mCategoryName);
				}

				if (!TextUtils.isEmpty(mPackageBeginName)
						&& mPackageBeginName != null) {
					tvOrderDetailBegin.setText(mPackageBeginName);
				}

				if (!TextUtils.isEmpty(mPackageEndName)
						&& mPackageEndName != null) {
					tvOrderDetailEnd.setText(mPackageEndName);
				}

				if ((!TextUtils.isEmpty(mPackageBeginCityText) && mPackageBeginCityText != null)
						&& (!TextUtils.isEmpty(mPackageBeginCountryText) && mPackageBeginCountryText != null)) {
					tvOrderDetailBeginCity.setText(mPackageBeginCityText + "·"
							+ mPackageBeginCountryText);
				}

				if ((!TextUtils.isEmpty(mPackageEndCityText) && mPackageEndCityText != null)
						&& (!TextUtils.isEmpty(mPackageEndCountryText) && mPackageEndCountryText != null)) {
					tvOrderDetailEndCity.setText(mPackageEndCityText + "·"
							+ mPackageEndCountryText);
				}

				if (mInsuranceType == 0) {
					ivOrderDetailBaoxian
							.setImageResource(R.drawable.packagedetail_cross);
				} else if (mInsuranceType == 1 || mInsuranceType == 2) {
					ivOrderDetailBaoxian
							.setImageResource(R.drawable.packagedetail_tick);
				}

				if (StringUtils.isStrNotNull(mCarRange)) {
					tvOrderDetailVehicleNumber.setText(mOrderCount + "/"
							+ mCarRange);
				}

				if (mPackageStatus == 1) {
					btOrderDetailPcs.setText("取消发布");
				} else if (mPackageStatus == 2) {
					btOrderDetailPcs.setText("订单停运");
				}

			} else {
				showToast(message);
			}

			pbOrderDetailBar.setVisibility(View.GONE);
			slOrderDetail.setVisibility(View.VISIBLE);
			llOrderDetailBottom.setVisibility(View.VISIBLE);
			break;
		case STOP_QIANG_TICKET: // 停止抢单
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				showToast(message);
				EmploterBaoBiaoFragment.isBack = true;
				finish();
			} else {
				showToast(message);
				setStopPackageEnabled(true);
			}
			break;
		case STOP_PACKAGE:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				showToast(message);
				EmploterBaoBiaoFragment.isBack = true;
				finish();
			} else {
				showToast(message);
				setStopPackageEnabled(true);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		Log.e("TAG", "------------传过来的包ID----------------" + packageID);
		showToast(getString(R.string.net_error_toast));
		switch (requestId) {
		case STOP_QIANG_TICKET:
			setStopPackageEnabled(true);
			break;

		case STOP_PACKAGE:
			setStopPackageEnabled(true);
			break;

		default:
			break;
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, String packageId, int anNiuHui) {
		Intent intent = new Intent(context, BaoLieBiaoDetailActivity.class);
		intent.putExtra("id", packageId);
		intent.putExtra("anNiuHui", anNiuHui);
		context.startActivity(intent);
	}

	public static void invokeWithOnRefresh(Context context, String packageId,
			Class className) {
		Intent intent = new Intent(context, BaoLieBiaoDetailActivity.class);
		intent.putExtra("id", packageId);
		BaoLieBiaoDetailActivity.className = className;
		context.startActivity(intent);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invokeNewTask(Context context, String packageId) {
		Intent intent = new Intent(context, BaoLieBiaoDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (!TextUtils.isEmpty(packageId) && packageId != null) {
			intent.putExtra("id", packageId);// 包的id
		}
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		mSpManager.setPackageId(mId);
		switch (v.getId()) {
		// case R.id.bt_foot_bao_detail_stop: // 停止抢单
		// // 弹出框
		// showAlertDialog();
		//
		// setViewStopEnable(false);
		// break;
		case R.id.bt_order_detail_pcs:
			if (mPackageStatus == 1) {
				//友盟统计发包
				mUmeng.setCalculateEvents("order_click_details_cancel");

				showLogoutDialog();
			} else if (mPackageStatus == 2) {
				//友盟统计发包
				mUmeng.setCalculateEvents("order_click_details_stop");

				showStopDialog();
			}

			setStopPackageEnabled(false);
			break;
		case R.id.rl_order_detail_transactions: // 报价单页面
			String dateTime = tvOrderDetailDate.getText().toString().trim();

			if (mPackageStatus == 1 || mPackageStatus == 0) {
				showToast("当前订单正在审核中");
			} else {
				Log.e("TAG", "--------dateTime-----------" + dateTime);

				//友盟统计发包
				mUmeng.setCalculateEvents("order_click_details_quote");

				// 跳转到报价单界面，并报信息带过去
				QuoteActivity.invoke(mContext, packageID, mPackageBeginName,
						mPackageBeginCityText, mPackageBeginCountryText,
						mPackageEndName, mPackageEndCityText,
						mPackageEndCountryText, dateTime, mInsuranceType, 0,
						mBeforeExcute, mOnTheWayCount, mOrderBeforeSettleCount,
						mOrderSettledCount, mPrice, mPackageWeight);
			}
			break;

		case R.id.bt_order_detail_resend: // 复制发包
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_details_ship");

			// 跳转到发包界面，并报信息带过去
			NewSendPackageActivity.invoke(BaoLieBiaoDetailActivity.this,
					packageID);
			setViewCopyEnable(false);
			break;

		case R.id.rl_order_detail_sendcar: // 已抢订单
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_details_waybill");

			mSpManager.setStatusType(-1);
			CurrentTicketActivity.invoke(BaoLieBiaoDetailActivity.this,
					mPakcageCode, 1, -1);
			break;

		// case R.id.rl_foot_bao_detail_zhixing: // 执行中订单
		// mSpManager.setStatusType(2);
		// CurrentTicketActivity.invoke(BaoLieBiaoDetailActivity.this,
		// mPakcageCode);
		// break;

		case R.id.rl_order_detail_billing: // 待审核订单
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_details_waybill");

			mSpManager.setStatusType(3);
			CurrentTicketActivity.invoke(BaoLieBiaoDetailActivity.this,
					mPakcageCode, 1, 3);
			break;

		case R.id.rl_order_detail_finish: // 历史订单
			//友盟统计发包
			mUmeng.setCalculateEvents("order_click_details_waybill");

			mSpManager.setStatusType(4);
			CurrentTicketActivity.invoke(BaoLieBiaoDetailActivity.this,
					mPakcageCode, 1, 4);
			break;

		default:
			break;
		}
	}

	// // 将停止抢单按钮置为不可点击
	// private void setViewStopEnable(boolean bEnable) {
	// btOrderDetailPcs.setEnabled(bEnable);
	// }

	// 将复制发包按钮置为不可点击
	private void setViewCopyEnable(boolean bEnable) {
		btOrderDetailResend.setEnabled(bEnable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 设置发包按钮可点击
		setViewCopyEnable(true);
	}

	/**
	 * 终止包按钮的变化
	 */
	private void setStopPackageEnabled(boolean enabled) {
		if (!enabled) {
			btOrderDetailPcs.setEnabled(enabled);
			btOrderDetailPcs.setBackgroundResource(R.drawable.btn_zhihui);
		} else {
			btOrderDetailPcs.setEnabled(enabled);
			btOrderDetailPcs
					.setBackgroundResource(R.drawable.packagedetail_btn_left_background);
		}
	}

	/**
	 * @Description:订单停运
	 * @Title:showStopDialog
	 * @return:void
	 * @throws
	 * @Create: 2016-9-18 上午11:20:46
	 * @Author : mengwei
	 */
	private void showStopDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.employer_dialog_tingyun_package,
				null);
		RelativeLayout btnCancle = (RelativeLayout) view
				.findViewById(R.id.rl_package_tingyun_cancle);
		RelativeLayout btnSure = (RelativeLayout) view
				.findViewById(R.id.rl_package_tingyun_sure);
		AlertDialog.Builder builder = new Builder(mContext);
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setStopPackageEnabled(true);
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 停运包
				stopPackageRequest = new StopPackageRequest(mContext, packageID);
				stopPackageRequest.setRequestId(STOP_PACKAGE);
				httpGet(stopPackageRequest);
				dialog.dismiss();
			}
		});
	}

	/**
	 * 
	 * @Description:退出对话框
	 * @Title:showLogoutDialog
	 * @return:void
	 * @throws
	 * @Create: 2016年6月15日 下午4:30:37
	 * @Author : chengtao
	 */
	private void showLogoutDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.employer_dialog_quxiao_package,
				null);
		RelativeLayout btnCancle = (RelativeLayout) view
				.findViewById(R.id.rl_package_cancle);
		RelativeLayout btnSure = (RelativeLayout) view
				.findViewById(R.id.rl_package_sure);
		AlertDialog.Builder builder = new Builder(mContext);
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setStopPackageEnabled(true);
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取消包
				canclePackageRequest = new CanclePackageRequest(mContext,
						packageID);
				canclePackageRequest.setRequestId(STOP_QIANG_TICKET);
				httpGet(canclePackageRequest);
				dialog.dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			EmploterBaoBiaoFragment.isBack = false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
