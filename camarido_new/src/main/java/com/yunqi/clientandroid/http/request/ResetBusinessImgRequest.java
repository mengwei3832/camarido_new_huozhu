package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 重新上传营业执照
 * @date 16/03/28
 */
public class ResetBusinessImgRequest extends IRequest {

	public ResetBusinessImgRequest(Context context, String imgBase64,
			String imgName) {
		super(context);

		mParams.put("BusinessLicenseImgUrl", imgBase64);
		mParams.put("BusinessLicenseImgName", imgName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/ResetBusinessLicenseImg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
