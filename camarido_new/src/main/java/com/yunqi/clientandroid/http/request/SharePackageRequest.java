package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 分享包信息
 * @date 15/11/21
 */
public class SharePackageRequest extends IRequest {
	private String packageId;

	public SharePackageRequest(Context context, String packageId) {
		super(context);

		this.packageId = packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Share/GetSharePackageSummary?packageId="
				+ packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Share, NullObject>>() {
		}.getType();
	}
}
