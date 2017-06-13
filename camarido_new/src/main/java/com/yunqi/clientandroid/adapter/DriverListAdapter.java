package com.yunqi.clientandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DriverListInfo;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.view.CircleImageView;

import java.util.List;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 司机列表Adapter
 * @date 15/11/28
 */
public class DriverListAdapter extends ArrayAdapter<DriverListInfo> {
	private Context mContext;
	private List<DriverListInfo> mList;

	public DriverListAdapter(Activity context, List<DriverListInfo> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_driver_list,
					null);
		}
		DriverListViewHolder holder = DriverListViewHolder
				.getHolder(convertView);

		// 设置数据
		DriverListInfo driverListInfo = mList.get(position);
		String name = driverListInfo.name;// 真实姓名
		String userPhone = driverListInfo.userPhone;// 手机号
		String loginCount = driverListInfo.loginCount;// 司机登录次数
		String opCount = driverListInfo.opCount;// 司机用这辆车的拉运次数
		String headPortraitUrl = driverListInfo.headPortraitUrl;// 头像URL

		// 设置姓名
		if (!TextUtils.isEmpty(name) && name != null) {
			holder.tvDriverName.setText(name);
		}
		// 设置电话号码
		if (!TextUtils.isEmpty(userPhone) && userPhone != null) {
			holder.tvUserPhone.setText(userPhone);
		}
		// 设置拉运次数
		if (!TextUtils.isEmpty(opCount) && opCount != null) {
			holder.tvOpCount.setText(Html.fromHtml("拉运:<font color='#ff4400'>"
					+ opCount + "</font>次"));
		}
		// 设置登录次数
		if (!TextUtils.isEmpty(loginCount) && loginCount != null) {
			holder.tvLoginCount.setText(Html
					.fromHtml("登录:<font color='#ff4400'>" + loginCount
							+ "</font>次"));
		}

		// 设置头像
		if (!TextUtils.isEmpty(headPortraitUrl) && headPortraitUrl != null) {
			ImageLoader.getInstance().displayImage(headPortraitUrl,
					holder.civHeader, ImageLoaderOptions.options);
		}

		return convertView;

	}

	static class DriverListViewHolder {
		TextView tvDriverName;
		TextView tvUserPhone;
		TextView tvLoginCount;
		TextView tvOpCount;
		CircleImageView civHeader;

		public DriverListViewHolder(View convertView) {
			tvDriverName = (TextView) convertView
					.findViewById(R.id.tv_driverlist_driverName);
			tvUserPhone = (TextView) convertView
					.findViewById(R.id.tv_driverlist_UserPhone);
			tvLoginCount = (TextView) convertView
					.findViewById(R.id.tv_driverlist_logincount);
			tvOpCount = (TextView) convertView
					.findViewById(R.id.tv_driverlist_opcount);
			civHeader = (CircleImageView) convertView
					.findViewById(R.id.iv_driverlist_header);
		}

		public static DriverListViewHolder getHolder(View convertView) {
			DriverListViewHolder holder = (DriverListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new DriverListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
