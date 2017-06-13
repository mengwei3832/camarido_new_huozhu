package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PersonalSingle;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 发包方再派一车的请求类
 * @ClassName: AgainQiangRequest
 * @author: zhm
 * @date: 2016-4-21 下午6:18:17
 * 
 */
public class AgainQiangRequest extends IRequest {
	private String tickedID;

	public AgainQiangRequest(Context context, String tickedID) {
		super(context);
		this.tickedID = tickedID;
		mParams.put("ticketId", tickedID);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/ GrabOnceMore?ticketId=" + tickedID;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
