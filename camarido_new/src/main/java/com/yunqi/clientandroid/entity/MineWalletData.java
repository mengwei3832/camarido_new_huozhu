package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的页面 获取钱包数据
 * @date 15/12/9
 */
public class MineWalletData extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 330329936507711981L;

	public String accountBalance;// 账户余额
	public String accountBalanceLock;// 锁定的金额
	public String freight;// 待结运费
	public String totalIncome;// 总收入
	public String type;// 文件类型

}
