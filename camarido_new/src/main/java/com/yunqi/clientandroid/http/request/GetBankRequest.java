package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取银行接口
 * @date 15/11/21
 */
public class GetBankRequest extends IRequest {

	public GetBankRequest(Context context, String id) {
		super(context);
		mParams.put("id", id);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Enum/GetListByGroup/504";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, VehicleType>>() {
		}.getType();
	}
}
