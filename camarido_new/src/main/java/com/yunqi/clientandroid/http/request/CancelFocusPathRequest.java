package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 取消关注路线
 * @date 15/11/21
 */
public class CancelFocusPathRequest extends IRequest {

	public CancelFocusPathRequest(Context context, String startCity,
			String endCity) {
		super(context);

		mParams.put("StartCity", startCity);
		mParams.put("EndCity", endCity);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/CancelFocusSubscripts";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
