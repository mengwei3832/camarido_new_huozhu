package com.yunqi.clientandroid.entity;

/**
 * 
 * @Description:class 新手奖励的实体类
 * @ClassName: TaskAward
 * @author: zhm
 * @date: 2016-4-7 上午9:52:35
 * 
 */
public class TaskAward extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6100232605688161681L;

	public String vehicleNum; // 车牌号
	public double rewardRecordMoneyCarOne; // 新车奖励
	public double rewardRecordMoneyCarTwo; // 首次承运奖励
	public double rewardRecordMoneyCarThree;// 二次承运奖励
	public String endTime;// 截止日期

}
