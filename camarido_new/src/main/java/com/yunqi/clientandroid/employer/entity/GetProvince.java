package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class GetProvince extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -877404557590379058L;

	public int id; // RegionModel的id，省份的Id
	public String name; // 省份名称

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String pId; // 父级Id

	@Override
	public String toString() {
		return "GetProvince{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", pId='" + pId + '\'' +
				'}';
	}
}
