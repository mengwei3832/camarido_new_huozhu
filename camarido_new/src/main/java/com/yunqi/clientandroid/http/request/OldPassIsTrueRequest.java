package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 判断旧密码是否正确的请求类
 * @ClassName: OldPassIsTrueRequest
 * @author: zhm
 * @date: 2016-4-12 下午2:33:36
 * 
 */
public class OldPassIsTrueRequest extends IRequest {

	public OldPassIsTrueRequest(Context context, String oldPwdPay) {
		super(context);
		mParams.put("OldPwdPay", oldPwdPay);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/RegOldPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
