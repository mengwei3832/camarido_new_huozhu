package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 推送标签
 * @date 15/12/29
 */
public class PushInfo extends IDontObfuscate {
	private static final long serialVersionUID = -6182076916703179896L;

	// 0：按照useraccount推送，1：按照Topic推送，2：按照regid推送，3：按照alias推送。
	public int pushDataType;
	public String receiverMark;
}
