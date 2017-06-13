package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.utils.ColorUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 历史订单适配器
 * @date 15/11/28
 */
public class HistoryTicketAdapter extends ArrayAdapter<PerformListItem> {

	private Context mContext;
	private List<PerformListItem> mList;

	public HistoryTicketAdapter(Context context, List<PerformListItem> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_historyticket_list, null);
		}

		HistoryAdapterListViewHolder holder = HistoryAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		PerformListItem performListItem = mList.get(position);
		String vehicleNo = performListItem.vehicleNo;// 车牌号
		String packageBeginName = performListItem.packageBeginName;// 出发地名称
		String packageEndName = performListItem.packageEndName;// 目的地名称
		String name = performListItem.name;// 执行人姓名
		String userPhone = performListItem.userPhone;// 执行人电话
		String ticketStatus = performListItem.ticketStatus;// 执行状态：0：待生效；1：待执行；2：待换票；3：待装运；4：待收货；5：待审核；6：待结算；7：可领取，8：已结算；9：禁用；10：取消；
		String createTime = performListItem.createTime;// 创建时间
		String ticketCode = performListItem.ticketCode;// 订单号
		String freightPayable = performListItem.freightPayable;// 运费
		String subsidy = performListItem.subsidy;// 补助
		String actualSettleMent = performListItem.actualSettleMent;// 实结运费

		// 车牌号随机背景颜色
		Random random = new Random();
		int nextInt = random.nextInt(6);
		ColorUtil.changeImageViewStyle(nextInt, holder.ivVehicelBg);

		// 设置订单创建时间
		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			String times = StringUtils.formatPerform(createTime);
			String date = times.split("年")[1];
			holder.tvDate.setText(date);
		}

		// 设置订单号
		if (!TextUtils.isEmpty(ticketCode) && ticketCode != null) {
			holder.tvOrdernumber.setText("订单号:  " + ticketCode);
		}

		// 设置车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			String licensehead = vehicleNo.substring(0, 2);
			String licensenum = vehicleNo.substring(2);

			holder.tvLicensehead.setText(licensehead);
			holder.tvLicensenum.setText(licensenum);

		}

		// 设置起始地
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			holder.tvProvenance.setText(packageBeginName);
		}

		// 设置目的地
		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			holder.tvDestination.setText(packageEndName);
		}

		// 设置执行人
		if (!TextUtils.isEmpty(name) && name != null) {
			holder.tvName.setText(name);
		}

		// 设置电话号码
		if (!TextUtils.isEmpty(userPhone) && userPhone != null) {
			holder.tvPhone.setText(userPhone);
		}

		// 设置补贴
		if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
			holder.tvSubsidies.setText(Html.fromHtml("补贴"
					+ "<font color='#ff4400'>" + subsidy + "</font>" + "元"));
		}

		// 已完成--已结算
		if (ticketStatus != null && ticketStatus.equals("8")) {
			// 设置运费
			if (!TextUtils.isEmpty(actualSettleMent)
					&& actualSettleMent != null) {
				holder.tvFreight.setText(Html.fromHtml("实结运费:"
						+ "<font color='#ff4400'>" + actualSettleMent
						+ "</font>" + "元"));
			}
			holder.ivState.setImageResource(R.drawable.history_item_completed);
			holder.ivState.setVisibility(View.VISIBLE);
		} else if (ticketStatus != null
				&& (ticketStatus.equals("9") || ticketStatus.equals("10") || ticketStatus
						.equals("11"))) {
			// 已作废--禁用
			// 设置单价
			if (!TextUtils.isEmpty(freightPayable) && freightPayable != null) {
				holder.tvFreight.setText(Html.fromHtml("单价:"
						+ "<font color='#ff4400'>" + freightPayable + "</font>"
						+ "元"));
			}
			holder.ivState.setImageResource(R.drawable.history_item_cancle);
			holder.ivState.setVisibility(View.VISIBLE);
		}

		return convertView;

	}

	static class HistoryAdapterListViewHolder {
		TextView tvDate;
		TextView tvOrdernumber;
		TextView tvLicensehead;
		TextView tvLicensenum;
		TextView tvProvenance;
		TextView tvDestination;
		TextView tvFreight;
		TextView tvSubsidies;
		TextView tvName;
		TextView tvPhone;
		ImageView ivState;
		ImageView ivVehicelBg;

		public HistoryAdapterListViewHolder(View convertView) {
			tvOrdernumber = (TextView) convertView
					.findViewById(R.id.tv_historyticket_ordernumber);
			tvDate = (TextView) convertView
					.findViewById(R.id.tv_historyticket_date);
			tvLicensehead = (TextView) convertView
					.findViewById(R.id.tv_historyticket_licensehead);
			tvLicensenum = (TextView) convertView
					.findViewById(R.id.tv_historyticket_licensenum);
			tvProvenance = (TextView) convertView
					.findViewById(R.id.tv_historyticket_provenance);
			tvDestination = (TextView) convertView
					.findViewById(R.id.tv_historyticket_destination);
			tvFreight = (TextView) convertView
					.findViewById(R.id.tv_historyticket_freight);
			tvSubsidies = (TextView) convertView
					.findViewById(R.id.tv_historyticket_subsidies);
			tvName = (TextView) convertView
					.findViewById(R.id.tv_historyticket_name);
			tvPhone = (TextView) convertView
					.findViewById(R.id.tv_historyticket_phone);
			ivState = (ImageView) convertView
					.findViewById(R.id.iv_historyticket_state);
			ivVehicelBg = (ImageView) convertView
					.findViewById(R.id.iv_historyticket_vehiclebg);

		}

		public static HistoryAdapterListViewHolder getHolder(View convertView) {
			HistoryAdapterListViewHolder holder = (HistoryAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new HistoryAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
