package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 
 * @Description:class 首页最新资讯的实体类
 * @ClassName: HomeFragmentNew
 * @author: zhm
 * @date: 2016-5-13 下午4:24:00
 * 
 */
public class HomeFragmentZixun extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -4370591883355658973L;

	public int Id; // 活动信息ID
	public String MessageTitle;// 活动信息标题

	public HomeFragmentZixun() {

	}

	public HomeFragmentZixun(String messageTitle) {
		super();
		MessageTitle = messageTitle;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getMessageTitle() {
		return MessageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		MessageTitle = messageTitle;
	}

}
