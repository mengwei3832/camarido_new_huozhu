package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.entity.DriverListInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取可抢单司机列表
 * @date 15/11/27
 */
public class PackDriverListRequest extends IRequest {

	private String vehicleId;

	public PackDriverListRequest(Context context, String vehicleId) {
		super(context);
		this.vehicleId = vehicleId;
		mParams.put("vehicleId", vehicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/GetVehicleDriverListByVehicleId"
				+ "?vehicleId=" + vehicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, DriverDetailInfo>>() {
		}.getType();
	}

}
