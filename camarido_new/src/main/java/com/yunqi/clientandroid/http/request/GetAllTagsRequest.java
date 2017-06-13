package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Tag;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取已经关注的tag
 * @date 15/11/21
 */
public class GetAllTagsRequest extends IRequest {

	public GetAllTagsRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/GetAllSubscribeTags";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, Tag>>() {
		}.getType();
	}
}
