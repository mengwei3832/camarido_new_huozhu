package com.yunqi.clientandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunqi.clientandroid.R;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单筛选的adapter
 * @date 15/11/24
 */
public class OrderFilterCateoryAdapter extends ArrayAdapter<String> {

	private Context mContext;
	private List<String> mFilterList;

	public OrderFilterCateoryAdapter(Context context, List<String> filterlist) {
		super(context, 0, filterlist);

		this.mContext = context;
		this.mFilterList = filterlist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String discoverListItem = mFilterList.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_filter_catory, null);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private class ViewHolder {
		private ImageView ivImage;
		private TextView tvName;

	}
}
