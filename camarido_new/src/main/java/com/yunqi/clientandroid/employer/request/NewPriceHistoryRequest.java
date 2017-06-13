package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.SetNewPrice;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 请求设定最新价格历史记录
 * Created by mengwei on 2017/1/17.
 */

public class NewPriceHistoryRequest extends IRequest {
    private String packageId;

    public NewPriceHistoryRequest(Context context,String packageId) {
        super(context);
        this.packageId = packageId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/GetListByPackage?packageId="+packageId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<NullObject, SetNewPrice>>() {
        }.getType();
    }
}
