package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.PackagePath;

public class AllRoutesAdapter extends ArrayAdapter<PackagePath> {

	private String start_above;
	private String start_follow;
	private String end_above;
	private String end_follow;

	private Context mContext;
	private List<PackagePath> mList;

	public AllRoutesAdapter(Activity context, List<PackagePath> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_allroutes_list,
					null);
		}

		AllRoutesListViewHolder holder = AllRoutesListViewHolder
				.getHolder(convertView);

		// 设置数据
		PackagePath allRoutesDataEntity = mList.get(position);
		String packageBeginText = allRoutesDataEntity.packageBeginCityText;
		String packageEndText = allRoutesDataEntity.packageEndCityText;
		String packageCount = allRoutesDataEntity.packageCount;
		String orderCount = allRoutesDataEntity.orderCount;
		String packageDistance = allRoutesDataEntity.packageDistance;

		// 设置开始地址
		if (!TextUtils.isEmpty(packageBeginText) && packageBeginText != null) {
			start_above = packageBeginText.split(" ")[0];
			start_follow = packageBeginText.split(" ")[1];
		}

		// 设置结束地址
		if (!TextUtils.isEmpty(packageEndText) && packageEndText != null) {
			end_above = packageEndText.split(" ")[0];
			end_follow = packageEndText.split(" ")[1];
		}

		if (!TextUtils.isEmpty(start_above) && start_above != null) {
			holder.startAbove.setText(start_above);
		}

		if (!TextUtils.isEmpty(start_follow) && start_follow != null) {
			holder.startFollow.setText(start_follow);
		}

		if (!TextUtils.isEmpty(end_above) && end_above != null) {
			holder.endAbove.setText(end_above);
		}

		if (!TextUtils.isEmpty(end_follow) && end_follow != null) {
			holder.endFollow.setText(end_follow);
		}

		if (!TextUtils.isEmpty(packageCount) && packageCount != null) {
			holder.PackageCount.setText(packageCount + "包/");
		}

		if (!TextUtils.isEmpty(orderCount) && orderCount != null) {
			holder.OrderCount.setText(orderCount + "单");
		}

		if (!TextUtils.isEmpty(packageDistance) && packageDistance != null) {
			holder.PackageDistance.setText("全程" + packageDistance + "公里");
		}

		return convertView;

	}

	static class AllRoutesListViewHolder {
		TextView startAbove;
		TextView startFollow;
		TextView endAbove;
		TextView endFollow;
		TextView PackageCount;
		TextView OrderCount;
		TextView PackageDistance;

		public AllRoutesListViewHolder(View convertView) {
			startAbove = (TextView) convertView
					.findViewById(R.id.tv_routes_startabove);
			startFollow = (TextView) convertView
					.findViewById(R.id.tv_routes_startfollow);
			endAbove = (TextView) convertView
					.findViewById(R.id.tv_routes_endabove);
			endFollow = (TextView) convertView
					.findViewById(R.id.tv_routes_endfollow);
			PackageCount = (TextView) convertView
					.findViewById(R.id.tv_routes_package);
			OrderCount = (TextView) convertView
					.findViewById(R.id.tv_routes_order);
			PackageDistance = (TextView) convertView
					.findViewById(R.id.tv_routes_distance);

		}

		public static AllRoutesListViewHolder getHolder(View convertView) {
			AllRoutesListViewHolder holder = (AllRoutesListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new AllRoutesListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
