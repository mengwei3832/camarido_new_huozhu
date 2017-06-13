package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注列表页的adapter
 * @date 15/11/28
 */
public class AttentionAdapter extends ArrayAdapter<Message> {

	private Context mContext;
	private List<Message> mList;

	public AttentionAdapter(Context context, List<Message> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Message message = mList.get(position);
		String messageShowTimeBegin = message.messageShowTimeBegin;
		String messageTag = message.messageTag;
		String messageTitle = message.messageTitle;
		String messageAbstract = message.messageAbstract;
		String messageAuthor = message.messageAuthor;
		int messageReadCount = message.messageReadCount;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_attention, null);

			viewHolder.tvTitleTime = (TextView) convertView
					.findViewById(R.id.tv_attention_msg_time);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_attention_title);
			viewHolder.tvDigst = (TextView) convertView
					.findViewById(R.id.tv_attention_digst);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tv_attention_name);
			viewHolder.tvFirst = (TextView) convertView
					.findViewById(R.id.tv_attention_city_first);
			viewHolder.tvSecond = (TextView) convertView
					.findViewById(R.id.tv_attention_city_second);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_attention_time);
			viewHolder.tvReadCount = (TextView) convertView
					.findViewById(R.id.tv_attention_read_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			viewHolder.tvTitleTime.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(messageShowTimeBegin)
					&& messageShowTimeBegin != null) {
				viewHolder.tvTitleTime.setText(StringUtils
						.getMsgDate(messageShowTimeBegin));
			}
		} else {
			viewHolder.tvTitleTime.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(messageTitle) && messageTitle != null) {
			viewHolder.tvTitle.setText(Html.fromHtml(messageTitle));
		}

		if (!TextUtils.isEmpty(messageAbstract) && messageAbstract != null) {
			viewHolder.tvDigst.setText(Html.fromHtml(messageAbstract));
		}

		if (!TextUtils.isEmpty(messageAuthor) && messageAuthor != null) {
			viewHolder.tvName.setText(messageAuthor);
		}

		if (!TextUtils.isEmpty(messageTag) && messageTag != null) {
			if (messageTag.length() == 1 || messageTag.length() == 2) {
				viewHolder.tvFirst.setVisibility(View.VISIBLE);
				viewHolder.tvSecond.setVisibility(View.GONE);
				viewHolder.tvFirst.setText(messageTag);
			} else if (messageTag.length() == 3) {
				viewHolder.tvFirst.setVisibility(View.VISIBLE);
				viewHolder.tvSecond.setVisibility(View.VISIBLE);
				viewHolder.tvFirst.setText(messageTag.substring(0, 2));
				viewHolder.tvSecond.setText(messageTag.substring(2));

			} else if (messageTag.length() >= 4) {
				viewHolder.tvFirst.setVisibility(View.VISIBLE);
				viewHolder.tvSecond.setVisibility(View.VISIBLE);
				viewHolder.tvFirst.setText(messageTag.substring(0, 2));
				viewHolder.tvSecond.setText(messageTag.substring(2, 4));

			} else if (messageTag.length() == 0) {
				viewHolder.tvFirst.setVisibility(View.VISIBLE);
				viewHolder.tvSecond.setVisibility(View.VISIBLE);
				viewHolder.tvFirst.setText("卡梅");
				viewHolder.tvSecond.setText("利多");
			}
		} else {
			viewHolder.tvFirst.setVisibility(View.VISIBLE);
			viewHolder.tvSecond.setVisibility(View.VISIBLE);
			viewHolder.tvFirst.setText("卡梅");
			viewHolder.tvSecond.setText("利多");
		}

		if (!TextUtils.isEmpty(messageShowTimeBegin)
				&& messageShowTimeBegin != null) {
			viewHolder.tvTime.setText(StringUtils
					.friendly_time(messageShowTimeBegin));
		}

		if (!TextUtils.isEmpty(messageReadCount + "") && messageReadCount >= 0) {
			viewHolder.tvReadCount.setText("阅读 " + messageReadCount + "人");
		}

		return convertView;
	}

	class ViewHolder {

		private TextView tvTitleTime;
		private TextView tvTitle;
		private TextView tvDigst;
		private TextView tvName;
		private TextView tvFirst;
		private TextView tvSecond;
		private TextView tvTime;
		private TextView tvReadCount;
	}

}
