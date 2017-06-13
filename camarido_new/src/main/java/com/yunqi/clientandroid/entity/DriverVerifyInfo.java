package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 司机认证接口返回信息
 * @date 15/11/21
 */
public class DriverVerifyInfo extends IDontObfuscate {
	private static final long serialVersionUID = -7299851823274108099L;

	public int userId;
	public String userName;
	public int licenceImg;
	public String licenceTime;
	public String licenceImgUrl;
	public String licenceImgName;
	public String verifyRank;

}
