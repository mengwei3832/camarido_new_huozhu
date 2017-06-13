package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.VehicleType;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 银行列表的adapter
 * @date 15/11/24
 */
public class BankListAdapter extends ArrayAdapter<VehicleType> {

	private Context mContext;
	private List<VehicleType> mBankList;

	public BankListAdapter(Context context, List<VehicleType> mineList) {
		super(context, 0, mineList);

		this.mContext = context;
		this.mBankList = mineList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VehicleType bank = mBankList.get(position);
		String v = bank.v;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_bank_list, null);
			viewHolder.tvItem = (TextView) convertView
					.findViewById(R.id.tv_item);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(v) && v != null) {
			viewHolder.tvItem.setText(v);
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tvItem;

	}
}
