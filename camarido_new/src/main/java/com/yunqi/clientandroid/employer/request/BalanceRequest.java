package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 余额结算明细请求
 * Created by mengwei on 2017/4/7.
 */

public class BalanceRequest extends IRequest {

    public BalanceRequest(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/SendShareMsg?phone=";
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<NullObject, NullObject>>() {
        }.getType();
    }
}
