package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 上传头像接口
 * @date 15/11/21
 */
public class UploadHeadImageRequest extends IRequest {

	public UploadHeadImageRequest(Context context, String imgName,
			String imgBase64) {
		super(context);

		mParams.put("ImgName", imgName);
		mParams.put("ImgBase64", imgBase64);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/SetHeadPortrait";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
