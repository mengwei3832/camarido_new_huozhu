package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverListInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单抢单接口
 * @date 15/12/5
 */

public class GrabATicketRequest extends IRequest {

	private String packageId;
	private String id;
	private String inputCode;

	public GrabATicketRequest(Context context, String packageId, String id) {
		super(context);
		this.packageId = packageId;
		this.id = id;

		mParams.put("packageId", packageId);
		mParams.put("VehicleId", id);

	}

	public GrabATicketRequest(Context context, String packageId, String id,
			String inputCode) {
		super(context);
		this.packageId = packageId;
		this.id = id;
		this.inputCode = inputCode;

		mParams.put("PackageId", packageId);
		mParams.put("VehicleId", id);
		mParams.put("InviteCode", inputCode);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/GrabATicket";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
