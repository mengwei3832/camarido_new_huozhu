package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的订单模块三个列表返回的数据
 * @date 15/12/9
 */
public class PerformListItem extends IDontObfuscate {

	private static final long serialVersionUID = -2053499844412420978L;

	// public String packageBeginCityText;// 出发地市描述
	public double ticketCalorificReach;
	public String packageBeginAddress;// 出发地详细地址
	public String ticketStatus;// 执行状态
	public int userIdSettle;// 结算人Id
	public int packagePriceType;// 运价方式 0：/吨，1：/吨*公里，2：/车数
	public double ticketWeightInit;
	public String userName;// 执行人用户名
	public int userIdExecute;// 执行人
	public int userIdApply;// 抢单人Id
	public String userPhone;// 执行人电话
	// public String packageEndCityText;// 目的地市描述
	public int packageGoodsKind;// 煤种Id
	public String packageEndLongitude;// 目的地经度
	public int packageBeginCity;// 出发地省市Id
	public String packageBeginLongitude;// 出发地经度
	public String packageEndLatitude;// 目的地维度
	public int vehicleId;// 车辆Id
	public String packageEndAddress;// 目的地详细地址
	public String packageDistance;// 距离
	public double ticketCalorificInit;
	public String packageBeginLatitude;// 出发地维度
	public int packageAddressCity;
	public int packageGoodsType;// 煤品Id
	public double ticketWeightReach;
	public String packagePrice;// 运价
	public String name;// 执行人姓名
	public String packageGoodsCalorific;// 发热量
	public String ticketMemo;// 备注
	public String packageEndName;// 目的地名称
	public String vehicleNo;// 车牌号
	public String id;// 订单id
	public String packageGoodsKindText;// 煤种
	public String packageBeginName;// 出发地名称
	public String packageGoodsTypeText;// 煤品
	public String ticketCode;// 订单号
	public String createTime;// 创建时间
	public String packageRecommendPath;// 推荐路线
	public int categoryId;// 品类Id
	public String categoryName;// 品类名称
	public String packageRoadToll;// 过路费
	public String freightPayable;// 运费
	public String shortfallDebit;// 亏吨
	public String subsidy;// 补助
	// public String packageBeginProvinceText;// 出发地省份名称
	// public String packageEndProvinceText;// 目的地省份名称
	public String packageAutoSecond;// 剩余时间，竞价包的订单用到这个字段
	public String actualSettleMent;// 实结运费

	// 新增字段
	public int packageType;// 包类型：0：普通包 1：竞价包
	public int insuranceType;// 0：无保险 1：平台送保险 2：自己购买保险

	public String PackageBeginProvinceText;// 开始省份名称
	public String PackageBeginCityText; // 开始城市名称
	public String PackageBeginCountryText; // 开始区县名称
	public String PackageEndProvinceText; // 结束省份名称
	public String PackageEndCityText; // 结束城市名称
	public String PackageEndCountryText; // 结束区县名称

}
