package com.yunqi.clientandroid.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
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
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 活动列表页的adapter
 * @date 15/11/28
 */
public class ActiveAdapter extends ArrayAdapter<Message> {

	private Context mContext;
	private ArrayList<Message> mList;
	private DisplayImageOptions option;

	public ActiveAdapter(Context context, ArrayList<Message> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		option = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.active_image)
				.showImageForEmptyUri(R.drawable.active_image)
				.showImageOnFail(R.drawable.active_image).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ActiveViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ActiveViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_active, null);
			viewHolder.ivImage = (ImageView) convertView
					.findViewById(R.id.iv_active_image);

			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_active_time);

			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_active_title);

			viewHolder.tvDigst = (TextView) convertView
					.findViewById(R.id.tv_active_digst);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ActiveViewHolder) convertView.getTag();
		}

		// ActiveViewHolder viewHolder =
		// ActiveViewHolder.getHolder(convertView);

		Message message = mList.get(position);
		String messageShowTimeBegin = message.messageShowTimeBegin;
		String messageTitle = message.messageTitle;
		String messageAbstract = message.messageAbstract;
		String messageTitleImgUrl = message.messageTitleImgUrl;

		if (!TextUtils.isEmpty(messageShowTimeBegin)
				&& messageShowTimeBegin != null) {
			viewHolder.tvTime.setText(StringUtils
					.getMsgDate(messageShowTimeBegin));
		}

		if (!TextUtils.isEmpty(messageTitle) && messageTitle != null) {
			viewHolder.tvTitle.setText(Html.fromHtml(messageTitle));
		}

		if (!TextUtils.isEmpty(messageAbstract) && messageAbstract != null) {
			viewHolder.tvDigst.setText(Html.fromHtml(messageAbstract));
		}

		if (!TextUtils.isEmpty(messageTitleImgUrl)
				&& messageTitleImgUrl != null) {
			ImageLoader.getInstance().displayImage(messageTitleImgUrl,
					viewHolder.ivImage, option);
		}
		// else {
		// viewHolder.ivImage.setImageResource(R.drawable.active_image);
		// }

		return convertView;
	}

	static class ActiveViewHolder {
		ImageView ivImage;
		TextView tvTime;
		TextView tvTitle;
		TextView tvDigst;

		// public ActiveViewHolder(View convertView) {
		// ivImage = (ImageView) convertView
		// .findViewById(R.id.iv_active_image);
		//
		// tvTime = (TextView) convertView.findViewById(R.id.tv_active_time);
		//
		// tvTitle = (TextView) convertView.findViewById(R.id.tv_active_title);
		//
		// tvDigst = (TextView) convertView.findViewById(R.id.tv_active_digst);
		// }

		// public static ActiveViewHolder getHolder(View convertView) {
		// ActiveViewHolder holder = (ActiveViewHolder) convertView.getTag();
		// if (holder == null) {
		// holder = new ActiveViewHolder(convertView);
		// convertView.setTag(holder);
		// }
		// return holder;
		// }

	}

}
