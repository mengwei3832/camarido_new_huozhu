package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:平台服务选项（是否送保险）
 * @ClassName: GetPlatfromServiceOptionsRequest
 * @author: chengtao
 * @date: 2016年6月17日 下午12:08:23
 * 
 */
public class GetPlatfromServiceOptionsRequest extends IRequest {

	public GetPlatfromServiceOptionsRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/GetPlatfromServiceOptions";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
