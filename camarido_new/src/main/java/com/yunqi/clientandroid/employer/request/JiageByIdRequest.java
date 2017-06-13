package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:根据Id获取活动内容，包含内容MessageContent
 * @ClassName: GetMessageByIdRequest
 * @author: chengtao
 * @date: 2016年7月1日 上午11:04:07
 * 
 */
public class JiageByIdRequest extends IRequest {
	private String id;

	public JiageByIdRequest(Context context, String id) {
		super(context);
		this.id = id;
		mParams.put("id", id);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Message/GetMessageByParentTagId/" + id;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Message, NullObject>>() {
		}.getType();
	}
}
