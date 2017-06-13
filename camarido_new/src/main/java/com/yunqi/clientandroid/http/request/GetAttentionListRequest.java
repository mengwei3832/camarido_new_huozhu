package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取消息列表
 * @date 15/11/21
 */
public class GetAttentionListRequest extends IRequest {

	public GetAttentionListRequest(Context context, int pageIndex, int pageSize) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageIndex", pageIndex);
			Pagenation.put("PageSize", pageSize);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/GetSubscibedMessageLiteList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, Message>>() {
		}.getType();
	}
}
