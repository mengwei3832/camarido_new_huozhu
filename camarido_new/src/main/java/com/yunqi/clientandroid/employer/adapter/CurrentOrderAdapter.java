package com.yunqi.clientandroid.employer.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方当前订单adapter
 * @date 16/1/18
 */
public class CurrentOrderAdapter extends ArrayAdapter<PerformListItem> {

	private Context mContext;
	private List<PerformListItem> mList;

	public CurrentOrderAdapter(Context context, List<PerformListItem> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_current_list_employer, null);
		}

		CurrentAdapterListViewHolder holder = CurrentAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		PerformListItem performListItem = mList.get(position);
		String vehicleNo = performListItem.vehicleNo;// 车牌号码
		final String packageBeginName = performListItem.packageBeginName;// 出发地名称
		final String packageBeginAddress = performListItem.packageBeginAddress;// 出发地详细地址
		final String packageEndName = performListItem.packageEndName;// 目的地名称
		final String packageEndAddress = performListItem.packageEndAddress;// 目的地详细地址
		String packagePrice = performListItem.packagePrice;// 运价
		String name = performListItem.name;// 执行人姓名
		String userPhone = performListItem.userPhone;// 执行人电话
		final String ticketStatus = performListItem.ticketStatus;// 执行状态
		final String createTime = performListItem.createTime;// 创建时间
		String ticketCode = performListItem.ticketCode;// 订单号
		final String id = performListItem.id;// 订单Id
		final int fInsuranceType = performListItem.insuranceType;// 0：无保险
																	// 1：平台送保险
																	// 2：自己购买保险
		String packageBeginProvinceText = performListItem.PackageBeginProvinceText;
		final String packageBeginCityText = performListItem.PackageBeginCityText;
		final String packageBeginCountryText = performListItem.PackageBeginCountryText;
		final String packageEndProvinceText = performListItem.PackageEndProvinceText;
		final String packageEndCityText = performListItem.PackageEndCityText;
		final String packageEndCountryText = performListItem.PackageEndCountryText;

		// 车牌随机背景颜色
		Random random = new Random();
		int nextInt = random.nextInt(6);
		ColorUtil.changeImageViewStyle(nextInt, holder.ivVehicleBg);

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

		// 显示运价
		if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
			holder.tvPrice.setText("￥" + packagePrice);
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

		holder.btnState.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据按钮上显示的状态显示不同的界面
				if (ticketStatus != null
						&& (ticketStatus.equals("5") || ticketStatus
								.equals("6"))) {
					// 待审核和已结算跳转到结算界面
					if (!TextUtils.isEmpty(id) && id != null) {
						/*
						 * UploadOrderAuditActivity.invoke(mContext, id,
						 * ticketStatus, packageBeginName, packageBeginAddress,
						 * packageEndName, packageEndAddress, createTime,
						 * fInsuranceType, packageBeginCityText,
						 * packageBeginCountryText, packageEndCityText,
						 * packageEndCountryText);
						 */
					}

				} else {
					// 跳转到查看页面界面
					if (!TextUtils.isEmpty(id) && id != null) {
						/*
						 * UploadOrderActivity.invoke(mContext, id,
						 * ticketStatus, packageBeginName, packageEndName,
						 * createTime, packageBeginCityText,
						 * packageBeginCountryText, packageEndCityText,
						 * packageEndCountryText);
						 */
					}
				}

			}
		});

		// 根据执行状态显示
		if (ticketStatus != null && ticketStatus.equals("2")) {
			holder.btnState.setText("待换票");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			holder.btnState.setText("待装运");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			holder.btnState.setText("待收货");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("5")) {
			holder.btnState.setText("待审核");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			holder.btnState.setText("已结算");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("0")) {
			holder.btnState.setText("待生效");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("1")) {
			holder.btnState.setText("待执行");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("6")) {
			holder.btnState.setText("待结算");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("7")) {
			holder.btnState.setText("可领取");
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

	static class CurrentAdapterListViewHolder {
		TextView tvDate;
		TextView tvOrdernumber;
		TextView tvLicensehead;
		TextView tvLicensenum;
		TextView tvPrice;
		TextView tvProvenance;
		TextView tvDestination;
		TextView tvName;
		TextView tvPhone;
		Button btnState;
		ImageView ivVehicleBg;

		public CurrentAdapterListViewHolder(View convertView) {
			tvOrdernumber = (TextView) convertView
					.findViewById(R.id.tv_current_ordernumber_employer);
			tvDate = (TextView) convertView
					.findViewById(R.id.tv_current_date_employer);
			tvLicensehead = (TextView) convertView
					.findViewById(R.id.tv_current_licensehead_employer);
			tvLicensenum = (TextView) convertView
					.findViewById(R.id.tv_current_licensenum_employer);
			tvPrice = (TextView) convertView
					.findViewById(R.id.tv_current_price_employer);
			tvProvenance = (TextView) convertView
					.findViewById(R.id.tv_current_provenance_employer);
			tvDestination = (TextView) convertView
					.findViewById(R.id.tv_current_destination_employer);
			tvName = (TextView) convertView
					.findViewById(R.id.tv_current_name_employer);
			tvPhone = (TextView) convertView
					.findViewById(R.id.tv_current_phone_employer);
			btnState = (Button) convertView
					.findViewById(R.id.btn_current_state_employer);
			ivVehicleBg = (ImageView) convertView
					.findViewById(R.id.iv_current_vehiclebg_employer);

		}

		public static CurrentAdapterListViewHolder getHolder(View convertView) {
			CurrentAdapterListViewHolder holder = (CurrentAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new CurrentAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
