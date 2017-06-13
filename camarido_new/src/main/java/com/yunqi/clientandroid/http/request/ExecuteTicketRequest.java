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
 * @Description class 开始执行订单接口
 * @date 15/12/3
 */
public class ExecuteTicketRequest extends IRequest {

	private String id;

	public ExecuteTicketRequest(Context context, String id) {
		super(context);
		this.id = id;
		// mParams.put("id", id);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/ExecuteTicket/" + id;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
