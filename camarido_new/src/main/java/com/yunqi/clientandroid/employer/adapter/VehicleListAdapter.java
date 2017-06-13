package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.VehicleList;

/**
 * 
 * @Description:车辆列表适配器
 * @ClassName: VehicleListAdapter
 * @author: chengtao
 * @date: 2016年6月17日 上午12:08:50
 * 
 */
@SuppressLint("InflateParams")
public class VehicleListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<VehicleList> mList;
	private OnCarListDelteListener listner;

	public VehicleListAdapter(Context context, List<VehicleList> mList,
			OnCarListDelteListener listner) {
		super();
		this.mList = mList;
		this.listner = listner;
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
		ViewHolder viewHolder = null;
		final int mPos = position;
		VehicleList list = mList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.employer_adpater_carlist,
					null);
			viewHolder = new ViewHolder();
			viewHolder.carNum = (TextView) convertView
					.findViewById(R.id.car_num);
			viewHolder.carHost = (TextView) convertView
					.findViewById(R.id.car_host);
			viewHolder.btnDel = (ImageButton) convertView
					.findViewById(R.id.delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String VehicleOwnerName = list.VehicleOwnerName;
		String VehicleNo = list.VehicleNo;
		if (VehicleNo != null && !TextUtils.isEmpty(VehicleNo)) {
			viewHolder.carNum.setText(VehicleNo);
		}
		if (VehicleOwnerName != null && !TextUtils.isEmpty(VehicleOwnerName)) {
			viewHolder.carHost.setText(VehicleOwnerName);
		}
		viewHolder.btnDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listner.deleteCar(mPos);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView carNum;
		TextView carHost;
		ImageButton btnDel;
	}

	/**
	 * 
	 * @Description:删除车辆的接口
	 * @ClassName: OnCarListDelteListener
	 * @author: chengtao
	 * @date: 2016年6月17日 上午12:29:20
	 * 
	 */
	public interface OnCarListDelteListener {
		void deleteCar(int position);
	}
}
