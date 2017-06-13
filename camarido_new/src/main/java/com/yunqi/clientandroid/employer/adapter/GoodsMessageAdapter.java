package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class GoodsMessageAdapter extends BaseAdapter {
	private Context context;
	private List<PackagePickersInfo> cateList;
	private LayoutInflater mInflater;
	private int selectPosition = -1;// 用于记录用户选择的变量

	public GoodsMessageAdapter(Context context,
			List<PackagePickersInfo> cateList) {
		super();
		this.context = context;
		this.cateList = cateList;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cateList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = mInflater.inflate(
					R.layout.employer_item_goods_message, parent, false);
			viewHolder.rbGoods = (RadioButton) convertView
					.findViewById(R.id.rb_goods_message);
			viewHolder.tvGoods = (TextView) convertView
					.findViewById(R.id.tv_goods_message_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvGoods.setText(cateList.get(position).CategoryNum);

		if (selectPosition == position) {
			viewHolder.rbGoods.setChecked(true);
		} else {
			viewHolder.rbGoods.setChecked(false);
		}

		return convertView;
	}

	public class ViewHolder {
		RadioButton rbGoods;
		TextView tvGoods;
	}

}
