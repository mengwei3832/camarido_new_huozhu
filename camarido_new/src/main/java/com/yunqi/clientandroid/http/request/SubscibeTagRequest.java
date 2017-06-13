package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 关注标签的接口
 * @date 15/11/21
 */
public class SubscibeTagRequest extends IRequest {

	private int tagId;

	public SubscibeTagRequest(Context context, int tagId) {
		super(context);
		this.tagId = tagId;
		mParams.put("tagId", tagId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/SubscibeTag?tagId=" + tagId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
