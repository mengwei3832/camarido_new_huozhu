package com.yunqi.clientandroid.employer.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.employer.util.CurrentTicketListItemOnClick;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.CustomDigitalClock;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方当前订单adapter
 * @date 16/1/18
 */
public class CurrentTicketAdapter extends BaseAdapter {
	private List<TicketCurrentBean> ticketList;
	private Context context;
	private UmengStatisticsUtils mUmeng;

	public CurrentTicketAdapter(List<TicketCurrentBean> ticketList,
			Context context) {
		super();
		this.ticketList = ticketList;
		this.context = context;
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	@Override
	public int getCount() {
		return ticketList != null ? ticketList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return ticketList != null ? ticketList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return ticketList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoler vh = null;
		if (convertView == null) {
			vh = new ViewHoler();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_item_waybill, null);

			// vh.tvCreateTime = (TextView) convertView
			// .findViewById(R.id.tv_current_item_createTime);
			// vh.ivPackageType = (ImageView) convertView
			// .findViewById(R.id.iv_current_item_jiage);
			// vh.tvCategoryName = (TextView) convertView
			// .findViewById(R.id.tv_current_item_zhonglei);
			// vh.ivInsuranceType = (ImageView) convertView
			// .findViewById(R.id.iv_current_item_baoxian_img);
			// vh.tvInsuranceType = (TextView) convertView
			// .findViewById(R.id.tv_current_item_baoxian_text);
			// vh.tvSubsidy = (TextView) convertView
			// .findViewById(R.id.tv_current_item_butie);
			// vh.tvTicketCode = (TextView) convertView
			// .findViewById(R.id.tv_current_item_ticket_bianhao);
			// vh.tvPackageBeginName = (TextView) convertView
			// .findViewById(R.id.tv_current_item_startName);
			// vh.tvPackageBeginAddress = (TextView) convertView
			// .findViewById(R.id.tv_current_item_startCity);
			// vh.tvPackageEndName = (TextView) convertView
			// .findViewById(R.id.tv_current_item_endName);
			// vh.tvPackageEndAddress = (TextView) convertView
			// .findViewById(R.id.tv_current_item_endCity);
			// vh.btnState = (Button) convertView
			// .findViewById(R.id.btn_current_item_state);
			// vh.tvVehicleNo = (TextView) convertView
			// .findViewById(R.id.tv_current_item_carNumber);
			// vh.tvPackagePrice = (TextView) convertView
			// .findViewById(R.id.tv_current_item_yunfei);
			vh.tvWayBillBegin = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_begin);
			vh.tvWayBillEnd = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_end);
			vh.tvKuangFa = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_kuangfa);
			vh.tvKuangFaDate = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_kuangfa_date);
			vh.tvQianShou = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_qianshou);
			vh.tvQianShouDate = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_qianshou_date);
			vh.llWayBillCall = (LinearLayout) convertView
					.findViewById(R.id.ll_waybill_item_call);
			vh.tvWayBillVehicleNo = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_vehicleno);
			vh.tvJingJiRen = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_jingjiren);
			vh.tvWayBillAction = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_action);
			vh.ivWayBillStatus = (ImageView) convertView
					.findViewById(R.id.iv_waybill_item_status);
			vh.tvYunfeiPrice = (TextView) convertView
					.findViewById(R.id.tv_waybill_item_yunfei_price);

			convertView.setTag(vh);
		} else {
			vh = (ViewHoler) convertView.getTag();
		}

		// 设置数据
		TicketCurrentBean performListItem = ticketList.get(position);
		String vehicleNo = performListItem.VehicleNo;// 车牌号码
		final String packageBeginName = performListItem.PackageBeginName;// 出发地名称
		final String packageBeginAddress = performListItem.PackageBeginAddress;// 出发地详细地址
		final String packageEndName = performListItem.PackageEndName;// 目的地名称
		final String packageEndAddress = performListItem.PackageEndAddress;// 目的地详细地址
		final String ticketStatus = String
				.valueOf(performListItem.TicketStatus);// 执行状态
		final String createTime = performListItem.CreateTime;// 创建时间
		final String id = String.valueOf(performListItem.id);// 订单Id
		final int fInsuranceType = performListItem.InsuranceType;// 0：无保险
																	// 1：平台送保险
																	// 2：自己购买保险
		final String packageBeginCityText = performListItem.PackageBeginCityText;
		final String packageBeginCountryText = performListItem.PackageBeginCountryText;
		final String packageEndCityText = performListItem.PackageEndCityText;
		final String packageEndCountryText = performListItem.PackageEndCountryText;
		final String tenantTel = performListItem.TenantTel;
		final String vehicleContacts = performListItem.VehicleContacts;
		String ticketWeightInit = performListItem.TicketWeightInit;// 矿发吨数
		String ticketWeightReach = performListItem.TicketWeightReach;// 签收吨数
		String ticketLoadTime = performListItem.TicketLoadTime;// 矿发时间
		String ticketSettleTime = performListItem.TicketSettleTime;// 签收时间
		String infoTenantName = performListItem.InfoTenantName;// 信息部名称
		int isSettleType = performListItem.IsSettleType;// 是否结算申请
		String mFreightPayable = performListItem.FreightPayable;
		String mInfoTenantAliasesname = performListItem.InfoTenantAliasesname;
		final String mTicketCode = performListItem.TicketCode;//订单编号

		// 显示矿发吨数和签收吨数
		if (StringUtils.isStrNotNull(ticketWeightInit)) {
			vh.tvKuangFa.setText(ticketWeightInit);
		} else {
			vh.tvKuangFa.setText("0.00");
		}
		if (StringUtils.isStrNotNull(ticketWeightReach)) {
			vh.tvQianShou.setText(ticketWeightReach);
		} else {
			vh.tvQianShou.setText("0.00");
		}

		// 显示矿发时间
		if (StringUtils.isStrNotNull(ticketLoadTime)) {
			String kuangfaDate = StringUtils.formatModify(ticketLoadTime);
			vh.tvKuangFaDate.setText("(" + kuangfaDate + ")");
		} else {
			vh.tvKuangFaDate.setText("");
		}

		// 显示签收时间
		if (StringUtils.isStrNotNull(ticketSettleTime)) {
			String qianshouDate = StringUtils.formatModify(ticketSettleTime);
			vh.tvQianShouDate.setText("(" + qianshouDate + ")");
		} else {
			vh.tvQianShouDate.setText("");
		}

		// 显示运费单价
		if (StringUtils.isStrNotNull(mFreightPayable)) {
			vh.tvYunfeiPrice.setText("运费单价：" + mFreightPayable + "元/吨");
		} else {
			vh.tvYunfeiPrice.setText("运费单价：0.00元/吨");
		}

		// 显示信息部名称
		if (StringUtils.isStrNotNull(mInfoTenantAliasesname)) {
			vh.tvJingJiRen.setText("("
					+ context.getString(R.string.employer_jingjiren_name1)
					+ mInfoTenantAliasesname
					+ context.getString(R.string.employer_jingjiren_name2)
					+ ")");
		} else {
			vh.tvJingJiRen.setText("");
		}

		// 显示车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			vh.tvWayBillVehicleNo.setText(vehicleNo);
		}

		// 显示起点名称
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			vh.tvWayBillBegin.setText(packageBeginName);
		}

		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			vh.tvWayBillEnd.setText(packageEndName);
		}

		vh.llWayBillCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//sign_up_click_item_call
				//友盟统计首页
				mUmeng.setCalculateEvents("sign_up_click_item_call");
				// 拨打电话
				if (StringUtils.isStrNotNull(vehicleContacts)) {
					CommonUtil.callPhoneIndirect(context, vehicleContacts);
				} else {
					// 分机号
					CommonUtil.callPhoneIndirect(context, tenantTel);
				}
			}
		});

		// 根据执行状态显示动作
		if ((ticketStatus != null && ticketStatus.equals("1"))
				|| (ticketStatus != null && ticketStatus.equals("2"))
				|| (ticketStatus != null && ticketStatus.equals("3"))
				|| (ticketStatus != null && ticketStatus.equals("4"))) {
			vh.tvWayBillAction.setText("查看");
			vh.tvWayBillAction.setVisibility(View.VISIBLE);
		} else if ((ticketStatus != null && ticketStatus.equals("5"))
				|| (ticketStatus != null && ticketStatus.equals("6"))) {
			if (isSettleType == 0 || isSettleType == 2) {
				vh.tvWayBillAction.setText("结算");
				vh.tvWayBillAction.setVisibility(View.VISIBLE);
			} else if (isSettleType == 1) {
				vh.tvWayBillAction.setText("运费确认中");
				vh.tvWayBillAction.setVisibility(View.VISIBLE);
			}
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			vh.tvWayBillAction.setText("查看");
			vh.tvWayBillAction.setVisibility(View.VISIBLE);
		} else if ((ticketStatus != null && ticketStatus.equals("9"))
				|| (ticketStatus != null && ticketStatus.equals("10"))) {
			vh.tvWayBillAction.setVisibility(View.GONE);
		}

		// 根据执行状态显示状态
		if ((ticketStatus != null && ticketStatus.equals("1"))
				|| (ticketStatus != null && ticketStatus.equals("2"))
				|| (ticketStatus != null && ticketStatus.equals("3"))) {
			vh.ivWayBillStatus.setImageResource(R.drawable.waybill_daizhixing);
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			vh.ivWayBillStatus
					.setImageResource(R.drawable.waybill_ticket_daishouhuo);
		} else if ((ticketStatus != null && ticketStatus.equals("5"))
				|| (ticketStatus != null && ticketStatus.equals("6"))) {
			vh.ivWayBillStatus.setImageResource(R.drawable.waybill_daijiesuan);
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			vh.ivWayBillStatus.setImageResource(R.drawable.waybill_yijiesuan);
		} else if ((ticketStatus != null && ticketStatus.equals("9"))
				|| (ticketStatus != null && ticketStatus.equals("10"))) {
			vh.ivWayBillStatus.setImageResource(R.drawable.waybill_yiquxiao);
		}

		// 根据动作跳转不同页面
		vh.tvWayBillAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.e("TAG", "---ticketStatus-----" + ticketStatus);
				// 根据按钮上显示的状态显示不同的界面
				if (ticketStatus != null
						&& (ticketStatus.equals("1") || ticketStatus.equals("4") || ticketStatus.equals("5") || ticketStatus
								.equals("6"))) {
					//友盟统计首页
					mUmeng.setCalculateEvents("sign_up_click_item_settlement");
					// 待审核和已结算跳转到结算界面
					if (!TextUtils.isEmpty(id) && id != null) {
						UploadOrderAuditActivity.newInvoke(context, id, false,
								ticketStatus, mTicketCode);
					}
				} else {
					//友盟统计首页
					mUmeng.setCalculateEvents("sign_up_click_item_to_view");
					// 跳转到待换票、待装运、待收货界面
					if (!TextUtils.isEmpty(id) && id != null) {
						UploadOrderActivity.invoke(context, id, ticketStatus,
								true, mTicketCode);
					}
				}
			}
		});

		return convertView;
	}

	class ViewHoler {
		TextView tvWayBillBegin;
		TextView tvWayBillEnd;
		LinearLayout llWayBillCall;
		TextView tvWayBillVehicleNo;
		ImageView ivWayBillStatus;
		TextView tvWayBillAction;
		TextView tvKuangFa;
		TextView tvKuangFaDate;
		TextView tvQianShou;
		TextView tvQianShouDate;
		TextView tvJingJiRen;
		TextView tvYunfeiPrice;
	}

}
