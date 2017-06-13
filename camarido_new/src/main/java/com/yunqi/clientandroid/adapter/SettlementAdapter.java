package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.CanReceiveActivity;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 待结算运单适配器
 * @date 15/11/28
 */
public class SettlementAdapter extends ArrayAdapter<PerformListItem> {

	private Context mContext;
	private List<PerformListItem> mList;

	public SettlementAdapter(Context context, List<PerformListItem> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_ticket_list_settlement, null);
		}

		SettlementAdapterListViewHolder holder = SettlementAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		PerformListItem performListItem = mList.get(position);
		final String vehicleNo = performListItem.vehicleNo;// 车牌号码
		final String packageBeginName = performListItem.packageBeginName;// 出发地名称
		final String packageEndName = performListItem.packageEndName;// 目的地名称
		String packageBeginAdress = performListItem.packageBeginAddress;// 出发地地址
		String packageEndAdress = performListItem.packageEndAddress; // 目的地地址
		final String name = performListItem.name;// 执行人姓名
		String userPhone = performListItem.userPhone;// 执行人电话
		final String ticketStatus = performListItem.ticketStatus;// 执行状态
		final String createTime = performListItem.createTime;// 创建时间
		final String ticketCode = performListItem.ticketCode;// 订单号
		String freightPayable = performListItem.freightPayable;// 运费
		String subsidy = performListItem.subsidy;// 补贴
		final String id = performListItem.id;// 订单Id

		int sPackageType = performListItem.packageType;// 包类型：0：普通包 1：竞价包

		// 车牌随机背景颜色
		// Random random = new Random();
		// int nextInt = random.nextInt(6);
		// ColorUtil.changeImageViewStyle(nextInt, holder.ivVehicleBg);
		holder.ivVehicleBg.setImageResource(R.drawable.chepai_bg);

		// 显示创建时间
		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			String formatPerform = StringUtils.formatPerform(createTime);
			String date = formatPerform.split("年")[1];
			holder.tvDate.setText(date);
		}

		// 显示订单号
		if (!TextUtils.isEmpty(ticketCode) && ticketCode != null) {
			holder.tvOrdernumber.setText("订单号:  " + ticketCode);
		}

		// 显示车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			String licensehead = vehicleNo.substring(0, 2);
			String licensenum = vehicleNo.substring(2);

			holder.tvLicensehead.setText(licensehead);
			holder.tvLicensenum.setText(licensenum);

		}

		// 显示起点名称
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			holder.tvProvenance.setText(packageBeginName);
		}

		// 显示终点名称
		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			holder.tvDestination.setText(packageEndName);
		}

		// 显示执行人名称
		if (!TextUtils.isEmpty(name) && name != null) {
			holder.tvName.setText(name);
		}

		// 显示执行人电话
		if (!TextUtils.isEmpty(userPhone) && userPhone != null) {
			holder.tvPhone.setText(userPhone);
		}

		// 显示运费
		if (!TextUtils.isEmpty(freightPayable) && freightPayable != null) {
			//
			holder.tvFreightHinit.setText("运费:");

			holder.tvFreight.setText(freightPayable);
			holder.tvFreight.setVisibility(View.VISIBLE);
		} else {
			// 不显示
			holder.tvFreight.setVisibility(View.INVISIBLE);
		}

		// // 显示补贴
		// if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
		// holder.tvSubsidies.setText(Html.fromHtml("补贴:"
		// + "<font color='#ff4400'>" + subsidy + "</font>" + "元"));
		// holder.tvSubsidies.setVisibility(View.VISIBLE);
		// } else {
		// // 不显示
		// holder.tvSubsidies.setVisibility(View.INVISIBLE);
		// }

		holder.btnState.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO--点击可领取
				// if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(ticketCode)
				// && !TextUtils.isEmpty(vehicleNo)
				// && !TextUtils.isEmpty(createTime)
				// && !TextUtils.isEmpty(name)
				// && !TextUtils.isEmpty(packageBeginName)
				// && !TextUtils.isEmpty(packageEndName)) {}

				CanReceiveActivity.invoke(mContext, id, ticketCode, vehicleNo,
						createTime, name, packageBeginName, packageEndName);

			}
		});

		// 根据执行状态显示
		if (ticketStatus != null && ticketStatus.equals("6")) {
			holder.btnState.setText("待结算");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("7")) {
			holder.btnState.setText("可领取");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("2")) {
			holder.btnState.setText("待换票");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			holder.btnState.setText("待装运");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			holder.btnState.setText("待收货");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("5")) {
			holder.btnState.setText("待审核");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			holder.btnState.setText("已结算");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("0")) {
			holder.btnState.setText("待生效");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("1")) {
			holder.btnState.setText("待执行");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("9")) {
			holder.btnState.setText("禁用");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("10")) {
			holder.btnState.setText("取消");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		}

		return convertView;

	}

	static class SettlementAdapterListViewHolder {
		TextView tvDate;
		TextView tvOrdernumber;
		TextView tvLicensehead;
		TextView tvLicensenum;
		TextView tvProvenance;
		TextView tvDestination;
		TextView tvFreight;
		// TextView tvSubsidies;
		TextView tvName;
		TextView tvPhone;
		Button btnState;
		ImageView ivVehicleBg;
		//
		TextView tvFreightHinit;

		public SettlementAdapterListViewHolder(View convertView) {
			tvOrdernumber = (TextView) convertView
					.findViewById(R.id.tv_settlement_ordernumber);
			tvDate = (TextView) convertView
					.findViewById(R.id.tv_settlement_date);
			tvLicensehead = (TextView) convertView
					.findViewById(R.id.tv_settlement_licensehead);
			tvLicensenum = (TextView) convertView
					.findViewById(R.id.tv_settlement_licensenum);
			tvProvenance = (TextView) convertView
					.findViewById(R.id.tv_settlement_provenance);
			tvDestination = (TextView) convertView
					.findViewById(R.id.tv_settlement_destination);
			tvFreight = (TextView) convertView
					.findViewById(R.id.tv_settlement_freight);
			// tvSubsidies = (TextView) convertView
			// .findViewById(R.id.tv_settlement_subsidies);
			tvName = (TextView) convertView
					.findViewById(R.id.tv_settlement_name);
			tvPhone = (TextView) convertView
					.findViewById(R.id.tv_settlement_phone);
			btnState = (Button) convertView
					.findViewById(R.id.btn_settlement_state);
			ivVehicleBg = (ImageView) convertView
					.findViewById(R.id.iv_settlement_vehiclebg);
			// 新增
			tvFreightHinit = (TextView) convertView
					.findViewById(R.id.tv_settlement_freight_hinit);
		}

		public static SettlementAdapterListViewHolder getHolder(View convertView) {
			SettlementAdapterListViewHolder holder = (SettlementAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new SettlementAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
