package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 注册接口
 * 
 * @Description:
 * @ClassName: RegisterRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:20:52
 * 
 */
public class RegisterRequest extends IRequest {

	public RegisterRequest(Context context, String userName, String password,
			String shortMsg, String inviteCode) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("Password", password);
		mParams.put("ShortMsg", shortMsg);
		mParams.put("InviteCode", inviteCode);
	}

	@Override
	public String getUrl() {
		return HostUtil.getApiHost() + "api/Auth/Regist";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, Object>>() {
		}.getType();
	}

}
