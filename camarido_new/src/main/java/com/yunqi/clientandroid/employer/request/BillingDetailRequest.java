package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.BillingDetail;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:结算预览的请求
 * @ClassName: BillingDetailRequest
 * @author: chengtao
 * @date: 2016-6-27 下午4:31:46
 * 
 */
public class BillingDetailRequest extends IRequest {
	private String ticketId;

	public BillingDetailRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("id", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/TicketSettledInfo/" + ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<BillingDetail, NullObject>>() {
		}.getType();
	}

}
