package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.request.GetMessageByIdRequest;
import com.yunqi.clientandroid.employer.request.JiageByIdRequest;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注的文章详情页面
 * @date 15/12/15
 */
public class GetMessageActivity extends BaseActivity {
	private TextView tvTitle;
	private TextView tvReadCount;
	private TextView tvAuthor;
	private TextView tvDate;
	private TextView tvContent;
	private ProgressWheel pbBar;
	private ImageView ivBlank;
	private WebView wvDiscoverPrice;
	private ScrollView svDiscover;

	private GetMessageByIdRequest getMessageByIdRequest;
	private JiageByIdRequest jiageByIdRequest;

	private final int GET_MESSAGE_REQUEST = 1;
	private final int GET_MESSAGE_JIAGE = 2;

	private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	private String mMessageId;// 消息的id
	private int checkId;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_attentiondetail;
	}

	@Override
	protected void initView() {
		Log.e("TAG", "------------jinlai-------------------");

		tvTitle = obtainView(R.id.tv_attentionDetail_title);
		tvReadCount = obtainView(R.id.tv_attentionDetail_read_count);
		tvAuthor = obtainView(R.id.tv_attentionDetail_author);
		tvDate = obtainView(R.id.tv_attentionDetail_date);
		tvContent = obtainView(R.id.tv_attentionDetail_content);
		pbBar = obtainView(R.id.pb_attentionDetail_bar);
		ivBlank = obtainView(R.id.iv_attentionDetail_blank);
		wvDiscoverPrice = obtainView(R.id.wv_discover_price);
		svDiscover = obtainView(R.id.sv_discover);
	}

	@Override
	protected void initData() {
		// 接收从包列表页跳转传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey(EXTRA_MESSAGE)) {
			mMessageId = bundle.getString(EXTRA_MESSAGE);
		}

		if (bundle != null && bundle.containsKey("checkId")) {
			checkId = bundle.getInt("checkId");
		}

		// 初始化titileBar
		initActionBar();

		if (!TextUtils.isEmpty(mMessageId) && mMessageId != null) {
			if (checkId == 0) {
				getMessageByIdRequest = new GetMessageByIdRequest(mContext,
						mMessageId);
				getMessageByIdRequest.setRequestId(GET_MESSAGE_REQUEST);
				httpPost(getMessageByIdRequest);
			} else if (checkId == 1) {
				jiageByIdRequest = new JiageByIdRequest(mContext, mMessageId);
				jiageByIdRequest.setRequestId(GET_MESSAGE_JIAGE);
				httpPost(jiageByIdRequest);
			}
		}

		// 设置Web视图
		wvDiscoverPrice.setWebViewClient(new webViewClient());
		wvDiscoverPrice.setWebChromeClient(new WebChromeClient());
		wvDiscoverPrice.getSettings().setBuiltInZoomControls(false);
		wvDiscoverPrice.getSettings().setDomStorageEnabled(true);
		// 如果有缓存 就使用缓存数据 如果没有 就从网络中获取
		wvDiscoverPrice.getSettings().setCacheMode(
				WebSettings.LOAD_CACHE_ELSE_NETWORK);
		wvDiscoverPrice.getSettings().setDatabaseEnabled(true);
		wvDiscoverPrice.getSettings().setAppCacheEnabled(true);

	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			showOrHide(2);
		}
	}

	@Override
	protected void setListener() {

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
				finish();
			}
		});
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_MESSAGE_JIAGE:
			isSuccess = response.isSuccess;
			message = response.message;
			if (response.singleData == null) {
				showToast(message);
				ivBlank.setVisibility(View.VISIBLE);
			} else {
				ivBlank.setVisibility(View.GONE);
			}
			if (isSuccess) {
				Message msg = (Message) response.singleData;
				String messageTitle = msg.messageTitle;
				int messageReadCount = msg.messageReadCount;
				String messageAuthor = msg.messageAuthor;
				String messageShowTimeBegin = msg.messageShowTimeBegin;
				String messageContent = msg.messageContent;
				int mMessageTargetCategory = msg.messageTargetCategory;
				String mMessageLink = msg.messageLink;

				if (mMessageTargetCategory == 7){
					wvDiscoverPrice.loadUrl(mMessageLink);
				} else {

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
					showOrHide(1);
				}
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case GET_MESSAGE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (response.singleData == null) {
				showToast(message);
				ivBlank.setVisibility(View.VISIBLE);
			} else {
				ivBlank.setVisibility(View.GONE);
			}
			if (isSuccess) {
				Message msg = (Message) response.singleData;
				String messageTitle = msg.messageTitle;
				int messageReadCount = msg.messageReadCount;
				String messageAuthor = msg.messageAuthor;
				String messageShowTimeBegin = msg.messageShowTimeBegin;
				String messageContent = msg.messageContent;
				int mMessageTargetCategory = msg.messageTargetCategory;
				String mMessageLink = msg.messageLink;

				if (mMessageTargetCategory == 7){
					wvDiscoverPrice.loadUrl(mMessageLink);
				} else {

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
					showOrHide(1);
				}
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			showOrHide(1);
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	/**
	 * 显示或隐藏web页面
	 * @param isShow
     */
	private void showOrHide(int isShow){
		if (isShow == 0){
			pbBar.setVisibility(View.VISIBLE);
			svDiscover.setVisibility(View.GONE);
			wvDiscoverPrice.setVisibility(View.GONE);
		} else if (isShow == 1){
			pbBar.setVisibility(View.GONE);
			svDiscover.setVisibility(View.VISIBLE);
			wvDiscoverPrice.setVisibility(View.GONE);
		} else if (isShow == 2){
			pbBar.setVisibility(View.GONE);
			svDiscover.setVisibility(View.GONE);
			wvDiscoverPrice.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context, String messageId, int checkId) {
		Intent intent = new Intent(context, GetMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, messageId);
		intent.putExtra("checkId", checkId);
		context.startActivity(intent);
	}

}
