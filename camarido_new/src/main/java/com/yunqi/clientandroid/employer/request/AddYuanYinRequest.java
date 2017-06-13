package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:报价单取消的请求
 * @ClassName: SetYiJiaPrice
 * @author: mengwei
 * @date: 2016-6-29 下午7:58:59
 * 
 */
public class AddYuanYinRequest extends IRequest {
	private int infoId;
	private String memo;

	public AddYuanYinRequest(Context context, int infoId, String memo) {
		super(context);
		this.infoId = infoId;
		this.memo = memo;
		mParams.put("InfoId", infoId);
		mParams.put("Memo", memo);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/TerminationOrderInfo?InfoId="
				+ infoId + "&Memo=" + memo;
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
