package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ExternalEntity;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 监护人员列表
 * Created by mengwei on 2017/1/5.
 */

public class ExternalListRequest extends IRequest {
    private String packageId;

    public ExternalListRequest(Context context,String packageId) {
        super(context);
        this.packageId = packageId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/GetCphoneUrl?packageId="+packageId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<NullObject, ExternalEntity>>() {
        }.getType();
    }
}
