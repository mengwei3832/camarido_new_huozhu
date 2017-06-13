package com.yunqi.clientandroid.photoview;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;

public class ImageScaleAdapter extends PagerAdapter {
	private String imageUrl;

	public ImageScaleAdapter(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(CamaridoApp.getContext(),
				R.layout.adapter_image_scale, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

		final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
		ImageLoader.getInstance().displayImage(imageUrl, imageView,
				ImageLoaderOptions.options, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						attacher.update();// 应该在ImageView加载完图片的时候,更新ImageView
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});

		container.addView(view);
		return view;
	}

}
