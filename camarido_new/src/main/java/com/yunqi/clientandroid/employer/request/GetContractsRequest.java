package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetContractsInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;

public class GetContractsRequest extends IRequest {

	public GetContractsRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/User/GetContracts";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Object, GetContractsInfo>>() {
		}.getType();
	}

}
