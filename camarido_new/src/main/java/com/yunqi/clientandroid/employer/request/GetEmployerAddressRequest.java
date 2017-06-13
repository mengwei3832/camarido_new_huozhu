package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.EmployerAddressInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetEmployerAddressRequest extends IRequest {

	public GetEmployerAddressRequest(Context context, int pageIndex,
			int pageSize) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("pageIndex", pageIndex);
			Pagenation.put("pageSize", pageSize);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetAddress";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, EmployerAddressInfo>>() {
		}.getType();
	}

}
