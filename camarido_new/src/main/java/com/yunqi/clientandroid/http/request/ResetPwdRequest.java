package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 重置密码接口
 * 
 * @Description:
 * @ClassName: ResetPwdRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:21:00
 * 
 */
public class ResetPwdRequest extends IRequest {

	public ResetPwdRequest(Context context, String userName, String password,
			String shortMsg) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("Password", password);
		mParams.put("ShortMsg", shortMsg);
	}

	@Override
	public String getUrl() {
		return HostPkgUtil.getApiHost() + "pkgapi/Auth/ResetRegistPwd";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<LoginInfo, Object>>() {
		}.getType();
	}

}
