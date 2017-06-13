package com.yunqi.clientandroid.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.PackagePath;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单简单列表Adapter
 * @date 15/11/28
 */
public class OrderSimpleListAdapter extends ArrayAdapter<PackagePath> {

	private Context mContext;
	private List<PackagePath> mList;

	public OrderSimpleListAdapter(Context context, List<PackagePath> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PackagePath packagePath = mList.get(position);
		String orderCount = packagePath.orderCount;
		String packageCount = packagePath.packageCount;
		String packageDistance = packagePath.packageDistance;
		String packageBeginProvinceText = packagePath.packageBeginProvinceText;
		String packageEndProvinceText = packagePath.packageEndProvinceText;
		String packageBeginCityText = packagePath.packageBeginCityText;
		String packageEndCityText = packagePath.packageEndCityText;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_order_simple_list, null);

			viewHolder.tvOrderSimpleCount = (TextView) convertView
					.findViewById(R.id.tv_order_simple_count);
			viewHolder.tvOrderAllPath = (TextView) convertView
					.findViewById(R.id.tv_order_all_path);
			viewHolder.tvStartProvince = (TextView) convertView
					.findViewById(R.id.tv_start_province);
			viewHolder.tvEndProvince = (TextView) convertView
					.findViewById(R.id.tv_end_province);
			viewHolder.tvStartCity = (TextView) convertView
					.findViewById(R.id.tv_start_city);
			viewHolder.tvEndCity = (TextView) convertView
					.findViewById(R.id.tv_end_city);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(orderCount) && orderCount != null
				&& !TextUtils.isEmpty(packageCount) && packageCount != null) {
			viewHolder.tvOrderSimpleCount.setText(Html
					.fromHtml("<font color='#ff4400'>" + orderCount + "/"
							+ packageCount + "</font>" + "单"));
		}

		if (!TextUtils.isEmpty(packageDistance) && packageDistance != null) {
			viewHolder.tvOrderAllPath.setText("全程" + packageDistance + "公里");
		}

		if (!TextUtils.isEmpty(packageBeginProvinceText)
				&& packageBeginProvinceText != null) {
			viewHolder.tvStartProvince.setText(packageBeginProvinceText);
		}

		if (!TextUtils.isEmpty(packageEndProvinceText)
				&& packageEndProvinceText != null) {
			viewHolder.tvEndProvince.setText(packageEndProvinceText);
		}

		if (!TextUtils.isEmpty(packageBeginCityText)
				&& packageBeginCityText != null) {
			viewHolder.tvStartCity.setText(packageBeginCityText);
		}

		if (!TextUtils.isEmpty(packageEndCityText)
				&& packageEndCityText != null) {
			viewHolder.tvEndCity.setText(packageEndCityText);
		}
		return convertView;
	}

	class ViewHolder {

		private TextView tvOrderSimpleCount;
		private TextView tvOrderAllPath;
		private TextView tvStartProvince;
		private TextView tvEndProvince;
		private TextView tvStartCity;
		private TextView tvEndCity;
	}

}
