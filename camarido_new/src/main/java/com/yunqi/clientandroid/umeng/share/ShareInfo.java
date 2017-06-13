package com.yunqi.clientandroid.umeng.share;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享的bean
 * @date 15/12/14
 */
public class ShareInfo extends IDontObfuscate {
	private static final long serialVersionUID = 8481601167394541790L;

	public ShareInfo(int shareType, int shareIcon, String shareText) {
		this.shareType = shareType;
		this.shareIcon = shareIcon;
		this.shareText = shareText;
	}

	public int shareType;
	public int shareIcon;
	public String shareText;
}
