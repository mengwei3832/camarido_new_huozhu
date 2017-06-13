package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:货主方登录的请求
 * @ClassName: RegisterPkgRequest
 * @author: mengwei
 * @date: 2016-6-2 下午8:02:42
 * 
 */
public class RegisterPkgRequest extends IRequest {

	public RegisterPkgRequest(Context context, String mUserName,
			String mPassword) {
		super(context);
		mParams.put("UserName", mUserName);
		mParams.put("Password", mPassword);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/Logon";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, Object>>() {
		}.getType();
	}

}
