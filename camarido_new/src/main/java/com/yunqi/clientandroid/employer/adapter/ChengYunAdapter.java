package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.adapter.CurrentTicketAdapter.ViewHoler;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJi;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class ChengYunAdapter extends BaseAdapter {
	private List<ChengYunTongJi> chengList;
	private Context context;
	private UmengStatisticsUtils mUmeng;

	public ChengYunAdapter(List<ChengYunTongJi> chengList, Context context) {
		super();
		this.chengList = chengList;
		this.context = context;
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	@Override
	public int getCount() {
		return chengList != null ? chengList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return chengList != null ? chengList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return chengList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_item_chengyun_tongji, null);

			vh.tvJingJiRen = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_jingjiren);
			vh.tvVehicleNo = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_vehicleno);
			vh.tvKuangFa = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_kuangfa);
			vh.tvQianShou = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_qianshoou);
			vh.tvKuangFaTime = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_kuangfa_time);
			vh.tvQianShouTime = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_qianshoou_time);
			vh.ivStatus = (ImageView) convertView
					.findViewById(R.id.iv_item_tongji_status);
			vh.tvYunFei = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_yunfei);
			vh.tvMoney = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_money);
			vh.tvCurrentYunfei = (TextView) convertView
					.findViewById(R.id.tv_item_tongji_current_money);
			vh.btAction = (Button) convertView
					.findViewById(R.id.bt_item_tongji_action);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		ChengYunTongJi cheng = chengList.get(position);

		final int ticketId = cheng.Id;
		final int ticketStatus = cheng.ticketStatus;
		int packageId = cheng.packageId;
		int departmentId = cheng.departmentId;
		int infoDepartPackagePriceId = cheng.infoDepartPackagePriceId;
		String ticketWeightInit = cheng.ticketWeightInit;
		String ticketWeightReach = cheng.ticketWeightReach;
		String vehicleNo = cheng.vehicleNo;
		String tenantName = cheng.tenantName;
		String loadTime = cheng.loadTime;
		String signTime = cheng.signTime;
		String shoudPayMoney = cheng.shoudPayMoney;
		String cutMoney = cheng.cutMoney;
		String payMoney = cheng.payMoney;
		int ticketPriceType = cheng.ticketPriceType;
		String intransitMoney = cheng.intransitMoney;
		String waitPayMoney = cheng.waitPayMoney;
		String mTicketPrice = cheng.TicketPrice;
		int mIsSettleType = cheng.IsSettleType;
		String mTenantAliasesname = cheng.TenantAliasesname;
		final String ticketCode = cheng.ticketCode;
		

		if (StringUtils.isStrNotNull(mTenantAliasesname)) {
			vh.tvJingJiRen.setText(context
					.getString(R.string.employer_jingjiren_name1)
					+ mTenantAliasesname
					+ context.getString(R.string.employer_jingjiren_name2));
		} else {
			vh.tvJingJiRen.setText(tenantName);
		}

		if (StringUtils.isStrNotNull(mTicketPrice)) {
			vh.tvCurrentYunfei.setText("当前运费：" + mTicketPrice + "元/吨");
		}

		if (StringUtils.isStrNotNull(vehicleNo)) {
			vh.tvVehicleNo.setText(vehicleNo);
		}

		vh.tvKuangFa.setText(ticketWeightInit);
		vh.tvQianShou.setText(ticketWeightReach);

		if (StringUtils.isStrNotNull(loadTime)) {
			loadTime = StringUtils.formatModify(loadTime);
			vh.tvKuangFaTime.setText("(" + loadTime + ")");
		} else {
			vh.tvKuangFaTime.setText("");
		}

		if (StringUtils.isStrNotNull(signTime)) {
			signTime = StringUtils.formatModify(signTime);
			vh.tvQianShouTime.setText("(" + signTime + ")");
		} else {
			vh.tvQianShouTime.setText("");
		}

		if (ticketPriceType == 1) {
			vh.tvYunFei.setText("在途");
			if (StringUtils.isStrNotNull(intransitMoney)){
				vh.tvMoney
						.setText(Html.fromHtml("<font color='#ff8811'>"
								+ intransitMoney
								+ "</font><font color='#909090'>元</font>"));
			} else {
				vh.tvMoney
						.setText(Html.fromHtml("<font color='#ff8811'>"
								+ "0.00"
								+ "</font><font color='#909090'>元</font>"));
			}
		} else if (ticketPriceType == 2 || ticketPriceType == 0) {
			vh.tvYunFei.setText("待结");
			if (StringUtils.isStrNotNull(waitPayMoney)){
				vh.tvMoney.setText(Html.fromHtml("<font color='#ff8811'>"
						+ waitPayMoney + "</font><font color='#909090'>元</font>"));
			} else {
				vh.tvMoney.setText(Html.fromHtml("<font color='#ff8811'>"
						+ "0.00" + "</font><font color='#909090'>元</font>"));
			}
		} else if (ticketPriceType == 3) {
			vh.tvYunFei.setText("已结");
			if (StringUtils.isStrNotNull(payMoney)){
				vh.tvMoney.setText(Html.fromHtml("<font color='#ff8811'>"
						+ payMoney + "</font><font color='#909090'>元</font>"));
			} else {
				vh.tvMoney.setText(Html.fromHtml("<font color='#ff8811'>"
						+ "0.00" + "</font><font color='#909090'>元</font>"));
			}
		}

		if (ticketStatus == 1) {
			vh.ivStatus.setImageResource(R.drawable.waybill_daizhixing);
			vh.btAction.setText("查看");
			vh.tvYunFei.setText("派车");
			vh.tvMoney.setText(Html.fromHtml("<font color='#ff8811'>" + "装运中"
					+ "</font>"));
		} else if (ticketStatus == 4) {
			vh.ivStatus.setImageResource(R.drawable.waybill_ticket_daishouhuo);
			vh.btAction.setText("查看");
		} else if (ticketStatus == 6 || ticketStatus == 5) {
			vh.ivStatus.setImageResource(R.drawable.waybill_daijiesuan);
			if (mIsSettleType == 0 || mIsSettleType == 2) {
				vh.btAction.setText("结算");
			} else if (mIsSettleType == 1) {
				vh.btAction.setText("运费确认中");
			}
		} else if (ticketStatus == 8) {
			vh.ivStatus.setImageResource(R.drawable.waybill_yijiesuan);
			vh.btAction.setText("查看");
		}

		// 根据动作跳转不同页面
		vh.btAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.e("TAG", "---ticketStatus-----" + ticketStatus);
				// 根据按钮上显示的状态显示不同的界面
				if (ticketStatus == 1 || ticketStatus == 4 || ticketStatus == 5 || ticketStatus == 6) {
					//友盟统计首页
					mUmeng.setCalculateEvents("business_center_fragment");

					//友盟统计首页
					mUmeng.setCalculateEvents("waybill_management_click_item_settlement");
					// 待审核和已结算跳转到结算界面
					UploadOrderAuditActivity.newInvoke(context,
							String.valueOf(ticketId), false,
							String.valueOf(ticketStatus), ticketCode);
				} else {
					//友盟统计首页
					mUmeng.setCalculateEvents("waybill_management_click_item_toview");
					// 跳转到待换票、待装运、待收货界面
					UploadOrderActivity.invoke(context,
							String.valueOf(ticketId),
							String.valueOf(ticketStatus), true, ticketCode);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView tvJingJiRen;
		TextView tvVehicleNo;
		TextView tvKuangFa;
		TextView tvQianShou;
		TextView tvKuangFaTime;
		TextView tvQianShouTime;
		ImageView ivStatus;
		TextView tvYunFei;
		TextView tvMoney;
		TextView tvCurrentYunfei;
		Button btAction;
	}
}
