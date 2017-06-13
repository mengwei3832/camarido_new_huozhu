package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:煤种类型ID
 * @ClassName: PinZhongId
 * @author: chengtao
 * @date: 2016-6-20 上午11:20:15
 * 
 */
public class PinZhongId extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 1291752045292551837L;

	private int packageGoodsCategoryId;

	public int getPackageGoodsCategoryId() {
		return packageGoodsCategoryId;
	}

	public void setPackageGoodsCategoryId(int packageGoodsCategoryId) {
		this.packageGoodsCategoryId = packageGoodsCategoryId;
	}

	@Override
	public String toString() {
		return "PinZhongId [packageGoodsCategoryId=" + packageGoodsCategoryId
				+ "]";
	}

}
