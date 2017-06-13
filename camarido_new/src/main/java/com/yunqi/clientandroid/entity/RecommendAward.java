package com.yunqi.clientandroid.entity;

import com.yunqi.clientandroid.http.request.IRequest;

/**
 * 
 * @Description:class 推荐奖励的实体类
 * @ClassName: RecommendAward
 * @author: zhm
 * @date: 2016-4-7 下午1:14:20
 * 
 */
public class RecommendAward extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 3609384822556205539L;

	public double rewardRecordMoney;
	public int rewardRecordType;
	public String UserPhone;
	public String VehicleNum;

}
