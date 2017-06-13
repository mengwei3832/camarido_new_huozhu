package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取企业信息
 * @date 16/03/25
 */
public class CompanyRegisteredInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 1466468001532851135L;
	public String name;// 姓名
	public String idCode;// 身份证号
	public String comName;// 企业名称
	public String comStatus; // 是否企业认证：0：未认证，1：认证中，2：已认证，3：认证失败
	public String businessLicenseImgId;// 营业执照的id
	public String businessLicenseImgUrl;// 营业执照的路径
	public String businessLicenseImgStatus;// 营业执照的状态
	public String authorizationLetterImgId;// 授权书的id
	public String authorizationLetterImgUrl;// 授权书的路径
	public String authorizationLetterImgStatus;// 授权书的状态
	public String handIdCodeImgId;// 手持证件照的id
	public String handIdCodeImgUrl;// 手持证件照的路径
	public String handIdCodeImgStatus;// 手持证件照的状态

	// 新增字段
	public String businessLicenseImgUrl800;// 营业执照大图
	public String authorizationLetterImgUrl800;// 授权书大图
	public String handIdCodeImgUrl800;// 手持身份证大图

}
