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
 * @Description class 点击进行结算接口
 * @date 16/3/1
 */
public class ApproveAndSettleRequest extends IRequest {

	public ApproveAndSettleRequest(Context context, int ticketId,
			String payCode, double ticketSettleAmount, String remake,
			int payType, String ticketExpectAmount) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("TicketExpectAmount", ticketExpectAmount);
		mParams.put("PayPwd", payCode);
		mParams.put("TicketSettleAmount", ticketSettleAmount);
		mParams.put("TicketSettleMemo", remake);
		mParams.put("PayType", payType);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/TicketApproveAndSettle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
