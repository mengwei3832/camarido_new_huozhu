package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.Wallet;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的钱包请求
 * @date 15/11/21
 */
public class GetWalletRequest extends IRequest {

	public GetWalletRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/GetWalletData";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Wallet, NullObject>>() {
		}.getType();
	}
}
