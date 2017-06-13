package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.entity.PushInfo;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取推送相关信息
 * @date 15/11/21
 */
public class GetPushInfoRequest extends IRequest {

	public GetPushInfoRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Push/GetPushInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PushInfo>>() {
		}.getType();
	}
}
