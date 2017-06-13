package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.QuoteActivity;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.employer.util.interfaces.YuShePriceSure;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description: 发包管理页面适配器
 * @ClassName: GetBaoBiaoAdapter
 * @author: zhm
 * @date: 2016-5-23 下午2:36:44
 * 
 */
public class GetBaoBiaoAdapter extends BaseAdapter {
	private Context context;
	private List<GetBiaoLieBiao> mBiaoList;
	private UmengStatisticsUtils mUmeng;
	private YuShePriceSure mYuShePriceSure;

	public GetBaoBiaoAdapter(Context context, List<GetBiaoLieBiao> mBiaoList, YuShePriceSure yuShePriceSure) {
		super();
		this.context = context;
		this.mBiaoList = mBiaoList;
		this.mYuShePriceSure = yuShePriceSure;
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	@Override
	public int getCount() {
		return mBiaoList != null ? mBiaoList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mBiaoList != null ? mBiaoList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return mBiaoList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_item_order_management, null);
			// vh.tvBiaoType = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_type);
			// vh.tvBiaoMeizhong = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_meizhong);
			// vh.tvBiaoDate = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_date);
			// vh.tvBiaoBeginAdress = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_qidian);
			// vh.tvBiaoEndAdress = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_zhongdian);
			// vh.tvBiaoCarSum = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_carSum);
			// vh.tvBiaoStatus = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_zhuangtai);
			// vh.tvBiaoSubSidy = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_buzhu);
			// vh.tvBiaoBaoxian = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_baoxian);
			// vh.ivBiaoBaoXian = (ImageView) convertView
			// .findViewById(R.id.iv_employer_biao_baoxian);
			// vh.tvBiaoDanJia = (TextView) convertView
			// .findViewById(R.id.tv_employer_biao_danjia);
			vh.tvOrderBegin = (TextView) convertView
					.findViewById(R.id.tv_order_item_begin);
			vh.tvOrderEnd = (TextView) convertView
					.findViewById(R.id.tv_order_item_end);
			vh.tvOrderQuotedPrice = (TextView) convertView
					.findViewById(R.id.tv_order_item_quoted_price);
			vh.tvOrderSure = (TextView) convertView
					.findViewById(R.id.tv_order_item_sure);
			vh.tvOrderFinish = (TextView) convertView
					.findViewById(R.id.tv_order_item_finish);
			vh.tvOrderDate = (TextView) convertView
					.findViewById(R.id.tv_order_item_date);
			vh.tvOrderCocal = (TextView) convertView
					.findViewById(R.id.tv_order_item_cocal);
			vh.tvOrderVehicleNumber = (TextView) convertView
					.findViewById(R.id.tv_order_item_vehicle_number);
			vh.btnOrderManager = (Button) convertView
					.findViewById(R.id.tv_order_item_manager);
			vh.ivOrderInsurance = (ImageView) convertView
					.findViewById(R.id.iv_order_item_insurance);
			vh.tvOrderSumdun = (TextView) convertView
					.findViewById(R.id.tv_order_item_sumdun);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		GetBiaoLieBiao mGetBiaoLieBiao = mBiaoList.get(position);

		// 获取请求的数据
		final String packageId = mGetBiaoLieBiao.Id;
		int mPackageType = mGetBiaoLieBiao.PackageType; // 包类型：0：一口价；1：竞价；2：定向指派
		final String mCategoryName = mGetBiaoLieBiao.CategoryName;// 品类名称
		String mBeginTime = StringUtils
				.formatSimpleDate(mGetBiaoLieBiao.PackageStartTime);// 包开始时间
		String mEndTime = StringUtils
				.formatSimpleDate(mGetBiaoLieBiao.PackageEndTime); // 包结束时间
		final int mOrderCount = mGetBiaoLieBiao.OrderCount; // 已报名车辆
		final String mPackageCount = mGetBiaoLieBiao.PackageCount; // 总车辆
		String mPackageBeginAddress = mGetBiaoLieBiao.PackageBeginAddress; // 开始地址
		String mPackageEndAddress = mGetBiaoLieBiao.PackageEndAddress; // 结束地址
		final int mPackageStatus = mGetBiaoLieBiao.PackageStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成
		double mSubsidy = mGetBiaoLieBiao.Subsidy; // 奖励
		final int mInsuranceType = mGetBiaoLieBiao.InsuranceType; // 保险：0：无保险；1：平台送保险；2：自己购买保险
		double mPackagePriceOrigin = mGetBiaoLieBiao.PackagePriceOrigin; // 发包方价格
		final String mPackageBeginName = mGetBiaoLieBiao.PackageBeginName;
		final String mPackageEndName = mGetBiaoLieBiao.PackageEndName;
		String mQuotationcount = mGetBiaoLieBiao.Quotationcount;// 已报价
		String mConfirmQuotationcount = mGetBiaoLieBiao.ConfirmQuotationcount;// 已成交
		final String mOrderSettledCount = mGetBiaoLieBiao.OrderSettledCount;// 已完成
		final String beginCity = mGetBiaoLieBiao.PackageBeginCityText;
		final String beginCounty = mGetBiaoLieBiao.PackageBeginCountryText;
		final String endCity = mGetBiaoLieBiao.PackageEndCityText;
		final String endCounty = mGetBiaoLieBiao.PackageEndCountryText;
		final String pcsPhone = mGetBiaoLieBiao.PcsPhone;// 平台客服电话
		final int orderInfoCount = mGetBiaoLieBiao.OrderInfoCount;// 已下单车数
		final String mPackageWeight = mGetBiaoLieBiao.PackageWeight;// 货品总吨
		final String mTicketArrangeCount = mGetBiaoLieBiao.ticketArrangeCount;
		final String mOrderBeforeSettleCount = mGetBiaoLieBiao.OrderBeforeSettleCount;
		final String mPrice = mGetBiaoLieBiao.PackagePrice;
		final String mCarRange = mGetBiaoLieBiao.CarRange;
		final String mBeforeExcute = mGetBiaoLieBiao.BeforeExecute;
		final String mOnTheWayCount = mGetBiaoLieBiao.onTheWayCount;

		/* 进行赋值 */

		Log.e("TAG", "adapter-----mPackageBeginName------" + mPackageBeginName);
		Log.e("TAG", "adapter-----beginCity------" + beginCity);
		Log.e("TAG", "adapter-----beginCounty------" + beginCounty);
		Log.e("TAG", "adapter-----mPackageEndName------" + mPackageEndName);
		Log.e("TAG", "adapter-----endCity------" + endCity);
		Log.e("TAG", "adapter-----endCounty------" + endCounty);

		if (mPackageBeginName != null && !TextUtils.isEmpty(mPackageBeginName)) {
			vh.tvOrderBegin.setText(mPackageBeginName);
		}

		if (mPackageEndName != null && !TextUtils.isEmpty(mPackageEndName)) {
			vh.tvOrderEnd.setText(mPackageEndName);
		}

		if ((mBeginTime != null && !TextUtils.isEmpty(mBeginTime))
				&& (mEndTime != null && !TextUtils.isEmpty(mEndTime))) {
			vh.tvOrderDate.setText(mBeginTime + "~" + mEndTime);
		}

		if (mCategoryName != null && !TextUtils.isEmpty(mCategoryName)) {
			vh.tvOrderCocal.setText(mCategoryName);
		}

		// 货品总吨
		if (StringUtils.isStrNotNull(mPackageWeight)) {
			vh.tvOrderSumdun.setText("总吨数：" + mPackageWeight + "吨");
		}

		if (mInsuranceType == 0) {
			vh.ivOrderInsurance.setVisibility(View.GONE);
		} else if (mInsuranceType == 1 || mInsuranceType == 2) {
			vh.ivOrderInsurance.setVisibility(View.VISIBLE);
		}

		Log.e("TAG", "----------mOrderSettledCount-----------"
				+ mOrderSettledCount);

		vh.tvOrderVehicleNumber.setText(mCarRange);
		vh.tvOrderQuotedPrice.setText(Html.fromHtml("<font color='#ff4444'>"
				+ mQuotationcount + "</font>家报价"));
		vh.tvOrderSure.setText(Html.fromHtml("<font color='#ff4444'>"
				+ mConfirmQuotationcount + "</font>家确认"));
		vh.tvOrderFinish.setText(Html.fromHtml("<font color='#ff4444'>"
				+ mOrderSettledCount + "</font>车完成"));

		final String dateTime = vh.tvOrderDate.getText().toString().trim();

		Log.e("TAG", "--------dateTime-----------" + dateTime);

		if (mPackageStatus == 1) {
			vh.btnOrderManager.setText("审核中");
			vh.btnOrderManager.setBackgroundResource(R.drawable.btn_zhihui_bg);
			vh.btnOrderManager.setOnClickListener(null);
		} else {
			vh.btnOrderManager.setText("订单管理");
			vh.btnOrderManager
					.setBackgroundResource(R.drawable.package_btn_background);
			vh.btnOrderManager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Log.e("TAG", "进入报价单页面-----packageId-------------"
							+ packageId);

					Log.e("TAG", "报价单-----mPackageBeginName------"
							+ mPackageBeginName);
					Log.e("TAG", "报价单-----beginCity------" + beginCity);
					Log.e("TAG", "报价单-----beginCounty------" + beginCounty);
					Log.e("TAG", "报价单-----mPackageEndName------"
							+ mPackageEndName);
					Log.e("TAG", "报价单-----endCity------" + endCity);
					Log.e("TAG", "报价单-----endCounty------" + endCounty);
					Log.e("TAG", "报价单-----mBeforeExcute------" + mBeforeExcute);

					//友盟统计发包
					mUmeng.setCalculateEvents("order_click_management_item");

					mYuShePriceSure.onNextRequest(packageId, mPackageBeginName,
							beginCity, beginCounty, mPackageEndName, endCity,
							endCounty, dateTime, mInsuranceType, 0,
							mBeforeExcute, mOnTheWayCount,
							mOrderBeforeSettleCount, mOrderSettledCount,
							mPrice, mPackageWeight);

					// 进入报价单页面
//					QuoteActivity.invoke(context, packageId, mPackageBeginName,
//							beginCity, beginCounty, mPackageEndName, endCity,
//							endCounty, dateTime, mInsuranceType, 0,
//							mBeforeExcute, mOnTheWayCount,
//							mOrderBeforeSettleCount, mOrderSettledCount,
//							mPrice, mPackageWeight);

					Log.e("TAG", "进入报价单页面-----packageId-------------"
							+ packageId);
				}
			});
		}

		/* 包类型：0：一口价；1：竞价；2：定向指派 */
		// if (mPackageType == 0) {
		// vh.tvBiaoType.setText("一口价");
		// } else if (mPackageType == 1) {
		// vh.tvBiaoType.setText("竞价");
		// } else if (mPackageType == 2) {
		// vh.tvBiaoType.setText("定向指派");
		// }

		/* 品类名称 */
		// if (!TextUtils.isEmpty(mCategoryName) && mCategoryName != null) {
		// vh.tvOrderCocal.setText(mCategoryName);
		// }

		/* 包开始时间到结束时间 */
		// if (!TextUtils.isEmpty(mBeginTime) && mBeginTime != null
		// && !TextUtils.isEmpty(mEndTime) && mEndTime != null) {
		// vh.tvBiaoDate.setText(mBeginTime + "-" + mEndTime);
		// }
		//
		// /* 开始地址 */
		// if (!TextUtils.isEmpty(mPackageBeginAddress)
		// && mPackageBeginAddress != null) {
		// vh.tvBiaoBeginAdress.setText(mPackageBeginAddress);
		// }
		//
		// /* 结束地址 */
		// if (!TextUtils.isEmpty(mPackageEndAddress)
		// && mPackageEndAddress != null) {
		// vh.tvBiaoEndAdress.setText(mPackageEndAddress);
		// }

		/* 已报名车数和总车数 */
		// if (mPackageCount != 0) {
		// vh.tvBiaoCarSum.setText(mOrderCount + "/" + mPackageCount + "车");
		// }

		/* 包状态：0：草稿；1：待审核；2：已发布；3：已完成 */
		// if (mPackageStatus == 0) {
		// vh.tvBiaoStatus.setText("草稿");
		// } else if (mPackageStatus == 1) {
		// vh.tvBiaoStatus.setText("待审核");
		// } else if (mPackageStatus == 2) {
		// vh.tvBiaoStatus.setText("已发布");
		// } else if (mPackageStatus == 3) {
		// vh.tvBiaoStatus.setText("已完成");
		// }

		/* 奖励 */
		// if (mSubsidy != 0) {
		// vh.tvBiaoSubSidy.setVisibility(View.VISIBLE);
		// vh.tvBiaoSubSidy.setText("补贴" + mSubsidy + "元");
		// } else {
		// vh.tvBiaoSubSidy.setVisibility(View.INVISIBLE);
		// }

		/* 保险：0：无保险；1：平台送保险；2：自己购买保险 */
		// if (mInsuranceType == 0) {
		// vh.tvBiaoBaoxian.setVisibility(View.INVISIBLE);
		// vh.ivBiaoBaoXian.setVisibility(View.INVISIBLE);
		// } else if (mInsuranceType == 1 || mInsuranceType == 2) {
		// vh.tvBiaoBaoxian.setVisibility(View.VISIBLE);
		// vh.ivBiaoBaoXian.setVisibility(View.VISIBLE);
		// }

		/* 货值单价 */
		// if (mPackagePriceOrigin != 0) {
		// vh.tvBiaoDanJia.setText("￥" + mPackagePriceOrigin + "/吨");
		// }

		// /* 对草稿的点击监听 */
		// vh.tvBiaoStatus.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (mPackageStatus == 0) {
		// // TODO 跳转到发包界面，并把数据带过去
		//
		// } else {
		// return;
		// }
		// }
		// });

		return convertView;
	}

	class ViewHolder {
		// TextView tvBiaoType; // 包类型：0：一口价；1：竞价；2：定向指派
		// TextView tvBiaoMeizhong; // 品类名称
		// TextView tvBiaoDate; // 包开始时间到结束时间
		// TextView tvBiaoBeginAdress; // 开始地址
		// TextView tvBiaoEndAdress; // 结束地址
		// TextView tvBiaoCarSum; // 已报名车数和总车数
		// TextView tvBiaoStatus; // 包的状态
		// TextView tvBiaoSubSidy; // 奖励
		// TextView tvBiaoBaoxian; // 保险字
		// ImageView ivBiaoBaoXian; // 保险图
		// TextView tvBiaoDanJia; // 单价
		TextView tvOrderBegin;// 始发地
		TextView tvOrderEnd;// 目的地
		TextView tvOrderQuotedPrice;// 报价
		TextView tvOrderSure;// 确认
		TextView tvOrderFinish;// 完成
		TextView tvOrderDate;// 日期
		TextView tvOrderCocal;// 煤品种类
		TextView tvOrderVehicleNumber;// 车数
		Button btnOrderManager;// 订单管理
		ImageView ivOrderInsurance;// 保险
		TextView tvOrderSumdun;// 总吨数
	}

}
