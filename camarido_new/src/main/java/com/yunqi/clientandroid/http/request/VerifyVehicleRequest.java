package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleExist;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 验证车牌号是否已存在
 * @date 15/11/21
 */
public class VerifyVehicleRequest extends IRequest {

	private String mVehicleNo;

	public VerifyVehicleRequest(Context context, String vehicleNo) {
		super(context);
		this.mVehicleNo = vehicleNo;
		mParams.put("vehicleNo", vehicleNo);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/VerifyVehicleNo" + "?vehicleNo="
				+ mVehicleNo;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<VehicleExist, NullObject>>() {
		}.getType();
	}
}
