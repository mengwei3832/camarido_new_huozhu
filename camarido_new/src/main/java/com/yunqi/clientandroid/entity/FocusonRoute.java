package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注路线下的包列表
 * @date 15/11/24
 */
public class FocusonRoute extends IDontObfuscate {
	private static final long serialVersionUID = -5150341137084719159L;

	public String packageCount;
	public String packagePubTime;
	public String packageBeginCityText;
	public String packageBeginProvinceText;
	public String packageMemo;
	public String packageBeginAddress;
	public String packagePriceType;
	public String packageEndCityText;
	public String packageEndProvinceText;
	public String appraisal;
	public int packageGoodsKind;
	public String packageEndLongitude;
	public int packageBeginCity;
	public int tenantId;
	public String packageType;
	public String packageBeginLongitude;
	public String packageEndLatitude;
	public String packageStartTime;
	public String packageTenantCode;
	public String packageEndAddress;
	public String packageDistance;
	public String packageBeginLatitude;
	public int packageEndCity;
	public int contractId;
	public int packageGoodsType;
	public String subsidy;
	public int packageSettlementType;
	public String packagePrice;
	public String packageGoodsCalorific;
	public String orderCount;
	public String packageEndName;
	public String packageEndTime;
	public String id;// 包的id
	public String packageGoodsKindText;
	public String packageBeginName;
	public int packageStatus;
	public String packageGoodsTypeText;
	public String pakcageCode;
	// 新增字段
	public int categoryId;
	public String categoryName;
	public String packageRoadToll;
	public String packageRecommendPath;
	public boolean isFocused;
	public boolean isNeedInviteCode;

	public boolean isPayFor;// 是否付款
	public boolean isPayIn;// 是否缴纳保证金
	public String tenantName;// 公司全称
	public String tenantShortName;// 公司简称
}
