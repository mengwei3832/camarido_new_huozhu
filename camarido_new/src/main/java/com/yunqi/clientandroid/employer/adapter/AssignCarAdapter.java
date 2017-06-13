package com.yunqi.clientandroid.employer.adapter;

import java.util.HashMap;
import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @Description: 指派车辆适配器
 * @ClassName: AssignCarAdapter
 * @author: zhm
 * @date: 2016-5-23 下午2:36:14
 * 
 */
public class AssignCarAdapter extends BaseAdapter {
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	// 上下文
	private Context mContext;
	// 用来导入布局
	private LayoutInflater inflater = null;
	private List<CarListZhipai> carList;

	public AssignCarAdapter(Context context, List<CarListZhipai> carList) {
		super();
		this.mContext = context;
		this.carList = carList;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据
		initDate();
	}

	private void initDate() {
		for (int i = 0; i < carList.size(); i++) {
			getIsSelected().put(i, false);
		}
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
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.employer_activity_assign_carlist_item, null);
			viewHolder.tvAssignCarPai = (TextView) convertView
					.findViewById(R.id.tv_assign_carPai);
			viewHolder.tvAssignCarName = (TextView) convertView
					.findViewById(R.id.tv_assign_carName);
			viewHolder.tvAssignCarNumber = (TextView) convertView
					.findViewById(R.id.tv_assign_carNumber);
			viewHolder.cbAssignCheck = (CheckBox) convertView
					.findViewById(R.id.cb_assign_carCheck);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CarListZhipai mCarListZhipai = carList.get(position);

		// 获取详细数据
		final int mVehicleId = mCarListZhipai.VehicleId; // 车ID
		final int mVehicleOwnerId = mCarListZhipai.VehicleOwnerId; // 车主ID
		final String mVehicleNo = mCarListZhipai.VehicleNo; // 车牌号
		int mOrderCount = mCarListZhipai.OrderCount; // 该车在该公司承运次数
		String mVehicleOwnerName = mCarListZhipai.VehicleOwnerName; // 车主名称

		viewHolder.cbAssignCheck.setChecked(getIsSelected().get(position));
		viewHolder.tvAssignCarPai.setText(mVehicleNo);
		viewHolder.tvAssignCarName.setText(mVehicleOwnerName);
		viewHolder.tvAssignCarNumber.setText(mOrderCount + "次");

		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		AssignCarAdapter.isSelected = isSelected;
	}

	public static class ViewHolder {
		public TextView tvAssignCarPai;
		public TextView tvAssignCarName;
		public TextView tvAssignCarNumber;
		public CheckBox cbAssignCheck;
	}

}
