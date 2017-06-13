package com.yunqi.clientandroid.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.entity.Tag;
import com.yunqi.clientandroid.http.request.CancelTagRequest;
import com.yunqi.clientandroid.http.request.GetAttentionDetailRequest;
import com.yunqi.clientandroid.http.request.GetMessageTagRequest;
import com.yunqi.clientandroid.http.request.ShareMessageRequest;
import com.yunqi.clientandroid.http.request.SubscibeTagRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.umeng.share.ShareUtil;
import com.yunqi.clientandroid.umeng.share.SharelistHelper;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注的文章详情页面
 * @date 15/12/15
 */
public class AttentionDetailActivity extends BaseActivity implements
		View.OnClickListener {
	private TextView tvTitle;
	private TextView tvReadCount;
	private TextView tvAuthor;
	private TextView tvDate;
	private TextView tvContent;
	private LinearLayout llTagFirst;
	private LinearLayout llTagSecond;
	private TextView tvFocusonCount;
	private TextView tvCancelFocuson;
	private PopupWindow mFoucusPopuWindow;

	private GetAttentionDetailRequest mGetAttentionDetailRequest;
	private GetMessageTagRequest mGetMessageTagRequest;
	private ShareMessageRequest mShareMessageRequest;
	private CancelTagRequest mCancelTagRequest;
	private SubscibeTagRequest mSubscibeTagRequest;

	private final int GET_ATTENTION_DETAIL_REQUEST = 1;
	private final int GET_MESSAGE_TAG_REQUEST = 2;
	private final int SHARE_MESSAGE_REQUEST = 3;
	private final int CANCEL_TAG_REQUEST = 4;
	private final int SUBSCIBE_TAG_REQUEST = 5;

	private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	private String mMessageId;// 消息的id
	private List<Tag> mTags;
	private Share mShare;
	private Tag mTag;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_attentiondetail;
	}

	@Override
	protected void initView() {
		Log.e("TAG", "------------jinlai-------------------");

		createFoucusPopuwindow();
		tvTitle = obtainView(R.id.tv_attentionDetail_title);
		tvReadCount = obtainView(R.id.tv_attentionDetail_read_count);
		tvAuthor = obtainView(R.id.tv_attentionDetail_author);
		tvDate = obtainView(R.id.tv_attentionDetail_date);
		tvContent = obtainView(R.id.tv_attentionDetail_content);
		llTagFirst = obtainView(R.id.ll_attentionDetail_tag1);
		llTagSecond = obtainView(R.id.ll_attentionDetail_tag2);
	}

	@Override
	protected void initData() {
		// 接收从包列表页跳转传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey(EXTRA_MESSAGE)) {
			mMessageId = bundle.getString(EXTRA_MESSAGE);
		}

		// 初始化titileBar
		initActionBar();

		if (!TextUtils.isEmpty(mMessageId) && mMessageId != null) {
			mGetAttentionDetailRequest = new GetAttentionDetailRequest(
					AttentionDetailActivity.this, mMessageId);
			mGetAttentionDetailRequest
					.setRequestId(GET_ATTENTION_DETAIL_REQUEST);
			httpPost(mGetAttentionDetailRequest);
		}

		// 获取tag
		getTags();

		if (!TextUtils.isEmpty(mMessageId) && mMessageId != null) {
			mShareMessageRequest = new ShareMessageRequest(
					AttentionDetailActivity.this, mMessageId);
			mShareMessageRequest.setRequestId(SHARE_MESSAGE_REQUEST);
			httpPost(mShareMessageRequest);
		}

	}

	/**
	 * 获取tag
	 */
	private void getTags() {
		if (!TextUtils.isEmpty(mMessageId) && mMessageId != null) {
			mGetMessageTagRequest = new GetMessageTagRequest(
					AttentionDetailActivity.this, mMessageId);
			mGetMessageTagRequest.setRequestId(GET_MESSAGE_TAG_REQUEST);
			httpPost(mGetMessageTagRequest);
		}
	}

	@Override
	protected void setListener() {
		tvCancelFocuson.setOnClickListener(this);
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		// setActionBarTitle(mMessage.messageTitle);
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AttentionDetailActivity.this.finish();
			}
		});
		setActionBarRight(true, 0, "分享");
		setOnActionBarRightClickListener(false, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mShare != null) {
					ShareUtil.share(AttentionDetailActivity.this,
							SharelistHelper.FROM_TYPE_ATTENTION_DETAIL, mShare);
				} else {
					showToast("无法获取分享内容");
				}

			}
		});
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_ATTENTION_DETAIL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				Message msg = (Message) response.singleData;
				String messageTitle = msg.messageTitle;
				int messageReadCount = msg.messageReadCount;
				String messageAuthor = msg.messageAuthor;
				String messageShowTimeBegin = msg.messageShowTimeBegin;
				String messageContent = msg.messageContent;

				if (!TextUtils.isEmpty(messageTitle) && messageTitle != null) {
					tvTitle.setText(messageTitle);
				}

				if (!TextUtils.isEmpty(messageReadCount + "")
						&& messageReadCount >= 0) {
					tvReadCount.setText("阅读 " + messageReadCount + "人");
				}

				if (!TextUtils.isEmpty(messageAuthor) && messageAuthor != null) {
					tvAuthor.setText(messageAuthor);
				}

				if (!TextUtils.isEmpty(messageShowTimeBegin)
						&& messageShowTimeBegin != null) {
					tvDate.setText(StringUtils.getMsgDate(messageShowTimeBegin));
				}

				if (!TextUtils.isEmpty(messageContent)
						&& messageContent != null) {
					tvContent.setText(Html.fromHtml("" + messageContent));
				}

				if (!TextUtils.isEmpty(messageTitle) && messageTitle != null) {
					setActionBarTitle(msg.messageTitle);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case GET_MESSAGE_TAG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mTags = response.data;
				llTagFirst.removeAllViews();
				llTagSecond.removeAllViews();
				if (mTags != null && mTags.size() > 0) {
					for (int i = 0; i < mTags.size(); i++) {
						final Tag tag = mTags.get(i);
						String name = tag.name;

						final TextView tvTag = new TextView(
								AttentionDetailActivity.this);

						if (!TextUtils.isEmpty(name) && name != null) {
							tvTag.setText(name);
						}
						tvTag.setTextSize(6);
						tvTag.setTextColor(AttentionDetailActivity.this
								.getResources().getColor(R.color.tag_color));
						tvTag.setBackgroundResource(R.drawable.tag);
						tvTag.setGravity(Gravity.CENTER);

						tvTag.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mTag = tag;
								tvFocusonCount.setText(tag.subCount + "人关注");
								if (tag.isFocus) {
									tvCancelFocuson.setText("取消关注");
								} else {
									tvCancelFocuson.setText("关注");
								}
								showPupWindow(tvTag);
							}
						});
						if (mTags.size() <= 3) {
							llTagFirst.addView(tvTag);
							llTagSecond.setVisibility(View.GONE);
						} else if (mTags.size() > 3) {
							llTagSecond.setVisibility(View.VISIBLE);
							if (i == 0 || i == 1 || i == 2) {
								llTagFirst.addView(tvTag);
							} else {
								llTagSecond.addView(tvTag);
							}

						}
					}
				}
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;

		case SHARE_MESSAGE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mShare = (Share) response.singleData;
			}
			break;
		case CANCEL_TAG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// getTags();
				MiPushUtil.setMiPushUnTopic(AttentionDetailActivity.this, "T"
						+ mTag.id);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;

		case SUBSCIBE_TAG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				MiPushUtil.setMiPushTopic(AttentionDetailActivity.this, "T"
						+ mTag.id);
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		switch (requestId) {
		case GET_ATTENTION_DETAIL_REQUEST:
			showToast(this.getResources().getString(R.string.net_error_toast));
			break;
		case CANCEL_TAG_REQUEST:
			showToast(this.getResources().getString(R.string.net_error_toast));
			break;
		case SUBSCIBE_TAG_REQUEST:
			showToast(this.getResources().getString(R.string.net_error_toast));
			break;
		}
	}

	/**
	 * 创建关注Pupwindow
	 */
	private void createFoucusPopuwindow() {
		View popupView = AttentionDetailActivity.this.getLayoutInflater()
				.inflate(R.layout.detail_popuwindow, null);
		mFoucusPopuWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mFoucusPopuWindow.setFocusable(true);
		mFoucusPopuWindow.setOutsideTouchable(true);
		mFoucusPopuWindow.setBackgroundDrawable(new BitmapDrawable());
		tvFocusonCount = (TextView) popupView
				.findViewById(R.id.tv_focuson_count);
		tvCancelFocuson = (TextView) popupView
				.findViewById(R.id.tv_cancel_focuson);
	}

	/**
	 * 弹出框消失
	 */
	private void dismissPupWindows() {
		if (null != mFoucusPopuWindow) {
			mFoucusPopuWindow.dismiss();
		}
	}

	/**
	 * 显示弹出框
	 */
	private void showPupWindow(TextView textView) {
		mFoucusPopuWindow.showAsDropDown(textView, -100, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel_focuson:
			dismissPupWindows();
			if (mTag.isFocus) {
				cancelTag();
			} else {
				subscibeTag();
			}

			break;
		}
	}

	/**
	 * 订阅请求
	 */
	private void subscibeTag() {
		mSubscibeTagRequest = new SubscibeTagRequest(
				AttentionDetailActivity.this, mTag.id);
		mSubscibeTagRequest.setRequestId(SUBSCIBE_TAG_REQUEST);
		httpPost(mSubscibeTagRequest);
	}

	/**
	 * 取消订阅
	 */
	private void cancelTag() {
		mCancelTagRequest = new CancelTagRequest(AttentionDetailActivity.this,
				mTag.id);
		mCancelTagRequest.setRequestId(CANCEL_TAG_REQUEST);
		httpPost(mCancelTagRequest);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, String messageId) {
		Intent intent = new Intent(context, AttentionDetailActivity.class);
		intent.putExtra(EXTRA_MESSAGE, messageId);
		context.startActivity(intent);
	}

	/**
	 * 本界面增加flag跳转方法
	 */
	public static void invokeNewTask(Context context, String messageId) {
		Intent intent = new Intent(context, AttentionDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_MESSAGE, messageId);
		context.startActivity(intent);
	}

}
