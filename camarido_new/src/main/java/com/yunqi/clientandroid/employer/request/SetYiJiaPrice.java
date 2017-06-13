package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:议价时的请求
 * @ClassName: SetYiJiaPrice
 * @author: mengwei
 * @date: 2016-6-29 下午7:58:59
 * 
 */
public class SetYiJiaPrice extends IRequest {

	public SetYiJiaPrice(Context context, String infoId, String price,
			String vehicleCount) {
		super(context);
		mParams.put("InfoId", infoId);
		mParams.put("Price", price);
		mParams.put("VehicleCount", vehicleCount);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/AppBargining";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
