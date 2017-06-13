package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取关注路线下的包列表信息--旧包
 * @date 15/11/27
 */
public class GetPackageSubscriptsRequestTwo extends IRequest {

	public GetPackageSubscriptsRequestTwo(Context context, int pageSize,
			int pageIndex) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();

		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetPackageListOfSubscripts";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, FocusonRoute>>() {
		}.getType();
	}
}
