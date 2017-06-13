package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:结算详情实体类
 * @ClassName: BillingDetail
 * @author: mengwei
 * @date: 2016-6-27 下午4:39:23
 * 
 */
public class BillingDetail extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 8231081773168480597L;

	public int ticketId; // 订单Id
	public String TicketCode; // 单号
	public String ticketWeightInit; // 矿发吨数
	public String ticketWeightReach; // 签收吨数
	public String ticketWeightShortfall; // 亏损吨数
	public String freightPrice; // 运费单价
	public String goodsPrice; // 货品单价
	public String shortfallMoney; // 亏损金额
	public String freightMoney; // 运费金额
	public String ticketExpectedAmount; // 应结金额
	public String ticketSettleAmount; // 实结金额
	public int settleType; // 0：可以线上线下支付；10：只能线上（余额）支付；20：只能线下（现金）支付
	public String memo;
	public String settletime; // 结算时间

	// 新增
	public String StartAddressName;// 起始地址名称
	public String StartCityName;// 起始市级名称
	public String StartSubName;// 起始区县名称
	public String EndAddressName;// 目的地址名称
	public String EndCityName;// 目的市级名称
	public String EndSubName;// 目的区县名称

	public String InfoDerpartTenant;// 信息部公司名称
	public String VehicleNo;// 车牌号
	public String PackageStartTime;// 起始时间
	public String PackageEndTime;// 截止时间
	public String TicketLoadTime;// 矿发时间
	public String TicketSignTime;// 签收时间

	public String packageLoseRate;
	public String InfoDerpartTenantAliasesname;
	public String PolicyNo;// 保险单号
	public String CategoryName;// 货品种类
	public String PackageBeginAddress;// 包开始详细地址
	public String PackageEndAddress;// 包结束详细地址
	public String TicketAmount;//运费
	public String ExcessWeight;//超额途损（吨数）
	public String ExcessAmount;//超额途损（运费）
	public String ResonWeight;//合理途损（吨数）
	public int ShortFallType;//途损方式
}
