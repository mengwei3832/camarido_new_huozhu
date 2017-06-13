package com.yunqi.clientandroid.entity;

/**
 * 用户信息
 * 
 * @Description:
 * @ClassName: UserInfo
 * @author: zhm
 * @date: 2015年11月20日 上午12:13:27
 * 
 */
public class UserInfo extends IDontObfuscate {
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 2233148790595289981L;

	public String userName;// 用户名
	public String userEmail;// 邮箱
	public String userPhone;// 手机
	public String nickname;// 昵称
	public String name;// 姓名
	public String gender;// 性别：0：未知，1：男，2：女
	public String birthday;// 生日
	public String iDCode;// 身份证号
	public String isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
	public String isDriver;// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
	public String age;// 年龄
	public String years;// 出生年代
	public String headPortraitUrl;// 头像URL

}
