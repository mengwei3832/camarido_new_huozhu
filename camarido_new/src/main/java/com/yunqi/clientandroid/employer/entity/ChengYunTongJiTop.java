package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class ChengYunTongJiTop extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -2913108388200810137L;

	public int BeforeExecuteCount; // 待执行数量
	public int BeforeSettlementCount; // 待结算数量
	public int SettledCount; // 已结算数量
	public int VehicleCount; // 车数
	public String BeginAddress; // 开始地址
	public String EndAddress; // 结束地址

	public String BeginCity;
	public String BeginRegion;
	public String EndCity;
	public String EndRegion;
	public String CarRange;
	public String OnTheWayCount;// 在途中车辆
}
