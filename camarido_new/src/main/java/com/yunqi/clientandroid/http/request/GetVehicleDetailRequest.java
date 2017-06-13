package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleDetailInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取车辆详细信息
 * @date 15/11/21
 */
public class GetVehicleDetailRequest extends IRequest {

	private String vehicleId;

	public GetVehicleDetailRequest(Context context, String vehicleId) {
		super(context);
		this.vehicleId = vehicleId;
		mParams.put("Id", vehicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/GetVehicle/" + vehicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<VehicleDetailInfo, NullObject>>() {
		}.getType();
	}
}
