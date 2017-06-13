package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.R.color;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.entity.ChengYunShuJu;
import com.yunqi.clientandroid.employer.entity.GetTicketInfo;
import com.yunqi.clientandroid.employer.request.CancleTicketRequest;
import com.yunqi.clientandroid.employer.request.ChengYunShuJuRequest;
import com.yunqi.clientandroid.employer.request.GetTicketRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * 
 * @Description:运单详情
 * @ClassName: WaybillDetailActivity
 * @author: chengtao
 * @date: 2016年6月28日 下午7:12:09
 * 
 */
@SuppressLint("InflateParams")
public class WaybillDetailActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout rlContianer;
	private TextView tvService;// 客服
	private TextView tvWaybillNumber;// 订单号码
	private TextView tvCarNumber;// 车牌号码
	private TextView tvTime;// 发货时限
	private TextView tvZhuang;// 装货地址
	private TextView tvXie;// 卸货地址
	private TextView tvGoodsType;// 煤种
	private TextView tvYunJia;// 运价
	private TextView tvGoodsAvePrice;// 货物单价
	private TextView tvCarNum;// 车数
	private TextView tvCarType;// 车型
	private TextView tvJieSuan;// 结算模式
	private TextView tvFaPiao;// 发票
	private TextView tvNote;// 备注
	private Button btnCancle;// 申请取消
	private ProgressBar progressBar;
	private PopupWindow canclePopupWindow;
	private View parentView;
	private TextView tvChengYun;

	// 传递过来的参数
	private String ticketId = "";
	private static String TICKET_ID = "ticketId";
	private String cancleReason = "";
	// 运单信息实体
	private GetTicketInfo ticketInfo;
	// 客服电话
	private String servicePhoneNumber = "4006751756";

	// 页面请求
	private GetTicketRequest getTicketRequest;// 获取运单信息
	private CancleTicketRequest cancleTicketRequest;// 取消运单
	private ChengYunShuJuRequest chengYunShuJuRequest;

	private String yunDanNumber;// 运单号
	// 请求ID
	private final static int GET_TICKET_REQUEST = 1;// 获取运单信息
	private final static int CANCLE_TICKET_REQUEST = 2;
	private final static int CHENG_YUN_SHU_JU = 3;
	// 联系客服文字
	private static String LIAN_XI_KE_FU = "联系客服";

	// 承运数据
	private String kuangTime21;// 矿发时间
	private String kuangTime22;
	private String kuangTime31;
	private String kuangTime32;
	private String kuangTime33;

	private float kuangDun21;// 吨数
	private float kuangDun22;
	private float kuangDun31;
	private float kuangDun32;
	private float kuangDun33;

	private String kuangRen21;// 操作人
	private String kuangRen22;
	private String kuangRen31;
	private String kuangRen32;
	private String kuangRen33;

	private String qianTime21;// 签收时间
	private String qianTime22;
	private String qianTime31;
	private String qianTime32;
	private String qianTime33;

	private float qianDun21;// 吨数
	private float qianDun22;
	private float qianDun31;
	private float qianDun32;
	private float qianDun33;

	private String qianRen21;// 操作人
	private String qianRen22;
	private String qianRen31;
	private String qianRen32;
	private String qianRen33;

	private String jieTime21;
	private String jieTime22;
	private String jieTime31;
	private String jieTime32;
	private String jieTime33;

	private double jieQian21;
	private double jieQian22;
	private double jieQian31;
	private double jieQian32;
	private double jieQian33;

	private String jieRen21;
	private String jieRen22;
	private String jieRen31;
	private String jieRen32;
	private String jieRen33;

	// 拼接字符串
	private String chengStr21;
	private String chengStr22;
	private String chengStr31;
	private String chengStr32;
	private String chengStr33;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_waybill_detial;
	}

	@Override
	protected void initView() {
		rlContianer = obtainView(R.id.rl_contianer);
		tvService = obtainView(R.id.tv_service);// 客服
		tvWaybillNumber = obtainView(R.id.tv_waybill_number);// 订单号码
		tvCarNumber = obtainView(R.id.tv_car_number);// 车牌号码
		tvTime = obtainView(R.id.tv_time);// 发货时限
		tvZhuang = obtainView(R.id.tv_zhuang);// 装货地址
		tvXie = obtainView(R.id.tv_xie);// 卸货地址
		tvGoodsType = obtainView(R.id.tv_goods_type);// 煤种
		tvYunJia = obtainView(R.id.tv_yun_jia);// 运价
		tvGoodsAvePrice = obtainView(R.id.tv_goods_ave_price);// 货物单价
		tvCarNum = obtainView(R.id.tv_car_num);// 车数
		tvCarType = obtainView(R.id.tv_car_type);// 车型
		tvJieSuan = obtainView(R.id.tv_jie_suan);// 结算模式
		tvFaPiao = obtainView(R.id.tv_fa_piao);// 发票
		tvNote = obtainView(R.id.tv_note);// 备注
		btnCancle = obtainView(R.id.btn_cancel);// 申请取消
		progressBar = obtainView(R.id.progress_bar);
		tvChengYun = obtainView(R.id.tv_chengyun_shuju);

		ticketId = getIntent().getStringExtra(TICKET_ID);
		parentView = this.getLayoutInflater().inflate(
				R.layout.employer_activity_waybill_detial, null);

		initActionBar();
		displayView(false);
		createCanclePopupWindow();
	}

	/**
	 * 
	 * @Description:创建取消订单弹窗
	 * @Title:createCanclePopupWindow
	 * @return:void
	 * @throws
	 * @Create: 2016年7月7日 上午11:20:18
	 * @Author : chengtao
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void createCanclePopupWindow() {
		canclePopupWindow = new PopupWindow(mContext);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.employer_item_cancle_yun_dan_popwindow, null);
		canclePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		canclePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		canclePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		canclePopupWindow.setContentView(view);
		canclePopupWindow.setOutsideTouchable(true);
		canclePopupWindow.setFocusable(true);
		canclePopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		canclePopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		TextView tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
		TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
		final EditText etReason = (EditText) view
				.findViewById(R.id.et_cancle_reason);
		View blackView = view.findViewById(R.id.black_block);
		blackView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canclePopupWindow.dismiss();
			}
		});
		tvCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canclePopupWindow.dismiss();
			}
		});
		tvSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancleReason = etReason.getText().toString();
				if (StringUtils.isStrNotNull(cancleReason)) {
					canclePackage();
					canclePopupWindow.dismiss();
				} else {
					showToast("请填写取消原因");
				}
			}
		});

	}

	/**
	 * 
	 * @Description:显示界面
	 * @Title:displayView
	 * @param b
	 * @return:void
	 * @throws
	 * @Create: 2016年7月1日 下午3:34:27
	 * @Author : chengtao
	 */
	private void displayView(boolean b) {
		if (b) {
			rlContianer.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		} else {
			rlContianer.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	private void initActionBar() {
		setActionBarLeft(R.drawable.fanhui);
		setActionBarTitle("运单详情");
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		// 获取包信息
		getTicketInfo();
	}

	/**
	 * 
	 * @Description:获取包信息
	 * @Title:getTicketInfo
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 下午7:40:43
	 * @Author : chengtao
	 */
	private void getTicketInfo() {
		Log.v("TAG", "getTicketInfo--------" + ticketId);
		getTicketRequest = new GetTicketRequest(mContext, ticketId);
		getTicketRequest.setRequestId(GET_TICKET_REQUEST);
		httpPost(getTicketRequest);

	}

	@Override
	protected void setListener() {
		tvService.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		Log.v("TAG", "onSuccess");
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		int totalCount = response.totalCount;
		switch (requestId) {
		case CHENG_YUN_SHU_JU:// 承运数据
			if (isSuccess) {
				ArrayList<ChengYunShuJu> chengList = response.data;
				switch (totalCount) {
				case 1:
					boolean isSettle = chengList.get(0).IsSettle;
					if (isSettle) {
						String jieTime = StringUtils.formatModify(chengList
								.get(0).CreateTime);
						double moneyJie = chengList.get(0).TicketSettleMount;
						String personJie = chengList.get(0).TicketOperator;
						String chengStr = "运单号:" + yunDanNumber + "<br/>结算时间:"
								+ jieTime
								+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>金额:"
								+ moneyJie
								+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>操作人:"
								+ personJie;
						tvChengYun.setText(Html.fromHtml(chengStr));
					} else {
						int ticketOperationType = chengList.get(0).TicketOperationType;
						if (ticketOperationType == 30) {
							String kuangTime = StringUtils
									.formatModify(chengList.get(0).CreateTime);
							float kuangDun = chengList.get(0).TicketWeightInit;
							String kuangRen = chengList.get(0).TicketOperator;
							String chengStr = "运单号:" + yunDanNumber
									+ "<br/>矿发时间:" + kuangTime
									+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>吨数:"
									+ kuangDun
									+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>操作人:"
									+ kuangRen;
							tvChengYun.setText(Html.fromHtml(chengStr));
						}
						if (ticketOperationType == 40) {
							String qianTime = StringUtils
									.formatModify(chengList.get(0).CreateTime);
							float qianDun = chengList.get(0).TicketWeightReach;
							String qianRen = chengList.get(0).TicketOperator;
							String chengStr = "运单号:" + yunDanNumber
									+ "<br/>签收时间:" + qianTime
									+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>吨数:"
									+ qianDun
									+ ";<span>&nbsp;&nbsp;&nbsp;</sapn>操作人:"
									+ qianRen;
							tvChengYun.setText(Html.fromHtml(chengStr));
						}
					}
					break;
				case 2:
					boolean isSettle21 = chengList.get(0).IsSettle;
					boolean isSettle22 = chengList.get(1).IsSettle;
					if (isSettle21) {
						jieTime21 = StringUtils
								.formatModify(chengList.get(0).CreateTime);
						jieQian21 = chengList.get(0).TicketSettleMount;
						jieRen21 = chengList.get(0).TicketOperator;
						chengStr21 = "运单号:<span>&nbsp</sapn>"
								+ yunDanNumber
								+ "<br/>结算:<span>&nbsp</sapn>"
								+ jieTime21
								+ ";<span>&nbsp;&nbsp;</sapn>金额:<span>&nbsp</sapn>"
								+ jieQian21
								+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
								+ jieRen21;
					} else {
						int ticketOperationType = chengList.get(0).TicketOperationType;
						if (ticketOperationType == 30) {
							kuangTime21 = StringUtils.formatModify(chengList
									.get(0).CreateTime);
							kuangDun21 = chengList.get(0).TicketWeightInit;
							kuangRen21 = chengList.get(0).TicketOperator;
							chengStr21 = "运单号:<span>&nbsp</sapn>"
									+ yunDanNumber
									+ "<br/>矿发:<span>&nbsp</sapn>"
									+ kuangTime21
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ kuangDun21
									+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ kuangRen21;
						}
						if (ticketOperationType == 40) {
							qianTime21 = StringUtils.formatModify(chengList
									.get(0).CreateTime);
							qianDun21 = chengList.get(0).TicketWeightReach;
							qianRen21 = chengList.get(0).TicketOperator;
							chengStr21 = "运单号:<span>&nbsp</sapn>"
									+ yunDanNumber
									+ "<br/>签收:<span>&nbsp</sapn>"
									+ qianTime21
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ qianDun21
									+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ qianRen21;
						}
					}
					if (isSettle22) {
						jieTime22 = StringUtils
								.formatModify(chengList.get(1).CreateTime);
						jieQian22 = chengList.get(1).TicketSettleMount;
						jieRen22 = chengList.get(1).TicketOperator;
						chengStr22 = "结算:<span>&nbsp</sapn>"
								+ jieTime22
								+ ";<span>&nbsp;&nbsp;</sapn>金额:<span>&nbsp</sapn>"
								+ jieQian22
								+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
								+ jieRen22;
						tvChengYun.setText(Html.fromHtml(chengStr21 + "<br/>"
								+ chengStr22));
					} else {
						int ticketOperationType = chengList.get(1).TicketOperationType;
						if (ticketOperationType == 30) {
							kuangTime22 = StringUtils.formatModify(chengList
									.get(1).CreateTime);
							kuangDun22 = chengList.get(1).TicketWeightInit;
							kuangRen22 = chengList.get(1).TicketOperator;
							chengStr22 = "矿发:<span>&nbsp</sapn>"
									+ kuangTime22
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ kuangDun22
									+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ kuangRen22;
							tvChengYun.setText(Html.fromHtml(chengStr21
									+ "<br/>" + chengStr22));
						}
						if (ticketOperationType == 40) {
							qianTime22 = StringUtils.formatModify(chengList
									.get(1).CreateTime);
							qianDun22 = chengList.get(1).TicketWeightReach;
							qianRen22 = chengList.get(1).TicketOperator;
							chengStr22 = "签收:<span>&nbsp</sapn>"
									+ qianTime22
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ qianDun22
									+ ";<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ qianRen22;
							tvChengYun.setText(Html.fromHtml(chengStr21
									+ "<br/>" + chengStr22));
						}
					}
					break;
				case 3:
					boolean isSettle31 = chengList.get(0).IsSettle;
					boolean isSettle32 = chengList.get(1).IsSettle;
					boolean isSettle33 = chengList.get(2).IsSettle;
					if (isSettle31) {
						jieTime31 = StringUtils
								.formatModify(chengList.get(0).CreateTime);
						jieQian31 = chengList.get(0).TicketSettleMount;
						jieRen31 = chengList.get(0).TicketOperator;
						chengStr31 = "运单号:<span>&nbsp</sapn>"
								+ yunDanNumber
								+ "<br/>结算:<span>&nbsp</sapn>"
								+ jieTime31
								+ ";<span>&nbsp;&nbsp;</sapn>金额:<span>&nbsp</sapn>"
								+ jieQian31
								+ "元;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
								+ jieRen31;
					} else {
						int ticketOperationType = chengList.get(0).TicketOperationType;
						if (ticketOperationType == 30) {
							kuangTime31 = StringUtils.formatModify(chengList
									.get(0).CreateTime);
							kuangDun31 = chengList.get(0).TicketWeightInit;
							kuangRen31 = chengList.get(0).TicketOperator;
							chengStr31 = "运单号:<span>&nbsp</sapn>"
									+ yunDanNumber
									+ "<br/>矿发:<span>&nbsp</sapn>"
									+ kuangTime31
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ kuangDun31
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ kuangRen31;
						}
						if (ticketOperationType == 40) {
							qianTime31 = StringUtils.formatModify(chengList
									.get(0).CreateTime);
							qianDun31 = chengList.get(0).TicketWeightReach;
							qianRen31 = chengList.get(0).TicketOperator;
							chengStr31 = "运单号:<span>&nbsp</sapn>"
									+ yunDanNumber
									+ "<br/>签收:<span>&nbsp</sapn>"
									+ qianTime31
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ qianDun31
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ qianRen31;
						}
					}
					if (isSettle32) {
						jieTime32 = StringUtils
								.formatModify(chengList.get(1).CreateTime);
						jieQian32 = chengList.get(1).TicketSettleMount;
						jieRen32 = chengList.get(1).TicketOperator;
						chengStr32 = "结算:<span>&nbsp</sapn>"
								+ jieTime32
								+ ";<span>&nbsp;&nbsp;</sapn>金额:<span>&nbsp</sapn>"
								+ jieQian32
								+ "元;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
								+ jieRen32;
					} else {
						int ticketOperationType = chengList.get(1).TicketOperationType;
						if (ticketOperationType == 30) {
							kuangTime32 = StringUtils.formatModify(chengList
									.get(1).CreateTime);
							kuangDun32 = chengList.get(1).TicketWeightInit;
							kuangRen32 = chengList.get(1).TicketOperator;
							chengStr32 = "矿发:<span>&nbsp</sapn>"
									+ kuangTime32
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ kuangDun32
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ kuangRen32;
						}
						if (ticketOperationType == 40) {
							qianTime32 = StringUtils.formatModify(chengList
									.get(1).CreateTime);
							qianDun32 = chengList.get(1).TicketWeightReach;
							qianRen32 = chengList.get(1).TicketOperator;
							chengStr32 = "签收:<span>&nbsp</sapn>"
									+ qianTime32
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ qianDun32
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ qianRen32;
						}
					}

					if (isSettle33) {
						jieTime33 = StringUtils
								.formatModify(chengList.get(2).CreateTime);
						jieQian33 = chengList.get(2).TicketSettleMount;
						jieRen33 = chengList.get(2).TicketOperator;
						chengStr33 = "结算:<span>&nbsp</sapn>"
								+ jieTime33
								+ ";<span>&nbsp;&nbsp;</sapn>金额:<span>&nbsp</sapn>"
								+ jieQian33
								+ "元;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
								+ jieRen33;
						tvChengYun.setText(Html.fromHtml(chengStr31 + "<br/>"
								+ chengStr32 + "<br/>" + chengStr33));
					} else {
						int ticketOperationType = chengList.get(2).TicketOperationType;
						if (ticketOperationType == 30) {
							kuangTime33 = StringUtils.formatModify(chengList
									.get(2).CreateTime);
							kuangDun33 = chengList.get(2).TicketWeightInit;
							kuangRen33 = chengList.get(2).TicketOperator;
							chengStr33 = "矿发:<span>&nbsp</sapn>"
									+ kuangTime33
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ kuangDun33
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ kuangRen33;
							tvChengYun.setText(Html.fromHtml(chengStr31
									+ "<br/>" + chengStr32 + "<br/>"
									+ chengStr33));
						}
						if (ticketOperationType == 40) {
							qianTime33 = StringUtils.formatModify(chengList
									.get(2).CreateTime);
							qianDun33 = chengList.get(2).TicketWeightReach;
							qianRen33 = chengList.get(2).TicketOperator;
							chengStr33 = "签收:<span>&nbsp</sapn>"
									+ qianTime33
									+ ";<span>&nbsp;&nbsp;</sapn>吨数:<span>&nbsp</sapn>"
									+ qianDun33
									+ "吨;<span>&nbsp;&nbsp;</sapn>操作人:<span>&nbsp</sapn>"
									+ qianRen33;
							tvChengYun.setText(Html.fromHtml(chengStr31
									+ "<br/>" + chengStr32 + "<br/>"
									+ chengStr33));
						}
					}
					break;

				default:
					break;
				}
			}
			break;
		case GET_TICKET_REQUEST:// 获取运单信息
			showToast(message);
			if (isSuccess) {
				ticketInfo = (GetTicketInfo) response.singleData;
				setInfoFromTicket(ticketInfo);
				displayView(true);
				chengYunShuJuRequest = new ChengYunShuJuRequest(mContext,
						ticketId);
				chengYunShuJuRequest.setRequestId(CHENG_YUN_SHU_JU);
				httpPost(chengYunShuJuRequest);
			} else {
				finish();
			}
			break;
		case CANCLE_TICKET_REQUEST:
			showToast(message);
			if (isSuccess) {
				finish();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:将运单信息填写进界面
	 * @Title:setInfoFromTicket
	 * @param ticketInfo2
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 下午8:12:38
	 * @Author : chengtao
	 */
	private void setInfoFromTicket(GetTicketInfo info) {
		Log.v("TAG", info.toString());
		// 订单号
		if (StringUtils.isStrNotNull(info.TicketCode)) {
			tvWaybillNumber.setText(info.TicketCode);
		}
		// 车牌号
		if (StringUtils.isStrNotNull(info.VehicleNo)) {
			tvCarNumber.setText(info.VehicleNo);
		}
		// 装货时限
		if (StringUtils.isStrNotNull(info.PackageStartTime)
				&& StringUtils.isStrNotNull(info.PackageEndTime)) {
			tvTime.setText(StringUtils.formatModify(info.PackageStartTime)
					+ " ~ " + StringUtils.formatModify(info.PackageEndTime));
		}

		// 装货地址
		if (StringUtils.isStrNotNull(info.PackageBeginAddress)) {
			tvZhuang.setText(info.PackageBeginAddress);
		}
		// 卸货地址
		if (StringUtils.isStrNotNull(info.PackageEndAddress)) {
			tvXie.setText(info.PackageEndAddress);
		}
		// 煤种
		if (StringUtils.isStrNotNull(info.CategoryName)) {
			tvGoodsType.setText(info.CategoryName);
		}
		// 运价
		if (info.price > 0) {
			if (info.PackagePriceType == 0) {
				tvYunJia.setText(info.price + " 元/吨");
			} else if (info.PackagePriceType == 1) {
				tvYunJia.setText(info.price + " 元/吨*公里");
			} else if (info.PackagePriceType == 2) {
				tvYunJia.setText(info.price + " 元/车数");
			}

		}
		// 货物单价
		if (info.PackageGoodsPrice > 0) {
			tvGoodsAvePrice.setText(info.PackageGoodsPrice + " 元");
		}
		// 车数
		if (info.PackageCount > 0) {
			tvCarNum.setText(info.PackageCount + "车");
		}
		// 车型
		if (StringUtils.isStrNotNull(info.VehicleTypeList)) {
			tvCarType.setText(info.VehicleTypeList);
		}
		// 结算模式
		if (info.PackageSettlementType == 0) {
			tvJieSuan.setText("线上结算");
		} else if (info.PackageSettlementType == 1) {
			tvJieSuan.setText("线下结算");
		}
		// 发票
		if (info.NeedInvoice) {
			tvFaPiao.setText("要发票");
		} else {
			tvFaPiao.setText("不要发票");
		}
		// 备注
		if (StringUtils.isStrNotNull(info.TicketMemo)) {
			tvNote.setText(info.TicketMemo);
		}

		// 获取承运数据

		if (StringUtils.isStrNotNull(info.TicketCode)) {
			yunDanNumber = info.TicketCode;// 运单号
		}

		int customOperation = info.CustomOperation;// 运单锁定状态：0：未申请客服仲裁；1：锁定；2:解除锁定
		if (customOperation == 0 || customOperation == 2) {
			btnCancle.setBackgroundResource(color.color_2299EE);
			btnCancle.setEnabled(true);
			// 设置申请按钮的显示
			if (info.TicketStatus == 8 || info.TicketStatus == 10) {
				btnCancle.setVisibility(View.GONE);
			}
			// 设置申请按钮的文字显示
			if (info.TicketStatus == 5 || info.TicketStatus == 6) {
				btnCancle.setText(LIAN_XI_KE_FU);
			}
		} else if (customOperation == 1) {
			btnCancle.setBackgroundResource(color.color_c5c5c5);
			btnCancle.setEnabled(false);
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		Log.v("TAG", "onFailure");
		Log.v("TAG", requestId + "");
		showToast(getResources().getString(R.string.net_error_toast));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:// 申请取消
			if (btnCancle.getText().toString().equals(LIAN_XI_KE_FU)) {
				CommonUtil.callPhoneIndirect(mContext, servicePhoneNumber);
			} else {
				canclePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0,
						0);
			}
			break;
		case R.id.tv_service:// 客服
			CommonUtil.callPhoneIndirect(mContext, servicePhoneNumber);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:申请取消订单
	 * @Title:canclePackage
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 下午7:43:17
	 * @Author : chengtao
	 */
	private void canclePackage() {
		cancleTicketRequest = new CancleTicketRequest(mContext, ticketId,
				cancleReason);
		cancleTicketRequest.setRequestId(CANCLE_TICKET_REQUEST);
		httpPost(cancleTicketRequest);
	}

	/**
	 * 
	 * @Description:本界面跳转
	 * @Title:invoke
	 * @param activity
	 * @param ticketId
	 * @return:void
	 * @throws
	 * @Create: 2016年6月29日 下午12:41:57
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, String ticketId) {
		Intent intent = new Intent(activity, WaybillDetailActivity.class);
		intent.putExtra(TICKET_ID, ticketId);
		activity.startActivity(intent);
	}

	public static void invokeNewTask(Context activity, String ticketId) {
		Intent intent = new Intent(activity, WaybillDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(TICKET_ID, ticketId);
		activity.startActivity(intent);
	}
}
