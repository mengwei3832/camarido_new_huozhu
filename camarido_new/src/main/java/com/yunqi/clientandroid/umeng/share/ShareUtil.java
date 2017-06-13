package com.yunqi.clientandroid.umeng.share;

import android.app.Activity;
import android.view.Gravity;
import com.yunqi.clientandroid.entity.Share;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享工具类
 * @date 15/12/20
 */
public class ShareUtil {

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	public static void share(Activity activity, int fromType, Share share) {
		CustomShareBoard shareBoard = new CustomShareBoard(activity, fromType);
//		shareBoard.setShareContent(share);
		shareBoard.showAtLocation(activity.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
	}
}
