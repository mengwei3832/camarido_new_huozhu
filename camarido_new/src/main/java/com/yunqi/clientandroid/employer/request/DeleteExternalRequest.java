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

public class DeleteExternalRequest extends IRequest {
    private String phId;

    public DeleteExternalRequest(Context context,String phId) {
        super(context);
        this.phId = phId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/DeleteCphoneUrl?phId="+phId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<NullObject, NullObject>>() {
        }.getType();
    }
}
