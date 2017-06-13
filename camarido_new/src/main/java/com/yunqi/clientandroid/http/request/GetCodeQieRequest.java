package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.HostUtil.HostType;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;
import android.util.Log;

public class GetCodeQieRequest extends IRequest {
	private String code;

	public GetCodeQieRequest(Context context, String code) {
		super(context);
		this.code = code;
		mParams.put("key", code);
	}

	@Override
	public String getUrl() {
		return "http://qa.api.yqtms.com/api/User/CanSwitchSystem?key=" + code;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
