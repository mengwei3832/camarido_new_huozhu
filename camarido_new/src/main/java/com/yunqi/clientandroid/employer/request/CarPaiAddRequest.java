package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 添加车辆的请求
 * @ClassName: CarPaiAddRequest
 * @author: zhm
 * @date: 2016-5-19 上午9:46:29
 * 
 */
public class CarPaiAddRequest extends IRequest {

	public CarPaiAddRequest(Context context, String vehicleNo) {
		super(context);
		mParams.put("vehicleNo", vehicleNo);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Vehicle/AddVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
