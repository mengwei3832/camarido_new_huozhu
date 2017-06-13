package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的订单详情
 * @date 15/12/11
 */
public class GetVTicketByIdInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -1529060115626563837L;

	public String packageBeginCityText;// 出发地市描述
	public int packageEndCity;// 目的地省市Id
	public String packageBeginAddress;// 出发地详细地址
	public String ticketStatus;// 执行状态：0：待生效；1：待执行；2：待换票；3：待装运；4：待收货；5：待审核；6：待结算；7：可领取，8：已结算；9：禁用；10：取消；
	public int userIdSettle;// 结算人Id
	public String packagePriceType;// 运价方式 0：/吨，1：/吨*公里，2：/车数
	public String ticketCode;// 订单号
	public String userName;// 执行人用户名
	public int userIdExecute;// 执行人
	public int userIdApply;// 抢单人Id
	public String packageRecommendPath;// 推荐路线
	public String packageEndCityText;// 目的地市描述
	public String userPhone;// 执行人电话
	public int packageGoodsKind;// 煤种Id
	public String packageEndLongitude;// 目的地经度
	public int packageBeginCity;// 出发地省市Id
	public int categoryId;// 品类Id
	public double shortfallDebit;// 亏吨
	public String packageBeginLongitude;// 出发地经度
	public String freightPayable;// 运费
	public String packageEndLatitude;// 目的地维度
	public String vehicleId;// 车辆Id
	public String packageRoadToll;// 过路费
	public String packageEndAddress;// 目的地详细地址
	public String packageDistance;// 距离
	public String packageBeginLatitude;// 出发地维度
	public String categoryName;// 品类名称
	public String packageBeginProvinceText;// 出发地省份名称
	public String packageEndProvinceText;// 目的地省份名称
	public int packageGoodsType;// 煤品Id
	public String subsidy;// 补助
	public int packageAutoSecond;// 剩余时间，竞价包的订单用到这个字段
	public String packagePrice;// 运价
	public String name;// 执行人姓名
	public String packageGoodsCalorific;// 发热量
	public String ticketMemo;// 备注
	public String packageEndName;// 目的地名称
	public String createTime;// 创建时间
	public String vehicleNo;// 车牌号
	public int id;// 订单Id
	public String packageGoodsKindText;// 煤种
	public String packageBeginName;// 出发地名称
	public String packageGoodsTypeText;// 煤品
	public double ticketCalorificReach;
	public double ticketCalorificInit;
	public double ticketWeightInit;
	public double ticketWeightReach;

	// 新增字段
	public String packageGoodsPrice;// 货值价格
	public String tmcPhone;// 驻矿联系电话
	public String stcPhone;// 签收联系电话
	public String loadingFee;// 装车费
	public String unloadingFee;// 卸车费
	public String pcsPhone;// 平台客服电话
	public String vehicleContacts;// 跟车电话

	// 新增字段
	public String tenantName;// 公司名称
	public String tenantShortname;// 公司简称
	public boolean isPayFor;// 是否垫付
	public boolean isPayIn;// 是否缴纳保证金
	public int packageId;// 包ID
	public int insuranceType;// 0：无保险 1：平台送保险 2：自己购买保险

}
