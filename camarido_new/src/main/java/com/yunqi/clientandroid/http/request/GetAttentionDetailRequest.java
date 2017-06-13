package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取文章详情接口
 * @date 15/11/21
 */
public class GetAttentionDetailRequest extends IRequest {

	private String id;

	public GetAttentionDetailRequest(Context context, String id) {
		super(context);
		this.id = id;
		mParams.put("id", id);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/GetMessageById/" + id;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Message, NullObject>>() {
		}.getType();
	}
}
