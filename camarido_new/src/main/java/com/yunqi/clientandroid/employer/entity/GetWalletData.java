package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 
 * @Description:查看钱包数据接口
 * @ClassName: GetWalletData
 * @author: chengtao
 * @date: 2016年6月16日 下午10:57:42
 * 
 */
public class GetWalletData extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6559958355769231630L;
	public String AccountBalance;// 账户余额
	public String AccountBalanceLock;// 锁定的金额
	public String Freight;// 待结运费
	public String DownOut;// 现金结算金额
	public String LineOut;// 余额结算金额

}
