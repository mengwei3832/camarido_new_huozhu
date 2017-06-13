package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:判断是否存在支付密码
 * @ClassName: IsExitsPwdPayRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午1:43:55
 * 
 */
public class IsExitsPwdPayRequest extends IRequest {

	public IsExitsPwdPayRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/IsExitsPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
