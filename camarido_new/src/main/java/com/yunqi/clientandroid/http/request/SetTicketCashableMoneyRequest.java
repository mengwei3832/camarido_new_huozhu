package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class SetTicketCashableMoneyRequest extends IRequest {

	public SetTicketCashableMoneyRequest(Context context, String ticketId,
			int userId, double userMoney, int user2Id, double user2Money,
			String totalMoney, String inputCode) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("UserId", userId);
		mParams.put("UserMoney", userMoney);
		mParams.put("User2Id", user2Id);
		mParams.put("User2Money", user2Money);
		mParams.put("TotalMoney", totalMoney);
		mParams.put("ShortMsg", inputCode);
	}

	public SetTicketCashableMoneyRequest(Context context, String ticketId,
			int userId, double userMoney, String totalMoney, String inputCode) {
		super(context);
		mParams.put("TicketId", ticketId);
		mParams.put("UserId", userId);
		mParams.put("UserMoney", userMoney);
		mParams.put("TotalMoney", totalMoney);
		mParams.put("ShortMsg", inputCode);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/SetTicketCashableMoney";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
