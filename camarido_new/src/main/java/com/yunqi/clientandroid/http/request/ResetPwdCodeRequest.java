package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class ResetPwdCodeRequest extends IRequest {

	public ResetPwdCodeRequest(Context context, String userName) {
		super(context);
		mParams.put("UserName", userName);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/GetResetPwdMsg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
