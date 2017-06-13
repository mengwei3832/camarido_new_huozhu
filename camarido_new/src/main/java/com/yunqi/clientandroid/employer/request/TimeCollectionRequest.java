package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.TimeCollection;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 请求有数据的时间
 */
public class TimeCollectionRequest extends IRequest {
	private String packageId;
	private String date;

	public TimeCollectionRequest(Context context,String packageId, String date) {
		super(context);
		this.packageId = packageId;
		this.date = date;
		mParams.put("PackageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetTransDate?packageId="+packageId+"&date="+date+"&count=5";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, TimeCollection>>() {
		}.getType();
	}

}
