package com.yunqi.clientandroid.employer.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取操作过程列表
 * @date 15/12/17
 */
public class GetTicketExecutedRequest extends IRequest {

	private String ticketId;

	public GetTicketExecutedRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("id", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetExecuteHis/" + ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ModifyListItem>>() {
		}.getType();
	}

}
