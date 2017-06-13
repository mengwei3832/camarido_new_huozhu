package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class EmployerAddressInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -5106442975037426982L;

	public int Id;// 地址ID
	public int TenantId;// 公司ID
	public String TenantName;// 公司名称
	public String TenantShortName;// 公司简称
	public int AddressId;// 地址关联关系
	public String AddressCustomName;// 地址名称
	public String AddressCustomMemo;// 备注
	public boolean AddressAsStart;// 是否作为起始地址
	public boolean AddressAsEnd;// 是否作为终止地址
	public int ProvinceRegionId;// 省份ID
	public int CityRegionId;// 城市ID
	public int SubRegionId;// 地址ID
	public String Addressdetail;// 详细地址
	public String Longitude;// 出发地经度
	public String Latitude;// 出发地维度
	public String provicename;// 省份名称
	public String cityname;// 城市名称
	public String areaname;// 区名称
	public String AddressName;// 地址名称
}
