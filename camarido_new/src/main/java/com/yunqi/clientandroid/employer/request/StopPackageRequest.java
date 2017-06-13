package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class StopPackageRequest extends IRequest {
	private String packageId;

	public StopPackageRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/TerminationPackage?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
