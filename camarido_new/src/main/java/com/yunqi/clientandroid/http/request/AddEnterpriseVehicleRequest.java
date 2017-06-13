package com.yunqi.clientandroid.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 新增企业车辆
 * @date 15/11/21
 */
public class AddEnterpriseVehicleRequest extends IRequest {

	public AddEnterpriseVehicleRequest(Context context, String vehicleNo,
			String businessLicenseImgBase64, String businessLicenseImgName,
			String vehicleLicenseImgUrl, String vehicleLicenseImgName,
			String vehicleImgBase64, String vehicleImgName, String vehicleCall) {
		super(context);

		mParams.put("VehicleNo", vehicleNo);
		mParams.put("BusinessLicenseImgBase64", businessLicenseImgBase64);
		mParams.put("BusinessLicenseImgName", businessLicenseImgName);
		mParams.put("VehicleLicenseImgUrl", vehicleLicenseImgUrl);
		mParams.put("VehicleLicenseImgName", vehicleLicenseImgName);
		mParams.put("VehicleImgBase64", vehicleImgBase64);
		mParams.put("VehicleImgName", vehicleImgName);
		mParams.put("VehicleContacts", vehicleCall);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/AddComonentVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
