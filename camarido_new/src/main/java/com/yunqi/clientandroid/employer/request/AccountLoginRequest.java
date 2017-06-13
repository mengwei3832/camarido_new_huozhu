package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.AccountLogin;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

public class AccountLoginRequest extends IRequest {
	private int userId;
	public AccountLoginRequest(Context context, int userId) {
		super(context);
		this.userId = userId;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/ChangeLogon?userId="+userId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<AccountLogin, NullObject>>() {
		}.getType();
	}
}
