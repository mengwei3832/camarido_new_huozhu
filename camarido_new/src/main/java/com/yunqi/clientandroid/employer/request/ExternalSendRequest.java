package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * Created by mengwei on 2016/12/27.
 */

public class ExternalSendRequest extends IRequest {
    private String mPhone;
    private String packageId;

    public ExternalSendRequest(Context context,String userPhone,String packageId) {
        super(context);
        this.mPhone = userPhone;
        this.packageId = packageId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/SendShareMsg?phone="+mPhone+"&packageId="+packageId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<NullObject, NullObject>>() {
        }.getType();
    }
}
