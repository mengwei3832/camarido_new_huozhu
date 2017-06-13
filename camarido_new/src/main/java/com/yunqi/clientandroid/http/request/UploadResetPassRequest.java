package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

public class UploadResetPassRequest extends IRequest {

	public UploadResetPassRequest(Context context, String oldPassword,
			String newPassword) {
		super(context);
		// TODO 传入参数
		mParams.put("OldPwdPay", oldPassword);
		mParams.put("NewPwdPay", newPassword);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/ResetPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
