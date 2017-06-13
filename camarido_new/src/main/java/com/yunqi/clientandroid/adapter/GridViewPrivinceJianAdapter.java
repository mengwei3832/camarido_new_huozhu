package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.GetProvinceJian;

/**
 * @Description:省份简称的适配器
 * @ClassName: GridViewPrivinceJianAdapter
 * @author: zhm
 * @date: 2016-3-24 下午5:11:40
 * 
 */
public class GridViewPrivinceJianAdapter extends BaseAdapter {

	private List<GetProvinceJian> list;
	private Context context;

	public GridViewPrivinceJianAdapter(List<GetProvinceJian> list,
			Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return list != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder h;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.pass_input_keyboard, null);
			h.tv = (TextView) convertView.findViewById(R.id.grid_ada_tv);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		GetProvinceJian mGetProvinceJian = list.get(position);

		h.tv.setText(mGetProvinceJian.ShortName);
		h.tv.setBackgroundResource(R.drawable.bg_blue);
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

}
