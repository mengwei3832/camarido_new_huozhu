package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 删除车辆
 * @date 15/11/21
 */
public class DeleteVehicleRequest extends IRequest {

	private String mVehicleId;

	public DeleteVehicleRequest(Context context, String vehicleId) {
		super(context);
		this.mVehicleId = vehicleId;
		mParams.put("vehicleId", vehicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/DelVehicle" + "?vehicleId="
				+ mVehicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
