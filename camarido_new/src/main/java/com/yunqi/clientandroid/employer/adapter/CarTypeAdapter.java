package com.yunqi.clientandroid.employer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.CarType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @Description:车型适配器
 * @ClassName: CarTypeAdapter
 * @author: mengwei
 * @date: 2016-6-24 上午10:34:57
 * 
 */
public class CarTypeAdapter extends BaseAdapter {
	// 用来控制CheckBox的选中状况
	private ArrayList<String> carIdList;
	private List<CarType> carList;
	private Context context;
	private Map<String, Boolean> carTypeItem;

	@SuppressLint("UseSparseArrays")
	public CarTypeAdapter(List<CarType> carList, Context context,
			ArrayList<String> carIdList) {
		super();
		this.carList = carList;
		this.context = context;
		this.carIdList = carIdList;
		carIdList = new ArrayList<String>();
		carTypeItem = new HashMap<String, Boolean>();
	}

	@Override
	public int getCount() {
		return carList != null ? carList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return carList != null ? carList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return carList != null ? position : 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_item_cartype_listview, null);
			vh.cbCarCheck = (CheckBox) convertView
					.findViewById(R.id.cb_cartype_item);
			vh.tvCarType = (TextView) convertView
					.findViewById(R.id.tv_cartype_model);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final CarType carType = carList.get(position);
		final String carTypeName = carType.enumName;
		final String id = carType.id;
		if (carIdList.contains(id)) {
			vh.cbCarCheck.setChecked(true);
			carTypeItem.put(String.valueOf(position), true);
		} else {
			vh.cbCarCheck.setChecked(false);
			carTypeItem.put(String.valueOf(position), false);
		}
		vh.tvCarType.setText(carTypeName);

		return convertView;
	}

	public static class ViewHolder {
		public CheckBox cbCarCheck;
		public TextView tvCarType;
	}

	public Map<String, Boolean> getCarTypeItem() {
		return carTypeItem;
	}

}
