package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 副账户信息
 * 
 * @ClassName: AccountEntity
 * @date: 2016年11月22日 上午10:17:22
 */
public class AccountEntity extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -3438851728346547162L;

	public int MasterUserId; // 主账户用户id
	public int ViceUserId; // 副账户用户id
	public String UserName; // 用户名
	public int AppId; // 角色分配id
	public String TenantName; // 公司名称
}
