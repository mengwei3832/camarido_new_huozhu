package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.PingTaiKeFu;
import com.yunqi.clientandroid.employer.entity.SenumListInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:平台客服电话请求
 * @ClassName: PingTaiKeFuRequest
 * @author: chengtao
 * @date: 2016-7-8 下午7:21:11
 * 
 */
public class PingTaiKeFuRequest extends IRequest {

	public PingTaiKeFuRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Auth/GetPlatServicePhone";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<PingTaiKeFu, NullObject>>() {
		}.getType();
	}

}
