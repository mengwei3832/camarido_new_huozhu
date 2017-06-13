package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetSunCar;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 指派总车数的的请求
 * @ClassName: GetSumCarRequest
 * @author: zhm
 * @date: 2016-5-20 下午2:39:05
 * 
 */
public class GetSumCarRequest extends IRequest {
	private int packageId;

	public GetSumCarRequest(Context context, int packageId) {
		super(context);
		packageId = this.packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/ AppointVehicleNum?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetSunCar, NullObject>>() {
		}.getType();
	}

}
