package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.VehicleList;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:我的车辆接口
 * @ClassName: GetVehicleRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午11:56:48
 * 
 */
public class GetVehicleRequest extends IRequest {

	public GetVehicleRequest(Context context, int pageIndex, int pageSize) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("pageIndex", pageIndex);
			Pagenation.put("pageSize", pageSize);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Vehicle/GetVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, VehicleList>>() {
		}.getType();
	}

}
