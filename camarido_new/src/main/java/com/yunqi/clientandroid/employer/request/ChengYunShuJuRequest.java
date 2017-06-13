package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ChengYunShuJu;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:请求承运数据
 * @ClassName: ChengYunShuJuRequest
 * @author: chengtao
 * @date: 2016-8-2 上午9:05:48
 * 
 */
public class ChengYunShuJuRequest extends IRequest {
	private String ticketId;

	public ChengYunShuJuRequest(Context context, String ticketId) {
		super(context);
		this.ticketId = ticketId;
		mParams.put("ticketId", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetVticketOperationList?ticketId="
				+ ticketId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ChengYunShuJu>>() {
		}.getType();
	}
}
