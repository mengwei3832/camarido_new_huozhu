package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:获取所有货品种类
 * @ClassName: GetPakagePickersRequest
 * @author: chengtao
 * @date: 2016年6月17日 下午6:47:05
 * 
 */
public class GetPakagePickersRequest extends IRequest {

	public GetPakagePickersRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetPakagePickers";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, PackagePickersInfo>>() {
		}.getType();
	}

}
