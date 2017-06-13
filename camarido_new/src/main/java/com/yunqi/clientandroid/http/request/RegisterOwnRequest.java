package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:
 * @ClassName: RegisterOwnRequest
 * @author: mengwei
 * @date: 2016-6-1 下午2:53:28
 * 
 */
public class RegisterOwnRequest extends IRequest {

	public RegisterOwnRequest(Context context, String UserPhone,
			String UserPwd, String ShortMsg, String inviteCode) {
		super(context);
		mParams.put("UserName", UserPhone);
		mParams.put("Password", UserPwd);
		mParams.put("ShortMsg", ShortMsg);
		mParams.put("InviteCode", inviteCode);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/RegistTenant";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, NullObject>>() {
		}.getType();
	}

}
