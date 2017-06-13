package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:停止抢单的请求
 * @ClassName: StopApplyRequest
 * @author: chengtao
 * @date: 2016-5-24 下午3:52:05
 * 
 */
public class StopApplyRequest extends IRequest {
	private String packageID;

	public StopApplyRequest(Context context, String packageID) {
		super(context);
		this.packageID = packageID;
		mParams.put("packageId", packageID);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/StopApply?packageId=" + packageID;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
