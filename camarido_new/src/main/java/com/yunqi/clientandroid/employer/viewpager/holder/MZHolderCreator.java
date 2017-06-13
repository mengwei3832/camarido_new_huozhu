package com.yunqi.clientandroid.employer.viewpager.holder;

/**
 * Created by mengwei on 2017/6/1.
 */

public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
