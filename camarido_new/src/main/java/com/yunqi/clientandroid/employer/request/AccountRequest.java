package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.AccountEntity;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 请求副账户信息
 * @ClassName:	AccountRequest 
 * @date:	2016年11月22日 上午10:41:49 
 *
 */
public class AccountRequest extends IRequest {
	
	public AccountRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/GetVRightsAssignUser";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, AccountEntity>>() {
		}.getType();
	}
}
