package com.yunqi.clientandroid.umeng.share;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.ShortMessage;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享的adapter
 * @date 15/11/28
 */
public class ShareAdapter extends ArrayAdapter<ShareInfo> {

	private Context mContext;
	private List<ShareInfo> mList;

	public ShareAdapter(Context context, List<ShareInfo> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ShareInfo shareInfo = mList.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_share, null);
			viewHolder.ivShareIcon = (ImageView) convertView
					.findViewById(R.id.iv_share_icon);
			viewHolder.tvShareText = (TextView) convertView
					.findViewById(R.id.tv_share_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ivShareIcon.setImageResource(shareInfo.shareIcon);
		viewHolder.tvShareText.setText(shareInfo.shareText);
		return convertView;
	}

	class ViewHolder {
		private ImageView ivShareIcon;
		private TextView tvShareText;
	}

}
