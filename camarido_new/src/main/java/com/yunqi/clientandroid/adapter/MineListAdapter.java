package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.MineListItem;
import com.yunqi.clientandroid.utils.MinelistHelper;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 个人中心列表的adapter
 * @date 15/11/24
 */
public class MineListAdapter extends ArrayAdapter<MineListItem> {

	private Context mContext;
	private List<MineListItem> mMineList;

	public MineListAdapter(Context context, List<MineListItem> mineList) {
		super(context, 0, mineList);
		this.mContext = context;
		this.mMineList = mineList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MineListItem mineListItem = mMineList.get(position);
		String status = mineListItem.status;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_mine_list, null);

			viewHolder.ivImage = (ImageView) convertView
					.findViewById(R.id.iv_mine_image);
			viewHolder.tvItem = (TextView) convertView
					.findViewById(R.id.tv_mine_item);
			viewHolder.ivGap = (ImageView) convertView
					.findViewById(R.id.iv_mine_gap);
			viewHolder.llItem = (LinearLayout) convertView
					.findViewById(R.id.ll_mine_item);
			viewHolder.viewLine = convertView.findViewById(R.id.view_mine_line);
			viewHolder.tvLogout = (TextView) convertView
					.findViewById(R.id.tv_login_out);
			viewHolder.ivCertification = (ImageView) convertView
					.findViewById(R.id.iv_mine_certification);
			// 右边箭头
			viewHolder.ivArrow = (ImageView) convertView
					.findViewById(R.id.iv_mine_arrow);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mineListItem.mineType == MinelistHelper.TYPE_BACKGROUND) { // 间隔
			viewHolder.llItem.setVisibility(View.GONE);
			viewHolder.ivGap.setVisibility(View.VISIBLE);
			viewHolder.viewLine.setVisibility(View.GONE);
			viewHolder.tvLogout.setVisibility(View.GONE);

		} else if (mineListItem.mineType == MinelistHelper.TYPE_LOGOUT) { // 退出登录
			viewHolder.llItem.setVisibility(View.GONE);
			viewHolder.ivGap.setVisibility(View.GONE);
			viewHolder.viewLine.setVisibility(View.VISIBLE);
			viewHolder.tvLogout.setVisibility(View.VISIBLE);
			// 隐藏箭头
			viewHolder.ivArrow.setVisibility(View.GONE);
		} else { // 其他条目
			viewHolder.ivImage.setImageResource(mineListItem.imageResId);
			viewHolder.tvItem.setText(mineListItem.itemName);
			viewHolder.llItem.setVisibility(View.VISIBLE);
			viewHolder.ivGap.setVisibility(View.GONE);
			if (mineListItem.mineType == MinelistHelper.TYPE_ISDRIVER) {
				viewHolder.viewLine.setVisibility(View.VISIBLE);
				// 设置认证状态
				if (status != null && status.equals("2")) {
					viewHolder.ivCertification
							.setImageResource(R.drawable.certification_ok);
				} else {
					viewHolder.ivCertification
							.setImageResource(R.drawable.certification_fail);
				}

				viewHolder.ivCertification.setVisibility(View.VISIBLE);
			} else if (mineListItem.mineType == MinelistHelper.TYPE_ISREAL) {
				viewHolder.viewLine.setVisibility(View.VISIBLE);
				// 设置认证状态
				if (status != null && status.equals("2")) {
					viewHolder.ivCertification
							.setImageResource(R.drawable.certification_ok);
				} else {
					viewHolder.ivCertification
							.setImageResource(R.drawable.certification_fail);
				}
				viewHolder.ivCertification.setVisibility(View.VISIBLE);
			} else if (mineListItem.mineType == MinelistHelper.TYPE_MY_WALLET) {
				// 我的钱包
				viewHolder.viewLine.setVisibility(View.GONE);
				viewHolder.ivCertification.setVisibility(View.GONE);
			} else if (mineListItem.mineType == MinelistHelper.TYPE_ATTENTION) {
				// 我的关注
				viewHolder.viewLine.setVisibility(View.GONE);
				viewHolder.ivCertification.setVisibility(View.GONE);
			} else {
				viewHolder.viewLine.setVisibility(View.VISIBLE);
				viewHolder.ivCertification.setVisibility(View.GONE);
			}
			viewHolder.tvLogout.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView ivImage;
		private ImageView ivGap;
		private TextView tvItem;
		private TextView tvLogout;
		private LinearLayout llItem;
		// private RelativeLayout rlLogout;
		private View viewLine;
		private ImageView ivCertification;
		// 新增
		private ImageView ivArrow;// 右边箭头

	}
}
