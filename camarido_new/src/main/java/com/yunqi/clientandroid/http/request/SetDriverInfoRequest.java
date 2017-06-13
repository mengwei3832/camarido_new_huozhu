package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.DriverVerifyInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 司机认证
 * @date 15/11/21
 */
public class SetDriverInfoRequest extends IRequest {

	public SetDriverInfoRequest(Context context, String userName,
			String idCode, String licenceImgName, String licenceImgUrl) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("IDCode", idCode);
		mParams.put("LicenceImgName", licenceImgName);
		mParams.put("LicenceImgUrl", licenceImgUrl);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Driver/SetDriverInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<DriverVerifyInfo, NullObject>>() {
		}.getType();
	}
}
