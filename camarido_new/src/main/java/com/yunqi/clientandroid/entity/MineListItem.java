package com.yunqi.clientandroid.entity;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 个人中心列表数据item
 * @date 15/11/24
 */
public class MineListItem extends IDontObfuscate {
	private static final long serialVersionUID = 3607711368311072619L;

	public MineListItem(int _mineType, int _imageResId, String _itemName) {
		this.mineType = _mineType;
		this.imageResId = _imageResId;
		this.itemName = _itemName;
	}

	public MineListItem(int _mineType, int _imageResId, String _itemName,
			String _status) {
		this.mineType = _mineType;
		this.imageResId = _imageResId;
		this.itemName = _itemName;
		this.status = _status;
	}

	/**
	 * 0代表间隔
	 */
	public int mineType;
	public int imageResId;
	public String status;
	public String itemName;
}
