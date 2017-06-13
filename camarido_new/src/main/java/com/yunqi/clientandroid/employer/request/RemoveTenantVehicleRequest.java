package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:删除公司车辆
 * @ClassName: RemoveTenantVehicleRequest
 * @author: chengtao
 * @date: 2016年6月17日 上午9:37:49
 * 
 */
public class RemoveTenantVehicleRequest extends IRequest {

	public RemoveTenantVehicleRequest(Context context, int vehicleTenantId) {
		super(context);
		mParams.put("vehicleTenantId", vehicleTenantId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Vehicle/RemoveTenantVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
