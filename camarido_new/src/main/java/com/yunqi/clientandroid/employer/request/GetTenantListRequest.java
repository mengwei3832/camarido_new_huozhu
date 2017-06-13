package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.TenantList;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:
 * @ClassName: GetTenantListRequest
 * @author: chengtao
 * @date: Aug 13, 2016 1:46:30 PM
 * 
 */
public class GetTenantListRequest extends IRequest {
	private String packageId;

	public GetTenantListRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetTenantList?packageId=" + packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, TenantList>>() {
		}.getType();
	}

}
