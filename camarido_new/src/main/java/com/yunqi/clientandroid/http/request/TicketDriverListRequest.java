package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

public class TicketDriverListRequest extends IRequest {

	private String vehicleId;

	public TicketDriverListRequest(Context context, String vehicleId) {
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
