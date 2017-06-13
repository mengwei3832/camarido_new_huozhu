package com.yunqi.clientandroid.entity;

public class ChooseAddressItem extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6360686640074103818L;

	public int Id;// 地址ID
	public int TenantId;// 公司ID
	public String TenantName;// 公司名称
	public String TenantShortName;// 公司简称
	public int AddressId;// 地址关联关系
	public String AddressCustomName;// 地址名称
	public String AddressCustomMemo;// 备注
	public String AddressAsStart;// 是否作为起始地址
	public String AddressAsEnd;// 是否作为终止地址
	public int ProvinceRegionId;// 省份ID
	public int CityRegionId;// 城市ID
	public int SubRegionId;// 地址ID
	public String Addressdetail;// 详细地址
	public String Longitude;// 出发地经度
	public String Latitude;// 出发地维度
	public String Provicename;// 省份名称
	public String Cityname;// 城市名称
	public String Areaname;// 区名称
	public String AddressName;// 地址名称

}
