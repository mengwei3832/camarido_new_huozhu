package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.yunqi.clientandroid.http.request.IRequest;

public class GetEnterpriseInfoRequest extends IRequest {

	public GetEnterpriseInfoRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return getHost() + "";

	}

	@Override
	public Type getParserType() {
		// TODO Auto-generated method stub
		return null;
	}

}
