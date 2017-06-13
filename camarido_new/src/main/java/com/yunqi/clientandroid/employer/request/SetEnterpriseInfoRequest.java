package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.yunqi.clientandroid.http.request.IRequest;

public class SetEnterpriseInfoRequest extends IRequest {

	public SetEnterpriseInfoRequest(Context context, String enterpriseName,
			String enterprisemessage) {
		super(context);
		// TODO Auto-generated constructor stub
		mParams.put("UserName", enterpriseName);
		mParams.put("UserMessage", enterprisemessage);
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
