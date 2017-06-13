package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CompanyRegisteredInfo;
import com.yunqi.clientandroid.entity.REcommendSum;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 推荐奖励总价列表的请求
 * @ClassName: RecommendSumRequest
 * @author: zhm
 * @date: 2016-4-8 上午8:54:49
 * 
 */
public class RecommendSumRequest extends IRequest {

	public RecommendSumRequest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost() + "api/RewardRecord/GetRefereeRewardRecordSum";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, REcommendSum>>() {
		}.getType();
	}

}
