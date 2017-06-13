package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.VehicleListInfo;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.view.CircleImageView;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 车辆列表的adapter
 * @date 15/11/28
 */
public class VehicleListAdapter extends ArrayAdapter<VehicleListInfo> {

	private List<VehicleListInfo> mCarList;
	private Context mContext;

	public VehicleListAdapter(Activity context,
			List<VehicleListInfo> carListInfos) {
		super(context, 0, carListInfos);
		this.mCarList = carListInfos;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_car_list, null);
		}
		CarListViewHolder holder = CarListViewHolder.getHolder(convertView);

		// 设置数据
		VehicleListInfo vehicleListInfo = mCarList.get(position);
		String vehicleNo = vehicleListInfo.vehicleNo;// 车牌号
		String driverType = vehicleListInfo.driverType;// 0：司机，1:车主,2经纪人
		String vehicleImgUrl = vehicleListInfo.vehicleImgUrl;// 图标的路径
		int sVehicleStatus = vehicleListInfo.vehicleStatus;
		int xStarsLevel = vehicleListInfo.starsLevel;// 星级1，一星 2，二星 3，三星

		// 设置车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			holder.tvVehicleNo.setText(vehicleNo);
		}

		// 判断星级1，一星 2，二星 3，三星
		if (xStarsLevel == 1) {
			holder.iv_vehiclelist_xingji1.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji2.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji3.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji1.setImageResource(R.drawable.xingxing);
			holder.iv_vehiclelist_xingji2
					.setImageResource(R.drawable.xingxinghui);
			holder.iv_vehiclelist_xingji3
					.setImageResource(R.drawable.xingxinghui);
		} else if (xStarsLevel == 2) {
			holder.iv_vehiclelist_xingji1.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji2.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji3.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji1.setImageResource(R.drawable.xingxing);
			holder.iv_vehiclelist_xingji2.setImageResource(R.drawable.xingxing);
			holder.iv_vehiclelist_xingji3
					.setImageResource(R.drawable.xingxinghui);
		} else if (xStarsLevel == 3) {
			holder.iv_vehiclelist_xingji1.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji2.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji3.setVisibility(View.VISIBLE);
			holder.iv_vehiclelist_xingji1.setImageResource(R.drawable.xingxing);
			holder.iv_vehiclelist_xingji2.setImageResource(R.drawable.xingxing);
			holder.iv_vehiclelist_xingji3.setImageResource(R.drawable.xingxing);
		} else if (xStarsLevel == 0) {
			holder.iv_vehiclelist_xingji1.setVisibility(View.GONE);
			holder.iv_vehiclelist_xingji2.setVisibility(View.GONE);
			holder.iv_vehiclelist_xingji3.setVisibility(View.GONE);
		}

		// 设置是否是车主
		if (!TextUtils.isEmpty(driverType) && driverType != null) {
			if (driverType.equals("1")) {
				holder.ivOwner.setVisibility(View.VISIBLE);
			} else {
				holder.ivOwner.setVisibility(View.GONE);
			}
		}

		if (sVehicleStatus == 0) {
			holder.state.setText("未认证");
		} else if (sVehicleStatus == 1) {
			holder.state.setText("已认证");
		} else if (sVehicleStatus == 2) {
			holder.state.setText("未通过");
		} else if (sVehicleStatus == 3) {
			holder.state.setText("禁用");
		}

		// 设置车辆图片
		if (!TextUtils.isEmpty(vehicleImgUrl) && vehicleImgUrl != null) {
			ImageLoader.getInstance().displayImage(vehicleImgUrl,
					holder.civHeader, ImageLoaderOptions.options);
		}

		return convertView;

	}

	static class CarListViewHolder {
		TextView tvVehicleNo;
		CircleImageView civHeader;
		ImageView ivOwner;
		TextView state;
		ImageView iv_vehiclelist_xingji1;
		ImageView iv_vehiclelist_xingji2;
		ImageView iv_vehiclelist_xingji3;

		public CarListViewHolder(View convertView) {
			tvVehicleNo = (TextView) convertView
					.findViewById(R.id.tv_vehiclelist_vehicleNo);
			civHeader = (CircleImageView) convertView
					.findViewById(R.id.iv_vehiclelist_header);
			ivOwner = (ImageView) convertView
					.findViewById(R.id.iv_vehiclelist_owner);
			state = (TextView) convertView
					.findViewById(R.id.tv_vehiclelist_state);
			iv_vehiclelist_xingji1 = (ImageView) convertView
					.findViewById(R.id.iv_vehiclelist_xingji1);
			iv_vehiclelist_xingji2 = (ImageView) convertView
					.findViewById(R.id.iv_vehiclelist_xingji2);
			iv_vehiclelist_xingji3 = (ImageView) convertView
					.findViewById(R.id.iv_vehiclelist_xingji3);

		}

		public static CarListViewHolder getHolder(View convertView) {
			CarListViewHolder holder = (CarListViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new CarListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
