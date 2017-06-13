package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:行业资讯列表请求
 * @ClassName: ZiXunLieBiaoRequest
 * @author: chengtao
 * @date: 2016-7-7 上午10:17:47
 * 
 */
public class ZiXunLieBiaoRequest extends IRequest {

	public ZiXunLieBiaoRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Message/GetFirstLevelTags";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, ZiXunLieBiaoBean>>() {
		}.getType();
	}

}
