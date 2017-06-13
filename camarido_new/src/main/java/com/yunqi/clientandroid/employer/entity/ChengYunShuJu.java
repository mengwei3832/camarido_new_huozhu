package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class ChengYunShuJu extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -9018514059438416169L;

	public int Id; // 操作id
	public int TicketId; // 运单id
	public String TicketOperator; // 操作人
	public int TicketOperationType; // 操作类型 30：矿发；40：签收
	public String CreateTime; // 时间
	public float TicketWeightInit; // 矿发吨数
	public float TicketWeightReach; // 签收吨数
	public boolean IsSettle; // 是否结算 false：显示吨数，true：显示钱
	public double TicketSettleMount;// 结算金额

}
