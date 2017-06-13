package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:判断公司信息是否采集
 * @ClassName: DecideCompanyMessageRequest
 * @author: mengwei
 * @date: 2016-6-22 上午10:09:39
 * 
 */
public class DecideCompanyMessageRequest extends IRequest {

	public DecideCompanyMessageRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/IsCollectInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
