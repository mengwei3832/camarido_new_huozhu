package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 车辆详细信息
 * @date 15/11/21
 */
public class VehicleDetailInfo extends IDontObfuscate {
	private static final long serialVersionUID = 632844287088602215L;

	public String vehicleForceInsuImgUrl; // 交强险图片Url
	public String vehicleForceInsuImgUrl800;// 交强险图片Url(大图)
	public String vehicleForceInsuImgName;
	public String vehicleTypeText;
	public String vehicleCertificateImgName;
	public String vehicleLicenseImgUrl;// 行驶证图片Url
	public String vehicleLicenseImgUrl800;// 行驶证图片Url（大图）
	public String vehicleStatus;// 车辆状态。0：未认证，1：认证中，2：认证通过,3:认证不通过
	public String vehicleBand;
	public String vehicleType;
	public String vehicleBandText;
	public String vehicleLicenseImgName;
	public String vehicleEngineCode;
	public String vehicleForceInsuImgIdx;
	public String vehicleCertificateTime;
	public String tenantId;
	public String vehicleCertificateImgIdx;
	public String vehicleLicenseTime;
	public String vehicleLicenseImgIdx;
	public String vehicleStatusText;
	public String vehicleNo;// 车牌号
	public String id;// 车辆id
	public String vehicleOwnerId;
	public String vehicleForceInsuTime;
	public String vehicleCertificateImgUrl; // 营运证图片Url
	public String vehicleCertificateImgUrl800;// 营运证图片Url（大图）
	public String verifyBy;
	public String verifyTime;
	public String vehicleForceInsuImgStatus;
	public String vehicleLicenseImgStatus;// 1：待审核；2：审核通过；3：审核未通过；4：已过期
	public String vehicleCertificateImgStatus;
	public String vehicleOwnerType;// 车辆所属人类型 --1,个人车辆；2，公司车辆
	public String handIdCodeImgBase64;// 手持身份证照路径或者64位字符串
	public String handIdCodeImgBase64800;// 手持身份证照路径大图
	public String handIdCodeImgName;// 手持身份证照名称
	public String handIdCodeImgStatus;
	public String businessLicenseImgBase64;// 营业执照路径或者64位字符串
	public String businessLicenseImgBase64800;// 营业执照路径大图
	public String businessLicenseImgName;// 营业执照名称
	public String businessLicenseImgStatus;
	public String vehicleImgBase64;// 车辆图片路径或64位字符串
	public String vehicleImgBase64800;// 车辆图片路径大图
	public String vehicleImgName;// 车辆图片名称
	public String vehicleImgStatus;
	public int starsLevel;// 星级1，一星 2，二星 3，三星
	public String vehicleContacts;// 跟车联系方式
	public int IsSubmit;

}
