package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:行业资讯列表实体类
 * @ClassName: HangYeZiXun
 * @author: chengtao
 * @date: 2016-7-7 下午3:13:22
 * 
 */
public class HangYeZiXun extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -2900566690552348491L;

	public int id;
	public String messageShowTimeBegin;
	public String MessageShowTimeEnd;
	public int MessageTargetCategory;
	public int MessageReadCount;
	public String MessageAuthor;
	public String MessageTitle;
	public String MessageTitleImgIndex;
	public String MessageTitleImgUrl;
	public String MessageAbstract;// 消息摘要
	public String MessageContent;
	public String MessageLink;
	public String MessageTag;

}
