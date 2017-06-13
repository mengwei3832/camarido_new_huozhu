package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:报价单下单的请求
 * @ClassName: SetYiJiaPrice
 * @author: mengwei
 * @date: 2016-6-29 下午7:58:59
 * 
 */
public class XiaDanOrderRequest extends IRequest {
	private int infoId;
	private String price;

	public XiaDanOrderRequest(Context context, int infoId, String price) {
		super(context);
		this.infoId = infoId;
		this.price = price;
		mParams.put("InfoId", infoId);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/AppOrderInfo?InfoId=" + infoId
				+ "&price=" + price;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
