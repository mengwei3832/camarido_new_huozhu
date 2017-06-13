package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

public class UploadNewPayPassRequest extends IRequest {

	public UploadNewPayPassRequest(Context context, String password, String code) {
		super(context);
		// TODO 传入参数
		mParams.put("NewPwdPay", password);
		mParams.put("ShortMsg", code);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/ForgetPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
