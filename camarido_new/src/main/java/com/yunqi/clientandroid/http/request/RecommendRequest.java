package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.CompanyRegisteredInfo;
import com.yunqi.clientandroid.entity.RecommendAward;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 推荐奖励的请求
 * @ClassName: RecommendRequest
 * @author: zhm
 * @date: 2016-4-7 下午4:03:08
 * 
 */
public class RecommendRequest extends IRequest {

	public RecommendRequest(Context context, int pageSize, int pageIndex) {
		super(context);
		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUrl() {
		return getHost() + "api/RewardRecord/GetRefereeRewardRecordByUser";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, RecommendAward>>() {
		}.getType();
	}

}
