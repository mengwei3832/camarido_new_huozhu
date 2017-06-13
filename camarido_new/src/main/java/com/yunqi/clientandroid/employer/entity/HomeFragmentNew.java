package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 
 * @Description:class 首页最新动态的实体类
 * @ClassName: HomeFragmentNew
 * @author: zhm
 * @date: 2016-5-13 下午4:24:00
 * 
 */
public class HomeFragmentNew extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -920191094487057804L;

	public String Id; // 包ID
	public String PackagePubTime;
	public String PackageBeginAddress;
	public String PackageEndAddress;
	public int OrderCount;// 已经报过的
	public int PackageCount;// 总车数

}
