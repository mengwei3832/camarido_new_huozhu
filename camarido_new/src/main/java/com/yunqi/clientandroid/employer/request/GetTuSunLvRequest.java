package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.TuSunLvBean;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;

public class GetTuSunLvRequest extends IRequest {

	public GetTuSunLvRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetDefaultPackageLoseRate";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<TuSunLvBean, Object>>() {
		}.getType();
	}

}
