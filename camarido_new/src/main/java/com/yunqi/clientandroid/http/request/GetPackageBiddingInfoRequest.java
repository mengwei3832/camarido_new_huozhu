package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PackageBiddingInfo;
import com.yunqi.clientandroid.entity.PackageDetailInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取包竞价历史统计信息
 * @date 15/12/12
 */
public class GetPackageBiddingInfoRequest extends IRequest {

	private String packageId;

	public GetPackageBiddingInfoRequest(Context context, String packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetPackageBiddingInfo/" + "?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<PackageBiddingInfo, NullObject>>() {
		}.getType();
	}

}
