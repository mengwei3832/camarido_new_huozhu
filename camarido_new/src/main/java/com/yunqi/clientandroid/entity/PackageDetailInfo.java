package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 包的订单详情
 * @date 15/11/27
 */
public class PackageDetailInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -13431438810105164L;

	public String packageCount;
	public int packagePubTime;
	public String packageBeginCityText;
	public int packageEndCity;
	public String packageMemo;
	public String packageBeginAddress;
	public boolean isSameCity;
	public boolean isNeedInviteCode;
	public String packagePriceType;
	public String packageRecommendPath;
	public String packageEndCityText;
	public String appraisal;
	public int packageGoodsKind;
	public int packageBeginCity;
	public int tenantId;
	public int categoryId;
	public String packageType;
	public String packageBeginLongitude;
	public String packageBeginLatitude;
	public String packageEndLatitude;
	public String packageEndLongitude;
	public String packageStartTime;
	public String packageRoadToll;
	public String packageTenantCode;
	public String packageEndAddress;
	public String packageDistance;
	public String categoryName;
	public String packageBeginProvinceText;
	public int contractId;
	public String packageEndProvinceText;
	public int packageGoodsType;
	public String subsidy;
	public int packageSettlementType;
	public String packagePrice;
	public String packageGoodsCalorific;
	public String orderCount;
	public String packageEndName;
	public String packageEndTime;
	public boolean isFocused;
	public int id;
	public String packageGoodsKindText;
	public String packageBeginName;
	public int packageStatus;
	public String packageGoodsTypeText;
	public String pakcageCode;
	// 新增字段
	public String packageGoodsPrice;// 货值价格
	public String tmcPhone;// 驻矿联系电话
	public String tdscPhone;// 发包方电话
	public String stcPhone;// 签收联系电话
	public String loadingFee;// 装车费
	public String unloadingFee;// 卸车费
	public String pcsPhone;// 平台客服电话
	// 新增字段
	public boolean isPayFor;// 是否付款
	public boolean isPayIn;// 是否缴纳保证金
	public String tenantName;// 公司全称
	public String tenantShortName;// 公司简称
	public int insuranceType;// 0：无保险 1：平台送保险 2：自己购买保险
	public boolean needInvoice;// 是否需要开票

	public int orderCountByCurrentUser;
}
