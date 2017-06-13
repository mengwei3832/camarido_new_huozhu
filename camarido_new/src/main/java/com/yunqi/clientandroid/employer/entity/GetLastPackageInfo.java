package com.yunqi.clientandroid.employer.entity;

/**
 * 
 * @Description:最新的包消息
 * @ClassName: GetLastPackageInfo
 * @author: chengtao
 * @date: 2016年5月20日 下午3:14:58
 * 
 */
public class GetLastPackageInfo {
	public String Id; // 包Id
	public String ContractId; // 合同Id
	public String ContractCode; // 合同编号
	public String TenantId; // 租户Id
	public String PakcageCode; // 我方编号
	public String PackageTenantCode; // 客户方编号
	public String PackageStatus; // 状态 0：启用，1：停用
	public String PackagePubTime; // 发布时间
	public String PackageSettlementType; // 结算方式 0：定期；1、实时
	public String PackageBeginName; // 出发地名称
	public String PackageBeginCity; // 出发地省市Id
	public String PackageBeginProvinceText; // 出发地省份名称
	public String PackageBeginCityText; // 出发地市描述
	public String PackageBeginLongitude; // 出发地经度
	public String PackageBeginLatitude; // 出发地维度
	public String PackageBeginAddress; // 出发地详细地址
	public String PackageEndName; // 目的地名称
	public String PackageEndCity; // 目的地省市Id
	public String PackageEndProvinceText; // 目的地省份名称
	public String PackageEndCityText; // 目的地市描述
	public String PackageEndLongitude; // 目的地经度
	public String PackageEndLatitude; // 目的地维度
	public String PackageEndAddress; // 目的地详细地址
	public String PackageDistance; // 距离
	public String PackagePrice; // 运价
	public String PackagePriceType; // 运价方式 0：/吨，1：/吨*公里，2：/车数
	public String PackageGoodsType; // 煤品Id
	public String PackageGoodsTypeText; // 煤品
	public String PackageGoodsKind; // 煤种Id
	public String PackageGoodsKindText; // 煤种
	public String PackageGoodsCalorific; // 发热量
	public String PackageCount; // 包的运单量
	public String OrderCount; // 已运运单量
	public String PackageStartTime; // 包有效期开始时间
	public String PackageEndTime; // 包有效期结束时间
	public String PackageMemo; // 备注
	public String PackageType; // 包类型 0：普通包，1：竞价包，2：定向指派
	public String Appraisal; // 估价
	public String Subsidy; // 补助
	public String CategoryId; // 货品分类Id
	public String CategoryName; // 货品分类名称
	public String PackageRoadToll; // 过路费
	public String PackageRecommendPath; // 推荐路线
	public String IsFocused; // 是否已关注
	public String IsNeedInviteCode; // 是否锁定（即需要推荐码）
	public String PackageGoodsPrice; // 货值价格
	public String TmcPhone; // 驻矿联系电话
	public String StcPhone; // 签收联系电话
	public String LoadingFee; // 装车费
	public String UnloadingFee; // 卸车费
	public String TdscPhone; // 发货方联系电话
	public String TlscPhone; // 物流方联系电话
	public String PcsPhone; // 平台客服电话
	public String IsPayFor; // 是否付款
	public String IsPayIn; // 是否缴纳保证金
	public String TenantName; // 公司全称
	public String TenantShortName; // 公司简称
	public String InsuranceType; // 0：无保险 1：平台送保险 2：自己购买保险
	public String NeedInvoice; // 是否需要开票
}
