package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ShipHeighWay;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:公共点列表的请求
 * @ClassName: ShipHighWayRequest
 * @author: mengwei
 * @date: 2016-6-22 下午6:49:53
 * 
 */
public class ShipHighWayRequest extends IRequest {

	public ShipHighWayRequest(Context context, int PageIndex, int PageSize,
							  int ProvinceId, int CityId, int RegionId) {
		super(context);
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", PageSize);
			Pagenation.put("PageIndex", PageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("ProvinceId", ProvinceId);
			mParams.put("CityId", CityId);
			mParams.put("RegionId", RegionId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Address/GetPublicPointList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ShipHeighWay>>() {
		}.getType();
	}

}
