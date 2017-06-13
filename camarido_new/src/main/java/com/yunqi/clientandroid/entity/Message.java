package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 消息model
 * @date 15/12/7
 */
public class Message extends IDontObfuscate {
	private static final long serialVersionUID = 4611092743643931969L;
	public String id;// Id
	public String messageShowTimeBegin;// 开始显示时间
	public String messageShowTimeEnd;// 结束显示时间
	public int messageTargetCategory;// 1：消息，2：活动，3：关注
	public int messageReadCount;// 已阅读次数
	public int messageTitleImgIndex;// 题图图片Id
	public String messageAuthor;// 消息作者
	public String messageTitle;// 消息标题
	public String messageTitleImgUrl;// 题图URL
	public String messageAbstract;// 消息摘要
	public String messageContent;// 内容
	public String messageLink;// 文件链接
	public String messageTag;

}
