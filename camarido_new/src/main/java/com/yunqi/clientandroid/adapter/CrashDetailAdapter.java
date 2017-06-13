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
import com.yunqi.clientandroid.entity.CrashDetail;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的钱包明细的adapter
 * @date 15/11/28
 */
public class CrashDetailAdapter extends ArrayAdapter<CrashDetail> {

	private Context mContext;
	private List<CrashDetail> mList;

	public CrashDetailAdapter(Context context, List<CrashDetail> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CrashDetail crashDetail = mList.get(position);
		String codeDescr = crashDetail.codeDescr;
		String typeDescr = crashDetail.typeDescr;
		String createTime = crashDetail.createTime;
		String amount = crashDetail.amount;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_crashdetail, null);
			viewHolder.tvCode = (TextView) convertView
					.findViewById(R.id.tv_code);
			viewHolder.tvType = (TextView) convertView
					.findViewById(R.id.tv_type);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tvCount = (TextView) convertView
					.findViewById(R.id.tv_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(codeDescr) && codeDescr != null) {
			viewHolder.tvCode.setText(codeDescr);
		}

		if (!TextUtils.isEmpty(typeDescr) && typeDescr != null) {
			viewHolder.tvType.setText(typeDescr);
		}

		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			viewHolder.tvTime.setText(StringUtils.getMsgDate(createTime));
		}

		if (!TextUtils.isEmpty(amount) && amount != null) {
			double parseDouble = Double.parseDouble(amount);
			if (parseDouble >= 0) {
				viewHolder.tvCount
						.setText(Html.fromHtml("<font color='#4dee4e'>"
								+ amount + "</font>"));
			} else {
				viewHolder.tvCount
						.setText(Html.fromHtml("<font color='#ed6139'>"
								+ amount + "</font>"));
			}

		}

		return convertView;
	}

	class ViewHolder {

		private TextView tvCode;
		private TextView tvType;
		private TextView tvTime;
		private TextView tvCount;
	}

}
