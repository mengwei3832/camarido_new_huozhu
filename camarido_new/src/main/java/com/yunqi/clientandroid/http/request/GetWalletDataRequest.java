package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.MineWalletData;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class GetWalletDataRequest extends IRequest {

	public GetWalletDataRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/GetWalletData";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<MineWalletData, NullObject>>() {
		}.getType();
	}

}
