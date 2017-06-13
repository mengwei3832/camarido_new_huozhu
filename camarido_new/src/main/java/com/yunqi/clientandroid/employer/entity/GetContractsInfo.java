package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class GetContractsInfo extends IDontObfuscate {
	/**
	 * @Fields serialVersionUID:
	 */
	private static final long serialVersionUID = 2922578320097275633L;

	//
	public String Id; // 合同ID
	public String ContractCode; // 合同编号
	public String ContractStatus; // 合同状态
	public String TenantId; // 发包方
	public String ContractStartTime; // 合同开始时间
	public String ContractEndTime; // 合同结束时间
	public String ContractSignTime; // 合同签署时间
	public String ContractSignAddress; // 合同签署地点
	public String ContractPartA; // 甲方
	public String ContractPartAName; // 甲方名称
	public String ContractPartAAddress; // 甲方地址
	public String ContractPartATel; // 甲方 电话
	public String ContractPartAPostCode; // 甲方邮政编号
	public String ContractPartB; // 乙方
	public String ContractPartBName; // 乙方名称
	public String ContractPartBAddress; // 乙方地址
	public String ContractPartBTel; // 乙方电话
	public String ContractPartBPostCode; // 乙方邮政编号
	public String ContractPartAProxy; // 甲方代理人
	public String ContractPartAProxyName; // 甲方代理人名称
	public String ContractPartAProxyAddress; // 甲方代理人地址
	public String ContractPartAProxyTel; // 甲方代理人电话
	public String ContractPartAProxyPostCode; // 甲方代理人邮政编码
	public String ContractPartBProxy; // 乙方代理人
	public String ContractPartBProxyName; // 乙方代理人名称
	public String ContractPartBProxyAddress; // 乙方代理人地址
	public String ContractPartBProxyTel; // 乙方代理人电话
	public String ContractPartBProxyPostCode; // 乙方代理人邮政编码
	public String SettleType; // 包 结算方式
	public int PackageIsNeedAudit; // 发包是否审核
	public String PackageIsProxy; // 类型
	public String ContractMemo; // 备注信息
	public String LoseBearPart; // 超额亏损承担方
	public String LoseRate; // 合理途损比率
	public String PaymentProcess; // 支付流程
	public String NeedInvoice; // 是否需要开票

}
