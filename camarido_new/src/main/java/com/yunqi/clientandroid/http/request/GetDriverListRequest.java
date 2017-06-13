package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverListInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取司机列表
 * @date 15/11/24
 */
public class GetDriverListRequest extends IRequest {

	public GetDriverListRequest(Context context, int PageSize, int PageIndex,
			String VehicleId) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", PageSize);
			Pagenation.put("PageIndex", PageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("VehicleId", VehicleId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/GetVehicleDriverBriefInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, DriverListInfo>>() {
		}.getType();
	}
}
