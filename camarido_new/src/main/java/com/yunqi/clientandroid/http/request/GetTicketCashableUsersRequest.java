package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetTicketCashableUsers;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetTicketCashableUsersRequest extends IRequest {

	private String ticketId;

	public GetTicketCashableUsersRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("ticketId", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/GetTicketCashableUsers" + "?ticketId="
				+ ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetTicketCashableUsers, NullObject>>() {
		}.getType();
	}

}
