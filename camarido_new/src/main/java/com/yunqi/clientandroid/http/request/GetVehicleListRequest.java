package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleListInfo;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取车辆列表
 * @date 15/11/21
 */
public class GetVehicleListRequest extends IRequest {

	public GetVehicleListRequest(Context context, int pageSize, int pageIndex) {
		super(context);

		mParams.put("PageSize", pageSize + "");
		mParams.put("PageIndex", pageIndex + "");
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/GetVehicleBriefInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<String, VehicleListInfo>>() {
		}.getType();
	}
}
