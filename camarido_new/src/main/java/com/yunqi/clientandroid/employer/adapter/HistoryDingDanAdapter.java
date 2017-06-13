package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.QuoteActivity;
import com.yunqi.clientandroid.employer.entity.HistoryDingDan;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @Description:历史订单适配器
 * @ClassName: HistoryYunDanAdapter
 * @author: chengtao
 * @date: Aug 30, 2016 7:38:44 PM
 * 
 */
@SuppressLint("InflateParams")
public class HistoryDingDanAdapter extends BaseAdapter {
	private Context context;
	private List<HistoryDingDan> historyDingDanList;
	private UmengStatisticsUtils mUmeng;

	public HistoryDingDanAdapter(Context context,
			List<HistoryDingDan> historyDingDanList) {
		super();
		this.context = context;
		this.historyDingDanList = historyDingDanList;
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	@Override
	public int getCount() {
		return historyDingDanList.size();
	}

	@Override
	public Object getItem(int position) {
		return historyDingDanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_activity_history_ding_dan, null);
			viewHolder = new ViewHolder();
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 获取控件
		viewHolder.tvStartPlace = obtainView(convertView,
				R.id.tv_history_ding_dan_start_palce);
		viewHolder.tvEndPlace = obtainView(convertView,
				R.id.tv_history_ding_dan_end_palce);
		viewHolder.tvTime = obtainView(convertView,
				R.id.tv_hitory_ding_dan_time);
		viewHolder.tvGoodsType = obtainView(convertView,
				R.id.tv_hitory_ding_dan_goods_type);
		viewHolder.tvCarNum = obtainView(convertView,
				R.id.tv_hitory_ding_dan_car_num);
		viewHolder.btnStatus = obtainView(convertView,
				R.id.btn_hitory_ding_dan_status);
		viewHolder.ivInsurance = obtainView(convertView,
				R.id.iv_historty_ding_dan_insurance);
		// 给控件赋值
		HistoryDingDan historyDingDan = historyDingDanList.get(position);
		// 获取数据
		final String packageId = historyDingDan.Id + "";// 包ID
		final String PackageBeginName = historyDingDan.PackageBeginName; // 开始地址名称
		final String PackageBeginCity = historyDingDan.PackageBeginCityText; // 开始城市
		final String PackageBeginCountry = historyDingDan.PackageBeginCountryText; // 开始区县
		final String PackageEndName = historyDingDan.PackageEndName; // 结束地址名称
		final String PackageEndCity = historyDingDan.PackageEndCityText; // 结束城市
		final String PackageEndCountry = historyDingDan.PackageEndCountryText; // 结束区县
		final String PackageStartTime = historyDingDan.PackageStartTime; // 包开始时间
		final String PackageEndTime = historyDingDan.PackageEndTime; // 包结束时间
		final String CategoryName = historyDingDan.CategoryName; // 品类名称
		final String OrderCount = historyDingDan.OrderCount;// 已经报名车数
		final String PackageCount = historyDingDan.PackageCount; // 总车数
		final String PackageStatus = historyDingDan.PackageStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成
		final int InsuranceType = Integer.valueOf(historyDingDan.InsuranceType); // 保险：0：无保险；1：平台送保险；2：自己购买保险

		final String mTicketArrangeCount = historyDingDan.ticketArrangeCount;// 已派车
		final String mOrderBeforeSettleCount = historyDingDan.OrderBeforeSettleCount;// 待结算
		final String mOrderSettledCount = historyDingDan.OrderSettledCount;// 已结算
		final String mPrice = historyDingDan.PackagePrice;
		final String mPackageWeight = historyDingDan.PackageWeight;
		final String mBeforeExcute = historyDingDan.beforeExcute;
		final String mOnTheWayCount = historyDingDan.onTheWayCount;

		// 开始地址
		if (StringUtils.isStrNotNull(PackageBeginName)) {
			viewHolder.tvStartPlace.setText(PackageBeginName);
		}
		// 结算地址
		if (StringUtils.isStrNotNull(PackageEndName)) {
			viewHolder.tvEndPlace.setText(PackageEndName);
		}
		// 时间
		if (StringUtils.isStrNotNull(PackageStartTime)
				&& StringUtils.isStrNotNull(PackageEndTime)) {
			viewHolder.tvTime.setText(StringUtils
					.formatSimpleDate(PackageStartTime)
					+ "~"
					+ StringUtils.formatSimpleDate(PackageEndTime));
		}
		// 品类
		if (StringUtils.isStrNotNull(CategoryName)) {
			viewHolder.tvGoodsType.setText(CategoryName);
		}
		// 总车数
		if (StringUtils.isStrNotNull(PackageCount)) {
			viewHolder.tvCarNum.setText("共" + mTicketArrangeCount + "车");
		} else {
			viewHolder.tvCarNum.setText("共0车");
		}
		// 包状态
		if (StringUtils.isStrNotNull(PackageStatus)) {
			final String timeString = viewHolder.tvTime.getText().toString()
					.trim();
			try {
				switch (Integer.parseInt(PackageStatus)) {
				case 0:// 草稿
						// viewHolder.btnStatus.setText("草稿");
					break;
				case 3:// 已完成
					viewHolder.btnStatus.setText("已完成");

					viewHolder.btnStatus
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Log.e("TAG", PackageBeginName + "----"
											+ PackageBeginCity + "--------"
											+ PackageBeginCountry);
									Log.e("TAG", PackageEndName + "----"
											+ PackageEndCity + "--------"
											+ PackageEndCountry);
									try {
										//友盟统计发包
										mUmeng.setCalculateEvents("order_click_history_stop");

										QuoteActivity.invoke(context,
												packageId, PackageBeginName,
												PackageBeginCity,
												PackageBeginCountry,
												PackageEndName, PackageEndCity,
												PackageEndCountry, timeString,
												InsuranceType, 1,
												mBeforeExcute, mOnTheWayCount,
												mOrderBeforeSettleCount,
												mOrderSettledCount, mPrice,
												mPackageWeight);
									} catch (Exception e) {

									}

								}
							});
					break;
				case 5:
					viewHolder.btnStatus.setText("已终止");

					viewHolder.btnStatus
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Log.e("TAG", PackageBeginName + "----"
											+ PackageBeginCity + "--------"
											+ PackageBeginCountry);
									Log.e("TAG", PackageEndName + "----"
											+ PackageEndCity + "--------"
											+ PackageEndCountry);
									try {
										QuoteActivity.invoke(context,
												packageId, PackageBeginName,
												PackageBeginCity,
												PackageBeginCountry,
												PackageEndName, PackageEndCity,
												PackageEndCountry, timeString,
												InsuranceType, 1,
												mBeforeExcute, mOnTheWayCount,
												mOrderBeforeSettleCount,
												mOrderSettledCount, mPrice,
												mPackageWeight);
									} catch (Exception e) {

									}
								}
							});
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 保险
		try {
			switch (InsuranceType) {
			case 0:// 无保险
				viewHolder.ivInsurance.setVisibility(View.GONE);
				break;
			case 1:// 平台送保险
			case 2:// 自己购买保险
				viewHolder.ivInsurance.setVisibility(View.VISIBLE);
			default:
				viewHolder.ivInsurance.setVisibility(View.GONE);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvStartPlace;// 起始地点
		TextView tvEndPlace;// 目的地
		TextView tvTime;// 时间
		TextView tvGoodsType;// 货物种类
		TextView tvCarNum;// 车数
		Button btnStatus;// 状态
		ImageView ivInsurance;// 保险
	}

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @Description: 获取控件
	 * @Title:obtainView
	 * @param id
	 * @return
	 * @return:T
	 * @throws @Create:
	 *             Aug 30, 2016 7:46:38 PM
	 * @Author : chengtao
	 */
	private <T extends View> T obtainView(View view, int id) {
		return (T) view.findViewById(id);
	}
}
