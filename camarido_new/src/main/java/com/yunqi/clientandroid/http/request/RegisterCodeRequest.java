package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 注册短信验证获取接口
 * 
 * @Description:
 * @ClassName: RegisterCodeRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:22:23
 * 
 */
public class RegisterCodeRequest extends IRequest {

	public RegisterCodeRequest(Context context, String userName) {
		super(context);
		mParams.put("UserName", userName);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/GetRegistMsg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
