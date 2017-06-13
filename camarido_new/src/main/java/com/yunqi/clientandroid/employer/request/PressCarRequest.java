package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:催促派车的接口
 * @ClassName: PressCarRequest
 * @author: mengwei
 * @date: 2016-7-1 下午3:14:39
 * 
 */
public class PressCarRequest extends IRequest {
	private String infoId;

	public PressCarRequest(Context context, String infoId) {
		super(context);
		this.infoId = infoId;
		mParams.put("InfoId", infoId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/ArgeToSendCar?InfoId=" + infoId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}
}
