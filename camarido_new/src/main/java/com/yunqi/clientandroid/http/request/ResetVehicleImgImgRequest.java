package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 重新上传车辆照片
 * @date 15/12/21
 */
public class ResetVehicleImgImgRequest extends IRequest {

	public ResetVehicleImgImgRequest(Context context, String vehicleId,
			String imgBase64, String imgName) {
		super(context);
		mParams.put("VehicleId", vehicleId);
		mParams.put("ImgBase64", imgBase64);
		mParams.put("ImgName", imgName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/ResetVehicleImgImg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
