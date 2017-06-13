package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 注册协议请求
 * @date 15/12/24
 */
public class RegisterMessageRequest extends IRequest {

	public RegisterMessageRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/GetLoginRegistMessage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Message, NullObject>>() {
		}.getType();
	}

}
