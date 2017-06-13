package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.yunqi.clientandroid.R;

public class KeyboardAdapter extends BaseAdapter {
	private List<String> keyList;
	private LayoutInflater layoutInflater;

	public KeyboardAdapter(List<String> keyList, Context context) {
		super();
		this.keyList = keyList;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return keyList.size();
	}

	@Override
	public Object getItem(int position) {
		return keyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String str = keyList.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.employer_adapter_passord_btn_key, parent, false);
			viewHolder.btnKey = (Button) convertView.findViewById(R.id.btn_key);
			viewHolder.ibtnKey = (ImageButton) convertView
					.findViewById(R.id.ibtn_key);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!str.equals("×")) {
			if (!str.equals("完成")) {
				viewHolder.btnKey.setText(str);
				viewHolder.ibtnKey.setVisibility(View.GONE);
			} else {
				viewHolder.ibtnKey.setVisibility(View.GONE);
			}
		} else {
			viewHolder.btnKey.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		Button btnKey;
		ImageButton ibtnKey;
	}
}
