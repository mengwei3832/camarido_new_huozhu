package com.yunqi.clientandroid.employer.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取发包方当前运单列表接口
 * @date 15/12/3
 */

public class GetCurrentOrderListRequest extends IRequest {

	public GetCurrentOrderListRequest(Context context, int pageSize,
			int pageIndex, int ticketStatus, String keyword) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();

		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("TicketStatus", ticketStatus);
			if (keyword != null && !TextUtils.isEmpty(keyword))
				mParams.put("Keyword", keyword);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/ProviderTicket/GetTicketList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PerformListItem>>() {
		}.getType();
	}

}
