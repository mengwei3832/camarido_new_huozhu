package com.yunqi.clientandroid.employer.util;

import java.util.Comparator;

import com.yunqi.clientandroid.employer.entity.TimeCollection;

public class ComparatorUser implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		TimeCollection t1 = (TimeCollection) lhs;
		TimeCollection t2 = (TimeCollection) rhs;
		
		int flag = t1.EffectiveTime.compareTo(t2.EffectiveTime);
		return flag;
	}

}
