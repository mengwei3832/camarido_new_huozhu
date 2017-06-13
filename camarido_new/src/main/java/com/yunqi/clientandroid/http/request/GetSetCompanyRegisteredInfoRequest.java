package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CompanyRegisteredInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取企业信息
 * @date 16/03/25
 */
public class GetSetCompanyRegisteredInfoRequest extends IRequest {

	public GetSetCompanyRegisteredInfoRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/GetSetCompanyRegisteredInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<CompanyRegisteredInfo, NullObject>>() {
		}.getType();
	}

}
