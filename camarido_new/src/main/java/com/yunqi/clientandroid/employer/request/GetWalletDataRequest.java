package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetWalletData;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:查看钱包数据接口
 * @ClassName: GetWalletDataRequest
 * @author: chengtao
 * @date: 2016年6月16日 下午10:56:10
 * 
 */
public class GetWalletDataRequest extends IRequest {

	public GetWalletDataRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Finance/GetWalletData";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetWalletData, NullObject>>() {
		}.getType();
	}

}
