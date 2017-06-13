package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetResetPwdPayMsgRequest extends IRequest {

	public GetResetPwdPayMsgRequest(Context context, String userName) {
		super(context);
		mParams.put("UserName", userName);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/GetResetPwdPayMsg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
