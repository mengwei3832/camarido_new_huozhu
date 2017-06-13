package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.FocusCount;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 所有路线点进去包列表获取关注人数和是否关注接口
 * @date 15/11/21
 */
public class GetFocusCountRequest extends IRequest {

	public GetFocusCountRequest(Context context, String startCity,
			String endCity) {
		super(context);

		mParams.put("StartCity", startCity);
		mParams.put("EndCity", endCity);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/SubscriptsFocusCount";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<FocusCount, NullObject>>() {
		}.getType();
	}
}
