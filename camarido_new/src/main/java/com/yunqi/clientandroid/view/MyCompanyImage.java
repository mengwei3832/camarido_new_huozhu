package com.yunqi.clientandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @Description:class 重写GridView，解决和ScrollView之间的冲突
 * @ClassName: MyCompanyImage
 * @author: zhm
 * @date: 2016-4-5 上午9:26:28
 * 
 */
public class MyCompanyImage extends GridView {

	public MyCompanyImage(Context context) {
		super(context);
	}

	public MyCompanyImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyCompanyImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
