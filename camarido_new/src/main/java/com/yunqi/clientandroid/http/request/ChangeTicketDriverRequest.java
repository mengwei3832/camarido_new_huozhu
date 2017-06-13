package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class ChangeTicketDriverRequest extends IRequest {

	public ChangeTicketDriverRequest(Context context, String ticketId,
			String vehicleDriverId) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("VehicleDriverId", vehicleDriverId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/ChangeTicketDriver";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
