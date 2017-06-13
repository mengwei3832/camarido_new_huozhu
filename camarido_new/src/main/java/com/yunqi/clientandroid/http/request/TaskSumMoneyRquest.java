package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.NewCarSum;
import com.yunqi.clientandroid.entity.TaskSumMoney;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

import android.content.Context;

/**
 * 
 * @Description:class 新车奖励的累计金额请求
 * @ClassName: TaskSumMoney
 * @author: zhm
 * @date: 2016-4-7 下午2:39:29
 * 
 */
public class TaskSumMoneyRquest extends IRequest {

	public TaskSumMoneyRquest(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return getHost()
				+ "api/RewardRecord/GetNewVehicleRewardRecordByUserSum";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NewCarSum, NullObject>>() {
		}.getType();
	}

}
