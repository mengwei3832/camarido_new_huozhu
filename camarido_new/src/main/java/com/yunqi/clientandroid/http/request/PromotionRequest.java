package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Promotion;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 推广接口
 * @date 15/11/21
 */
public class PromotionRequest extends IRequest {

	public PromotionRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Promotion/GetPromotionInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Promotion, NullObject>>() {
		}.getType();
	}
}
