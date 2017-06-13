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
 * @Description class 获取提现短信接口
 * @date 15/12/18
 */
public class WithdrawMsgRequest extends IRequest {

	public WithdrawMsgRequest(Context context) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Finance/GetWithdrawMsg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
