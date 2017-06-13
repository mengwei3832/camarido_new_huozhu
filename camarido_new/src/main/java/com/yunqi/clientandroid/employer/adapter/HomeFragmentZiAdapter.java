package com.yunqi.clientandroid.employer.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.ActiveActivity;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.employer.entity.HomeFragmentZixun;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @Description:class 首页最新资讯的适配器
 * @ClassName: HomeFragmentZiAdapter
 * @author: zhm
 * @date: 2016-5-13 下午5:49:12
 * 
 */
public class HomeFragmentZiAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> mBaoList;

	public HomeFragmentZiAdapter(Context mContext,
								 ArrayList<String> mBaoList) {
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
		return mBaoList != null ? mBaoList.size() : null;
	}

	@Override
	public long getItemId(int position) {
		return mBaoList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		HomeFragmentZixun mHomeFragmentZixun = mBaoList.get(position);

		String titleString = mBaoList.get(position);// 文章标题

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.employer_adapter_zixun, null);
			viewHolder.tvHomeTitleName = (TextView) convertView
					.findViewById(R.id.tv_zixun);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvHomeTitleName.setText(Html.fromHtml(titleString));

		return convertView;

	}

	private class ViewHolder {
		TextView tvHomeTitleName;
	}

}
