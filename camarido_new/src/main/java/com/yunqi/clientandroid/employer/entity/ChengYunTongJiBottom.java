package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class ChengYunTongJiBottom extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 8768373966600770063L;

	public int TicketCount; // 车数
	public String InitWeighSum; // 矿发总吨数
	public String ReachWeighSum; // 签收总吨数
	public String ShouldPayMoneySum;// 应结运费
	public String TruePayMoneySum; // 已结运费

	public String IntransitMoney; // 在途运费
	public String WaitPayMoney; // 待结运费

}
