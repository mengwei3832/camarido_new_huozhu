package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.YiJiaHistory;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class YiJiaHistoryRequest extends IRequest {

	public YiJiaHistoryRequest(Context context, String packageId,
			String departId, int pageIndex, int pageSize) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("PackageId", packageId);
			mParams.put("DepartId", departId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetInfoDepartPackagePriceHisList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, YiJiaHistory>>() {
		}.getType();
	}
}
