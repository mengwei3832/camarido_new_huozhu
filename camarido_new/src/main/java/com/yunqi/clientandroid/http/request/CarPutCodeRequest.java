package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 云启车辆列表输入邀请码请求
 * @date 15/11/20
 */
public class CarPutCodeRequest extends IRequest {

	public CarPutCodeRequest(Context context, String inviteCode,
			String vehicleNo) {
		super(context);
		mParams.put("InviteCode", inviteCode);
		mParams.put("VehicleCode", vehicleNo);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/AddVehicleByInviteCode";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
