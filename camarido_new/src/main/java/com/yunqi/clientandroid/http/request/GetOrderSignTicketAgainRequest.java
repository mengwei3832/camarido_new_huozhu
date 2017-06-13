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
 * @Description class 重新上传签收单接口
 * @date 15/12/5
 */

public class GetOrderSignTicketAgainRequest extends IRequest {

	public GetOrderSignTicketAgainRequest(Context context, String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("TicketRelationCode", ticketRelationCode);
		mParams.put("TicketWeight", ticketWeight);
		mParams.put("TicketOperationPicUrl", ticketOperationPicUrl);
		mParams.put("TicketOperationPicName", ticketOperationPicName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/ProviderTicket/SignTicketAgain";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
