package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 可抢单车辆列表
 * @date 15/12/30
 */
public class GetVehicleListInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -1839660501691163015L;

	public String vehicleId;// 车辆Id
	public String vehicleNo;// 车牌号
	public boolean vehicleIsOnWay;// 车辆是否在途
	public int StarsLevel;// 星级1，一星 2，二星 3，三星

}
