package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发现界面一级界面列表数据
 * @date 15/11/21
 */
public class GetDiscoverListRequest extends IRequest {

	public GetDiscoverListRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/GetFirstLevelTags";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, DiscoverListItem>>() {
		}.getType();
	}
}
