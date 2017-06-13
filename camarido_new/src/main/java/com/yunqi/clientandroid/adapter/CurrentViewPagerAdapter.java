package com.yunqi.clientandroid.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yunqi.clientandroid.view.BasePager;

public class CurrentViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<BasePager> mPagers;

	public CurrentViewPagerAdapter(ArrayList<BasePager> pagers, Context context) {
		this.mContext = context;
		this.mPagers = pagers;
	}

	@Override
	public int getCount() {
		return mPagers.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		BasePager basePager = mPagers.get(position);
		// 创建界面
		container.addView(basePager.rootView);
		// 加载数据
		return basePager.rootView;
	}

}
