package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.R.integer;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.OrderAuditInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:提交指派车辆的请求
 * @ClassName: TijiaoZhipaiCarRequest
 * @author: zhm
 * @date: 2016-5-19 下午1:54:38
 * 
 */
public class TijiaoZhipaiCarRequest extends IRequest {

	public TijiaoZhipaiCarRequest(Context context, int tenantId, int packageId,
			String[] sVehicleIDArray, String[] sVehicleNameIDArray,
			String[] vehicleNoList) {
		super(context);
		mParams.put("tenantId", tenantId);
		mParams.put("packageId", packageId);
		mParams.put("vehicleIdList", sVehicleIDArray);
		mParams.put("vowenIdList", sVehicleNameIDArray);
		mParams.put("vehicleNoList", vehicleNoList);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/ AppointVehicle";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
