package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.AddVehicleEntity;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:添加公司产权车辆的请求
 * @ClassName: AddPersonVehicleRequest
 * @author: zhm
 * @date: 2016-5-27 上午9:28:52
 * 
 */
public class AddCompanyVehicleRequest extends IRequest {

	public AddCompanyVehicleRequest(Context context, String VehicleNo,
			String VehicleContacts) {
		super(context);
		mParams.put("VehicleNo", VehicleNo);
		mParams.put("VehicleContacts", VehicleContacts);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/AddCompanyVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<AddVehicleEntity, NullObject>>() {
		}.getType();
	}

}
