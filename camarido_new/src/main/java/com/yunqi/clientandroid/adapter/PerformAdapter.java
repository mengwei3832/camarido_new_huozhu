package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.Random;

import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.CustomDigitalClock;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 待执行运单适配器
 * @date 15/11/28
 */
public class PerformAdapter extends ArrayAdapter<PerformListItem> {

	private Context mContext;
	private List<PerformListItem> mList;
	private PerformListItemOnClick callBack;

	public PerformAdapter(Context context, List<PerformListItem> list,
			PerformListItemOnClick callBack) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.callBack = callBack;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_ticket_list_zhixing, null);
		}

		PerformAdapterListViewHolder holder = PerformAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		PerformListItem performListItem = mList.get(position);
		String vehicleNo = performListItem.vehicleNo;// 车牌号码
		String packageBeginName = performListItem.packageBeginName;// 出发地名称
		String packageEndName = performListItem.packageEndName;// 目的地名称
		String packageBeginAdress = performListItem.packageBeginAddress;// 出发地地址
		String packageEndAdress = performListItem.packageEndAddress; // 目的地地址
		String packagePrice = performListItem.packagePrice;// 运价
		String name = performListItem.name;// 执行人姓名
		String userPhone = performListItem.userPhone;// 执行人电话
		final String ticketStatus = performListItem.ticketStatus;// 执行状态
		String createTime = performListItem.createTime;// 创建时间
		String ticketCode = performListItem.ticketCode;// 订单号
		String packageAutoSecond = performListItem.packageAutoSecond;// 剩余时间，竞价包的订单用到这个字段
		final String id = performListItem.id;// 订单Id
		int qPackageType = performListItem.packageType;// 包类型：0：普通包 1：竞价包

		Log.e("TAG", "包的类型----------" + qPackageType);

		// 车牌随机背景颜色
		// Random random = new Random();
		// int nextInt = random.nextInt(6);
		holder.ivVehicleBg.setImageResource(R.drawable.chepai_bg);
		// ColorUtil.changeImageViewStyle(nextInt, holder.ivVehicleBg);

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

		final int posi = position;
		final View view = convertView;

		// 点击待执行按钮
		holder.btnState.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据按钮上显示的状态显示不同的界面
				if (!TextUtils.isEmpty(id) && id != null) {
					callBack.onClick(view, posi, id);
				}

			}
		});

		// 根据执行状态显示
		if (ticketStatus != null && ticketStatus.equals("0")) {
			// 显示待生效订单的剩余时间，竞价包的订单用到这个字段
			if (!TextUtils.isEmpty(packageAutoSecond)
					&& packageAutoSecond != null) {
				long packageAutoSecondLong = Long.parseLong(packageAutoSecond);
				if (packageAutoSecondLong > 0) {
					holder.btnState.setEnabled(false);
					holder.btnState.setVisibility(View.INVISIBLE);
					holder.cdcRemainTime.setVisibility(View.VISIBLE);

					holder.cdcRemainTime.setEndTime(System.currentTimeMillis()
							+ packageAutoSecondLong * 1000);
				} else {
					holder.btnState.setText("正在处理");
					holder.btnState.setEnabled(false);
					holder.btnState.setVisibility(View.VISIBLE);
					holder.cdcRemainTime.setVisibility(View.INVISIBLE);

				}
			}

			holder.cdcRemainTime
					.setClockListener(new CustomDigitalClock.ClockListener() {
						@Override
						public void timeEnd() {
							// TODO Auto-generated method stub
						}

						@Override
						public void remainFiveMinutes() {
							// TODO Auto-generated method stub
						}
					});
		} else if (ticketStatus != null && ticketStatus.equals("1")) {
			holder.btnState.setText("待执行");
			holder.btnState.setEnabled(true);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("2")) {
			holder.btnState.setText("待换票");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			holder.btnState.setText("待装运");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			holder.btnState.setText("待收货");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("5")) {
			holder.btnState.setText("待审核");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			holder.btnState.setText("已结算");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("6")) {
			holder.btnState.setText("待结算");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("7")) {
			holder.btnState.setText("可领取");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("9")) {
			holder.btnState.setText("禁用");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		} else if (ticketStatus != null && ticketStatus.equals("10")) {
			holder.btnState.setText("取消");
			holder.btnState.setEnabled(false);
			holder.btnState.setVisibility(View.VISIBLE);
			holder.cdcRemainTime.setVisibility(View.INVISIBLE);
		}

		return convertView;

	}

	static class PerformAdapterListViewHolder {
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
		CustomDigitalClock cdcRemainTime;
		ImageView ivVehicleBg;

		public PerformAdapterListViewHolder(View convertView) {
			tvOrdernumber = (TextView) convertView
					.findViewById(R.id.tv_perform_ordernumber);
			tvDate = (TextView) convertView.findViewById(R.id.tv_perform_date);
			tvLicensehead = (TextView) convertView
					.findViewById(R.id.tv_perform_licensehead);
			tvLicensenum = (TextView) convertView
					.findViewById(R.id.tv_perform_licensenum);
			tvPrice = (TextView) convertView
					.findViewById(R.id.tv_perform_price);
			tvProvenance = (TextView) convertView
					.findViewById(R.id.tv_perform_provenance);
			tvDestination = (TextView) convertView
					.findViewById(R.id.tv_perform_destination);
			tvName = (TextView) convertView.findViewById(R.id.tv_perform_name);
			tvPhone = (TextView) convertView
					.findViewById(R.id.tv_perform_phone);
			btnState = (Button) convertView
					.findViewById(R.id.btn_perform_state);
			cdcRemainTime = (CustomDigitalClock) convertView
					.findViewById(R.id.cdc_perform_remainTime);
			ivVehicleBg = (ImageView) convertView
					.findViewById(R.id.iv_perform_vehiclebg);
		}

		public static PerformAdapterListViewHolder getHolder(View convertView) {
			PerformAdapterListViewHolder holder = (PerformAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new PerformAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
