package com.yunqi.clientandroid.entity;

/**
 * @Description:获取所有省份简称实体类
 * @ClassName: GetProvinceJian
 * @author: zhm
 * @date: 2016-5-26 上午11:23:13
 * 
 */
public class GetProvinceJian extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -5870025920073319602L;

	public int Id; // RegionModel的id，省份的Id
	public String Name; // 省份名称
	public int PId; // 父级Id
	public String ShortName; // 省份简称

}
