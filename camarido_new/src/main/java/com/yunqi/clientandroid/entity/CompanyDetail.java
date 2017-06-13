package com.yunqi.clientandroid.entity;

/**
 * 
 * @Description:class 公司详情的实体类
 * @ClassName: CompanyDetail
 * @author: zhm
 * @date: 2016-3-31 下午6:12:26
 * 
 */
public class CompanyDetail extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -7742936606446274574L;

	public int id; // 包Id
	public int tenantId; // 租户Id
	public String tenantName;// 公司全称
	public String tenantShortname;// 公司简称
	public String creatTime;// 注册时间
	public String tenantLegalMoney;// 注册资金
	public String tenantRegisterRegion;// 地址
	public String tenantMemo;// 公司说明
	public int packageCount;// 发包总数
	public int ticketCount;// 运单总数
	public int ticketAmount;// 结款总金额
	public int ticketOverCount;// 运单完成总数
	public int ticketDoingCount;// 运单执行总数
}
