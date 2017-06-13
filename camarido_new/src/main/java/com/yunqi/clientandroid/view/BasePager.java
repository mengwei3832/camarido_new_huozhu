package com.yunqi.clientandroid.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;

public class BasePager {

	public View rootView;// 返回给外界的布局、控件
	protected Context mContext;// 上下文对象
	protected TextView tvOrderNumber;
	protected TextView tvCarNumber;
	protected TextView tvDriverName;
	protected TextView tvCurrentOrderDate;
	protected TextView tvCurrentOrderStart;
	protected TextView tvCurrentOrderEnd;
	protected TextView tvCurrentOrderStatus;
	protected LinearLayout llCurrentOrder;

	public BasePager(Context context) {
		this.mContext = context;
		rootView = initView();
	}

	/**
	 * 返回布局、控件
	 * 
	 * @return
	 */
	protected View initView() {
		View currentPager = View.inflate(mContext,
				R.layout.viewpager_current_home, null);

		llCurrentOrder = (LinearLayout) currentPager
				.findViewById(R.id.ll_current_order);
		tvDriverName = (TextView) currentPager
				.findViewById(R.id.tv_driver_name);
		tvOrderNumber = (TextView) currentPager
				.findViewById(R.id.tv_order_number);
		tvCarNumber = (TextView) currentPager.findViewById(R.id.tv_car_number);
		tvCurrentOrderDate = (TextView) currentPager
				.findViewById(R.id.tv_pagerCurrent_date);
		tvCurrentOrderStart = (TextView) currentPager
				.findViewById(R.id.tv_pagerCurrent_start);
		tvCurrentOrderEnd = (TextView) currentPager
				.findViewById(R.id.tv_pagerCurrent_end);
		tvCurrentOrderStatus = (TextView) currentPager
				.findViewById(R.id.tv_order_status);

		return currentPager;
	}

	/**
	 * 子类加载数据，可选覆盖
	 */
	public void initData() {

	}

}
