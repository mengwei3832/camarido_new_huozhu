package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.ShipHeighWay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShipAddressAdapter extends BaseAdapter {
	private List<ShipHeighWay> shiplist;
	private Context context;
	private LayoutInflater layoutInflater;

	public ShipAddressAdapter(List<ShipHeighWay> shiplist, Context context) {
		super();
		this.shiplist = shiplist;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return shiplist != null ? shiplist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return shiplist != null ? shiplist.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return shiplist != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = layoutInflater.inflate(
					R.layout.employer_item_ship_address, null);
			viewHolder.tvShipName = (TextView) convertView
					.findViewById(R.id.tv_item_ship);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvShipName.setText(shiplist.get(position).PublicPointName);

		return convertView;
	}

	class ViewHolder {
		TextView tvShipName;
	}
}
