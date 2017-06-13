package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:统计数据列表适配器
 * @ClassName: StatisticalDataAdapter
 * @author: mengwei
 * @date: 2016-6-28 下午1:17:56
 * 
 */
public class StatisticalDataAdapter extends BaseAdapter {
	private List<GetBiaoLieBiao> ticketList;
	private Context context;

	public StatisticalDataAdapter(List<GetBiaoLieBiao> ticketList,
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
					R.layout.employer_item_statistical, null);

			viewHolder.tvDataTime = (TextView) convertView
					.findViewById(R.id.tv_item_data_time);
			viewHolder.tvDataBeginCityName = (TextView) convertView
					.findViewById(R.id.tv_item_data_cityName_begin);
			viewHolder.tvDataBeginCompanyName = (TextView) convertView
					.findViewById(R.id.tv_item_data_companyName_begin);
			viewHolder.tvDataEndCityName = (TextView) convertView
					.findViewById(R.id.tv_item_data_cityName_end);
			viewHolder.tvDataEndCompanyName = (TextView) convertView
					.findViewById(R.id.tv_item_data_companyName_end);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		GetBiaoLieBiao getBiaoLieBiao = ticketList.get(position);

		String beginCityName = getBiaoLieBiao.PackageBeginCityText;
		String beginCompanyName = getBiaoLieBiao.PackageBeginName;
		String endCityName = getBiaoLieBiao.PackageEndCityText;
		String endCompanyName = getBiaoLieBiao.PackageEndName;
		String createTime = StringUtils
				.formatModify(getBiaoLieBiao.PackageStartTime);

		viewHolder.tvDataTime.setText(createTime);
		viewHolder.tvDataBeginCityName.setText(beginCityName);
		viewHolder.tvDataBeginCompanyName.setText(beginCompanyName);
		viewHolder.tvDataEndCityName.setText(endCityName);
		viewHolder.tvDataEndCompanyName.setText(endCompanyName);

		return convertView;
	}

	class ViewHolder {
		TextView tvDataTime;
		TextView tvDataBeginCityName;
		TextView tvDataBeginCompanyName;
		TextView tvDataEndCityName;
		TextView tvDataEndCompanyName;
	}

}
