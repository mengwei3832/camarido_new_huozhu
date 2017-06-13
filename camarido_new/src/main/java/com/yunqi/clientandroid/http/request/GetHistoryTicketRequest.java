package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.PerformListItem;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取历史订单列表接口
 * @date 15/12/5
 */
public class GetHistoryTicketRequest extends IRequest {

	// 所有历史订单
	public GetHistoryTicketRequest(Context context, int pageSize, int pageIndex) {
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

	// 具体历史订单
	public GetHistoryTicketRequest(Context context, int pageSize,
			int pageIndex, String ticketCode) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();

		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("TicketCode", ticketCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 筛选后历史订单
	public GetHistoryTicketRequest(Context context, int pageSize,
			int pageIndex, int mCatoryCheckId, int mOrderType,
			int mPackageBeginProvinceId, int mPackageBeginCityId,
			int mPackageEndProvinceId, int mPackageEndCityId, String timeMin,
			String timeMax) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();

		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);

			if (!TextUtils.isEmpty(mCatoryCheckId + "") && mCatoryCheckId > 0) {
				mParams.put("CategoryId", mCatoryCheckId);
			}
			if (!TextUtils.isEmpty(mOrderType + "") && mOrderType > 0) {
				mParams.put("TicketStatus", mOrderType);
			}
			if (!TextUtils.isEmpty(mPackageBeginProvinceId + "")
					&& mPackageBeginProvinceId > 0) {
				mParams.put("BeginProvince", mPackageBeginProvinceId);
			}
			if (!TextUtils.isEmpty(mPackageBeginCityId + "")
					&& mPackageBeginCityId > 0) {
				mParams.put("BeginCity", mPackageBeginCityId);
			}
			if (!TextUtils.isEmpty(mPackageEndProvinceId + "")
					&& mPackageEndProvinceId > 0) {
				mParams.put("EndProvince", mPackageEndProvinceId);
			}
			if (!TextUtils.isEmpty(mPackageEndCityId + "")
					&& mPackageEndCityId > 0) {
				mParams.put("EndCity", mPackageEndCityId);
			}
			if (!TextUtils.isEmpty(timeMin) && timeMin != null) {
				mParams.put("BeginTime", timeMin);
			}
			if (!TextUtils.isEmpty(timeMax) && timeMax != null) {
				mParams.put("EndTime", timeMax);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Ticket/GetTicketListHis";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PerformListItem>>() {
		}.getType();
	}

}
