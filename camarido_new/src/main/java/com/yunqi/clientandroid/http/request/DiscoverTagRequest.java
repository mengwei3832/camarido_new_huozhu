package com.yunqi.clientandroid.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 根据tagid获取数据接口
 * @date 15/11/21
 */
public class DiscoverTagRequest extends IRequest {

	public DiscoverTagRequest(Context context, int pageIndex, int pageSize,
			int tagId) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageIndex", pageIndex);
			Pagenation.put("PageSize", pageSize);
			mParams.put("Pagenation", Pagenation);
			mParams.put("TagId", tagId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return HostUtil.getApiHost() + "api/Message/GetMessagesByTagId";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, Message>>() {
		}.getType();
	}
}
