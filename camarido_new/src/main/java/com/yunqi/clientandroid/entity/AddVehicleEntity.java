package com.yunqi.clientandroid.entity;

/**
 * @Description:添加车辆的实体类
 * @ClassName: AddVehicleEntity
 * @author: zhm
 * @date: 2016-5-27 下午2:02:31
 * 
 */
public class AddVehicleEntity extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 5100197257594145906L;

	public int Id; // 车辆Id
	public int VehicleOwnerId; // 车主
	public int VehicleStatus; // 车辆状态。0：未验证，1：启用，2：不通过，3：禁用
	public String VehicleStatusText; // 车辆状态显示值

}
