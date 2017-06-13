package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享bean
 * @date 15/12/20
 */
public class Share extends IDontObfuscate {
	private static final long serialVersionUID = -2477607838575184779L;

	public String title;
	public String summary;
	public String imgurl;
	public String redirectUrl;
	public long shareTime;
	public int shareType;
	public int relationId;
}
