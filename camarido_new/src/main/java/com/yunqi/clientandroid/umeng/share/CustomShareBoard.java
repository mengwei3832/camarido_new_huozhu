/**
 *
 */

package com.yunqi.clientandroid.umeng.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Share;

import java.util.ArrayList;

/**
 * 分享popuwindow
 */
public class CustomShareBoard extends PopupWindow implements
		AdapterView.OnItemClickListener {
	public static final String DESCRIPTOR = "com.yunqi.clientandroid";
//	private UMSocialService mController = UMServiceFactory
//			.getUMSocialService(DESCRIPTOR);
	private Activity mActivity;
	// 4个分享平台的appid和appkey
	private final String WX_ID = "wx66c50bde54c96f8f";
	private final String WX_KEY = "d4624c36b6795d1d99dcf0547af5443d";
	private final String QQ_ID = "1105033789";
	private final String QQ_KEY = "Xh2MnSjSyaDfIbI4";

	private GridView gvShare;
	private ShareAdapter mShareAdapter;
	private ArrayList<ShareInfo> mShareInfos;
	private Share mShare;

	public CustomShareBoard(Activity activity, int shareFromType) {
		super(activity);
		this.mActivity = activity;
//		initQQAndQZone();
//		initWXAndWXCircle();
		initView(activity);
		initData(shareFromType);
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.custom_board, null);
		gvShare = (GridView) rootView.findViewById(R.id.gv_share);
		gvShare.setOnItemClickListener(this);
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setTouchable(true);
		setAnimationStyle(R.style.DataSheetAnimation);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
	}

//	private void performShare(SHARE_MEDIA platform) {
//		mController.postShare(mActivity, platform, new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				String showText = platform.toString();
//				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					showText += "平台分享成功";
//				} else {
//					showText += "平台分享失败";
//				}
//				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
//				dismiss();
//			}
//		});
//	}

	/**
	 * @return
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 */
//	private void initQQAndQZone() {
//		// 添加QQ支持, 并且设置QQ分享内容的target url
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, QQ_ID,
//				QQ_KEY);
//		qqSsoHandler.addToSocialSDK();
//
//		// 添加QZone平台
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, QQ_ID,
//				QQ_KEY);
//		qZoneSsoHandler.addToSocialSDK();
//	}

	/**
	 * @功能描述 : 添加微信平台分享
	 *//*
	private void initWXAndWXCircle() {
		// 添加微信平台
//		UMWXHandler wxHandler = new UMWXHandler(mActivity, WX_ID, WX_KEY);
//		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, WX_ID, WX_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	*//**
	 * 设置分享内容
	 *//*
	public void setShareContent(Share share) {
		this.mShare = share;

		// 设置分享图片, 参数2为图片的url地址
		if (share.imgurl == null) {
			share.imgurl = "http://115.28.222.90:8088/Images/icons/logo56.png";
		}
		UMImage image = new UMImage(mActivity, share.imgurl);
		image.setTargetUrl(share.redirectUrl);

		// 设置微信分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(share.summary);
		weixinContent.setTitle(share.title);
		weixinContent.setTargetUrl(share.redirectUrl);
		weixinContent.setShareMedia(image);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(share.summary);
		circleMedia.setTitle(share.title);
		circleMedia.setTargetUrl(share.redirectUrl);
		circleMedia.setShareMedia(image);
		mController.setShareMedia(circleMedia);

		// 设置qq分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(share.summary);
		qqShareContent.setTitle(share.title);
		qqShareContent.setTargetUrl(share.redirectUrl);
		qqShareContent.setShareMedia(image);
		mController.setShareMedia(qqShareContent);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(share.summary);
		qzone.setTitle(share.title);
		qzone.setTargetUrl(share.redirectUrl);
		qzone.setShareMedia(image);
		mController.setShareMedia(qzone);

	}*/

	private void initData(int shareFromType) {
		mShareInfos = SharelistHelper.getShareList(shareFromType);
		mShareAdapter = new ShareAdapter(mActivity, mShareInfos);
		gvShare.setAdapter(mShareAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShareInfo shareInfo = mShareAdapter.getItem(position);
//		switch (shareInfo.shareType) {
//		case SharelistHelper.TYPE_WEIXIN: // 微信点击
//			performShare(SHARE_MEDIA.WEIXIN);
//			break;
//		case SharelistHelper.TYPE_WEIXIN_CICLE: // 朋友圈点击
//			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
//			break;
//		case SharelistHelper.TYPE_QQ: // QQ点击
//			performShare(SHARE_MEDIA.QQ);
//			break;
//		case SharelistHelper.TYPE_QZONE: // QQ空间点击
//			performShare(SHARE_MEDIA.QZONE);
//			break;
//		case SharelistHelper.TYPE_SMS: // 短信点击
//			sendSms();
//			break;
//		}
	}

	/**
	 * 短信分享
	 */
	private void sendSms() {
		StringBuilder builder = new StringBuilder("smsto:");
		Uri uri = Uri.parse(builder.toString());
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", mShare.summary);

		if (mActivity != null)
			mActivity.startActivity(it);
	}

}
