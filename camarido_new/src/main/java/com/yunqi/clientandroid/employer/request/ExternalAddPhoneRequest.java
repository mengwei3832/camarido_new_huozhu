package com.yunqi.clientandroid.employer.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.ExternalEntity;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import java.lang.reflect.Type;

/**
 * 添加电话的请求接口
 * Created by mengwei on 2016/12/27.
 */

public class ExternalAddPhoneRequest extends IRequest {
    private String userPhone;
    private String relationId;

    public ExternalAddPhoneRequest(Context context,String phone,String packageId) {
        super(context);
        this.userPhone = phone;
        this.relationId = packageId;
    }

    @Override
    public String getUrl() {
        return getHost() + "pkgapi/Package/AddCphoneUrl?userPhone="+userPhone+"&relationId="+relationId;
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ExternalEntity, NullObject>>() {
        }.getType();
    }
}
