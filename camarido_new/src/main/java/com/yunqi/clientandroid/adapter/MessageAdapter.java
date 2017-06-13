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
import com.yunqi.clientandroid.entity.ShortMessage;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 消息列表页的adapter
 * @date 15/11/28
 */
public class MessageAdapter extends ArrayAdapter<ShortMessage> {

	private Context mContext;
	private List<ShortMessage> mList;

	public MessageAdapter(Context context, List<ShortMessage> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ShortMessage message = mList.get(position);
		String createTime = message.createTime;
		String shortMessageContent = message.shortMessageContent;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_message, null);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_msg_time);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			viewHolder.tvTime.setText(StringUtils.getMsgDate(createTime));
		}

		if (!TextUtils.isEmpty(shortMessageContent)
				&& shortMessageContent != null) {
			viewHolder.tvContent.setText(Html.fromHtml(shortMessageContent));
		}

		return convertView;
	}

	class ViewHolder {

		private TextView tvTime;
		private TextView tvContent;
	}

}
