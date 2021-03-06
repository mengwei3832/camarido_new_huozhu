package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取待执行订单列表列表接口
 * @date 15/11/30
 */
public class GetBeforeExecuteTicketListRequest extends IRequest {

	public GetBeforeExecuteTicketListRequest(Context context, int pageSize,
			int pageIndex) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();

		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/GetBeforeExecuteTicketList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PerformListItem>>() {
		}.getType();
	}

}
