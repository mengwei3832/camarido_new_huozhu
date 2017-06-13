package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 订单过程审核订单
 * @ClassName: ShenHeTicketRequest
 * @author: chengtao
 * @date: 2016-6-14 上午11:20:34
 * 
 */
public class ShenHeTicketRequest extends IRequest {
	private String operationId;

	public ShenHeTicketRequest(Context context, String operationId) {
		super(context);
		this.operationId = operationId;
		mParams.put("operationId", operationId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/CheckTicketBill?operationId="
				+ operationId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
