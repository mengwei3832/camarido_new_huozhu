package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 时间的集合
 * @Description:
 * @ClassName:	TimeDateEntity 
 * @author:	chengtao
 * @date:	2016-11-16 上午11:37:13 
 *
 */
public class TimeDateEntity extends IDontObfuscate {

	/** 
	 * @Fields serialVersionUID : 
	 */ 
	private static final long serialVersionUID = 800374525756156346L;
	
	private String mDate;

	public TimeDateEntity(String mDate) {
		super();
		this.mDate = mDate;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	@Override
	public String toString() {
		return "TimeDateEntity [mDate=" + mDate + "]";
	}

}
