package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取邀请码
 * @date 15/11/21
 */
public class GetInviteCodeRequest extends IRequest {
	private String mVechicleId;

	public GetInviteCodeRequest(Context context, String vechicleId) {
		super(context);
		this.mVechicleId = vechicleId;
		mParams.put("vechicleId", vechicleId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Vehicle/GetVehicleInviteCode" + "?vechicleId="
				+ mVechicleId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
