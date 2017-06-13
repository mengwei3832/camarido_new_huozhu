package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.SenumListInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:获取包对应的车型
 * @ClassName: GetSenumList
 * @author: chengtao
 * @date: 2016年6月29日 下午1:56:05
 * 
 */
public class GetSenumListRequest extends IRequest {
	private String packageId;

	public GetSenumListRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetSenumListById?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, SenumListInfo>>() {
		}.getType();
	}

}
