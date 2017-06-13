package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 取消订单接口
 * @date 16/03/07
 */
public class CancelTicketRequest extends IRequest {

	public CancelTicketRequest(Context context, String ticketId, String memo) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("Memo", memo);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/CancelTicket";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
