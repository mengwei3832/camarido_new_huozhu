package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetTicketCashableMsgRequest extends IRequest {

	private String ticketId;

	public GetTicketCashableMsgRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/GetTicketCashableMsg?ticketId="
				+ ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
