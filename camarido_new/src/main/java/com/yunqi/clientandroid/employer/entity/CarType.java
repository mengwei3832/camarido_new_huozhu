package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @Description:车型的实体类
 * @ClassName: CarType
 * @author: mengwei
 * @date: 2016-6-21 下午7:13:01
 * 
 */
public class CarType extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 8822556927390362904L;

	public String id;
	public String enumName;
	public boolean check;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public String toString() {
		return "CarType [id=" + id + ", enumName=" + enumName + "]";
	}

}
