package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 获取手机信息的请求类
 * @ClassName: GetPhoneInfoRequest
 * @author: zhm
 * @date: 2016-5-12 下午2:45:34
 * 
 */
public class GetPhoneInfoRequest extends IRequest {

	public GetPhoneInfoRequest(Context context, String Imei, String Imsi,
			String AppVersion, int ActionType) {
		super(context);
		mParams.put("Imei", Imei);
		mParams.put("Imsi", Imsi);
		mParams.put("AppVersion", AppVersion);
		mParams.put("ActionType", ActionType);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/App/SetUserAppInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
