package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:当前包是否审核
 * @ClassName: IsXuShenHe
 * @author: mengwei
 * @date: 2016-6-20 下午6:33:34
 * 
 */
public class IsXuShenHe extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6512168124857407040L;

	private int packageIsNeedAudit;

	public IsXuShenHe(int packageIsNeedAudit) {
		super();
		this.packageIsNeedAudit = packageIsNeedAudit;
	}

	public int getPackageIsNeedAudit() {
		return packageIsNeedAudit;
	}

	public void setPackageIsNeedAudit(int packageIsNeedAudit) {
		this.packageIsNeedAudit = packageIsNeedAudit;
	}

	@Override
	public String toString() {
		return "IsXuShenHe [packageIsNeedAudit=" + packageIsNeedAudit + "]";
	}

}
