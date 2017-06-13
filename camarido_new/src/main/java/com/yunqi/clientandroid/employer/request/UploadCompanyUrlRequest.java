package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 上传企业头像的请求
 * @ClassName: UploadCompanyUrlRequest
 * @author: chengtao
 * @date: 2016-6-6 下午5:10:35
 * 
 */
public class UploadCompanyUrlRequest extends IRequest {

	public UploadCompanyUrlRequest(Context context, String name, String url) {
		super(context);
		mParams.put("ImgName", name);
		mParams.put("ImgBase64", url);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/User/SetTenantHeadPortrait";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
