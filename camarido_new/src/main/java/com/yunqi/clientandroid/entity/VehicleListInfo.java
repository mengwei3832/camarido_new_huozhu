package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 车辆信息
 * @date 15/11/21
 */
public class VehicleListInfo extends IDontObfuscate {
	private static final long serialVersionUID = -2030911761869144243L;

	public String vehicleId;// 车辆id
	public int driverId;// 司机id
	public String vehicleNo;// 车牌号
	public int vehicleOwnerId;// 车主Id
	public int vehicleStatus;// 附件Url
	public int vehicleDefaultDriver;// 默认司机Id
	public String driverCount;// 司机数量
	public int opCount;// 操作次数
	public String creditLimit;// 信用额度
	public String inCome;// 收入
	public String driverType;// 0：司机，1:车主,2经纪人
	public String vehicleImgUrl;// 列表小图标的路径
	public int starsLevel;// 星级1，一星 2，二星 3，三星
}
