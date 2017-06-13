package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.UploadAndModifyDocumentsActivity;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 当前运单适配器
 * @date 15/11/28
 */
public class CurrentAdapter extends ArrayAdapter<PerformListItem> {

	private Context mContext;
	private List<PerformListItem> mList;

	public CurrentAdapter(Context context, List<PerformListItem> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_ticket_list_current, null);
		}

		CurrentAdapterListViewHolder holder = CurrentAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		PerformListItem performListItem = mList.get(position);
		String vehicleNo = performListItem.vehicleNo;// 车牌号码
		final String packageBeginName = performListItem.packageBeginName;// 出发地名称
		final String packageEndName = performListItem.packageEndName;// 目的地名称
		String packageBeginAdress = performListItem.packageBeginAddress;// 出发地地址
		String packageEndAdress = performListItem.packageEndAddress; // 目的地地址
		String packagePrice = performListItem.packagePrice;// 运价
		String name = performListItem.name;// 执行人姓名
		String userPhone = performListItem.userPhone;// 执行人电话
		final String ticketStatus = performListItem.ticketStatus;// 执行状态
		final String createTime = performListItem.createTime;// 创建时间
		String ticketCode = performListItem.ticketCode;// 订单号
		final String id = performListItem.id;// 订单Id
		int cPackageType = performListItem.packageType;// 包类型：0：普通包 1：竞价包
		final int cInsuranceType = performListItem.insuranceType;// 0：无保险
																	// 1：平台送保险
																	// 2：自己购买保险

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

		holder.btnState.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据按钮上显示的状态显示不同的界面--所有的参数不为空才能传过去跳转
				if (!TextUtils.isEmpty(id) && id != null
						&& !TextUtils.isEmpty(ticketStatus)
						&& ticketStatus != null
						&& !TextUtils.isEmpty(packageBeginName)
						&& packageBeginName != null
						&& !TextUtils.isEmpty(packageEndName)
						&& packageEndName != null
						&& !TextUtils.isEmpty(createTime) && createTime != null) {
					UploadAndModifyDocumentsActivity.invoke(mContext, id,
							ticketStatus, packageBeginName, packageEndName,
							createTime, cInsuranceType);
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
					.findViewById(R.id.tv_current_ordernumber);
			tvDate = (TextView) convertView.findViewById(R.id.tv_current_date);
			tvLicensehead = (TextView) convertView
					.findViewById(R.id.tv_current_licensehead);
			tvLicensenum = (TextView) convertView
					.findViewById(R.id.tv_current_licensenum);
			tvPrice = (TextView) convertView
					.findViewById(R.id.tv_current_price);
			tvProvenance = (TextView) convertView
					.findViewById(R.id.tv_current_provenance);
			tvDestination = (TextView) convertView
					.findViewById(R.id.tv_current_destination);
			tvName = (TextView) convertView.findViewById(R.id.tv_current_name);
			tvPhone = (TextView) convertView
					.findViewById(R.id.tv_current_phone);
			btnState = (Button) convertView
					.findViewById(R.id.btn_current_state);
			ivVehicleBg = (ImageView) convertView
					.findViewById(R.id.iv_current_vehiclebg);

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
