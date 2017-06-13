package com.yunqi.clientandroid.employer.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetVTicketByIdInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的订单订单详情接口
 * @date 15/12/5
 */

public class GetOrderDetailRequest extends IRequest {

	private String ticketId;

	public GetOrderDetailRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("id", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetTicketById/?ticketId=" + ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetVTicketByIdInfo, NullObject>>() {
		}.getType();
	}

}
