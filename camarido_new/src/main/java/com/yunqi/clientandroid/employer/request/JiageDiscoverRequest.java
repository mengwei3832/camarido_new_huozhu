package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:发现页面的请求
 * @ClassName: JiageDiscoverRequest
 * @author: chengtao
 * @date: 2016-7-7 下午5:19:59
 * 
 */
public class JiageDiscoverRequest extends IRequest {

	public JiageDiscoverRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Message/GetSecLevelTags";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ZiXunLieBiaoBean>>() {
		}.getType();
	}

}
