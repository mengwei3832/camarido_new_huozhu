package com.yunqi.clientandroid.employer.util;

import android.content.Context;

import java.util.ArrayList;

/**
 * 经数组转化为集合
 * Created by mengwei on 2017/4/5.
 */

public class ArrayToListUtil {

    private Context mContext;

    public ArrayToListUtil(Context context) {
        mContext = context;
    }

    public ArrayList<String> getHomeCrashList(String[] strs){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < strs.length; i ++){
            list.add(strs[i]);
        }
        return list;
    }
}
