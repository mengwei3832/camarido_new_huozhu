package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.R.id;
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
 * @Description class description 发包方订单过程Adapter
 * @date 16/1/18
 */
public class ModifyOrderAdapter extends ArrayAdapter<ModifyListItem> {

	private Context mContext;
	private List<ModifyListItem> mList;
	private String packageBeginName;// 起始地名称
	private String packageEndName;// 目的地名称
	private String ticketId;// 订单id
	private String id;
	private ModifyTicketListItemOnClick modifyTicketListItemOnClick;
	private ModifyTicketBoHuiOnClick modifyTicketBoHuiOnClick;

	public ModifyOrderAdapter(Context context, List<ModifyListItem> list,
			String packageBeginName, String packageEndName, String createTime,
			ModifyTicketListItemOnClick modifyTicketListItemOnClick,
			ModifyTicketBoHuiOnClick modifyTicketBoHuiOnClick) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.packageBeginName = packageBeginName;
		this.packageEndName = packageEndName;
		this.modifyTicketListItemOnClick = modifyTicketListItemOnClick;
		this.modifyTicketBoHuiOnClick = modifyTicketBoHuiOnClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_modify_list_employer, null);
		}

		ModifyAdapterListViewHolder holder = ModifyAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		ModifyListItem modifyListItem = mList.get(position);

		id = String.valueOf(modifyListItem.id); // 操作ID
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
		String createTime = modifyListItem.CreateTime;// 创建时间
		final String ticketOperationPicUrlImg800 = modifyListItem.TicketOperationPicUrlImg800;
		final int ticketOperationStatus = modifyListItem.TicketOperationStatus;

		// 给驳回原因赋值
		if (!TextUtils.isEmpty(ticketMemo) && ticketMemo != null) {
			holder.tvModifyBoYuanYin.setText("驳回原因:" + ticketMemo);
		}

		// 设置订单执行状态
		if (ticketOperationType != null && ticketOperationType.equals("20")) {
			holder.tvDocuments.setText("操作人：");
			holder.tvWeight.setText("吨    数:");
			holder.ivUploadagain.setImageResource(R.drawable.modify_pickup);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("30")) {
			holder.tvDocuments.setText("操作人：");
			holder.tvWeight.setText("吨    数:");
			holder.ivUploadagain.setImageResource(R.drawable.modify_shipment);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {
			holder.tvDocuments.setText("操作人：");
			holder.tvWeight.setText("吨    数:");
			holder.ivUploadagain.setImageResource(R.drawable.modify_sign);
		}

		// //根据订单操作状态来显示
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
		// // holder.tvModifyBoYuanYin.setVisibility(View.GONE);
		// }

		// 设置根据执行状态显示操作人
		if (!TextUtils.isEmpty(ticketOperator) && ticketOperator != null) {
			holder.tvTickcode.setText(ticketOperator);
		}

		// 设置吨数
		if (!TextUtils.isEmpty(ticketWeight) && ticketWeight != null) {
			holder.tvWeightCount.setText(ticketWeight + "吨");
		}

		// 设置创建时间
		if (!TextUtils.isEmpty(createTime) && createTime != null) {
			String formatModify = StringUtils.formatModify(createTime);
			holder.tvCreateTime.setText(formatModify);
		}

		// 设置图片
		if (!TextUtils.isEmpty(ticketOperationPicUrl)
				&& ticketOperationPicUrl != null) {
			ImageLoader.getInstance().displayImage(ticketOperationPicUrl,
					holder.ivShowPhoto, ImageLoaderOptions.options);
		}

		final int posi = position - 1;
		final View view = convertView;
		Log.e("TAG", "操作人ID--------------" + id);
		holder.tvModifyShen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("TAG", "操作人ID审核--------------" + id);
				modifyTicketListItemOnClick.onClick(view, posi, id);
			}
		});

		holder.tvModifyboHui.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("TAG", "操作人ID驳回--------------" + id);
				modifyTicketBoHuiOnClick.onClick(view, posi, id);
			}
		});

		// TODO 点击跳转到重新上传资料界面
		holder.ivShowPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ticketOperationStatus == 1) {
					UploadOrderDocumentActivity.invoke(mContext, ticketId,
							ticketOperationType);
				} else {
					ImageScaleActivity.invoke(mContext,
							ticketOperationPicUrlImg800);
				}
			}
		});

		return convertView;
	}

	static class ModifyAdapterListViewHolder {
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

		public ModifyAdapterListViewHolder(View convertView) {
			llModifyShen = (LinearLayout) convertView
					.findViewById(R.id.ll_modify_shen_employer);
			tvModifyShen = (TextView) convertView
					.findViewById(R.id.tv_modify_shenhe_employer);
			tvModifyboHui = (TextView) convertView
					.findViewById(R.id.tv_modify_bo_employer);
			llModifyYiShen = (LinearLayout) convertView
					.findViewById(R.id.ll_modify_jie_employer);
			tvModifyShenRen = (TextView) convertView
					.findViewById(R.id.tv_modify_sname_employer);
			llModifyBo = (LinearLayout) convertView
					.findViewById(R.id.ll_modify_bo_employer);
			// tvModifyBoYuanYin = (TextView) convertView
			// .findViewById(R.id.tv_modify_bohui_yuanyin);
			tvModifyBoRen = (TextView) convertView
					.findViewById(R.id.tv_modify_boname_employer);
			ivShowPhoto = (ImageView) convertView
					.findViewById(R.id.iv_mofidy_showPhoto_employer);
			ivUploadagain = (ImageView) convertView
					.findViewById(R.id.iv_modify_uploadagain_employer);

			tvDocuments = (TextView) convertView
					.findViewById(R.id.tv_modify_documents_employer);

			tvTickcode = (TextView) convertView
					.findViewById(R.id.tv_modify_tickcode_employer);

			tvWeight = (TextView) convertView
					.findViewById(R.id.tv_modify_weight_employer);

			tvWeightCount = (TextView) convertView
					.findViewById(R.id.tv_modify_weightCount_employer);

			tvCreateTime = (TextView) convertView
					.findViewById(R.id.tv_modify_createTime_employer);

		}

		public static ModifyAdapterListViewHolder getHolder(View convertView) {
			ModifyAdapterListViewHolder holder = (ModifyAdapterListViewHolder) convertView
					.getTag();
			if (holder == null) {
				holder = new ModifyAdapterListViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
