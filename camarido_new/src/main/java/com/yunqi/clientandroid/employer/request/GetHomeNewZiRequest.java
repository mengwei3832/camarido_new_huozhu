package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.employer.entity.HomeFragmentZixun;
import com.yunqi.clientandroid.employer.entity.ProvinceNameModel;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:class 获取首页最新资讯数据请求
 * @ClassName: GetHomeNewRequest
 * @author: zhm
 * @date: 2016-5-13 下午2:58:30
 * 
 */
public class GetHomeNewZiRequest extends IRequest {

	public GetHomeNewZiRequest(Context context, int PageIndex, int PageSize) {
		super(context);
		// //传参
		// JSONObject Pagenation = new JSONObject();
		// try {
		// Pagenation.put("PageSize", PageSize);
		// Pagenation.put("PageIndex", PageIndex);
		// mParams.put("Pagenation", Pagenation);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

//		mParams.put("PageIndex", PageIndex);
//		mParams.put("PageSize", PageSize);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/vehicle/GetHighwayIndexChangeSummary";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<ProvinceNameModel, NullObject>>() {
		}.getType();
	}

}
