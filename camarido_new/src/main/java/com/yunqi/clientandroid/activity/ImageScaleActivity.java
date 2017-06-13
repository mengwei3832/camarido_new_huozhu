package com.yunqi.clientandroid.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.photoview.ImageScaleAdapter;

/**
 * 
 * @Description:class 将图片进行放大
 * @ClassName: ImageScaleActivity
 * @author: zhm
 * @date: 2016-4-14 上午10:38:52
 * 
 */
public class ImageScaleActivity extends BaseActivity {
	private ViewPager viewPager;
	private ImageScaleAdapter adapter;
	private String imageUrl;// 存放图片的URL

	@Override
	protected int getLayoutId() {
		return R.layout.activity_image_scale;
	}

	@Override
	protected void initView() {

		// 初始化titileBar
		initActionBar();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从企业认证页面传过来的数据

		if (bundle != null && bundle.containsKey("imageUrl")) {
			imageUrl = bundle.getString("imageUrl");
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		adapter = new ImageScaleAdapter(imageUrl);
		viewPager.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	// 初始化标题栏的方法
	private void initActionBar() {
		// 隐藏ActionBar
		ActionBar bar = getActionBar();
		bar.hide();
	}

	// 本界面的跳转的方法
	public static void invoke(Context activity, String imageUrl) {
		Intent intent = new Intent();
		intent.setClass(activity, ImageScaleActivity.class);
		intent.putExtra("imageUrl", imageUrl);// 传图片的路径
		activity.startActivity(intent);
	}

}
