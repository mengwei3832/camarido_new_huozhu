package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PersonalSingle;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 设置用户信息
 * 
 * @Description:
 * @ClassName: GetUserInfoRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:19:34
 * 
 */
public class GetUserInfoRequest extends IRequest {

	public GetUserInfoRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/GetUserInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<UserInfo, NullObject>>() {
		}.getType();
	}

}
