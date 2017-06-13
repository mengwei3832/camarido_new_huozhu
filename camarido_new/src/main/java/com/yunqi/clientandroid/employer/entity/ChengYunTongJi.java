package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class ChengYunTongJi extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -6497414304689160554L;

	public int Id; // 运单id
	public int ticketStatus; // 运单状态
	public int packageId; // 包id
	public int departmentId; // 信息部id
	public int infoDepartPackagePriceId;// 报价单id
	public String ticketWeightInit; // 矿发
	public String ticketWeightReach; // 签收
	public String vehicleNo; // 车牌号
	public String tenantName; // 公司名称
	public String loadTime; // 矿发时间
	public String signTime; // 签收时间
	public String shoudPayMoney; // 应付金额
	public String cutMoney; // 扣款
	public String payMoney; // 已结运费

	// 新增字段
	public int ticketPriceType; // 标记
	public String intransitMoney; // 在途运费
	public String waitPayMoney; // 待结运费
	public String TicketPrice;
	public int IsSettleType;
	public String TenantAliasesname;
	public String ticketCode;
}
