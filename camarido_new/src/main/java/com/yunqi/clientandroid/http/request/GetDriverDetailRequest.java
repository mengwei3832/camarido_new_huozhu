package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取司机详细信息
 * @date 15/11/21
 */
public class GetDriverDetailRequest extends IRequest {

	private String mVehicleDriverId;

	public GetDriverDetailRequest(Context context, String vehicleDriverId) {
		super(context);
		this.mVehicleDriverId = vehicleDriverId;
		mParams.put("vehicleDriverId", vehicleDriverId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/GetVehicleDriver" + "?vehicleDriverId="
				+ mVehicleDriverId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<DriverDetailInfo, NullObject>>() {
		}.getType();
	}
}
