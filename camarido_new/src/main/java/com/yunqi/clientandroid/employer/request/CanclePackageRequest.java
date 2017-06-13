package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:取消包的请求
 * @ClassName: CanclePackageRequest
 * @author: mengwei
 * @date: 2016-8-31 上午10:14:02
 * 
 */
public class CanclePackageRequest extends IRequest {
	private String packageId;

	public CanclePackageRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("packageId", packageId);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/CanclePackage?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
