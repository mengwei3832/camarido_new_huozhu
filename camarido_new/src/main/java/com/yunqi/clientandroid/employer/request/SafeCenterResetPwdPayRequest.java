package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:安全中心重置密码请求
 * @ClassName: ResetPwdPayRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午7:41:15
 * 
 */
public class SafeCenterResetPwdPayRequest extends IRequest {

	public SafeCenterResetPwdPayRequest(Context context, String OldPwdPay,
			String NewPwdPay) {
		super(context);
		mParams.put("OldPwdPay", OldPwdPay);
		mParams.put("NewPwdPay", NewPwdPay);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/ResetPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
