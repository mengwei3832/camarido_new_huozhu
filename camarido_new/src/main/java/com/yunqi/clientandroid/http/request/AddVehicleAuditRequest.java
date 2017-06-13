package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.AddVehicleEntity;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:提交车辆申请审核的请求
 * @ClassName: AddVehicleAuditRequest
 * @author: zhm
 * @date: 2016-5-30 上午10:30:39
 * 
 */
public class AddVehicleAuditRequest extends IRequest {
	private int vehicleId;

	public AddVehicleAuditRequest(Context context, int vehicleId) {
		super(context);
		this.vehicleId = vehicleId;
		mParams.put("vehicleId", vehicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/SendAuditMessage?vehicleId="
				+ vehicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
