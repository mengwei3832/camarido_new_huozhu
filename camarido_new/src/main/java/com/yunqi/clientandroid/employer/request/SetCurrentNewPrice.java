package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.QuoteSetNewPrice;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 设置当日最新价格
 */
public class SetCurrentNewPrice extends IRequest {
	private String packageId;
	private String price;
	private String effectTime;

	public SetCurrentNewPrice(Context context, String packageId, String price,String effectTime) {
		super(context);
		this.packageId = packageId;
		this.price = price;
		this.effectTime = effectTime;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/SetNewPackagePrice?packageId="
				+ packageId + "&price=" + price+"&effectTimeStr="+effectTime;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<QuoteSetNewPrice, NullObject>>() {
		}.getType();
	}

}
