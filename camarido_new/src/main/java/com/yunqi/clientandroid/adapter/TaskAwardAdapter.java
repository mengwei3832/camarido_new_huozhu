package com.yunqi.clientandroid.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.TaskAward;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Description:class 新手奖励的适配器
 * @ClassName: TaskAwardAdapter
 * @author: zhm
 * @date: 2016-4-6 下午2:10:44
 * 
 */
public class TaskAwardAdapter extends BaseAdapter {
	private List<TaskAward> list;
	private Context context;

	public TaskAwardAdapter(List<TaskAward> list, Context context) {
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
		TaskAward taskAward = list.get(position);

		String tVehicleNum = taskAward.vehicleNum;// 车牌号
		double tRewardRecordMoneyCarOne = taskAward.rewardRecordMoneyCarOne;// 新车奖励
		double tRewardRecordMoneyCarTwo = taskAward.rewardRecordMoneyCarTwo;// 首次承运
		double tRewardRecordMoneyCarThree = taskAward.rewardRecordMoneyCarThree;// 二次承运
		String tEndTime = StringUtils.formatCanReceive(taskAward.endTime);

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.task_listview_item, null);
			vh.task_plateNumber = (TextView) convertView
					.findViewById(R.id.tv_task_list_plateNumber);
			vh.task_addCar = (TextView) convertView
					.findViewById(R.id.tv_task_list_addCar);
			vh.task_firstTransport = (TextView) convertView
					.findViewById(R.id.tv_task_list_firstTransport);
			vh.task_twoTransport = (TextView) convertView
					.findViewById(R.id.tv_task_list_twoTransport);
			vh.task_date = (TextView) convertView
					.findViewById(R.id.tv_task_list_date);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.parseColor("#8c40f0"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#742add"));
		}

		if (!TextUtils.isEmpty(tVehicleNum) && tVehicleNum != null) {
			vh.task_plateNumber.setText(tVehicleNum);
		}

		if (tRewardRecordMoneyCarOne != 0) {
			vh.task_addCar.setText(tRewardRecordMoneyCarOne + "");
		}

		if (tRewardRecordMoneyCarTwo != 0) {
			vh.task_firstTransport.setText(tRewardRecordMoneyCarTwo + "");
			vh.task_firstTransport.setTextColor(Color.WHITE);
		} else {
			vh.task_firstTransport.setText("20");
		}

		if (tRewardRecordMoneyCarThree != 0) {
			vh.task_twoTransport.setText(tRewardRecordMoneyCarThree + "");
			vh.task_twoTransport.setTextColor(Color.WHITE);
		} else {
			vh.task_twoTransport.setText("30");
		}

		if (!TextUtils.isEmpty(tEndTime) && tEndTime != null) {
			vh.task_date.setText(tEndTime);
		}
		return convertView;
	}

	class ViewHolder {
		TextView task_plateNumber;
		TextView task_addCar;
		TextView task_firstTransport;
		TextView task_twoTransport;
		TextView task_date;
	}
}
