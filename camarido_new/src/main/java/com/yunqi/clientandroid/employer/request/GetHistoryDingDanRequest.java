package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.HistoryDingDan;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:获取历史订单
 * @ClassName: GetHistoryYunDanRequest
 * @author: chengtao
 * @date: Aug 31, 2016 9:51:39 AM
 * 
 */
public class GetHistoryDingDanRequest extends IRequest {

	public GetHistoryDingDanRequest(Context context, int pageIndex, int pageSize) {
		super(context);
		JSONObject pagination = new JSONObject();
		try {
			pagination.put("PageIndex", pageIndex);
			pagination.put("PageSize", pageSize);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mParams.put("Pagination", pagination);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetHistoryPackageList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, HistoryDingDan>>() {
		}.getType();
	}

}
