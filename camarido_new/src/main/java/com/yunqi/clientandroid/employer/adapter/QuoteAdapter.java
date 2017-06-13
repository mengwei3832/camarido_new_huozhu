package com.yunqi.clientandroid.employer.adapter;

import java.util.ArrayList;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyActicity;
import com.yunqi.clientandroid.employer.activity.QuoteYijiaActivity;
import com.yunqi.clientandroid.employer.entity.QuoteOrder;
import com.yunqi.clientandroid.employer.util.QuoteQuXiaoItemOnClick;
import com.yunqi.clientandroid.employer.util.QuoteXiadanItemOnClick;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteAdapter extends BaseAdapter {
	private ArrayList<QuoteOrder> ticketList;
	private Context context;
	private LayoutInflater layoutInflater;
	private PerformListItemOnClick yiJiaBack;
	private QuoteXiadanItemOnClick xiaDanBack;
	private QuoteQuXiaoItemOnClick quXiaoBack;
	private SpManager spManager;
	private UmengStatisticsUtils mUmeng;

	public QuoteAdapter(ArrayList<QuoteOrder> ticketList, Context context,
			PerformListItemOnClick yiJiaBack,
			QuoteXiadanItemOnClick xiaDanBack, QuoteQuXiaoItemOnClick quXiaoBack) {
		super();
		this.ticketList = ticketList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.yiJiaBack = yiJiaBack;
		this.xiaDanBack = xiaDanBack;
		this.quXiaoBack = quXiaoBack;
		spManager = SpManager.instance(context);
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	@Override
	public int getCount() {
		return ticketList != null ? ticketList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return ticketList != null ? ticketList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return ticketList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = layoutInflater.inflate(
					R.layout.employer_item_quote_view, null);
			viewHolder.tvQuoteBaoJia = (TextView) convertView
					.findViewById(R.id.tv_item_quote_baojia);
			viewHolder.tvQuoteCheShu = (TextView) convertView
					.findViewById(R.id.tv_item_quote_cheshu);
			viewHolder.tvQuoteWuLiu = (TextView) convertView
					.findViewById(R.id.tv_item_quote_wuliu);
			viewHolder.tvBaojiaDate = (TextView) convertView
					.findViewById(R.id.tv_item_quote_baojia_date);
			viewHolder.ivStatus = (ImageView) convertView
					.findViewById(R.id.iv_item_quote_status);
			viewHolder.btYijia = (Button) convertView
					.findViewById(R.id.bt_item_quote_yijia);
			viewHolder.btXiadan = (Button) convertView
					.findViewById(R.id.bt_item_quote_xiadan);
			// viewHolder.btStop = (Button) convertView
			// .findViewById(R.id.bt_item_quote_stop);
			viewHolder.tvJieCar = (TextView) convertView
					.findViewById(R.id.tv_item_quote_jie);
			viewHolder.btGrayShow = (Button) convertView
					.findViewById(R.id.bt_item_quote_gray);
			viewHolder.btCall = (Button) convertView
					.findViewById(R.id.bt_item_quote_call);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		QuoteOrder quoteOrder = ticketList.get(position);

		final String id = quoteOrder.id;// 报价单ID
		String price = quoteOrder.price;// 报价
		String vehiclesCount = quoteOrder.vehiclesCount;// 车数
		final String tenantName = quoteOrder.tenantName;// 公司名称
		int bidding = quoteOrder.bidding;// 信息部议价状态：1：已报价；2：已议价；3：已改价；4：已下单；5：已生效
		int status = quoteOrder.status;// 信息部议价单状态：0：已生效；1：已作废
		final String tenantTel = quoteOrder.tenantTel;// 公司电话
		final String packageId = quoteOrder.packageId;
		String extensionNumber = quoteOrder.extensionNumber;
		int customOperation = quoteOrder.customOperation;// 报价单锁定状态：0：未申请客服仲裁；1：锁定；2:解除锁定

		String mTicketOrderCount = quoteOrder.TicketOrderCount;
		String mTicketSettleCount = quoteOrder.TicketSettleCount;
		String mEffectiveTime = quoteOrder.EffectiveTime;// 报价单生效时间
		String mCreateTime = quoteOrder.CreateTime;// 报价时间
		String mUpdateTime = quoteOrder.UpdateTime;// 结束时间
		final String mTenantAliasesname = quoteOrder.TenantAliasesname;
		final String mDepartId = quoteOrder.departmentId;
		final String mPackageId = quoteOrder.packageId;

		// Log.e("TAG", shiJianCha);

		if (bidding == 5 || bidding == 7) {
			viewHolder.tvQuoteBaoJia.setText(Html
					.fromHtml("运价:<font color='#ff4444'>" + price
							+ "</font>元/吨"));
		} else {
			viewHolder.tvQuoteBaoJia.setText(Html
					.fromHtml("当前报价:<font color='#ff4444'>" + price
							+ "</font>元/吨"));
		}
		if (StringUtils.isStrNotNull(mTenantAliasesname)) {
			viewHolder.tvQuoteWuLiu.setText(context
					.getString(R.string.employer_jingjiren_name1)
					+ mTenantAliasesname
					+ context.getString(R.string.employer_jingjiren_name2));
		} else {
			viewHolder.tvQuoteWuLiu.setText(tenantName);
		}

		if (StringUtils.isStrNotNull(vehiclesCount)) {
			viewHolder.tvQuoteCheShu.setText(Html
					.fromHtml("车数：<font color='#ff4444'>" + vehiclesCount
							+ "</font>车"));
		} else {
			viewHolder.tvQuoteCheShu.setText(Html
					.fromHtml("车数：<font color='#ff4444'>0</font>车"));
		}
		
		viewHolder.btCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("quote_click_item_phone");

				/* 拨打电话 */
				if (StringUtils.isStrNotNull(tenantTel)) {
					CommonUtil.callPhoneIndirect(context, tenantTel);
				}
			}
		});
		

		if (bidding == 1) {
			viewHolder.ivStatus
					.setImageResource(R.drawable.quote_item_yibaoming);
			viewHolder.btYijia.setVisibility(View.VISIBLE);
			viewHolder.btXiadan.setVisibility(View.VISIBLE);
			// viewHolder.btStop.setVisibility(View.GONE);
			viewHolder.btGrayShow.setVisibility(View.GONE);
			viewHolder.tvQuoteCheShu.setVisibility(View.VISIBLE);
			viewHolder.tvJieCar.setVisibility(View.GONE);
			// 车数
			if (StringUtils.isStrNotNull(vehiclesCount)) {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("车数：<font color='#ff4444'>" + vehiclesCount
								+ "</font>车"));
			} else {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("车数：<font color='#ff4444'>0</font>车"));
			}
			// 报价时间
			if (StringUtils.isStrNotNull(mCreateTime)) {
				String mCreateString = StringUtils
						.formatSimpleDate(mCreateTime);
				viewHolder.tvBaojiaDate.setText("报价时间：" + mCreateString);
			} else {
				viewHolder.tvBaojiaDate.setText("");
			}
		} else if (bidding == 4) {
			viewHolder.ivStatus
					.setImageResource(R.drawable.quote_item_daishengxiao);
			viewHolder.btYijia.setVisibility(View.GONE);
			viewHolder.btXiadan.setVisibility(View.GONE);
			// viewHolder.btStop.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setText(context
					.getString(R.string.employer_activity_item_kefu));
			viewHolder.tvQuoteCheShu.setVisibility(View.VISIBLE);
			viewHolder.tvJieCar.setVisibility(View.GONE);
			// 车数
			if (StringUtils.isStrNotNull(vehiclesCount)) {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("车数：<font color='#ff4444'>" + vehiclesCount
								+ "</font>车"));
			} else {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("车数：<font color='#ff4444'>0</font>车"));
			}
			// 报价时间
			if (StringUtils.isStrNotNull(mCreateTime)) {
				String mCreateString = StringUtils
						.formatSimpleDate(mCreateTime);
				viewHolder.tvBaojiaDate.setText("报价时间：" + mCreateString);
			} else {
				viewHolder.tvBaojiaDate.setText("");
			}
		} else if (bidding == 5) {
			viewHolder.ivStatus
					.setImageResource(R.drawable.quote_item_chengyunzhong);
			viewHolder.btYijia.setVisibility(View.GONE);
			viewHolder.btXiadan.setVisibility(View.GONE);
			// viewHolder.btStop.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setText(context
					.getString(R.string.employer_activity_item_chengyun));
			viewHolder.tvQuoteCheShu.setVisibility(View.VISIBLE);
			viewHolder.tvJieCar.setVisibility(View.VISIBLE);
			// 车数
			if (StringUtils.isStrNotNull(vehiclesCount)) {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>"
								+ mTicketOrderCount + "</font>车"));
			} else {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>0</font>车"));
			}

			if (StringUtils.isStrNotNull(mTicketSettleCount)) {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>"
								+ mTicketSettleCount + "</font>车"));
			} else {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>0</font>车"));
			}
			// 报价生效时间
			if (StringUtils.isStrNotNull(mEffectiveTime)) {
				String mEffectiveString = StringUtils
						.formatSimpleDate(mEffectiveTime);
				viewHolder.tvBaojiaDate.setText("生效时间：" + mEffectiveString);
			} else {
				viewHolder.tvBaojiaDate.setText("");
			}
		} else if (bidding == 6) {
			viewHolder.ivStatus
					.setImageResource(R.drawable.quote_item_yiwancheng);
			viewHolder.btYijia.setVisibility(View.GONE);
			viewHolder.btXiadan.setVisibility(View.GONE);
			// viewHolder.btStop.setVisibility(View.GONE);
			viewHolder.btGrayShow.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setText(context
					.getString(R.string.employer_activity_item_stop));
			viewHolder.tvQuoteCheShu.setVisibility(View.VISIBLE);
			viewHolder.tvJieCar.setVisibility(View.VISIBLE);
			// 车数
			if (StringUtils.isStrNotNull(vehiclesCount)) {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>"
								+ mTicketOrderCount + "</font>车"));
			} else {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>0</font>车"));
			}
			if (StringUtils.isStrNotNull(mTicketSettleCount)) {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>"
								+ mTicketSettleCount + "</font>车"));
			} else {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>0</font>车"));
			}
			// 报价时间段
			if (StringUtils.isStrNotNull(mUpdateTime)) {
				String mCreateString = StringUtils
						.formatSimpleDate(mCreateTime);
				String mUpdateString = StringUtils
						.formatSimpleDate(mUpdateTime);
				viewHolder.tvBaojiaDate.setText("有效时间：" + mCreateString + "~"
						+ mUpdateString);
			} else {
				viewHolder.tvBaojiaDate.setText("");
			}
		} else if (bidding == 7) {
			viewHolder.ivStatus
					.setImageResource(R.drawable.quote_item_yitingyun);
			viewHolder.btYijia.setVisibility(View.GONE);
			viewHolder.btXiadan.setVisibility(View.GONE);
			// viewHolder.btStop.setVisibility(View.GONE);
			viewHolder.btGrayShow.setVisibility(View.VISIBLE);
			viewHolder.btGrayShow.setText(context
					.getString(R.string.employer_activity_item_stop));
			viewHolder.tvQuoteCheShu.setVisibility(View.VISIBLE);
			viewHolder.tvJieCar.setVisibility(View.VISIBLE);
			// 车数
			if (StringUtils.isStrNotNull(vehiclesCount)) {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>"
								+ mTicketOrderCount + "</font>车"));
			} else {
				viewHolder.tvQuoteCheShu.setText(Html
						.fromHtml("已派车：<font color='#ff4444'>0</font>车"));
			}
			if (StringUtils.isStrNotNull(mTicketSettleCount)) {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>"
								+ mTicketSettleCount + "</font>车"));
			} else {
				viewHolder.tvJieCar.setText(Html
						.fromHtml("已结算：<font color='#ff4444'>0</font>车"));
			}
			// 报价时间段
			if (StringUtils.isStrNotNull(mUpdateTime)) {
				String mCreateString = StringUtils
						.formatSimpleDate(mCreateTime);
				String mUpdateString = StringUtils
						.formatSimpleDate(mUpdateTime);
				viewHolder.tvBaojiaDate.setText("有效时间：" + mCreateString + "~"
						+ mUpdateString);
			} else {
				viewHolder.tvBaojiaDate.setText("");
			}
		}

		final int posi = position;
		final View view = convertView;

		viewHolder.btYijia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (StringUtils.isStrNotNull(id)) {
				// Log.e("TAG", "--------议价-----------");
				// yiJiaBack.onClick(view, posi, id);
				// }
				//友盟统计首页
				mUmeng.setCalculateEvents("quote_click_item_bargain");

				QuoteYijiaActivity.invoke(context, mPackageId, id, mDepartId);
			}
		});

		viewHolder.btXiadan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				xiaDanBack.onClick(view, posi, id);
			}
		});

		// viewHolder.btStop.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Log.e("TAG", "--------取消-----------");
		// if (StringUtils.isStrNotNull(mTenantAliasesname)) {
		// quXiaoBack.onClick(
		// view,
		// posi,
		// id,
		// context.getString(R.string.employer_jingjiren_name1)
		// + mTenantAliasesname
		// + context
		// .getString(R.string.employer_jingjiren_name2));
		// } else {
		// quXiaoBack.onClick(view, posi, id, tenantName);
		// }
		// }
		// });

		return convertView;
	}

	class ViewHolder {
		private TextView tvQuoteBaoJia;
		private TextView tvQuoteCheShu;
		private TextView tvQuoteWuLiu;
		private TextView tvBaojiaDate;
		private ImageView ivStatus;
		private Button btYijia;
		private Button btXiadan;
		// private Button btStop;
		private Button btGrayShow;
		private TextView tvJieCar;
		private Button btCall;
	}

}
