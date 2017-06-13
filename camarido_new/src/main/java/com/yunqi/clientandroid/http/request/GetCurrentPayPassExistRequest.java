package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.MineTicketCount;
import com.yunqi.clientandroid.entity.PayPassSafety;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 获取支付密码是否已设置接口
 * 
 * @Description:
 * @ClassName: GetCurrentPayPassExistRequest
 * @author: zhm
 * @date: 2016-3-25 上午10:04:53
 * 
 */
public class GetCurrentPayPassExistRequest extends IRequest {

	public GetCurrentPayPassExistRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Auth/IsExitsPwdPay";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
