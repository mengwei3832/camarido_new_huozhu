package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.UploadDocumentActivity;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.StringUtils;

public class ModifyAdapter extends ArrayAdapter<ModifyListItem> {

	private Context mContext;
	private List<ModifyListItem> mList;
	private String packageBeginName;// 起始地名称
	private String packageEndName;// 目的地名称
	private String ticketId;// 订单id

	public ModifyAdapter(Context context, List<ModifyListItem> list,
			String packageBeginName, String packageEndName, String createTime) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.packageBeginName = packageBeginName;
		this.packageEndName = packageEndName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_modify_list,
					null);
		}

		ModifyAdapterListViewHolder holder = ModifyAdapterListViewHolder
				.getHolder(convertView);

		// 设置数据
		ModifyListItem modifyListItem = mList.get(position);

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
		int ticketOperationStatus = modifyListItem.TicketOperationStatus;// 订单操作状态：0：未审核；1：审核未通过；2：已审核

		// 设置订单执行状态
		if (ticketOperationType != null && ticketOperationType.equals("20")) {
			holder.tvDocuments.setText("派发单");
			holder.ivUploadagain.setImageResource(R.drawable.modify_pickup);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("30")) {
			holder.tvDocuments.setText("装运单");
			holder.ivUploadagain.setImageResource(R.drawable.modify_shipment);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {
			holder.tvDocuments.setText("签收单");
			holder.ivUploadagain.setImageResource(R.drawable.modify_sign);
		}

		// 设置根据执行状态显示订单号
		if (!TextUtils.isEmpty(ticketRelationCode)
				&& ticketRelationCode != null) {
			holder.tvTickcode.setText(ticketRelationCode);
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

		if (ticketOperationStatus == 0) {

		}

		// TODO 点击跳转到重新上传资料界面
		holder.ivShowPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(ticketId) && ticketId != null
						&& !TextUtils.isEmpty(ticketOperationType)
						&& ticketOperationType != null) {
					UploadDocumentActivity.invoke(mContext, ticketId,
							ticketOperationType);
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
		TextView tvWeightCount;
		TextView tvCreateTime;

		public ModifyAdapterListViewHolder(View convertView) {
			ivShowPhoto = (ImageView) convertView
					.findViewById(R.id.iv_mofidy_showPhoto);
			ivUploadagain = (ImageView) convertView
					.findViewById(R.id.iv_modify_uploadagain);
			tvDocuments = (TextView) convertView
					.findViewById(R.id.tv_modify_documents);

			tvTickcode = (TextView) convertView
					.findViewById(R.id.tv_modify_tickcode);

			tvWeightCount = (TextView) convertView
					.findViewById(R.id.tv_modify_weightCount);

			tvCreateTime = (TextView) convertView
					.findViewById(R.id.tv_modify_createTime);

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
