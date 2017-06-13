package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.QuoteOrder;
import com.yunqi.clientandroid.employer.entity.TongJiDaoChu;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:统计页面导出请求
 * @ClassName: TongJiDaoChuRequest
 * @author: mengwei
 * @date: 2016-8-26 上午10:25:19
 * 
 */
public class TongJiDaoChuRequest extends IRequest {

	public TongJiDaoChuRequest(Context context, String packageId,
			String loadTime, String signTime, String vehicleNo,
			int departMentId, int ticketStatus, int todayCount) {
		super(context);
		mParams.put("PackageId", packageId);
		mParams.put("LoadTime", loadTime);
		mParams.put("SignTime", signTime);
		mParams.put("VehicleNo", vehicleNo);
		mParams.put("DepartMentId", departMentId);
		mParams.put("TicketStatus", ticketStatus);
		mParams.put("IsTodayCount", todayCount);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/ReportFormToExcel";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<TongJiDaoChu, NullObject>>() {
		}.getType();
	}

}
