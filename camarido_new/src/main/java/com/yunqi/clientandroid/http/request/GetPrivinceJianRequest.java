package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetProvinceJian;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:获取所有省份简称的请求
 * @ClassName: GetPrivinceJianRequest
 * @author: zhm
 * @date: 2016-5-26 上午11:20:51
 * 
 */
public class GetPrivinceJianRequest extends IRequest {

	public GetPrivinceJianRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Address/GetPabbreviationList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, GetProvinceJian>>() {
		}.getType();
	}

}
