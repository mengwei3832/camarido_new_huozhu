package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.entity.GetLastAppInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 版本更新
 * @date 16/01/05
 */
public class GetLastAppInfoRequest extends IRequest {

	public GetLastAppInfoRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/App/GetLastAppInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetLastAppInfo, NullObject>>() {
		}.getType();
	}

}
