package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.StopPackageEntity;
import com.yunqi.clientandroid.employer.util.QuoteQuXiaoItemOnClick;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class StopPackageAdapter extends BaseAdapter {
	private Context mContext;
	private List<StopPackageEntity> stopList;
	private LayoutInflater layoutInflater;
	private QuoteQuXiaoItemOnClick quXiaoBack;

	public StopPackageAdapter(Context mContext, List<StopPackageEntity> stopList,
			QuoteQuXiaoItemOnClick quXiaoBack) {
		this.mContext = mContext;
		this.stopList = stopList;
		layoutInflater = LayoutInflater.from(mContext);
		this.quXiaoBack = quXiaoBack;
	}

	@Override
	public int getCount() {
		return stopList != null ? stopList.size() : 0;
}

	@Override
	public Object getItem(int position) {
		return stopList != null ? stopList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return stopList != null ? position : 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = layoutInflater.inflate(
					R.layout.employer_item_stop_package_view, null);

			viewHolder.tvName = obtainView(convertView, R.id.tv_item_stop_name);
			viewHolder.tvPrice = obtainView(convertView,
					R.id.tv_stop_item_zhixing_price);
			viewHolder.tvDate = obtainView(convertView, R.id.tv_stop_item_date);
			viewHolder.tvYipai = obtainView(convertView,
					R.id.tv_stop_item_yipai);
			viewHolder.tvYijie = obtainView(convertView,
					R.id.tv_stop_item_yijie);
			viewHolder.btStop = obtainView(convertView,
					R.id.bt_item_stop_ticket);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		StopPackageEntity mStopPackageEntity = stopList.get(position);

		final String mId = mStopPackageEntity.id;
		final String mAliasName = mStopPackageEntity.tenantAliasesname;
		String mPackagePrice = mStopPackageEntity.price;
		String mEffectiveTime = mStopPackageEntity.effectiveTime;
		String mTicketOrderCount = mStopPackageEntity.ticketOrderCount;
		String mOrderSettledCount = mStopPackageEntity.TicketSettleCount;
		final String mTenantName = mStopPackageEntity.TenantName;
		String mUpdateTime = mStopPackageEntity.UpdateTime;

		if (StringUtils.isStrNotNull(mAliasName)) {
			viewHolder.tvName.setText(mAliasName
					+ mContext.getString(R.string.employer_jingjiren_name2));
		} else {
			viewHolder.tvName.setText(mTenantName);
		}

		if (StringUtils.isStrNotNull(mPackagePrice)) {
			viewHolder.tvPrice.setText(Html
					.fromHtml("执行价格：<font color='#ff4444'>" + mPackagePrice
							+ "</font>元/吨"));
		} else {
			viewHolder.tvPrice.setText(Html
					.fromHtml("执行价格：<font color='#ff4444'>" + "0"
							+ "</font>元/吨"));
		}

		if (StringUtils.isStrNotNull(mUpdateTime)) {
			String mDate = StringUtils.formatSimpleDate(mUpdateTime);
			viewHolder.tvDate.setText("(生效时间：" + mDate + ")");
		} else {
			viewHolder.tvDate.setText("");
		}

		if (StringUtils.isStrNotNull(mTicketOrderCount)) {
			viewHolder.tvYipai.setText(Html
					.fromHtml("已派车：<font color='#ff4444'>" + mTicketOrderCount
							+ "</font>车"));
		} else {
			viewHolder.tvYipai.setText(Html
					.fromHtml("已派车：<font color='#ff4444'>" + "0" + "</font>车"));
		}

		if (StringUtils.isStrNotNull(mOrderSettledCount)) {
			viewHolder.tvYijie.setText(Html
					.fromHtml("已结算：<font color='#ff4444'>" + mOrderSettledCount
							+ "</font>车"));
		} else {
			viewHolder.tvYijie.setText(Html
					.fromHtml("已结算：<font color='#ff4444'>" + "0" + "</font>车"));
		}
		
		final int posi = position;
		final View view = convertView;
		
		viewHolder.btStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isStrNotNull(mAliasName)) {
					quXiaoBack.onClick(view, position, mId, mAliasName);
				} else {
					quXiaoBack.onClick(view, position, mId, mTenantName);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvPrice;
		TextView tvDate;
		TextView tvYipai;
		TextView tvYijie;
		Button btStop;
	}

	/**
	 * 
	 * @Description: 获取控件
	 * @Title:obtainView
	 * @param id
	 * @return
	 * @return:T
	 */
	private <T extends View> T obtainView(View view, int id) {
		return (T) view.findViewById(id);
	}

}
