package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 审核订单接口
 * @ClassName: ShenHeTicketPassRequest
 * @author: chengtao
 * @date: 2016-6-14 下午3:03:01
 * 
 */
public class ShenHeTicketPassRequest extends IRequest {

	public ShenHeTicketPassRequest(Context context, String ticketId) {
		super(context);
		mParams.put("ticketId", ticketId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/DoTicketExamin";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
