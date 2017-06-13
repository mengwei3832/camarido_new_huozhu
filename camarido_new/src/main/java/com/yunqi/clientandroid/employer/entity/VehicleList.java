package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 
 * @Description:我的车辆接口
 * @ClassName: VehicleList
 * @author: chengtao
 * @date: 2016年6月16日 下午11:58:38
 * 
 */
public class VehicleList extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -8167577583950696188L;

	public int Id;// 公司车关系ID
	public int TenantId;// 公司ID
	public int VehicleId;// 车ID
	public int TenantVehicleType;// 公司和车关系 0：长期雇佣关系
	public String VehicleOwnerName;// 车主名称
	public String VehicleNo;// 车牌号
	public int VehicleOwnerId;// 车主ID

}
