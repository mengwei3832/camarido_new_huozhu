package com.yunqi.clientandroid.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.yunqi.clientandroid.activity.MyTicketActivity;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.StringUtils;

public class HomeCurrentPager extends BasePager {
	private int mPosition;
	private ArrayList<PerformListItem> mPerformListItem;
	private String ticketStatus;

	public HomeCurrentPager(Context context, int position,
			ArrayList<PerformListItem> performListItem) {
		super(context);
		this.mPosition = position;
		this.mPerformListItem = performListItem;

	}

	@Override
	public void initData() {
		PerformListItem performListItem = mPerformListItem.get(mPosition);
		String ticketCode = performListItem.ticketCode;
		String vehicleNo = performListItem.vehicleNo;
		String driverName = performListItem.name;
		String createTime = performListItem.createTime;
		String packageBeginName = performListItem.packageBeginName;
		String packageEndName = performListItem.packageEndName;
		ticketStatus = performListItem.ticketStatus;

		rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ticketStatus != null && ticketStatus.equals("0")
						|| ticketStatus != null && ticketStatus.equals("1")) {
					MyTicketActivity.invoke(mContext, "PERFORM");
				} else if (ticketStatus != null && ticketStatus.equals("2")
						|| ticketStatus != null && ticketStatus.equals("3")
						|| ticketStatus != null && ticketStatus.equals("4")
						|| ticketStatus != null && ticketStatus.equals("5")) {
					MyTicketActivity.invoke(mContext, "CURRENT");
				} else if (ticketStatus != null && ticketStatus.equals("6")
						|| ticketStatus != null && ticketStatus.equals("7")) {
					MyTicketActivity.invoke(mContext, "COMPLETED");
				}
			}
		});

		if (!TextUtils.isEmpty(ticketCode) && ticketCode != null) {
			tvOrderNumber.setText("单号:" + ticketCode);
		}

		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			tvCarNumber.setText("车辆:" + vehicleNo);
		}

		if (!TextUtils.isEmpty(driverName) && driverName != null) {
			tvDriverName.setText("司机:" + driverName);
		}

		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			tvCurrentOrderDate.setText("时间:"
					+ StringUtils.formatDate(createTime));
		}

		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			tvCurrentOrderStart.setText(packageBeginName);
		}

		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			tvCurrentOrderEnd.setText(packageEndName);
		}

		if (!TextUtils.isEmpty(ticketStatus) && ticketStatus != null) {
			int ticketStatusInt = Integer.parseInt(ticketStatus);
			tvCurrentOrderStatus
					.setText(getCurrentOrderStatus(ticketStatusInt));
		}

	}

	/**
	 * 获取当前订单的状态值
	 * 
	 * @param status
	 * @return
	 */
	private String getCurrentOrderStatus(int status) {
		switch (status) {
		case 0:
			return "待生效";
		case 1:
			return "待执行";
		case 2:
			return "待换票";
		case 3:
			return "待装运";
		case 4:
			return "待收货";
		case 5:
			return "待审核";
		case 6:
			return "待结算";
		case 7:
			return "可领取";
		case 8:
			return "已结算";
		case 9:
			return "禁用";
		case 10:
			return "取消";
		default:
			return "待发货";
		}
	}

}
