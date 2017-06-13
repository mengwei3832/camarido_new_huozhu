package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetTicketInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:根据Id获取订单详细
 * @ClassName: GetTicketRequest
 * @author: chengtao
 * @date: 2016年6月28日 下午7:54:47
 * 
 */
public class GetTicketRequest extends IRequest {
	private String ticketId;

	public GetTicketRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetTicketById?ticketId=" + ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetTicketInfo, NullObject>>() {
		}.getType();
	}

}
