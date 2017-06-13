package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.OrderAuditInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取结算预览数据接口
 * @date 16/3/1
 */
public class GetTicketSettlePreviewRequest extends IRequest {

	private String ticketId;

	public GetTicketSettlePreviewRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("id", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/TicketSettlePreview/" + ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<OrderAuditInfo, NullObject>>() {
		}.getType();
	}

}
