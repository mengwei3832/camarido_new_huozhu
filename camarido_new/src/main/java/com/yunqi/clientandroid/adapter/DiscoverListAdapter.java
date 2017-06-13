package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DiscoverListItem;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发现列表的adapter
 * @date 15/11/24
 */
public class DiscoverListAdapter extends ArrayAdapter<DiscoverListItem> {

	private Context mContext;
	private List<DiscoverListItem> mDiscoverList;
	private DisplayImageOptions option;

	public DiscoverListAdapter(Context context,
			List<DiscoverListItem> discoverList) {
		super(context, 0, discoverList);

		this.mContext = context;
		this.mDiscoverList = discoverList;
		option = new DisplayImageOptions.Builder().cacheInMemory(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DiscoverListItem discoverListItem = mDiscoverList.get(position);
		String imgUrl = discoverListItem.imgUrl;
		String tagName = discoverListItem.tagName;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_discover_list, null);
			viewHolder.ivImage = (ImageView) convertView
					.findViewById(R.id.iv_discover_image);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tv_discover_name);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(imgUrl) && imgUrl != null) {
			ImageLoader.getInstance().displayImage(imgUrl, viewHolder.ivImage,
					option);
		}
		if (!TextUtils.isEmpty(tagName) && tagName != null) {
			viewHolder.tvName.setText(tagName);
		}

		return convertView;
	}

	private class ViewHolder {
		private ImageView ivImage;
		private TextView tvName;

	}
}
