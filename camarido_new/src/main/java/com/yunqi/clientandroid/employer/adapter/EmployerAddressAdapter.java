package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.GetProvince;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:省市区列表的适配器
 * @ClassName: EmployerAddressAdapter
 * @author: mengwei
 * @date: 2016-6-16 下午8:46:10
 * 
 */
public class EmployerAddressAdapter extends BaseAdapter {
	private Context mContext;
	private List<GetProvince> provinceList;

	public EmployerAddressAdapter(Context mContext,
			List<GetProvince> provinceList) {
		super();
		this.mContext = mContext;
		this.provinceList = provinceList;
	}

	@Override
	public int getCount() {
		return provinceList != null ? provinceList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return provinceList != null ? provinceList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return provinceList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.employer_adapter_province, null);

			vh.tvProvince = (TextView) convertView
					.findViewById(R.id.tv_province);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		GetProvince mGetProvince = provinceList.get(position);

		int id = mGetProvince.id;
		String name = mGetProvince.name;

		vh.tvProvince.setText(name);

		return convertView;
	}

	class ViewHolder {
		TextView tvProvince;
	}

}
