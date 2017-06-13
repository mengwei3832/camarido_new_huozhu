package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.FocusonRoute;

public class FocusonRouteAdapter extends ArrayAdapter<FocusonRoute> {

	private Context mContext;
	private List<FocusonRoute> mList;
	private String priceType;

	public FocusonRouteAdapter(Activity context, List<FocusonRoute> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_focusonroute_list, null);
		}

		FocusonRouteListViewHolder holder = FocusonRouteListViewHolder
				.getHolder(convertView);

		// 设置数据
		FocusonRoute focusonRouteDataEntity = mList.get(position);
		String packageBeginName = focusonRouteDataEntity.packageBeginName;
		String packageEndName = focusonRouteDataEntity.packageEndName;
		String packageBeginAddress = focusonRouteDataEntity.packageBeginAddress;
		String packageEndAddress = focusonRouteDataEntity.packageEndAddress;
		String packageCount = focusonRouteDataEntity.packageCount;
		String packagePrice = focusonRouteDataEntity.packagePrice;
		String packagePriceType = focusonRouteDataEntity.packagePriceType;
		String packageType = focusonRouteDataEntity.packageType;
		String appraisal = focusonRouteDataEntity.appraisal;
		String subsidy = focusonRouteDataEntity.subsidy;

		if (packagePriceType != null && packagePriceType.equals("0")) {
			priceType = "/吨";
		} else if (packagePriceType != null && packagePriceType.equals("1")) {
			priceType = "/吨*公里";
		} else if (packagePriceType != null && packagePriceType.equals("2")) {
			priceType = "/车数";
		}

		// 设置起点
		if (!TextUtils.isEmpty(packageBeginAddress)
				&& packageBeginAddress != null) {
			holder.tv_provenance.setText(packageBeginAddress);
		}

		// 设置终点
		if (!TextUtils.isEmpty(packageEndAddress) && packageEndAddress != null) {
			holder.tv_destination.setText(packageEndAddress);
		}

		// 设置包的数量
		if (!TextUtils.isEmpty(packageCount) && packageCount != null) {
			holder.tv_waybill.setText(packageCount + "单");
		}

		if (packageType != null && packageType.equals("0")) {
			// 普通包
			holder.ll_subsidies.setVisibility(View.VISIBLE);
			holder.tv_valuation.setVisibility(View.GONE);

			// 设置包的价格和类型
			if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
				holder.bt_packageType.setText(packagePrice + "元" + priceType);
			}

			// 设置补助
			if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
				holder.tv_subsidies.setText("补" + subsidy + "元");
			} else {
				holder.tv_subsidies.setText("无补助金额");
			}

		} else if (packageType != null && packageType.equals("1")) {
			// 竞价包
			holder.ll_subsidies.setVisibility(View.GONE);
			holder.tv_valuation.setVisibility(View.VISIBLE);

			holder.bt_packageType.setText("竞价");

			// 设置估价
			if (!TextUtils.isEmpty(appraisal) && appraisal != null) {
				holder.tv_valuation.setText("估价" + appraisal + priceType);
			}
		}

		return convertView;
	}

	static class FocusonRouteListViewHolder {
		TextView tv_provenance;
		TextView tv_destination;
		TextView tv_waybill;
		TextView tv_valuation;
		TextView tv_subsidies;
		Button bt_packageType;
		LinearLayout ll_subsidies;

		public FocusonRouteListViewHolder(View convertView) {

			tv_provenance = (TextView) convertView
					.findViewById(R.id.tv_focuson_provenance);
			tv_destination = (TextView) convertView
					.findViewById(R.id.tv_focuson_destination);
			tv_waybill = (TextView) convertView
					.findViewById(R.id.tv_focuson_waybill);
			tv_valuation = (TextView) convertView
					.findViewById(R.id.tv_focuson_valuation);
			tv_subsidies = (TextView) convertView
					.findViewById(R.id.tv_focuson_subsidies);
			bt_packageType = (Button) convertView
					.findViewById(R.id.bt_focuson_packageType);
			ll_subsidies = (LinearLayout) convertView
					.findViewById(R.id.ll_focuson_subsidies);

		}

		public static FocusonRouteListViewHolder getHolder(View convertView) {
			FocusonRouteListViewHolder holder = (FocusonRouteListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new FocusonRouteListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
