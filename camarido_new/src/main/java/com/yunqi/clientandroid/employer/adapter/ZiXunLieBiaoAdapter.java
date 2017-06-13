package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZiXunLieBiaoAdapter extends BaseAdapter {
	private List<ZiXunLieBiaoBean> goodsList;
	private Context context;
	private LayoutInflater layoutInflater;

	public ZiXunLieBiaoAdapter(List<ZiXunLieBiaoBean> goodsList, Context context) {
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
					R.layout.employer_item_zixun_liebiao, null);

			viewHolder.tvLieBiaoName = (TextView) convertView
					.findViewById(R.id.tv_item_zixun_name);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvLieBiaoName.setText(goodsList.get(position).tagName);

		return convertView;
	}

	class ViewHolder {
		TextView tvLieBiaoName;
	}

}
