package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:根据合同号判断是否需要审核
 * @ClassName: HeTongShenHe
 * @author: chengtao
 * @date: 2016-6-20 下午12:25:02
 * 
 */
public class HeTongShenHe extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 2872074200889950696L;

	private String contractId;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	@Override
	public String toString() {
		return "HeTongShenHe [contractId=" + contractId + "]";
	}

}
