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
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注列表页的adapter
 * @date 15/11/28
 */
public class SearchAdapter extends ArrayAdapter<Message> {

	private Context mContext;
	private List<Message> mList;

	public SearchAdapter(Context context, List<Message> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Message message = mList.get(position);
		String messageShowTimeBegin = message.messageShowTimeBegin;
		String messageAuthor = message.messageAuthor;
		String messageAbstract = message.messageAbstract;
		int messageReadCount = message.messageReadCount;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_search, null);

			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_search_time);
			viewHolder.tvAuthor = (TextView) convertView
					.findViewById(R.id.tv_search_author);
			viewHolder.tvDigst = (TextView) convertView
					.findViewById(R.id.tv_search_digst);
			viewHolder.tvReadCount = (TextView) convertView
					.findViewById(R.id.tv_search_read_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(messageShowTimeBegin)
				&& messageShowTimeBegin != null) {
			viewHolder.tvTime.setText(StringUtils
					.getMsgDate(messageShowTimeBegin));
		}

		if (!TextUtils.isEmpty(messageAuthor) && messageAuthor != null) {
			viewHolder.tvAuthor.setText(messageAuthor);
		}

		if (!TextUtils.isEmpty(messageAbstract) && messageAbstract != null) {
			viewHolder.tvDigst.setText(Html.fromHtml(messageAbstract));
		}

		if (!TextUtils.isEmpty(messageReadCount + "") && messageReadCount >= 0) {
			viewHolder.tvReadCount.setText(Html
					.fromHtml("<font color='#ff4400'>" + messageReadCount
							+ "</font>" + "阅"));
		}

		return convertView;
	}

	class ViewHolder {

		private TextView tvTime;
		private TextView tvAuthor;
		private TextView tvReadCount;
		private TextView tvDigst;
	}

}
