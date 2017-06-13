package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.ChooseAddressItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetAddressRequest extends IRequest {

	public GetAddressRequest(Context context, int pageSize, int pageIndex) {
		super(context);
		JSONObject pagenation = new JSONObject();
		try {
			pagenation.put("PageSize", pageSize);
			pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Address/GetAddress";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ChooseAddressItem>>() {
		}.getType();
	}

}
