package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverVerifyInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取司机认证信息
 * @date 15/11/21
 */
public class GetDriverInfoRequest extends IRequest {

	public GetDriverInfoRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/GetDriverInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<DriverVerifyInfo, NullObject>>() {
		}.getType();
	}
}
