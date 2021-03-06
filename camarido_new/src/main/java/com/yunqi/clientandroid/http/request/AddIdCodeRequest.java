package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:上传手持身份证的请求类
 * @ClassName: AddIdCodeRequest
 * @author: zhm
 * @date: 2016-5-27 下午1:42:28
 * 
 */
public class AddIdCodeRequest extends IRequest {

	public AddIdCodeRequest(Context context, int VehicleId, String ImgBase64,
			String ImgName) {
		super(context);
		mParams.put("VehicleId", VehicleId);
		mParams.put("ImgBase64", ImgBase64);
		mParams.put("ImgName", ImgName);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/ResetHandIdCodeImg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
