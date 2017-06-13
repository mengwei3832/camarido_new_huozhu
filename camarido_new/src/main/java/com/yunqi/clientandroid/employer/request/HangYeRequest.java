package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.HangYeZiXun;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:行业资讯列表请求
 * @ClassName: HangYeRequest
 * @author: chengtao
 * @date: 2016-7-7 下午3:25:15
 * 
 */
public class HangYeRequest extends IRequest {

	public HangYeRequest(Context context, int PageSize, int PageIndex, int TagId) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", PageSize);
			Pagenation.put("PageIndex", PageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("TagId", TagId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Message/GetMessageByTagId";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, HangYeZiXun>>() {
		}.getType();
	}

}
