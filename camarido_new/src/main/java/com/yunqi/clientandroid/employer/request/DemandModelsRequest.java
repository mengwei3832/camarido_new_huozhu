package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.DemandModels;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:车型需求的请求
 * @ClassName: DemandModelsRequest
 * @author: mengwei
 * @date: 2016-6-23 下午3:48:26
 * 
 */
public class DemandModelsRequest extends IRequest {

	public DemandModelsRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetVehicleTypeList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, DemandModels>>() {
		}.getType();
	}

}
