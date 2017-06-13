package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:忘记支付密码设置密码接口
 * @ClassName: ForgetTenantPwdPayRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午8:53:18
 * 
 */
public class ForgetTenantPwdPayRequest extends IRequest {

	public ForgetTenantPwdPayRequest(Context context, String ShortMsg,
			String NewPwdPay) {
		super(context);
		mParams.put("ShortMsg", ShortMsg);
		mParams.put("NewPwdPay", NewPwdPay);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/ForgetTenantPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
