package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetCurrentCarNumber;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 获取当日统计的车辆数量
 */
public class GetCurrentCarNumberRequest extends IRequest {
	private String packageId, date;

	public GetCurrentCarNumberRequest(Context context, String packageId,
			String date) {
		super(context);
		this.packageId = packageId;
		this.date = date;
		mParams.put("packageId", packageId);
		mParams.put("date", date);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetCurrentDayTicketInfo?packageId="
				+ packageId + "&date=" + date;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetCurrentCarNumber, NullObject>>() {
		}.getType();
	}

}
