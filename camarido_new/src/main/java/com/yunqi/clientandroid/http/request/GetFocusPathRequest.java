package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.FocusPath;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取已经关注的路线
 * @date 15/11/24
 */
public class GetFocusPathRequest extends IRequest {

	public GetFocusPathRequest(Context context, int PageIndex, int PageSize) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageIndex", PageIndex);
			Pagenation.put("PageSize", PageSize);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetSubscripts";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, FocusPath>>() {
		}.getType();
	}
}
