package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:分享预览适配器
 * @ClassName: SharePreViewAdapter
 * @author: mengwei
 * @date: 2016-6-28 下午3:21:25
 * 
 */
public class SharePreViewAdapter extends BaseAdapter {
	private List<TicketCurrentBean> ticketList;
	private Context context;

	public SharePreViewAdapter(List<TicketCurrentBean> ticketList,
			Context context) {
		super();
		this.ticketList = ticketList;
		this.context = context;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_item_share_preview, null);

			viewHolder.tvShareVehicleNo = (TextView) convertView
					.findViewById(R.id.tv_share_vehicleno);
			viewHolder.tvShareKuangFa = (TextView) convertView
					.findViewById(R.id.tv_share_kuangfa);
			viewHolder.tvShareQianShou = (TextView) convertView
					.findViewById(R.id.tv_share_qianshou);
			viewHolder.tvShareShiJie = (TextView) convertView
					.findViewById(R.id.tv_share_shijie);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TicketCurrentBean ticketCurrentBean = ticketList.get(position);

		String vehicleNo = ticketCurrentBean.VehicleNo;
		String actualSettleMent = ticketCurrentBean.ActualSettleMent;// 实结运费
		String ticketWeightInit = ticketCurrentBean.TicketWeightInit;// 矿发吨数
		String ticketWeightReach = ticketCurrentBean.TicketWeightReach;// 签收吨数

		viewHolder.tvShareVehicleNo.setText(vehicleNo);
		if (StringUtils.isStrNotNull(ticketWeightInit)) {
			viewHolder.tvShareKuangFa.setText(ticketWeightInit + "吨");
		} else {
			viewHolder.tvShareKuangFa.setText("0.00吨");
		}

		if (StringUtils.isStrNotNull(ticketWeightReach)) {
			viewHolder.tvShareQianShou.setText(ticketWeightReach + "吨");
		} else {
			viewHolder.tvShareQianShou.setText("0.00吨");
		}

		if (StringUtils.isStrNotNull(actualSettleMent)) {
			viewHolder.tvShareShiJie.setText(actualSettleMent + "元");
		} else {
			viewHolder.tvShareShiJie.setText("0.00元");
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvShareVehicleNo;
		TextView tvShareKuangFa;
		TextView tvShareQianShou;
		TextView tvShareShiJie;
	}

}
