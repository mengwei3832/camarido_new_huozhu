package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的 界面获取各种状态的订单数量
 * @date 15/12/9
 */
public class MineTicketCount extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 8044720992813533791L;

	public String beforeExecuteCount;// 待执行订单数量
	public String executingCount;// 执行中订单数量
	public String executedCount;// 已完成订单数量

}
