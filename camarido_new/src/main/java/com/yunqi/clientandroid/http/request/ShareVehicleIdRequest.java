package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享车辆信息
 * @date 15/11/21
 */
public class ShareVehicleIdRequest extends IRequest {
	private String vehicleId;

	public ShareVehicleIdRequest(Context context, String vehicleId) {
		super(context);

		this.vehicleId = vehicleId;
		mParams.put("vehicleId", vehicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Share/GetShareVehicleSummary?vehicleId="
				+ vehicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Share, NullObject>>() {
		}.getType();
	}
}
