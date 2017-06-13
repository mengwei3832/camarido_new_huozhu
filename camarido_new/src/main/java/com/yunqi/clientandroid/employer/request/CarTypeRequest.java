package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CarType;
import com.yunqi.clientandroid.employer.entity.CopyPackageInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:车型的请求
 * @ClassName: CarTypeRequest
 * @author: chengtao
 * @date: 2016-6-21 下午7:39:03
 * 
 */
public class CarTypeRequest extends IRequest {

	public CarTypeRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetVehicleTypeList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, CarType>>() {
		}.getType();
	}

}
