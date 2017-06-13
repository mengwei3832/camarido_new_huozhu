package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CopyPackageInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class CopyPackageRequest extends IRequest {
	private String packageId;

	public CopyPackageRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetPackageById?packageID="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<CopyPackageInfo, NullObject>>() {
		}.getType();
	}

}
