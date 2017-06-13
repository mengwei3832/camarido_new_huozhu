package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleDetailInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 修改跟车电话上传的请求类
 * @ClassName: UpdateGencheCallRequest
 * @author: zhm
 * @date: 2016-5-5 下午4:45:43
 * 
 */
public class UpdateGencheCallRequest extends IRequest {
	private String vehicleId;
	private String callPhone;

	public UpdateGencheCallRequest(Context context, String vehicleId,
			String callPhone) {
		super(context);
		this.vehicleId = vehicleId;
		this.callPhone = callPhone;
		mParams.put("vehicleId", vehicleId);
		mParams.put("vehicleContacts", callPhone);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/ResetVehicleContacts?vehicleId="
				+ vehicleId + "&vehicleContacts=" + callPhone;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
