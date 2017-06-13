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
 * @Description class 竞价接口
 * @date 15/12/9
 */
public class BiddingTicketRequest extends IRequest {

	private String packageId;
	private String Id;
	private String biddingPrice;

	public BiddingTicketRequest(Context context, String packageId, String Id,
			String biddingPrice) {
		super(context);
		this.packageId = packageId;
		this.Id = Id;
		this.biddingPrice = biddingPrice;

		mParams.put("PackageId", packageId + "");
		mParams.put("VehicleId", Id);
		mParams.put("BiddingPrice", biddingPrice);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/BiddingATicket";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
