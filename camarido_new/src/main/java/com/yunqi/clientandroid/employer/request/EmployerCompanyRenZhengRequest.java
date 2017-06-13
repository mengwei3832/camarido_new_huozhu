package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:申请企业认证的请求
 * @ClassName: EmployerCompanyRenZhengRequest
 * @author: mengwei
 * @date: 2016-6-3 上午11:39:54
 * 
 */
public class EmployerCompanyRenZhengRequest extends IRequest {

	public EmployerCompanyRenZhengRequest(Context context, String mTenantName,
			String userphone, String mContactPhone) {
		super(context);
		mParams.put("TenantName", mTenantName);
		mParams.put("UserPhone", userphone);
		mParams.put("ContactPhone", mContactPhone);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/GatherTenantInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
