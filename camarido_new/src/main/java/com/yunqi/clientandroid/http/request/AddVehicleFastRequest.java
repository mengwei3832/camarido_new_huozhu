package com.yunqi.clientandroid.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 快速新增车辆
 * @date 16/3/4
 */
public class AddVehicleFastRequest extends IRequest {

	public AddVehicleFastRequest(Context context, String name, String idCode,
			String vehicleNo, int vehicleType, String vehicleEngineCode,
			String vehicleForceInsuImgUrl, String vehicleForceInsuImgName,
			String vehicleLicenseImgUrl, String vehicleLicenseImgName,
			String vehicleCertificateImgUrl, String vehicleCertificateImgName) {
		super(context);

		mParams.put("Name", name);
		mParams.put("IDCode", idCode);
		mParams.put("VehicleNo", vehicleNo);
		mParams.put("VehicleType", vehicleType);
		if (!TextUtils.isEmpty(vehicleEngineCode) && vehicleEngineCode != null) {
			mParams.put("VehicleEngineCode", vehicleEngineCode);
		}
		mParams.put("VehicleForceInsuImgUrl", vehicleForceInsuImgUrl);
		mParams.put("VehicleForceInsuImgName", vehicleForceInsuImgName);
		mParams.put("VehicleLicenseImgUrl", vehicleLicenseImgUrl);
		mParams.put("VehicleLicenseImgName", vehicleLicenseImgName);
		mParams.put("VehicleCertificateImgUrl", vehicleCertificateImgUrl);
		mParams.put("VehicleCertificateImgName", vehicleCertificateImgName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/AddVehicleFast";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
