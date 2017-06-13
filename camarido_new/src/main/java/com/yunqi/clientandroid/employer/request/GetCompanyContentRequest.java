package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetCompanyContent;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 获取企业信息的请求
 * @ClassName: GetCompanyContentRequest
 * @author: mengwei
 * @date: 2016-6-6 下午3:41:31
 * 
 */
public class GetCompanyContentRequest extends IRequest {

	public GetCompanyContentRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/User/GetTenantUserInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetCompanyContent, NullObject>>() {
		}.getType();
	}

}
