package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CompanyDetail;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;

import android.content.Context;

public class CompanyDetailRequest extends IRequest {
	private int packageId;

	public CompanyDetailRequest(Context context, int packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("id", packageId);
	}

	@Override
	public String getUrl() {
		L.e("TAG", "进行请求的网址==================================");

		return getHost() + "api/Package/GetTenantData/" + packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<CompanyDetail, NullObject>>() {
		}.getType();
	}

}
