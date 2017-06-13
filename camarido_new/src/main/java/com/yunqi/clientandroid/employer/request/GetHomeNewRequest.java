package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:class 获取首页最新动态数据请求
 * @ClassName: GetHomeNewRequest
 * @author: zhm
 * @date: 2016-5-13 下午2:58:30
 * 
 */
public class GetHomeNewRequest extends IRequest {

	public GetHomeNewRequest(Context context, int PageIndex, int PageSize) {
		super(context);
		// 传参
		// JSONObject Pagenation = new JSONObject();
		// try {
		// Pagenation.put("PageSize", PageSize);
		// Pagenation.put("PageIndex", PageIndex);
		// mParams.put("Pagenation", Pagenation);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		mParams.put("PageIndex", PageIndex);
		mParams.put("PageSize", PageSize);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetIndexPackage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, HomeFragmentNew>>() {
		}.getType();
	}

}
