package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

public class UploadPassRequest extends IRequest {

	public UploadPassRequest(Context context, String password) {
		super(context);
		// TODO 传入参数
		mParams.put("OldPwdPay", password);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/SetPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
