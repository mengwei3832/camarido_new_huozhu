package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 用户登录接口
 * 
 * @Description:
 * @ClassName: LoginRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:20:27
 * 
 */
public class LoginRequest extends IRequest {

	public LoginRequest(Context context, String userName, String password) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("Password", password);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/Logon";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, Object>>() {
		}.getType();
	}

}
