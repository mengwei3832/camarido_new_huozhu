package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 有数据天的集合实体
 */
public class TimeCollection extends IDontObfuscate {

	/** 
	 * @Fields serialVersionUID : 
	 */ 
	private static final long serialVersionUID = 1577768064687276795L;

	public String EffectiveTime;

	public String getEffectiveTime() {
		return EffectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		EffectiveTime = effectiveTime;
	}

	@Override
	public String toString() {
		return "TimeCollection [EffectiveTime=" + EffectiveTime + "]";
	}
}
