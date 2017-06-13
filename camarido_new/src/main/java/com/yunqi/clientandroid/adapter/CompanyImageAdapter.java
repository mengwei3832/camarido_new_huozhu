package com.yunqi.clientandroid.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.GridViewKeybroadAdapter.ViewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @Description:class 公司的详情的适配器
 * @ClassName: CompanyImageAdapter
 * @author: zhm
 * @date: 2016-4-5 上午9:39:50
 * 
 */
public class CompanyImageAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	private DisplayImageOptions option;

	public CompanyImageAdapter(List<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		option = new DisplayImageOptions.Builder().cacheInMemory(true).build();
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return list != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String data = list.get(position).toString();
		ViewHolder h;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.gridview_company_item, null);
			h.iv_company_imageView = (ImageView) convertView
					.findViewById(R.id.iv_company_imageView);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(data) && data != null) {
			ImageLoader.getInstance().displayImage(data,
					h.iv_company_imageView, option);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView iv_company_imageView;
	}

}
