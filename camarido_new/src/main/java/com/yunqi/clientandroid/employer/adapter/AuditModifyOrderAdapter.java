package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.ImageScaleActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderDocumentActivity;
import com.yunqi.clientandroid.employer.util.ModifyTicketBoHuiOnClick;
import com.yunqi.clientandroid.employer.util.ModifyTicketListItemOnClick;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方待审核订单过程Adapter
 * @date 16/1/18
 */
public class AuditModifyOrderAdapter extends ArrayAdapter<ModifyListItem> {

	private Context mContext;
	private List<ModifyListItem> mList;
	private String packageBeginName;// 起始地名称
	private String packageEndName;// 目的地名称
	private String ticketId;// 订单id
	private String ticketStatus;// 订单执行状态
	private String id;

	public AuditModifyOrderAdapter(Context mContext, List<ModifyListItem> mList) {
		super(mContext, 0, mList);
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_modify_list_employer, null);
		}

		AuditAdapterListViewHolder holder = AuditAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		ModifyListItem modifyListItem = mList.get(position);

		id = String.valueOf(modifyListItem.id);
		ticketId = String.valueOf(modifyListItem.TicketId);// 订单Id
		String ticketMemo = modifyListItem.TicketMemo;// 备注
		int ticketOperationPicIndex = modifyListItem.TicketOperationPicIndex;// 图片Id
		String ticketOperationPicName = modifyListItem.TicketOperationPicName;// 图片名称
		String ticketOperationPicUrl = modifyListItem.TicketOperationPicUrl;// 图片Url
		String ticketOperator = modifyListItem.TicketOperator;// 操作人
		String ticketRelationCode = modifyListItem.TicketRelationCode;// 相关单号
		String ticketWeight = String.valueOf(modifyListItem.TicketWeight);// 吨数
		final String ticketOperationType = String
				.valueOf(modifyListItem.TicketOperationType);// 执行状态
		String ticketSettleAmount = String
				.valueOf(modifyListItem.TicketSettleAmount);// 实结金额
		String ticketSettleMemo = modifyListItem.TicketSettleMemo;// 结算备注
		String createTime = modifyListItem.CreateTime;// 创建时间
		final String ticketOperationPicUrlImg800 = modifyListItem.TicketOperationPicUrlImg800;
		int ticketOperationStatus = modifyListItem.TicketOperationStatus;// 订单操作状态：0：未审核；1：审核未通过；2：已审核

		// 给驳回原因赋值
		// if (ticketMemo != null && !TextUtils.isEmpty(ticketMemo)) {
		// holder.tvModifyBoYuanYin.setText("驳回原因:"+ticketMemo);
		// }
		//
		// if (ticketOperator != null && !TextUtils.isEmpty(ticketOperator)) {
		// holder.tvModifyBoRen.setText(ticketOperator);
		// holder.tvModifyShenRen.setText(ticketOperator);
		// }

		// 根据订单操作状态来显示
		// if (ticketOperationStatus == 0) {
		//
		// holder.llModifyShen.setVisibility(View.VISIBLE);
		// holder.llModifyBo.setVisibility(View.GONE);
		// holder.llModifyYiShen.setVisibility(View.GONE);
		// holder.tvModifyBoYuanYin.setVisibility(View.GONE);
		// } else if (ticketOperationStatus == 1) {
		// holder.llModifyShen.setVisibility(View.GONE);
		// holder.llModifyBo.setVisibility(View.VISIBLE);
		// holder.llModifyYiShen.setVisibility(View.GONE);
		// holder.tvModifyBoYuanYin.setVisibility(View.VISIBLE);
		// } else if (ticketOperationStatus == 2) {
		// holder.llModifyShen.setVisibility(View.GONE);
		// holder.llModifyBo.setVisibility(View.GONE);
		// holder.llModifyYiShen.setVisibility(View.VISIBLE);
		// holder.tvModifyBoYuanYin.setVisibility(View.GONE);
		// }

		// final int posi = position;
		// final View view = convertView;
		// holder.tvModifyShen.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Log.e("TAG", "---------审核----------");
		// modifyTicketListItemOnClick.onClick(v, posi, id);
		// }
		// });
		// holder.tvModifyboHui.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Log.e("TAG", "---------驳回----------");
		// modifyTicketListItemOnClick.onClick(v, posi, id);
		// }
		// });

		// 设置图片放大
		holder.ivShowPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isStrNotNull(ticketOperationPicUrlImg800)) {
					ImageScaleActivity.invoke(mContext,
							ticketOperationPicUrlImg800);
				}
			}
		});

		// 设置订单执行状态
		if (ticketOperationType != null && ticketOperationType.equals("30")) {
			// holder.tvDocuments.setText("操作人:" + ticketOperator);
			// holder.tvWeight.setText("吨    数:");
			holder.ivUploadagain.setImageResource(R.drawable.zhuanghuodan);

			// 设置根据执行状态显示订单号
			if (!TextUtils.isEmpty(ticketOperator) && ticketOperator != null) {
				holder.tvTickcode.setText(ticketOperator);
			}

			// 设置吨数
			if (!TextUtils.isEmpty(ticketWeight) && ticketWeight != null) {
				holder.tvWeightCount.setText(StringUtils
						.saveTwoNumber(ticketWeight) + "吨");
			}

			// 设置图片
			if (!TextUtils.isEmpty(ticketOperationPicUrl)
					&& ticketOperationPicUrl != null) {
				ImageLoader.getInstance().displayImage(ticketOperationPicUrl,
						holder.ivShowPhoto, ImageLoaderOptions.options);
			}

			// } else if (ticketOperationType != null
			// && ticketOperationType.equals("30")) {
			// holder.tvDocuments.setText("操作人:" + ticketOperator);
			// // holder.tvWeight.setText("吨    数:");
			// if (ticketStatus != null && ticketStatus.equals("5")) {
			// holder.ivUploadagain
			// .setImageResource(R.drawable.modify_shipment);
			// }
			//
			// // 设置根据执行状态显示订单号
			// if (!TextUtils.isEmpty(ticketRelationCode)
			// && ticketRelationCode != null) {
			// // holder.tvTickcode.setText(ticketRelationCode);
			// }
			//
			// // 设置吨数
			// if (!TextUtils.isEmpty(ticketWeight) && ticketWeight != null) {
			// holder.tvWeightCount.setText(ticketWeight + "吨");
			// }
			//
			// // 设置图片
			// if (!TextUtils.isEmpty(ticketOperationPicUrl)
			// && ticketOperationPicUrl != null) {
			// ImageLoader.getInstance().displayImage(ticketOperationPicUrl,
			// holder.ivShowPhoto, ImageLoaderOptions.options);
			// }
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {
			// holder.tvDocuments.setText("操作人:" + ticketOperator);
			// holder.tvWeight.setText("吨    数:");
			holder.ivUploadagain.setImageResource(R.drawable.qianshoudan);

			// 设置根据执行状态显示订单号
			if (!TextUtils.isEmpty(ticketOperator) && ticketOperator != null) {
				holder.tvTickcode.setText(ticketOperator);
			}

			// 设置吨数
			if (!TextUtils.isEmpty(ticketWeight) && ticketWeight != null) {
				holder.tvWeightCount.setText(StringUtils
						.saveTwoNumber(ticketWeight) + "吨");
			}

			// 设置图片
			if (!TextUtils.isEmpty(ticketOperationPicUrl)
					&& ticketOperationPicUrl != null) {
				ImageLoader.getInstance().displayImage(ticketOperationPicUrl,
						holder.ivShowPhoto, ImageLoaderOptions.options);
			}
		}
		// else if (ticketOperationType != null
		// && ticketOperationType.equals("60")) {
		// holder.tvDocuments.setText("操作人:" + ticketOperator);
		// // holder.tvWeight.setText("备        注:");
		// holder.ivShowPhoto.setImageResource(R.drawable.settlement_money);
		//
		// // 设置根据执行状
		// if (!TextUtils.isEmpty(ticketOperator) && ticketOperator != null) {
		// holder.tvTickcode.setText(ticketOperator);
		// }
		//
		// // 设置结算备注
		// if (!TextUtils.isEmpty(ticketWeight) && ticketWeight != null) {
		// holder.tvWeightCount.setText(ticketWeight);
		// }
		// }

		// 设置创建时间
		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			String formatModify = StringUtils.formatPerform(createTime);
			Log.e("TAG", "----------formatModify----------" + formatModify);
			holder.tvCreateTime.setText(formatModify);
		}

		// 当时待审核状态是可以进行图片重新上传
		// if (ticketOperationStatus == 1) {
		// // TODO 点击跳转到重新上传资料界面
		// holder.ivShowPhoto.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!TextUtils.isEmpty(ticketId) && ticketId != null
		// && !TextUtils.isEmpty(ticketOperationType)
		// && ticketOperationType != null) {
		// UploadOrderDocumentActivity.invoke(mContext, ticketId,
		// ticketOperationType);
		// }
		// }
		// });
		//
		// } else {
		// // TODO 点击跳转到重新上传资料界面
		// holder.ivShowPhoto.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// ImageScaleActivity.invoke(mContext,
		// ticketOperationPicUrlImg800);
		// }
		// });
		// }

		return convertView;
	}

	static class AuditAdapterListViewHolder {
		ImageView ivShowPhoto;
		ImageView ivUploadagain;
		TextView tvDocuments;
		TextView tvTickcode;
		TextView tvWeight;
		TextView tvWeightCount;
		TextView tvCreateTime;
		LinearLayout llModifyShen;
		TextView tvModifyShen;
		TextView tvModifyboHui;
		LinearLayout llModifyYiShen;
		TextView tvModifyShenRen;
		LinearLayout llModifyBo;
		TextView tvModifyBoYuanYin;
		TextView tvModifyBoRen;

		public AuditAdapterListViewHolder(View convertView) {
			// llModifyShen = (LinearLayout) convertView
			// .findViewById(R.id.ll_modify_shen_employer);
			// tvModifyShen = (TextView) convertView
			// .findViewById(R.id.tv_modify_shenhe_employer);
			// tvModifyboHui = (TextView) convertView
			// .findViewById(R.id.tv_modify_bohui_employer);
			// llModifyYiShen = (LinearLayout) convertView
			// .findViewById(R.id.ll_modify_jie_employer);
			// tvModifyShenRen = (TextView) convertView
			// .findViewById(R.id.tv_modify_sname_employer);
			// llModifyBo = (LinearLayout) convertView
			// .findViewById(R.id.ll_modify_bo_employer);
			// // tvModifyBoYuanYin = (TextView) convertView
			// // .findViewById(R.id.tv_modify_bohui_yuanyin);
			// tvModifyBoRen = (TextView) convertView
			// .findViewById(R.id.tv_modify_boname_employer);
			ivShowPhoto = (ImageView) convertView
					.findViewById(R.id.iv_mofidy_showPhoto_employer);
			ivUploadagain = (ImageView) convertView
					.findViewById(R.id.iv_modify_uploadagain_employer);

			// tvDocuments = (TextView) convertView
			// .findViewById(R.id.tv_modify_documents_employer);

			tvTickcode = (TextView) convertView
					.findViewById(R.id.tv_modify_tickcode_employer);

			// tvWeight = (TextView) convertView
			// .findViewById(R.id.tv_modify_weight_employer);

			tvWeightCount = (TextView) convertView
					.findViewById(R.id.tv_modify_weightCount_employer);

			tvCreateTime = (TextView) convertView
					.findViewById(R.id.tv_modify_createTime_employer);

		}

		public static AuditAdapterListViewHolder getHolder(View convertView) {
			AuditAdapterListViewHolder holder = (AuditAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new AuditAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
