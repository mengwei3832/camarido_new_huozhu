package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JiaGeAdapter extends BaseAdapter {
	private List<ZiXunLieBiaoBean> ticketList;
	private Context context;
	private LayoutInflater layoutInflater;
	private int[] imgs = { R.drawable.huowu, R.drawable.zixun };

	public JiaGeAdapter(List<ZiXunLieBiaoBean> ticketList, Context context) {
		super();
		this.ticketList = ticketList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
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

			convertView = layoutInflater.inflate(
					R.layout.employer_item_fragment_discover, null);

			viewHolder.ivItemImg = (ImageView) convertView
					.findViewById(R.id.iv_item_discover_image);
			viewHolder.tvItemName = (TextView) convertView
					.findViewById(R.id.tv_item_discover_name);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ZiXunLieBiaoBean ziXunLieBiaoBean = ticketList.get(position);

		viewHolder.tvItemName.setText(ziXunLieBiaoBean.tagName);
		viewHolder.ivItemImg.setImageResource(imgs[position]);

		return convertView;
	}

	class ViewHolder {
		ImageView ivItemImg;
		TextView tvItemName;
	}

}
