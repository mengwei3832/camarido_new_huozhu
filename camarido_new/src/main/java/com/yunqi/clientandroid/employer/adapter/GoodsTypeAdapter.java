package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:货品种类适配器
 * @ClassName: GoodsTypeAdapter
 * @author: mengwei
 * @date: 2016-6-23 上午11:36:42
 * 
 */
public class GoodsTypeAdapter extends BaseAdapter {
	private List<PackagePickersInfo> goodsList;
	private Context context;
	private LayoutInflater layoutInflater;

	public GoodsTypeAdapter(List<PackagePickersInfo> goodsList, Context context) {
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
					R.layout.employer_pop_item_goods, null);

			viewHolder.tvGoodsName = (TextView) convertView
					.findViewById(R.id.tv_pop_item_goods);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvGoodsName.setText(goodsList.get(position).CategoryName);

		return convertView;
	}

	class ViewHolder {
		TextView tvGoodsName;
	}

}
