package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:class 请求签收车数和在途车数的请求类
 * @ClassName: GetSunCar
 * @author: zhm
 * @date: 2016-5-20 下午2:52:38
 * 
 */
public class GetSunCar extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 7806459304118964966L;

	public int Id; // 包ID
	public int ContractId; // 合同ID
	public int TenantId; // 公司ID
	public int PackageStatus; // 包状态0：草稿;1：待审核；2：已发布；3：已完成
	public String PackagePubTime; // 发布时间
	public int PackageSettlementType; // 结算方式：0：定期；1：实时
	public int PackagePriceOrigin; // 发包方价格
	public int PackageGoodsPrice; // 货值单价
	public int PackageCount; // 总车数
	public int OrderCount; // 已经报名车数

}
