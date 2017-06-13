package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.BasePagerAdapter;

/**
 * 引导页 date:2015年11月4日
 */
public class GuideActivity extends Activity implements
		ViewPager.OnPageChangeListener {

	private ViewPager viewPager;
	private RadioGroup radioGroup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		FrameLayout frameLayout = new FrameLayout(this);

		viewPager = new ViewPager(this);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(params);

		radioGroup = new RadioGroup(this);

		params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

		radioGroup.setLayoutParams(params);
		radioGroup.setOrientation(LinearLayout.HORIZONTAL);

		frameLayout.addView(viewPager);

		setContentView(frameLayout);

		init();

	}

	/**
	 * 初始化布局
	 */
	@SuppressWarnings("unchecked")
	private void init() {

		for (int i = 0; i < 4; i++) {
			RadioButton radioButton = new RadioButton(this);

			radioButton.setButtonDrawable(null);

			radioButton.setCompoundDrawables(
					this.getResources().getDrawable(
							R.drawable.guide_point_selector), null, null, null);

			radioGroup.addView(radioButton);
		}
		((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

		final ArrayList<Integer> integers = new ArrayList<Integer>();

		integers.add(R.drawable.guide_01);
		integers.add(R.drawable.guide_02);
		integers.add(R.drawable.guide_03);

		viewPager.setAdapter(new BasePagerAdapter(this, integers) {

			@Override
			public View fetchItemView(Object o) {
				ImageView imageView = new ImageView(GuideActivity.this);

				imageView.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));

				imageView.setImageResource((Integer) o);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);

				if ((Integer) o == R.drawable.guide_03) {
					imageView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Intent intent = new Intent(GuideActivity.this,
									LoginActicity.class);
							// intent.putExtra("isGuide" , true);
							startActivity(intent);
							finish();

						}
					});
				}

				return imageView;
			}
		});

		viewPager.setOnPageChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageSelected(int i) {
		((RadioButton) radioGroup.getChildAt(i)).setChecked(true);
	}

	@Override
	public void onPageScrollStateChanged(int i) {

	}
}