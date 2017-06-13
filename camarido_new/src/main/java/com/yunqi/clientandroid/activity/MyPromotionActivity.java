package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.Promotion;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.request.PromotionRequest;
import com.yunqi.clientandroid.http.request.ShareAppRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.umeng.share.ShareUtil;
import com.yunqi.clientandroid.umeng.share.SharelistHelper;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的推广界面
 * @date 15/12/22
 */
public class MyPromotionActivity extends BaseActivity implements
		View.OnClickListener {

	private PromotionRequest mPromotionRequest;
	private ShareAppRequest mShareAppRequest;

	private final int PROMOTION_REQUEST = 1;
	private final int SHARE_APP_REQUEST = 2;

	private TextView tvPromotionMsg;
	private TextView tvPromotionCode;
	private TextView tvPromotionRuleContent;
	private Button btnPromotion;

	private Share mShare;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_my_promotion;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionbar();

		tvPromotionMsg = obtainView(R.id.tv_promotion_msg);
		tvPromotionCode = obtainView(R.id.tv_promotion_code);
		tvPromotionRuleContent = obtainView(R.id.tv_active_rule_content);
		btnPromotion = obtainView(R.id.btn_promotion);

	}

	@Override
	protected void initData() {
		mPromotionRequest = new PromotionRequest(MyPromotionActivity.this);
		mPromotionRequest.setRequestId(PROMOTION_REQUEST);
		httpPost(mPromotionRequest);

		mShareAppRequest = new ShareAppRequest(MyPromotionActivity.this);
		mShareAppRequest.setRequestId(SHARE_APP_REQUEST);
		httpPost(mShareAppRequest);

	}

	@Override
	protected void setListener() {
		btnPromotion.setOnClickListener(this);

	}

	// 初始化titileBar的方法
	private void initActionbar() {
		setActionBarTitle(this.getResources().getString(R.string.my_promotion));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyPromotionActivity.this.finish();
			}
		});
		setActionBarRight(true, 0, "分享");
		setOnActionBarRightClickListener(false, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mShare != null) {
					ShareUtil.share(MyPromotionActivity.this,
							SharelistHelper.FROM_TYPE_PROMOTION, mShare);
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
		case PROMOTION_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				Promotion promotion = (Promotion) response.singleData;
				String promotionCount = promotion.promotionCount;
				String promotionMoney = promotion.promotionMoney;
				String promotionCode = promotion.promotionCode;
				String promotionRule = promotion.promotionRule;

				if (promotionCount != null
						&& !TextUtils.isEmpty(promotionCount)
						&& promotionMoney != null
						&& !TextUtils.isEmpty(promotionMoney)) {
					tvPromotionMsg.setText(Html
							.fromHtml("已介绍<font color='#2299ee'>"
									+ promotionCount + "</font>人，取得"
									+ "<font color='#2299ee'>" + promotionMoney
									+ "</font>元推广费"));
				}

				if (!TextUtils.isEmpty(promotionCode) && promotionCode != null) {
					tvPromotionCode.setText(promotionCode);
				}

				if (!TextUtils.isEmpty(promotionRule) && promotionRule != null) {
					tvPromotionRuleContent.setText(promotionRule.replace("\\n",
							"\n"));
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case SHARE_APP_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mShare = (Share) response.singleData;
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_promotion: // 马上推广
			if (mShare != null) {
				ShareUtil.share(MyPromotionActivity.this,
						SharelistHelper.FROM_TYPE_DOWNLOAD_APP, mShare);
			} else {
				showToast("无法获取分享内容");
			}
			break;
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, MyPromotionActivity.class);
		context.startActivity(intent);
	}

}
