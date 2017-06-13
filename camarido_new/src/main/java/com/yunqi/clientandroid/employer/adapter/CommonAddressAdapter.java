package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;
import com.yunqi.clientandroid.entity.ChooseAddressItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:常用地址适配器
 * @ClassName: GoodsTypeAdapter
 * @author: mengwei
 * @date: 2016-6-23 上午11:36:42
 * 
 */
public class CommonAddressAdapter extends BaseAdapter {
	private List<ChooseAddressItem> goodsList;
	private Context context;
	private LayoutInflater layoutInflater;

	public CommonAddressAdapter(List<ChooseAddressItem> goodsList,
			Context context) {
		super();
		this.goodsList = goodsList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return goodsList != null ? goodsList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return goodsList != null ? goodsList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return goodsList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = layoutInflater.inflate(
					R.layout.employer_item_common_address, null);

			viewHolder.tvSanName = (TextView) convertView
					.findViewById(R.id.tv_item_common_san);
			viewHolder.tvXiangName = (TextView) convertView
					.findViewById(R.id.tv_item_common_xiang);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String sanName = goodsList.get(position).Provicename
				+ goodsList.get(position).Cityname
				+ goodsList.get(position).Areaname;

		Log.i("TAG", "---------sanName-----------" + sanName);
		viewHolder.tvSanName.setText(sanName);
		viewHolder.tvXiangName.setText(goodsList.get(position).Addressdetail);

		return convertView;
	}

	class ViewHolder {
		TextView tvSanName;
		TextView tvXiangName;
	}

}
