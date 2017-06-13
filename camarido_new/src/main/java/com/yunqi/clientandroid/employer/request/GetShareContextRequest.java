package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.ShareContext;
import com.yunqi.clientandroid.entity.Share;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:请求分享的数据
 * @ClassName: GetShareContextRequest
 * @author: mengwei
 * @date: 2016-6-28 下午5:34:30
 * 
 */
public class GetShareContextRequest extends IRequest {
	private String packageID;

	public GetShareContextRequest(Context context, String packageId) {
		super(context);
		this.packageID = packageId;
		mParams.put("packageId", packageId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Share/GetShareTicket?packageId=" + packageID;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<Share, NullObject>>() {
		}.getType();
	}

}
