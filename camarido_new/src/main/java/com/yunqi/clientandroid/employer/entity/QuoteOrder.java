package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:报价单列表实体类
 * @ClassName: QuoteOrder
 * @author: mengwei
 * @date: 2016-6-29 下午2:04:54
 * 
 */
public class QuoteOrder extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6793699989816419192L;

	public String id; // 报价单id
	public String infoDepartPackagePriceCode; // 报价单号
	public String packageId; // 包id
	public int tenantId; // 发包方Id
	public String departmentId; // 信息部id
	public String vehiclesCount; // 车数
	public String price; // 价格
	public int priceStatus; // 信息部报价状态：0：已生效；1：已作废
	public int bidding; // 信息部议价状态：1：已报价；2：已议价；3：已改价；4：已下单；5：已生效
	public int payStatus; // 信息部议价单支付状态：0：未付款；1：已付款；2：部分付款
	public int status; // 信息部议价单状态：0：已生效；1：已作废
	public String memo; // 备注
	public String tenantName; // 公司名称
	public String tenantShortname; // 公司简称
	public String tenantTel; // 公司电话
	public String extensionNumber; // 分机号
	public String systemTime; // 现在的时间
	public int customOperation; // 报价单锁定状态：0：未申请客服仲裁；1：锁定；2:解除锁定

	public String TicketOrderCount;
	public String TicketSettleCount;
	public String EffectiveTime;
	public String CreateTime;
	public String UpdateTime;
	public String TenantAliasesname;
}
