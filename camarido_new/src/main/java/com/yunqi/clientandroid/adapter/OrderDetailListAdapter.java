package com.yunqi.clientandroid.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.location.Location;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单详情列表Adapter
 * @date 15/11/28
 */
public class OrderDetailListAdapter extends ArrayAdapter<FocusonRoute> {

	private Context mContext;
	private List<FocusonRoute> mList;
	private PreManager mPreManager;

	public OrderDetailListAdapter(Context context, List<FocusonRoute> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		mPreManager = PreManager.instance(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FocusonRoute packageRoute = mList.get(position);
		String packageStartTime = packageRoute.packageStartTime;

		String packageBeginName = packageRoute.packageBeginName;
		String packageEndName = packageRoute.packageEndName;
		String packageBeginProvinceText = packageRoute.packageBeginProvinceText;
		String packageBeginCityText = packageRoute.packageBeginCityText;
		String packageEndProvinceText = packageRoute.packageEndProvinceText;
		String packageEndCityText = packageRoute.packageEndCityText;

		String packageBeginAdress = packageRoute.packageBeginAddress;
		String packageEndAdress = packageRoute.packageEndAddress;
		String packageBeginLatitude = packageRoute.packageBeginLatitude;
		String packageBeginLongitude = packageRoute.packageBeginLongitude;
		String packageDistance = packageRoute.packageDistance;
		String packageEndLatitude = packageRoute.packageEndLatitude;
		String packageEndLongitude = packageRoute.packageEndLongitude;
		String categoryName = packageRoute.categoryName;
		String subsidy = packageRoute.subsidy;
		String packageRoadToll = packageRoute.packageRoadToll;
		String orderCount = packageRoute.orderCount;
		String packageCount = packageRoute.packageCount;
		// String packageEndCityText = packageRoute.packageEndCityText;
		String packageType = packageRoute.packageType;
		String packagePrice = packageRoute.packagePrice;
		String tenantShortName = packageRoute.tenantShortName;// 公司的简称

		//
		String packagePriceType = packageRoute.packagePriceType;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.new_adapter_order_detail_list, null);

			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tv_date);
			// viewHolder.tvLocationStart = (TextView) convertView
			// .findViewById(R.id.tv_location_start);
			// viewHolder.tvLocationEnd = (TextView) convertView
			// .findViewById(R.id.tv_location_end);
			// viewHolder.tvFromStart = (TextView) convertView
			// .findViewById(R.id.tv_start_from);
			viewHolder.tvFromAll = (TextView) convertView
					.findViewById(R.id.tv_all_from);
			// viewHolder.tvCatory = (TextView) convertView
			// .findViewById(R.id.tv_catory);
			viewHolder.tvtvOrderCount = (TextView) convertView
					.findViewById(R.id.tv_order_count);
			viewHolder.tvSubsidy = (TextView) convertView
					.findViewById(R.id.tv_subsidy);
			viewHolder.tvTolls = (TextView) convertView
					.findViewById(R.id.tv_tolls);
			viewHolder.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_price);
			// viewHolder.llTicketLocationBg = (LinearLayout) convertView
			// .findViewById(R.id.ll_ticketLocation_bg);
			// viewHolder.tvCityFirst = (TextView) convertView
			// .findViewById(R.id.tv_city_first);
			// viewHolder.tvCitySecond = (TextView) convertView
			// .findViewById(R.id.tv_city_second);
			viewHolder.ivOrderStatus = (ImageView) convertView
					.findViewById(R.id.iv_order_status);
			viewHolder.ivLock = (ImageView) convertView
					.findViewById(R.id.iv_lock);
			viewHolder.ivSameCity = (ImageView) convertView
					.findViewById(R.id.iv_same_city);
			viewHolder.ivFocus = (ImageView) convertView
					.findViewById(R.id.iv_notice);
			viewHolder.tv_company = (TextView) convertView
					.findViewById(R.id.tv_company);
			// 新增
			viewHolder.tvStartName = (TextView) convertView
					.findViewById(R.id.tv_location_start);
			viewHolder.tvEndName = (TextView) convertView
					.findViewById(R.id.tv_location_end);
			viewHolder.tvStartCity = (TextView) convertView
					.findViewById(R.id.tv_start_city);
			viewHolder.tvEndCity = (TextView) convertView
					.findViewById(R.id.tv_end_city);
			viewHolder.tvGoodsType = (TextView) convertView
					.findViewById(R.id.tv_goods_type);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 新增
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null) {
			viewHolder.tvStartName.setText(packageBeginName);
		}
		if (!TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			viewHolder.tvEndName.setText(packageEndName);
		}
		if (!TextUtils.isEmpty(packageBeginProvinceText)
				&& packageBeginProvinceText != null
				&& !TextUtils.isEmpty(packageBeginCityText)
				&& packageBeginCityText != null) {
			viewHolder.tvStartCity.setText(packageBeginProvinceText + "."
					+ packageBeginCityText);
		}
		if (!TextUtils.isEmpty(packageEndProvinceText)
				&& packageEndProvinceText != null
				&& !TextUtils.isEmpty(packageEndCityText)
				&& packageEndCityText != null) {
			viewHolder.tvEndCity.setText(packageEndProvinceText + "."
					+ packageEndCityText);
		}
		if (!TextUtils.isEmpty(categoryName) && categoryName != null) {
			viewHolder.tvGoodsType.setText(categoryName);
		}

		// 订单随机背景颜色
		Random random = new Random();
		int nextInt = random.nextInt(6);
		// ColorUtil.changeBackgroudStyle(nextInt,
		// viewHolder.llTicketLocationBg);

		if (!TextUtils.isEmpty(packageStartTime) && packageStartTime != null) {
			viewHolder.tvDate.setText(StringUtils.formatDate(packageStartTime));
		}

		/*
		 * if (!TextUtils.isEmpty(packageBeginAdress) && packageBeginAdress !=
		 * null) { viewHolder.tvLocationStart.setText(packageBeginAdress); }
		 * 
		 * if (!TextUtils.isEmpty(packageEndAdress) && packageEndAdress != null)
		 * { viewHolder.tvLocationEnd.setText(packageEndAdress); }
		 */

		if (!TextUtils.isEmpty(tenantShortName) && tenantShortName != null) {
			viewHolder.tv_company.setText(tenantShortName);
		}

		/*
		 * if (!TextUtils.isEmpty(packageBeginLatitude) && packageBeginLatitude
		 * != null && !TextUtils.isEmpty(packageBeginLongitude) &&
		 * packageBeginLongitude != null) {
		 * viewHolder.tvFromStart.setVisibility(View.VISIBLE); float[] results =
		 * new float[1];
		 * Location.distanceBetween(Double.parseDouble(packageBeginLatitude),
		 * Double.parseDouble(packageBeginLongitude),
		 * Double.parseDouble(mPreManager.getLatitude()),
		 * Double.parseDouble(mPreManager.getLongitude()), results);
		 * viewHolder.tvFromStart.setText("距我" + Math.rint(results[0] / 100) /
		 * 10 + "公里"); } else {
		 * viewHolder.tvFromStart.setVisibility(View.INVISIBLE); }
		 */

		if (!TextUtils.isEmpty(packageDistance) && packageDistance != null
				&& !packageDistance.equals("0.00")) {
			viewHolder.tvFromAll.setVisibility(View.VISIBLE);
			viewHolder.tvFromAll.setText("全程" + packageDistance + "公里");
		} else {
			if (!TextUtils.isEmpty(packageBeginLatitude)
					&& packageBeginLatitude != null
					&& !TextUtils.isEmpty(packageBeginLongitude)
					&& packageBeginLongitude != null
					&& !TextUtils.isEmpty(packageEndLatitude)
					&& packageEndLatitude != null
					&& !TextUtils.isEmpty(packageEndLongitude)
					&& packageEndLongitude != null) {
				viewHolder.tvFromAll.setVisibility(View.VISIBLE);
				float[] resultDistance = new float[1];
				Location.distanceBetween(
						Double.parseDouble(packageBeginLatitude),
						Double.parseDouble(packageBeginLongitude),
						Double.parseDouble(packageEndLatitude),
						Double.parseDouble(packageEndLongitude), resultDistance);
				viewHolder.tvFromAll.setText("全程"
						+ Math.rint(resultDistance[0] / 100) / 10 + "公里");
			} else {
				viewHolder.tvFromAll.setVisibility(View.INVISIBLE);
			}
		}

		/*
		 * if (!TextUtils.isEmpty(categoryName) && categoryName != null) {
		 * viewHolder.tvCatory.setText(categoryName); }
		 */

		if (!TextUtils.isEmpty(subsidy) && subsidy != null) {
			viewHolder.tvSubsidy.setVisibility(View.VISIBLE);
			viewHolder.tvSubsidy.setText(Html.fromHtml("补贴"
					+ "<font color='#ff4400'>" + subsidy + "</font>" + "元"));
		} else {
			viewHolder.tvSubsidy.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(packageRoadToll) && packageRoadToll != null
				&& !packageRoadToll.equals("0.00")) {
			viewHolder.tvTolls.setVisibility(View.VISIBLE);
			viewHolder.tvTolls.setText("过路费" + packageRoadToll + "元");
		} else {
			viewHolder.tvTolls.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(orderCount) && orderCount != null
				&& !TextUtils.isEmpty(packageCount) && packageCount != null) {
			viewHolder.tvtvOrderCount.setText(Html
					.fromHtml("<font color='#ff4400'>" + orderCount
							+ "/</font>" + packageCount));
		}

		/*
		 * if (!TextUtils.isEmpty(packageEndCityText) && packageEndCityText !=
		 * null) { if (packageEndCityText.length() == 2) {
		 * viewHolder.tvCityFirst.setVisibility(View.VISIBLE);
		 * viewHolder.tvCitySecond.setVisibility(View.GONE);
		 * viewHolder.tvCityFirst.setText(packageEndCityText); } else if
		 * (packageEndCityText.length() == 3) {
		 * viewHolder.tvCityFirst.setVisibility(View.VISIBLE);
		 * viewHolder.tvCitySecond.setVisibility(View.VISIBLE);
		 * viewHolder.tvCityFirst.setText(packageEndCityText.substring(0, 2));
		 * viewHolder.tvCitySecond .setText(packageEndCityText.substring(2));
		 * 
		 * } else if (packageEndCityText.length() == 4) {
		 * viewHolder.tvCityFirst.setVisibility(View.VISIBLE);
		 * viewHolder.tvCitySecond.setVisibility(View.VISIBLE);
		 * viewHolder.tvCityFirst.setText(packageEndCityText.substring(0, 2));
		 * viewHolder.tvCitySecond .setText(packageEndCityText.substring(2));
		 * 
		 * } }
		 */

		if (packageType != null && packageType.equals("0")) {
			viewHolder.ivOrderStatus.setImageResource(R.drawable.qiang);
			/*
			 * if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
			 * viewHolder.tvPrice.setText("￥" + packagePrice); }
			 */
		} else if (packageType != null && packageType.equals("1")) {
			viewHolder.ivOrderStatus.setImageResource(R.drawable.jing);
			/*
			 * if (!TextUtils.isEmpty(packagePrice) && packagePrice != null) {
			 * viewHolder.tvPrice.setText("￥" + packagePrice + " 估"); }
			 */
		}

		if (StringUtils.isStrNotNull(packagePrice)) {
			if (StringUtils.isStrNotNull(packagePriceType)
					&& packagePriceType.equals("0")) {
				viewHolder.tvPrice.setText("￥" + packagePrice + "/吨");
			} else if (StringUtils.isStrNotNull(packagePriceType)
					&& packagePriceType.equals("1")) {
				viewHolder.tvPrice.setText("￥" + packagePrice + "/吨*公里");
			} else if (StringUtils.isStrNotNull(packagePriceType)
					&& packagePriceType.equals("2")) {
				viewHolder.tvPrice.setText("￥" + packagePrice + "/车数");
			}
		}

		viewHolder.ivSameCity.setVisibility(View.GONE);

		if (packageRoute.isFocused) {
			viewHolder.ivFocus.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ivFocus.setVisibility(View.GONE);
		}

		if (packageRoute.isNeedInviteCode) {
			viewHolder.ivLock.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ivLock.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tvDate;//
		// private TextView tvLocationStart;
		// private TextView tvLocationEnd;
		// private TextView tvFromStart;
		private TextView tvFromAll;//
		// private TextView tvCatory;
		private TextView tvtvOrderCount;//
		private TextView tvSubsidy;//
		private TextView tvTolls;//
		private TextView tvPrice;//
		// private TextView tvCityFirst;
		// private TextView tvCitySecond;
		private ImageView ivOrderStatus;//
		private ImageView ivLock;//
		private ImageView ivSameCity;//
		private ImageView ivFocus;//
		// private LinearLayout llTicketLocationBg;
		private TextView tv_company;//
		/**
		 * 新增
		 */
		private TextView tvStartName;// 开始地点名称
		private TextView tvEndName;// 结束地点名称
		private TextView tvStartCity;// 开始城市，到市
		private TextView tvEndCity;// 结束城市，到市
		private TextView tvGoodsType;
	}

}
