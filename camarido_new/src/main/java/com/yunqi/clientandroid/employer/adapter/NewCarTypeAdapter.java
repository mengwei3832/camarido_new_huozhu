package com.yunqi.clientandroid.employer.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.CarType;

/**
 * 
 * @Description:车型弹窗适配器
 * @ClassName: NewCarTypeAdapter
 * @author: chengtao
 * @date: 2016年6月22日 下午4:33:44
 * 
 */
@SuppressLint("InflateParams")
public class NewCarTypeAdapter extends BaseAdapter implements
		OnItemClickListener {
	private List<CarType> list;
	private LayoutInflater inflater;
	private List<Integer> carIdList;// 存放被点击的车辆ID
	private Map<String, Boolean> checkedItems = new HashMap<String, Boolean>();// 存放ListView的item是否点击过

	public NewCarTypeAdapter(Context context, List<CarType> list,
			List<Integer> carIdList) {
		super();
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.carIdList = carIdList;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = inflater.inflate(R.layout.car_type_item, null);
			viewHolder.cbCarType = (CheckBox) convertView
					.findViewById(R.id.cb_car_type);
			viewHolder.tvCarType = (TextView) convertView
					.findViewById(R.id.tv_car_type);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CarType item = list.get(position);
		viewHolder.tvCarType.setText(item.enumName);
		if (carIdList.contains(item.id)) {
			viewHolder.cbCarType.setChecked(true);
			checkedItems.put(String.valueOf(position), true);
		} else {
			viewHolder.cbCarType.setChecked(false);
			checkedItems.put(String.valueOf(position), false);
		}
		Log.v("TAG", "getView");
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (checkedItems.get(String.valueOf(position))) {
			carIdList.remove(list.get(position).id);
			checkedItems.put(String.valueOf(position), false);
		} else {
			// carIdList.add(list.get(position).id);
			checkedItems.put(String.valueOf(position), true);
		}
		Log.v("TAG", "onItemClick");
		NewCarTypeAdapter.this.notifyDataSetChanged();
	}

	class ViewHolder {
		CheckBox cbCarType;
		TextView tvCarType;
	}

	public List<CarType> getList() {
		return list;
	}

	public List<Integer> getCarIdList() {
		return carIdList;
	}

}
