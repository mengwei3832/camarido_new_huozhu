package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetProvinceJian;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:获取省份简称
 * @ClassName: GetPrivinceJianRequest
 * @author: chengtao
 * @date: 2016年6月17日 下午4:19:12
 * 
 */
public class GetPrivinceJianRequest extends IRequest {

	public GetPrivinceJianRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return HostUtil.getApiHost() + "api/Address/GetPabbreviationList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, GetProvinceJian>>() {
		}.getType();
	}
}
