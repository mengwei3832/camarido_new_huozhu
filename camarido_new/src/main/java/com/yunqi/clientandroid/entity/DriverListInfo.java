package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取司机列表信息
 * @date 15/11/24
 */
public class DriverListInfo extends IDontObfuscate {
	private static final long serialVersionUID = 2145240560879060988L;

	public String opCount;// 司机用这辆车的拉运次数
	public String nickName;// 昵称
	public String driverName;// 驾驶证上姓名
	public int vehicleStatus;// 车辆审核状态。0：未审核，1：启用，2：不通过，3：停用
	public String userName;// 用户名
	public int userId;// 用户Id
	public int vehicleDefaultDriver;// 默认司机
	public String userPhone;// 手机号
	public String loginCount;// 司机登录次数
	public String name;// 真实姓名
	public int isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
	public int verifyRank;
	public int vehicleId;// 车辆Id
	public String vehicleNo;// 车牌号
	public String id;// VehicleDriver 的 Id
	public Boolean driverIsDeleted;// 司机是否逻辑删除
	public int driverId;// 司机Id
	public boolean vehicleIsOnWay;
	public boolean driverIsOnWay;
	public String headPortraitUrl;// 头像Url

}
