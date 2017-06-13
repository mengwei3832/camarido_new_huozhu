package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:class 指派车辆列表的实体类
 * @ClassName: CarListZhipai
 * @author: zhm
 * @date: 2016-5-18 上午10:56:40
 * 
 */
public class CarListZhipai extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -1045882413703181186L;
	public int VehicleId; // 车ID
	public int VehicleOwnerId; // 车主ID
	public String VehicleNo; // 车牌号
	public String VehicleOwnerName; // 车主
	public int OrderCount; // 该车在该公司承运次数
}
