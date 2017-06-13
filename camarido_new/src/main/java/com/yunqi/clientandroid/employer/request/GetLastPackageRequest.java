package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetLastPackageInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;

public class GetLastPackageRequest extends IRequest {

	public GetLastPackageRequest(Context context) {
		super(context);
		mParams.put("PageSize", 1);
		mParams.put("PageIndex", 1);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetLastPackage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetLastPackageInfo, Object>>() {
		}.getType();
	}

}
