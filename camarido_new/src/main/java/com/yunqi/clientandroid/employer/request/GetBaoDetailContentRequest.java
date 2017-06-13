package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.CarListZhipai;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.StopPackageEntity;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:获取包详细信息的请求
 * @ClassName: GetBaoDetailContentRequest
 * @author: zhm
 * @date: 2016-5-24 上午11:24:34
 * 
 */
public class GetBaoDetailContentRequest extends IRequest {
	private String packageID;

	public GetBaoDetailContentRequest(Context context, String packageID) {
		super(context);
		this.packageID = packageID;
		mParams.put("packageID", packageID);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/GetPackageById?packageID="
				+ packageID;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<GetBiaoLieBiao, NullObject>>() {
		}.getType();
	}

}
