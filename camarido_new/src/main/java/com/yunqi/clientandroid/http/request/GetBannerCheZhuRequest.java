package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取首页轮播图接口
 * @date 15/12/17
 */
public class GetBannerCheZhuRequest extends IRequest {

	public GetBannerCheZhuRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Attachment/GetBannersUrl";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, String>>() {
		}.getType();
	}
}
