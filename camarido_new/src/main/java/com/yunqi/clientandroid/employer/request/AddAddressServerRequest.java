package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 上传地址到服务器
 * @ClassName: AddAddressServerRequest
 * @author: zhm
 * @date: 2016-5-23 上午9:34:04
 * 
 */
public class AddAddressServerRequest extends IRequest {

	public AddAddressServerRequest(Context context, String addressdetail,
			String addressCustomName, String provinceId, String cityId,
			String areasId, String Longitude, String Latitude) {
		super(context);
		mParams.put("Addressdetail", addressdetail);
		mParams.put("AddressCustomName", addressCustomName);
		mParams.put("ProvinceRegionId", provinceId);
		mParams.put("CityRegionId", cityId);
		mParams.put("SubRegionId", areasId);
		mParams.put("Longitude", Longitude);
		mParams.put("Latitude", Latitude);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Address/AddAddress";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
