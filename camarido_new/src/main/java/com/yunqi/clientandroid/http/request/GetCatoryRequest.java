package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CatoryInfo;
import com.yunqi.clientandroid.entity.ProvinceInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取筛选种类接口
 * @date 15/11/21
 */
public class GetCatoryRequest extends IRequest {

	public GetCatoryRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetCategory";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, CatoryInfo>>() {
		}.getType();
	}
}
