package com.yunqi.clientandroid.receiver;

import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.yunqi.clientandroid.activity.AttentionDetailActivity;
import com.yunqi.clientandroid.activity.MessageActivity;
import com.yunqi.clientandroid.activity.MyTicketDetailActivity;
import com.yunqi.clientandroid.activity.PackageListDetailActivity;
import com.yunqi.clientandroid.employer.activity.BaoLieBiaoDetailActivity;
import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity;
import com.yunqi.clientandroid.employer.activity.MingXiActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.activity.WaybillDetailActivity;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * 1、PushMessageReceiver是个抽象类，该类继承了BroadcastReceiver。 以上这些方法运行在非UI线程中
 */
public class MiPushReceiver extends PushMessageReceiver {

	private final int PACAKGE_MSG = 0; // 跳转包详细
	private final int ORDER_MSG = 1; // 跳转订单详细
	private final int DETAIL_MSG = 2; // 跳转文章详情
	private final int MESSAGE_MSG = 3; // 跳转消息
	private final int ATTENTION_MSG = 4; // 关注消息
	private final int ACTIVE_MSG = 5; // 活动消息
	private final int PKG_PACKAGE_MSG = 1101;// 跳转货主订单详情
	private final int PKG_BAOJIA_MSG = 1102;// 跳转货主订单详情
	private final int PKG_SHENHE_MSG = 2200;// 跳转货主订单详情
	private final int PKG_TICKET_CAR_MSG = 1400;// 跳转到货主运单列表
	private final int PKG_TICKET_VEHICLE_MSG = 1201;// 跳转到货主运单列表
	private final int PKG_YIJIA_MSG = 2201;// 跳转货主订单详情
	private final int PKG_QUXIAO_MAG = 2203;// 跳转货主订单详情
	private final int PKG_TICKET_DANJU = 2600;// 跳转到待结算单据信息
	private final int PKG_TICKET_JIESUAN = 2500;// 跳转到已结算详情
	private final int PKG_QIANBAO_MINGXI = 6100;// 跳转到钱包明细界面
	private final int PKG_INSURANCE_SUCCESS = 1200;//投保成功

	public static final String MIPUSH_ACTION = "MIPUSH_ACTION"; // 小米推送广播action

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	/**
	 * 用来接收服务器向客户端发送的透传消息
	 * 
	 * @param context
	 * @param message
	 */
	@Override
	public void onReceivePassThroughMessage(Context context,
			MiPushMessage message) {
	}

	/**
	 * 用来接收服务器向客户端发送的通知消息， 这个回调方法会在用户手动点击通知后触发
	 * 
	 * @param context
	 * @param message
	 */
	@Override
	public void onNotificationMessageClicked(Context context,
			MiPushMessage message) {
		Log.e("TAG", "--------------jinlaile-----------");

		String content = message.getContent();

		Log.e("TAG", "------------content------------" + content.toString());

		JSONObject data = null;
		try {
			data = new JSONObject(content);

			Log.e("TAG", "--------------data-----------" + data.toString());

			int pushType = data.getInt("PushType");
			String relationId = data.getString("RelationId");

			Log.e("TAG", "--------------pushType-----------" + pushType);

			Log.e("TAG",
					"--------------relationId-----------"
							+ relationId.toString());

			miPushInvoke(context, pushType, relationId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用来接收服务器向客户端发送的通知消息 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
	 * 
	 * @param context
	 * @param message
	 */
	@Override
	public void onNotificationMessageArrived(Context context,
			MiPushMessage message) {
		String content = message.getContent();
		JSONObject data = null;
		try {
			data = new JSONObject(content);

			int pushType = data.getInt("PushType");

			miPushAddBubble(context, pushType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用来接收客户端向服务器发送命令后的响应结果
	 * 
	 * @param context
	 * @param message
	 */
	@Override
	public void onCommandResult(Context context, MiPushCommandMessage message) {

	}

	/**
	 * 用来接收客户端向服务器发送注册命令后的响应结果
	 * 
	 * @param context
	 * @param message
	 */
	@Override
	public void onReceiveRegisterResult(Context context,
			MiPushCommandMessage message) {

	}

	/**
	 * 推送跳转逻辑
	 * 
	 * @param pushType
	 * @param relationId
	 */
	private void miPushInvoke(Context context, int pushType, String relationId) {
		mUmeng = UmengStatisticsUtils.instance(context);
		switch (pushType) {
		case PACAKGE_MSG:
			PackageListDetailActivity.invokeNewTask(context, relationId);
			break;
		case ORDER_MSG:
			MyTicketDetailActivity.invokeNewTask(context, relationId);
			break;
		case DETAIL_MSG:
			AttentionDetailActivity.invokeNewTask(context, relationId);
			break;
		case MESSAGE_MSG:
			Log.e("TAG", "-----------跳转消息界面--------------");

			MessageActivity.invokeNewTask(context);
			// Intent intent = new Intent(context, MessageActivity.class);
			// context.startActivity(intent);
			break;
		case PKG_PACKAGE_MSG:// 跳转到订单详情页面
			//友盟统计发货成功通知
			mUmeng.setCalculateEvents("ship_click_release_notice");

			BaoLieBiaoDetailActivity.invokeNewTask(context, relationId);
			break;
		case PKG_BAOJIA_MSG:// 跳转到订单详情页面
			BaoLieBiaoDetailActivity.invokeNewTask(context, relationId);

			break;
		case PKG_SHENHE_MSG:// 跳转到运单页面
			CurrentTicketActivity.invokeNewTask(context, relationId);

			break;
		case PKG_TICKET_CAR_MSG:// 跳转到运单页面
			CurrentTicketActivity.invokeNewTask(context, relationId);

			break;
		case PKG_TICKET_VEHICLE_MSG:// 跳转到运单页面
			CurrentTicketActivity.invokeNewTask(context, relationId);

			break;

		case PKG_YIJIA_MSG: // 跳转到订单详情
			BaoLieBiaoDetailActivity.invokeNewTask(context, relationId);
			break;
		case PKG_QUXIAO_MAG: // 跳转到订单详情
			BaoLieBiaoDetailActivity.invokeNewTask(context, relationId);
			break;
		case PKG_TICKET_DANJU: // 跳转到单据信息
			UploadOrderAuditActivity.invokeNewTask(context, relationId, true,
					"6","");
			break;
		case PKG_TICKET_JIESUAN: // 跳转到已结算详情
			UploadOrderActivity.invokeNewTask(context, relationId, "8", false);
			break;
		case PKG_QIANBAO_MINGXI: // 跳转到钱包明细界面
			MingXiActivity.invokeNewTask(context);
			break;
		case PKG_INSURANCE_SUCCESS://跳转到消息列表
			MessageActivity.invokeNewTask(context);
			break;
		}

	}

	/**
	 * 增加消息气泡数
	 * 
	 * @param context
	 * @param pushType
	 */
	private void miPushAddBubble(Context context, int pushType) {
		PreManager preManager = PreManager.instance(context);
		Intent intent = null;
		switch (pushType) {
		case MESSAGE_MSG:
			preManager.setMsgBubble(preManager.getMsgBubble() + 1);
			intent = new Intent(MIPUSH_ACTION);
			context.sendBroadcast(intent);
			break;
		case ATTENTION_MSG:
			preManager.setAttentionBubble(preManager.getAttentionBubble() + 1);
			intent = new Intent(MIPUSH_ACTION);
			context.sendBroadcast(intent);
			break;
		case ACTIVE_MSG:
			preManager.setActiveBubble(preManager.getActiveBubble() + 1);
			intent = new Intent(MIPUSH_ACTION);
			context.sendBroadcast(intent);
			break;
		default:
			break;
		}

	}

}
