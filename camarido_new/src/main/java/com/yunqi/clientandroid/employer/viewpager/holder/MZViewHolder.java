package com.yunqi.clientandroid.employer.viewpager.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by mengwei on 2017/6/1.
 */

public interface MZViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
