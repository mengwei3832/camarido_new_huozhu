package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 设置用户信息
 * 
 * @Description:
 * @ClassName: SetUserInfoRequest
 * @author: zhm
 * @date: 2015年11月20日 下午7:19:34
 * 
 */
public class SetUserInfoRequest extends IRequest {

	public SetUserInfoRequest(Context context, String userName,
			String nickName, String gender, String year) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("Nickname", nickName);
		mParams.put("Gender", gender);
		mParams.put("Years", year);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/SetUserInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<UserInfo, NullObject>>() {
		}.getType();
	}

}
