package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:公共点的实体类
 * @ClassName: ShipHeighWay
 * @author: mengwei
 * @date: 2016-6-22 下午6:56:13
 * 
 */
public class ShipHeighWay extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -6803250794419273520L;

	public int Id; // 公共点地址ID
	public String PublicPointName; // 公共点地址
	public int ProvinceRegionId; // 省id
	public int CityRegionId; // 市id
	public int SubRegionId; // 区县id
	public String PublicPointDetail; // 详细地址
	public String Longitude; // 经度
	public String Latitude; // 纬度
	public int PublicPointStatus; // 状态0：启用；1：停用
	public String Provicename; // 省份
	public String Cityname; // 城市
	public String Areaname; // 区县
}