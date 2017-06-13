package com.yunqi.clientandroid.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description classviewpage 和listview 相互冲突 将父view 传递到viewpage 里面
 * 
 *              使用父类的方法 parent.requestDisallowInterceptTouchEvent(true);
 * 
 *              当 requestDisallowInterceptTouchEvent 如果为true的时候 表示:父view
 *              不拦截子view的touch 事件
 * 
 *              这个方法只是改变flag
 * @date 15/11/23
 */
public class ViewPagerExpand extends ViewPager {

	int mLastMotionY;
	int mLastMotionX;

	public ViewPagerExpand(Context context) {
		super(context);
	}

	public ViewPagerExpand(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 拦截 TouchEvent
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(arg0);
	}

	// 处理 TouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(arg0);
	}

	// 因为这个执行的顺序是 父布局先得到 action_down的事件

	/**
	 * onInterceptTouchEvent(MotionEvent ev)方法，这个方法只有ViewGroup类有
	 * 如LinearLayout，RelativeLayout等 可以包含子View的容器的
	 * 
	 * 用来分发 TouchEvent 此方法 返回true 就交给本 View的 onTouchEvent处理 此方法 返回false
	 * 就交给本View的 onInterceptTouchEvent 处理
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		// 让父类不拦截触摸事件就可以了。
		this.getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);

	}
}
