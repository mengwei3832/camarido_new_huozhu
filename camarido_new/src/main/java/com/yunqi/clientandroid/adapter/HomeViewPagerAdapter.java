package com.yunqi.clientandroid.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.employer.activity.ShowBannerArticalActivity;
import com.yunqi.clientandroid.entity.BannerInfo;

/**
 * 首页viewpager adapter
 */
public class HomeViewPagerAdapter extends PagerAdapter {
	private List<BannerInfo> imageViewList;
	private Context mContext;
	private DisplayImageOptions options;

	public HomeViewPagerAdapter(List<BannerInfo> list, Context context) {
		this.imageViewList = list;
		this.mContext = context;

		// 显示图片的配置
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public void setList(List<BannerInfo> list) {
		this.imageViewList = list;
	}

	@Override
	public int getCount() {
		return imageViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * container 就是ViewPager object 就是当前需要被移除的View
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// 把ImageView添加到ViewPager中, 并且把ImageView返回回去.
		String imageUrl = imageViewList.get(position).ImageUrl;
		ImageView image = new ImageView(mContext);
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		ImageLoader.getInstance().displayImage(imageUrl, image, options);
		container.addView(image);
		return image;
	}
}
