package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.RecommendAward;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Description:class 推荐奖励的适配器
 * @ClassName: TaskAwardAdapter
 * @author: zhm
 * @date: 2016-4-6 下午2:10:44
 * 
 */
public class RecommendAwardAdapter extends BaseAdapter {
	private List<RecommendAward> list;
	private Context context;

	public RecommendAwardAdapter(List<RecommendAward> list, Context context) {
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
		RecommendAward recommendAward = list.get(position);

		double rRewardRecordImpMoney = recommendAward.rewardRecordMoney;// 奖励钱数
		int rRewardRecordType = recommendAward.rewardRecordType;// 奖励类型
		String rUserPhone = recommendAward.UserPhone;// 被推荐人
		String cVehicleNum = recommendAward.VehicleNum;// 车牌号

		Log.e("TAG", "--奖励钱数---" + rRewardRecordImpMoney);
		Log.e("TAG", "--奖励类型---" + rRewardRecordType);

		String sUserPhone = "";

		if (!TextUtils.isEmpty(rUserPhone) && rUserPhone != null) {
			sUserPhone = rUserPhone.substring(0, 3) + "*****"
					+ rUserPhone.substring(8, rUserPhone.length());
		}

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.recommend_listview_item, null);
			vh.recommend_userName = (TextView) convertView
					.findViewById(R.id.tv_recommend_list_userName);
			vh.recommend_register = (TextView) convertView
					.findViewById(R.id.tv_recommend_list_register);
			vh.recommend_firstTransport = (TextView) convertView
					.findViewById(R.id.tv_recommend_list_firstTransport);
			vh.recommend_money = (TextView) convertView
					.findViewById(R.id.tv_recommend_list_money);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.recommend_userName.setText("用户" + sUserPhone);

		if (!TextUtils.isEmpty(cVehicleNum) && cVehicleNum != null) {
			vh.recommend_register.setText(cVehicleNum);
		}

		if (rRewardRecordType == 100) {
			vh.recommend_firstTransport.setText("未完成首运");
		} else if (rRewardRecordType == 101) {
			vh.recommend_firstTransport.setText("完成首运");
		}

		if (rRewardRecordImpMoney != 0) {
			vh.recommend_money.setText(rRewardRecordImpMoney + "元");
		}

		return convertView;
	}

	class ViewHolder {
		TextView recommend_userName;
		TextView recommend_register;
		TextView recommend_firstTransport;
		TextView recommend_money;
	}
}
