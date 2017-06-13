package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.HangYeZiXun;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;
import com.yunqi.clientandroid.entity.Message;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 搜索接口
 * @date 15/11/21
 */
public class DiscoverSearchRequest extends IRequest {

	public DiscoverSearchRequest(Context context, int pageIndex, int pageSize,
			String keyWord) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageIndex", pageIndex);
			Pagenation.put("PageSize", pageSize);
			mParams.put("Pagenation", Pagenation);
			mParams.put("KeyWord", keyWord);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/Message/SearchMessages";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, HangYeZiXun>>() {
		}.getType();
	}
}
