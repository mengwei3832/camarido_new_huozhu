package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.ActiveActivity;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Description:class 首页最新bao的适配器
 * @ClassName: HomeFragmentNewAdapter
 * @author: zhm
 * @date: 2016-5-13 下午5:50:04
 * 
 */
public class HomeFragmentNewAdapter extends BaseAdapter {
	private Context mContext;
	private List<HomeFragmentNew> mBaoList;
	private boolean bExist;

	public HomeFragmentNewAdapter(Context mContext,
			List<HomeFragmentNew> mBaoList, boolean bExist) {
		super();
		this.mContext = mContext;
		this.mBaoList = mBaoList;
	}

	@Override
	public int getCount() {
		return mBaoList != null ? mBaoList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mBaoList != null ? mBaoList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return mBaoList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HomeFragmentNew mHomeFragmentNew = mBaoList.get(position);

		String hDate = StringUtils
				.formatCanReceive(mHomeFragmentNew.PackagePubTime);// 发布时间
		String hBeginAddress = mHomeFragmentNew.PackageBeginAddress;// 始发地
		String hEndAddress = mHomeFragmentNew.PackageEndAddress;// 目的地
		int hOldBao = mHomeFragmentNew.OrderCount;// 已派包
		int hSumBao = mHomeFragmentNew.PackageCount;// 总包数

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.employer_fragment_home_header_listitem, null);
			viewHolder.tvHomeDate = (TextView) convertView
					.findViewById(R.id.tv_home_date);
			viewHolder.tvHomeAddress = (TextView) convertView
					.findViewById(R.id.tv_home_address);
			viewHolder.tvHomeBaoSum = (TextView) convertView
					.findViewById(R.id.tv_home_baoSum);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvHomeDate.setText(hDate);
		viewHolder.tvHomeAddress.setText(hBeginAddress + "到" + hEndAddress);
		viewHolder.tvHomeBaoSum.setText(hOldBao + "/" + hSumBao);

		return convertView;

	}

	private class ViewHolder {
		TextView tvHomeDate;
		TextView tvHomeAddress;
		TextView tvHomeBaoSum;
	}

}
