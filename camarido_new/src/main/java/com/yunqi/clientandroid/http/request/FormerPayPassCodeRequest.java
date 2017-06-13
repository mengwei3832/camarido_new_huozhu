package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 忘记支付密码的请求验证码
 * @ClassName: FormerPayPassCodeRequest
 * @author: zhm
 * @date: 2016-3-28 下午4:07:20
 * 
 */
public class FormerPayPassCodeRequest extends IRequest {

	public FormerPayPassCodeRequest(Context context, String useName) {
		super(context);
		mParams.put("UserName", useName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/GetResetPwdPayMsg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
