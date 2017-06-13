package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class IsEixtsPackageRequest extends IRequest {

	public IsEixtsPackageRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/IsEixtsPackage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
