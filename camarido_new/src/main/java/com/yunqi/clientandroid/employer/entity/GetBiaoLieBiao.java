package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:获取包列表详情信息的实体类
 * @ClassName: GetBiaoLieBiao
 * @author: zhm
 * @date: 2016-5-23 下午1:42:55
 * 
 */
public class GetBiaoLieBiao extends IDontObfuscate {
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 2439805867900766565L;

	public String Id; // 包ID
	public int ContractId; // 合同ID
	public int TenantId; // 公司ID
	public int InsuranceType; // 保险：0：无保险；1：平台送保险；2：自己购买保险
	public int PackageStatus; // 包状态：0：草稿；1：待审核；2：已发布；3：已完成; 5:已终止;
	public String PackageBeginAddress; // 开始地址
	public String PackageEndAddress; // 结束地址
	public int PackageType; // 包类型：0：一口价；1：竞价；2：定向指派
	public double PackageGoodsPrice; // 货值单价
	public double Subsidy; // 奖励
	public String PackageCount; // 总车数
	public int OrderCount; // 已报名车数
	public String PackageStartTime; // 包开始时间
	public String PackageEndTime; // 包结束时间
	public String CategoryName; // 品类名称
	public double PackagePriceOrigin; // 发包方价格
	public String PackageWeight; // 总吨数

	public String PakcageCode; // 包号
	public String PackageTenantCode; // 客户方编号--客户自己随意填写
	public String PackageRequireCode; // --NULL表示不需要邀请码,其他值需要客户输入邀请码才能抢单，4位（每位数字Or大写字母）
	public String PackagePubTime; // 发布时间
	public int PackageSettlementType; // 结算方式：1：实时；0：定期
	public int PackageBegin; // 开始地址编号
	public String PackageBeginName; // 开始地址名称
	public String PackageBeginProvince; // 开始省份
	public String PackageBeginCity; // 开始城市
	public String PackageBeginLongitude;// 开始地址经度
	public String PackageBeginLatitude; // 开始地址纬度
	public String PackageEnd; // 结束地址编号
	public String PackageEndName; // 结算地址名称
	public String PackageEndProvince; // 结束省份
	public String PackageEndCity; // 结束城市
	public String PackageEndLongitude; // 结束地址经度
	public String PackageEndLatitude; // 结束地址纬度
	public String PackageDistance; // 距离
	public String PackagePrice; // 司机方显示价格
	public int PackagePriceType; // 0：/吨；1：吨*公里；2：车数
	public double PackageRoadToll; // 过路费
	public String PackageRecommendPath; // 推荐路线
	public int PackageGoodsType; // ；煤品类型编号
	public String PackageGoodsTypeText; // 煤品名称
	public String PackageGoodsKind; // 煤种类型编号
	public String PackageGoodsKindText; // 煤种名称
	public String PackageGoodsCategoryId;// --商品/煤品
	public String PackageGoodsCalorific;// 商品、货品备注
	public String PackageAutoPrice; // 自动成交价位
	public String PackageAutoTimespan; // 报价后多久自动成交，切记不是发包后多久
	public String BAddressName;
	public String EAddressName;
	public String PackageMemo; // 备注
	public int CategoryId; // 品类
	public int FocusedTimes; // 关注次数，如果大于0，表示已关注
	public String TenantName; // 公司名称
	public String TenantShortName; // 公司简称
	public String TmcPhone; // 驻矿联系电话
	public String StcPhone;
	public double LoadingFee; // 装车费
	public double PaUnloadingFee; // 卸车费
	public String TdscPhone; // 发货方联系电话
	public String TlscPhone; // 物流方联系电话
	public String PcsPhone; // 平台客服电话
	public boolean NeedInvoice; // 0：否：1：是
	public String ContractCode; // 合同号
	public int OrderAllCount; // 已抢订单数量
	public int OrderExecutingCount; // 执行中订单数量
	public int OrderPendingCount; // 审核中订单数量
	public int OrderExecutedCount; // 已完成订单数量

	public String Quotationcount; // 执行情况
	public String ConfirmQuotationcount; // 成交
	public String OrderSettledCount; // 待结算
	public String OrderBeforeSettleCount; // 已完成运单

	public String PackageBeginProvinceText; // 开始省份名称
	public String PackageBeginCityText; // 开始城市名称
	public String PackageBeginCountryText; // 开始区县名称
	public String PackageEndProvinceText; // 结束省份名称
	public String PackageEndCityText; // 结束城市名称
	public String PackageEndCountryText; // 结束区县名称
	public int OrderInfoCount; // 已下单的车数

	// 新增字段
	public String ticketArrangeCount;
	public String price;
	public String CarRange;
	public String PackageLoseRate;
	public int ShortFallType;
	public String BeforeExecute;
	public String onTheWayCount;
}