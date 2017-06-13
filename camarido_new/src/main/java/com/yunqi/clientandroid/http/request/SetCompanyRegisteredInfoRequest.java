package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 企业认证
 * @date 16/03/25
 */

public class SetCompanyRegisteredInfoRequest extends IRequest {

	public SetCompanyRegisteredInfoRequest(Context context, String name,
			String idCode, String enterpriseName, String imgBusinessBase64,
			String businessImgName, String imgAttorneyBase64,
			String attorneyImgName, String imgWithCardBase64,
			String withCardImgName) {
		super(context);
		mParams.put("Name", name);
		mParams.put("IdCode", idCode);
		mParams.put("ComName", enterpriseName);
		mParams.put("BusinessLicenseImgUrl", imgBusinessBase64);
		mParams.put("BusinessLicenseImgName", businessImgName);
		mParams.put("AuthorizationLetterImgUrl", imgAttorneyBase64);
		mParams.put("AuthorizationLetterImgName", attorneyImgName);
		mParams.put("HandIdCodeImgUrl", imgWithCardBase64);
		mParams.put("HandIdCodeImgName", withCardImgName);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/SetCompanyRegisteredInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
