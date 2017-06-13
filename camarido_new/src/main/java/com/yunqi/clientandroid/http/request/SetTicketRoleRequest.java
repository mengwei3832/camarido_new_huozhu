package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 设置司机抢单权限
 * @date 15/11/21
 */
public class SetTicketRoleRequest extends IRequest {

	public SetTicketRoleRequest(Context context, String vehicleDriverId,
			boolean hasRole) {
		super(context);

		mParams.put("VehicleDriverId", vehicleDriverId);
		mParams.put("HasRole", hasRole);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/SetTicketRole";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
