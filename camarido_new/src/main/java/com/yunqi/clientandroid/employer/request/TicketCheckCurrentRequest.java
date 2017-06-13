package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 待结算订单的请求类
 * @ClassName: TicketAllCurrentRequest
 * @author: mengwei
 * @date: 2016-6-12 下午3:05:43
 * 
 */
public class TicketCheckCurrentRequest extends IRequest {

	public TicketCheckCurrentRequest(Context context, int pageSize,
			int pageIndex, String keyword, String packageId) {
		super(context);
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("KeyWord", keyword);
			mParams.put("PackageID", packageId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetBeforeSettleTicket";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, TicketCurrentBean>>() {
		}.getType();
	}

}
