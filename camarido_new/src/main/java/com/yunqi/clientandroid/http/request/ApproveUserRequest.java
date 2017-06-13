package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.entity.PersonalSingle;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 用户实名认证
 * 
 * @Description:
 * @ClassName: ApproveUserRequest
 * @author: zhangwb
 * @date: 2015年11月20日
 * 
 */
public class ApproveUserRequest extends IRequest {

	public ApproveUserRequest(Context context, String userName, String name,
			String idCode) {
		super(context);
		mParams.put("UserName", userName);
		mParams.put("Name", name);
		mParams.put("IDCode", idCode);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/User/ApproveUserInfo";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<PersonalSingle, NullObject>>() {
		}.getType();
	}

}
