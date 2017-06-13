package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetVehicleListInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取可抢单车辆列表
 * @date 15/11/27
 */
public class PackVehicleListRequest extends IRequest {

	private String packageId;

	public PackVehicleListRequest(Context context, String packageId) {
		super(context);

		this.packageId = packageId;

		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/GetVehicleByPackageId" + "?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, GetVehicleListInfo>>() {
		}.getType();
	}

}
