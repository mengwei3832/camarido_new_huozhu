package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:class 当前订单的实体类
 * @ClassName: TicketCurrentBean
 * @author: mengwei
 * @date: 2016-6-12 下午3:17:34
 * 
 */
public class TicketCurrentBean extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -2259910723236104766L;

	public int id; // 订单Id
	public String packageId; // 包ID
	public String TicketCode; // 订单号
	public int VehicleId; // 车辆Id
	public int UserIdApply; // 抢单人Id
	public int UserIdExecute; // 执行人
	public String UserName; // 执行人用户名
	public String Name; // 执行人姓名
	public String UserPhone; // 执行人电话
	public int UserIdSettle; // 结算人Id
	public int TicketStatus; // 执行状态：0：待生效；1：待执行；2：待换票；3：待装运；4：待收货；5：待审核；6：待结算；7：可领取，8：已结算；9：禁用；10：取消；
	public String TicketMemo; // 备注
	public int PackageType; // 包类型：0：普通包 1：竞价包，2：派车包
	public String PackageBeginName; // 出发地名称
	public int PackageBeginCity; // 出发地省市Id
	public String PackageBeginLongitude; // 出发地经度
	public String PackageBeginLatitude; // 出发地维度
	public String PackageBeginAddress; // 出发地详细地址
	public String PackageEndName; // 目的地名称
	public int PackageEndCity; // 目的地省市Id
	public String PackageEndLongitude; // 目的地经度
	public String PackageEndLatitude; // 目的地维度
	public String PackageEndAddress; // 目的地详细地址
	public double PackageDistance; // 距离
	public double PackagePrice; // 运价
	public int PackagePriceType; // 运价方式 0：/吨，1：/吨*公里，2：/车数
	public int PackageGoodsType; // 煤品Id
	public String PackageGoodsTypeText; // 煤品
	public int PackageGoodsKind; // 煤种Id
	public String PackageGoodsKindText; // 煤种
	public String PackageGoodsCalorific; // 发热量
	public String VehicleNo; // 车牌号
	public String CreateTime; // 创建时间
	public String packageRecommendPath; // 推荐路线
	public int CategoryId; // 品类Id
	public String CategoryName; // 品类名称
	public double PackageRoadToll; // 过路费
	public long PackageAutoSecond; // 剩余时间，竞价包的订单用到这个字段
	public String FreightPayable; // 运费
	public double ShortfallDebit; // 亏吨
	public double Subsidy; // 补助
	public String TmcPhone; // 驻矿联系电话
	public String StcPhone; // 签收联系电话
	public double LoadingFee; // 装车费
	public double UnloadingFee; // 卸车费
	public String TdscPhone; // 发货方联系电话
	public String TlscPhone; // 物流方联系电话
	public String PcsPhone; // 平台客服电话
	public boolean IsPayFor; // 是否垫付
	public boolean IsPayIn; // 是否缴纳保证金
	public int InsuranceType; // 0：无保险 1：平台送保险 2：自己购买保险
	public String TenantName; // 公司名称
	public String TenantShortName; // 公司简称
	public String PackageBeginProvinceText;// 开始省份名称
	public String PackageBeginCityText; // 开始城市名称
	public String PackageBeginCountryText; // 开始区县名称
	public String PackageEndProvinceText; // 结束省份名称
	public String PackageEndCityText; // 结束城市名称
	public String PackageEndCountryText; // 结束区县名称
	public String ActualSettleMent; // 实结运费
	public String TicketWeightInit; // 矿发吨数
	public String TicketWeightReach; // 签收吨数
	public String Price;
	public String VehicleOwnerPhone;
	public String PackageStartTime;
	public String PackageEndTime;
	public String VehicleContacts;// 跟车电话
	public String TenantTel;// 公司电话

	public int CustomOperation;// 运单锁定状态：0：未申请客服仲裁；1：锁定；2:解除锁定
	public String TicketLoadTime;// 矿发时间
	public String TicketSettleTime;// 签收时间
	public String InfoTenantName;// 信息部名称
	public int IsSettleType;// 是否申请结算：0：未申请结算；1：协商结算；2：同意结算
	public String InfoTenantAliasesname;
}
