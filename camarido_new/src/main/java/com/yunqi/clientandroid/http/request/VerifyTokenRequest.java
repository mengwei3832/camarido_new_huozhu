package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 请求验证登录
 * 
 * @Description:
 * @ClassName: VerifyTokenRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:19:54
 * 
 */
public class VerifyTokenRequest extends IRequest {

	public VerifyTokenRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/VerifyToken";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, Object>>() {
		}.getType();
	}

}
