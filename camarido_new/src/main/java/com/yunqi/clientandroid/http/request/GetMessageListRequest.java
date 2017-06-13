package com.yunqi.clientandroid.http.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.entity.ShortMessage;
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
 * @Description class 获取消息列表
 * @date 15/11/21
 */
public class GetMessageListRequest extends IRequest {
	private String userType;
	private String url;

	public GetMessageListRequest(Context context, int pageIndex, int pageSize) {
		super(context);
		userType = CacheUtils.getString(context, "USER_TYPE", "");

		Log.e("TAG", "----------userType-----------" + userType.toString());
		if (userType != null && !TextUtils.isEmpty(userType)) {
			if (userType.equals("1")) {
				// 传参
				JSONObject Pagenation = new JSONObject();
				try {
					Pagenation.put("PageIndex", pageIndex);
					Pagenation.put("PageSize", pageSize);
					mParams.put("Pagenation", Pagenation);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (userType.equals("2")) {
				mParams.put("PageIndex", pageIndex);
				mParams.put("PageSize", pageSize);
			}
		}

	}

	@Override
	public String getUrl() {
		if (userType != null && !TextUtils.isEmpty(userType)) {
			if (userType.equals("1")) {
				url = getHost() + "api/Message/GetShortMessages";
			} else if (userType.equals("2")) {
				url = getHost() + "pkgapi/Message/GetShortMessages";
			}
		}
		// return getHost() + "api/Message/GetShortMessages";
		return url;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ShortMessage>>() {
		}.getType();
	}
}
