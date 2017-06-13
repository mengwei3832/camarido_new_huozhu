package com.yunqi.clientandroid.umeng.share;

import com.yunqi.clientandroid.R;

import java.util.ArrayList;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享列表数据配置类
 * @date 15/11/24
 */
public class SharelistHelper {

	/**
	 * 分享来源 常量
	 */
	public static final int FROM_TYPE_DOWNLOAD_APP = 1; // 分享app下载
	public static final int FROM_TYPE_VEHICLE = 2; // 分享车辆
	public static final int FROM_TYPE_GRAB_ORDER = 3; // 分享抢单
	public static final int FROM_TYPE_ATTENTION_DETAIL = 4; // 分享文章
	public static final int FROM_TYPE_DEFAULT = 0; // 默认全都显示
	public static final int FROM_TYPE_PROMOTION = 5; // 分享推广码

	/**
	 * item类型
	 */
	public static final int TYPE_WEIXIN = 1;
	public static final int TYPE_WEIXIN_CICLE = 2;
	public static final int TYPE_QQ = 3;
	public static final int TYPE_QZONE = 4;
	public static final int TYPE_SMS = 5;

	/**
	 * item名字
	 */
	public static final String NAME_WEIXIN = "分享到微信";
	public static final String NAME_WEIXIN_CICLE = "分享到朋友圈";
	public static final String NAME_QQ = "分享到qq";
	public static final String NAME_QZONE = "分享到qq空间";
	public static final String NAME_SMS = "短信分享";

	/**
	 * 要用的 list
	 */
	private static ArrayList<ShareInfo> mShareList = new ArrayList<ShareInfo>();

	/**
	 * 根据类型获取分享list
	 * 
	 * @param shareFromType
	 * @return
	 */
	public static ArrayList<ShareInfo> getShareList(int shareFromType) {
		mShareList.clear();
		switch (shareFromType) {
		case FROM_TYPE_DOWNLOAD_APP:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_WEIXIN_CICLE,
					R.drawable.wechat_circle, NAME_WEIXIN_CICLE));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_QZONE, R.drawable.qq_zone,
					NAME_QZONE));
			return mShareList;
		case FROM_TYPE_VEHICLE:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_SMS, R.drawable.sms, NAME_SMS));
			return mShareList;
		case FROM_TYPE_GRAB_ORDER:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_WEIXIN_CICLE,
					R.drawable.wechat_circle, NAME_WEIXIN_CICLE));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_QZONE, R.drawable.qq_zone,
					NAME_QZONE));
			return mShareList;
		case FROM_TYPE_ATTENTION_DETAIL:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_WEIXIN_CICLE,
					R.drawable.wechat_circle, NAME_WEIXIN_CICLE));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_QZONE, R.drawable.qq_zone,
					NAME_QZONE));
			return mShareList;

		case FROM_TYPE_PROMOTION:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_WEIXIN_CICLE,
					R.drawable.wechat_circle, NAME_WEIXIN_CICLE));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_SMS, R.drawable.sms, NAME_SMS));
			return mShareList;
		default:
			mShareList.add(new ShareInfo(TYPE_WEIXIN, R.drawable.wechat,
					NAME_WEIXIN));
			mShareList.add(new ShareInfo(TYPE_WEIXIN_CICLE,
					R.drawable.wechat_circle, NAME_WEIXIN_CICLE));
			mShareList.add(new ShareInfo(TYPE_QQ, R.drawable.qq, NAME_QQ));
			mShareList.add(new ShareInfo(TYPE_QZONE, R.drawable.qq_zone,
					NAME_QZONE));
			mShareList.add(new ShareInfo(TYPE_SMS, R.drawable.sms, NAME_SMS));
			return mShareList;
		}
	}

}
