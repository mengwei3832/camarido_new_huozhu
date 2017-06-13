package com.yunqi.clientandroid.employer.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.EmployerAddressInfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Description:地址管理适配器
 * @ClassName: EmployerAddressListAdapter
 * @author: chengtao
 * @date: 2016年6月15日 下午5:38:50
 * 
 */
public class EmployerAddressListAdapter extends BaseAdapter {
	private List<EmployerAddressInfo> mList;
	private LayoutInflater inflater;

	public EmployerAddressListAdapter(Context context,
			List<EmployerAddressInfo> mList) {
		super();
		this.mList = mList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EmployerAddressInfo info = mList.get(position);
		String provicename = info.provicename;
		String cityname = info.cityname;
		String areaname = info.areaname;
		String AddressName = info.AddressName;
		String Addressdetail = info.Addressdetail;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.employer_adapter_address_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tvShengShiAddress = (TextView) convertView
					.findViewById(R.id.tv_sheng_shi_address);
			viewHolder.tvDetailAddress = (TextView) convertView
					.findViewById(R.id.tv_detail_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (provicename != null && !TextUtils.isEmpty(provicename)) {
			viewHolder.tvShengShiAddress.setText(provicename);
		}
		if (cityname != null && !TextUtils.isEmpty(cityname)) {
			viewHolder.tvShengShiAddress.setText(viewHolder.tvShengShiAddress
					.getText().toString() + cityname);
		}
		if (areaname != null && !TextUtils.isEmpty(areaname)) {
			viewHolder.tvShengShiAddress.setText(viewHolder.tvShengShiAddress
					.getText().toString() + areaname);
		}
		if (AddressName != null && !TextUtils.isEmpty(AddressName)) {
			viewHolder.tvShengShiAddress.setText(viewHolder.tvShengShiAddress
					.getText().toString() + AddressName);
		}
		if (Addressdetail != null && !TextUtils.isEmpty(Addressdetail)) {
			viewHolder.tvDetailAddress.setText(Addressdetail);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvShengShiAddress;
		TextView tvDetailAddress;
	}
}
