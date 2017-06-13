package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJiTop;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class ChengYunTongJiTopRequest extends IRequest {
	private String packageId;

	public ChengYunTongJiTopRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost()
				+ "pkgapi/Ticket/GetTicketCountModelByPackage?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<ChengYunTongJiTop, NullObject>>() {
		}.getType();
	}

}
