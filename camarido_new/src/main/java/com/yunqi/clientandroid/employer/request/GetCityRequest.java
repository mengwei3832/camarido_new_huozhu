package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 获取市的请求
 * @ClassName: GetProvinceRequest
 * @author: mengwei
 * @date: 2016-6-7 下午7:53:43
 * 
 */
public class GetCityRequest extends IRequest {
	private int provinceId;

	public GetCityRequest(Context context, int privinceId) {
		super(context);
		this.provinceId = privinceId;
		mParams.put("provinceId", privinceId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Address/GetCitysByProvinceId?provinceId="
				+ provinceId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, GetProvince>>() {
		}.getType();
	}

}
