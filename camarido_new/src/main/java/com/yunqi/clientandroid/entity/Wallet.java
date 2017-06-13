package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 钱包bean
 * @date 15/12/7
 */
public class Wallet extends IDontObfuscate {
	private static final long serialVersionUID = -9058887720383931917L;

	// 账户余额
	public float accountBalance;
	// 锁定的金额
	public String accountBalanceLock;
	// 待结运费
	public String freight;
	// 总收入
	public String totalIncome;
}
