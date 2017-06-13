package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取已完成订单列表接口
 * @date 15/12/5
 */
public class GetCompleteTicketListRequest extends IRequest {

	public GetCompleteTicketListRequest(Context context, int pageSize,
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
		return getHost() + "api/Ticket/GetCompleteTicketList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PerformListItem>>() {
		}.getType();
	}

}
