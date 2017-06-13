package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:安全中心设置支付密码请求
 * @ClassName: SetPwdPayRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午7:30:17
 * 
 */
public class SafeCenterSetPwdPayRequest extends IRequest {

	public SafeCenterSetPwdPayRequest(Context context, String OldPwdPay) {
		super(context);
		mParams.put("OldPwdPay", OldPwdPay);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/SetPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
