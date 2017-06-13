package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.FormerPayPass;
import com.yunqi.clientandroid.entity.MineTicketCount;
import com.yunqi.clientandroid.entity.PayPassSafety;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @Description:class 获取原来的密码
 * @ClassName: GetCurrentPayPassExistRequest
 * @author: zhm
 * @date: 2016-3-25 上午10:04:53
 * 
 */
public class GetFormerPayPassRequest extends IRequest {

	public GetFormerPayPassRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<FormerPayPass, NullObject>>() {
		}.getType();
	}

}
