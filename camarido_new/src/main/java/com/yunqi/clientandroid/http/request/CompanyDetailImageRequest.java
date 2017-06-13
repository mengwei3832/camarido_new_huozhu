package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CompanyDetail;
import com.yunqi.clientandroid.entity.CompanyDetailImage;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 公司详情图片请求
 * @ClassName: CompanyDetailImageRequest
 * @author: zhm
 * @date: 2016-4-1 上午10:22:32
 * 
 */
public class CompanyDetailImageRequest extends IRequest {
	private int packageId;

	public CompanyDetailImageRequest(Context context, int packageId) {
		super(context);
		this.packageId = packageId;
		mParams.put("id", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetPictureByPackageId/" + packageId;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<CompanyDetailImage, NullObject>>() {
		}.getType();
	}

}
