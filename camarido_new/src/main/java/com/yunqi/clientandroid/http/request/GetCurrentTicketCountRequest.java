package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.MineTicketCount;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取执行订单数量接口
 * @date 15/12/5
 */
public class GetCurrentTicketCountRequest extends IRequest {

	public GetCurrentTicketCountRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/GetCurrentTicketCount";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<MineTicketCount, NullObject>>() {
		}.getType();
	}

}
