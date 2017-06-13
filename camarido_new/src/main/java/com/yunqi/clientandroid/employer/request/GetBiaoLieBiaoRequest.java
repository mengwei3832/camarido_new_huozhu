package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:得到包列表详情数据的请求
 * @ClassName: GetBiaoLieBiaoRequest
 * @author: zhm
 * @date: 2016-5-23 下午1:36:41
 * 
 */
public class GetBiaoLieBiaoRequest extends IRequest {

	public GetBiaoLieBiaoRequest(Context context, int mPageSize, int mPageIndex) {
		super(context);
		// 传参
		JSONObject Pagination = new JSONObject();
		try {
			Pagination.put("PageSize", mPageSize);
			Pagination.put("PageIndex", mPageIndex);
			mParams.put("Pagination", Pagination);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetPackageAppList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, GetBiaoLieBiao>>() {
		}.getType();
	}

}
