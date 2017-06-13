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
 * @Description class 删除司机
 * @date 15/11/21
 */
public class DeleteDriverRequest extends IRequest {
	private String mVehicleDriverId;

	public DeleteDriverRequest(Context context, String vehicleDriverId) {
		super(context);
		this.mVehicleDriverId = vehicleDriverId;
		mParams.put("VehicleDriverId", vehicleDriverId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/DelVehicleDriver" + "?VehicleDriverId="
				+ mVehicleDriverId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
