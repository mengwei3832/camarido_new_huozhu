package com.yunqi.clientandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * @Description:class 重写listView解决和ScrollView的冲突
 * @ClassName: TaskAwardListView
 * @author: zhm
 * @date: 2016-4-6 上午10:53:33
 * 
 */
public class TaskAwardListView extends ListView {

	public TaskAwardListView(Context context) {
		super(context);
	}

	public TaskAwardListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TaskAwardListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		// heightMeasureSpec=MeasureSpec.makeMeasureSpec(300,
		// MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
