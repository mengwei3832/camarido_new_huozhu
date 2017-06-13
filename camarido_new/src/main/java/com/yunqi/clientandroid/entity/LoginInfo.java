package com.yunqi.clientandroid.entity;

/**
 * 登录信息
 * 
 * @Description:
 * @ClassName: LoginInfo
 * @author: zhm
 * @date: 2015年11月20日 上午12:13:27
 * 
 */
public class LoginInfo extends IDontObfuscate {

	private static final long serialVersionUID = -2318190625404865680L;

	public String tokenValue;
	public long tokenExpires;
	public String userId;
	public String userType;
	public boolean isTempUser;

}
