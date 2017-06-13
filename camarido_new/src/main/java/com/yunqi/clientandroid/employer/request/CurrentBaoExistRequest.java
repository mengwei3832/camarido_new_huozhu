package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.GetVTicketByIdInfo;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:class 获取首页的包是否存在
 * @ClassName: GetHomeNewRequest
 * @author: zhm
 * @date: 2016-5-13 下午2:58:30
 * 
 */
public class CurrentBaoExistRequest extends IRequest {

	public CurrentBaoExistRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/IsEixtsPackage";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
