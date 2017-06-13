package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:为公司添加常用车辆
 * @ClassName: AddVehicleRequest
 * @author: chengtao
 * @date: 2016年6月17日 上午9:41:52
 * 
 */
public class AddVehicleRequest extends IRequest {

	public AddVehicleRequest(Context context, String vehicleNo) {
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
