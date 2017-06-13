package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 指派车辆列表请求
 * @ClassName: GetAddCarListRequest
 * @author: zhm
 * @date: 2016-5-17 下午2:02:57
 * 
 */
public class GetAddCarListRequest extends IRequest {

	public GetAddCarListRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Vehicle/GetSendCarAppVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, CarListZhipai>>() {
		}.getType();
	}

}
