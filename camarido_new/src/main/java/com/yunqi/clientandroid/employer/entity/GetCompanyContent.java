package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:class 获取企业信息的实体类
 * @ClassName: GetCompanyContent
 * @author: chengtao
 * @date: 2016-6-6 下午3:44:04
 * 
 */
public class GetCompanyContent extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 2151783072200923591L;

	public int id; // 用户ID
	public int tenantId; // 公司ID
	public String tenantName; // 公司名称
	public String tenantShortname; // 公司简称
	public String tenantType; // 公司类型0：平台-物流公司；1：客户-发包方
	public String userName; // 当前登录人
	public String logoUrl; // 公司logo
	public String extensionNumber; // 客服电话

}
