package com.yunqi.clientandroid.employer.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 上传提货单接口
 * @date 16/1/19
 */
public class UploadCargoRolRequest extends IRequest {

	public UploadCargoRolRequest(Context context, String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		super(context);

		mParams.put("TicketId", ticketId);
		mParams.put("TicketRelationCode", ticketRelationCode);
		mParams.put("TicketWeight", ticketWeight);
		mParams.put("TicketOperationPicUrl", ticketOperationPicUrl);
		mParams.put("TicketOperationPicName", ticketOperationPicName);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/SignBill";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
