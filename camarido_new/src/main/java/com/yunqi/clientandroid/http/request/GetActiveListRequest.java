package com.yunqi.clientandroid.http.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取活动列表
 * @date 15/11/21
 */
public class GetActiveListRequest extends IRequest {
	private String userType;
	private String url;

	public GetActiveListRequest(Context context, int pageIndex, int pageSize) {
		super(context);
		userType = CacheUtils.getString(context, "USER_TYPE", "");

		Log.e("TAG", "----------userType-----------" + userType.toString());

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
		if (userType != null && !TextUtils.isEmpty(userType)) {
			if (userType.equals("1")) {
				url = getHost() + "api/Message/GetNewsaggregations";
			} else if (userType.equals("2")) {
				url = getHost() + "pkgapi/Message/GetActivities";
			}
		}
		// return getHost() + "api/Message/GetNewsaggregations";
		return url;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, Message>>() {
		}.getType();
	}
}
