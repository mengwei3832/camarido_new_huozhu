package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享文章详情
 * @date 15/11/21
 */
public class ShareMessageRequest extends IRequest {
	private String messageId;

	public ShareMessageRequest(Context context, String messageId) {
		super(context);

		this.messageId = messageId;
		mParams.put("messageId", messageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Share/GetShareMessageSummary?messageId="
				+ messageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Share, NullObject>>() {
		}.getType();
	}
}
