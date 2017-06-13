package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:取消订单
 * @ClassName: CancleTicketRequest
 * @author: chengtao
 * @date: 2016年6月28日 下午8:13:44
 * 
 */
public class CancleTicketRequest extends IRequest {

	public CancleTicketRequest(Context context, String ticketId,
			String cancleReason) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("MeMo", cancleReason);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/CancleTicket";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
