package com.yunqi.clientandroid.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用viewpager适配器
 * 
 * 
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
	protected Context mContext;
	protected List<T> dataList;

	public BasePagerAdapter(Context context, List<T> list) {
		mContext = context;
		dataList = list;
	}

	@Override
	public int getCount() {
		if (dataList == null)
			return 0;
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		T t = dataList.get(position);
		View view = fetchItemView(t);
		container.addView(view);
		return view;
	}

	/**
	 * 获取view
	 * 
	 * @param t
	 * @return
	 */
	public abstract View fetchItemView(T t);
}
