package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 提现接口
 * @date 15/12/18
 */
public class WithdrawRequest extends IRequest {

	public WithdrawRequest(Context context, String name, String cardNo,
			int cardType, String money, String shortMsg) {
		super(context);

		mParams.put("Name", name);
		mParams.put("CardNo", cardNo);
		mParams.put("CardType", cardType);
		mParams.put("Money", money);
		mParams.put("ShortMsg", shortMsg);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/Withdraw";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
