package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.QuoteOrder;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:已中标的报价单请求
 * @ClassName: QuoteAllOrderRequest
 * @author: chengtao
 * @date: 2016-6-29 下午2:14:18
 * 
 */
public class QuoteSuccessFulOrderRequest extends IRequest {

	public QuoteSuccessFulOrderRequest(Context context, int PageIndex,
			int PageSize, String packageId) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", PageSize);
			Pagenation.put("PageIndex", PageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("packageId", packageId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetOverPartInforByPakcage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, QuoteOrder>>() {
		}.getType();
	}

}
