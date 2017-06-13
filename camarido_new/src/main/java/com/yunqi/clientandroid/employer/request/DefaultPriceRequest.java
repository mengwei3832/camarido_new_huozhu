package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.DefaultPriceEntity;
import com.yunqi.clientandroid.employer.entity.DemandModels;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 预设价格请求类
 * Created by mengwei on 2017/5/5.
 */

public class DefaultPriceRequest extends IRequest {
    private String packageId;

    public DefaultPriceRequest(Context context,String packageId) {
        super(context);
        this.packageId = packageId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/GetAdvancePrice?packageId="+packageId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<DefaultPriceEntity, NullObject>>() {
        }.getType();
    }
}
