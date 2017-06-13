package com.yunqi.clientandroid.http.request;

import java.lang.reflect.Type;

import android.R.bool;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.FilterManager;

/**
 * 网络请求基本类
 * 
 * @Description:
 * @ClassName: IRequest
 * @author: zhm
 * @date: 2015年11月19日 下午11:52:18
 * 
 */
public abstract class IRequest {

	protected Context mContext;
	protected RequestParams mParams = new RequestParams();
	private int mRequestId = 0;
	private String mChooseIdentity;
	private String urlRequest;
	private String userType;

	// 解决json字符串序列化问题，使用表单获取
	private boolean isUseFormRequest = false;

	public IRequest(Context context) {
		mContext = context;
		userType = CacheUtils.getString(context, "USER_TYPE", "");
	}

	/**
	 * 接口请求参数
	 * 
	 * @Title:getParams
	 * @return
	 * @return:RequestParams
	 * @throws
	 * @Create: 2015年11月19日 下午11:54:51
	 * @Author : zhm
	 */
	public RequestParams getParams() {
		return mParams;
	}

	/**
	 * http直接post数据
	 * 
	 * @Description:
	 * @Title:getPostData
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2015年11月20日 上午12:02:54
	 * @Author : zhm
	 */
	public String getPostData() {
		return null;
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * 设置接口请求唯一标识
	 * 
	 * @Description:
	 * @Title:setRequestId
	 * @param requestId
	 * @return:void
	 * @throws
	 * @Create: 2015年11月19日 下午11:56:32
	 * @Author : zhm
	 */
	public void setRequestId(int requestId) {
		mRequestId = requestId;
	}

	/**
	 * 返回请求接口唯一标识
	 * 
	 * @Description:
	 * @Title:getRequestId
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2015年11月19日 下午11:56:43
	 * @Author : zhm
	 */
	public int getRequestId() {
		return mRequestId;
	}

	/**
	 * 
	 * @Description:当前接口的url地址
	 * @Title:getUrl
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2015年11月19日 下午11:53:21
	 * @Author : zhm
	 */
	public abstract String getUrl();

	/**
	 * 获取解析类型
	 * 
	 * @Description:
	 * @Title:getParserType
	 * @return
	 * @return:Type
	 * @throws
	 * @Create: 2015年11月20日 上午12:02:38
	 * @Author : zhm
	 */
	public abstract Type getParserType();

	/**
	 * 
	 * @Description:返回服务器接口地址
	 * @Title:getHost
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2015年11月19日 下午11:54:28
	 * @Author : zhm
	 */
	protected String getHost() {

		if (userType != null && !TextUtils.isEmpty(userType)) {
			if (userType.equals("1")) {
				urlRequest = HostUtil.getApiHost();
			} else if (userType.equals("2")) {
				urlRequest = HostPkgUtil.getApiHost();
			}
		}

		// Log.e("TAG", "-------urlRequest---------"+urlRequest.toString());
		return urlRequest;
	}

	public boolean isUseFormRequest() {
		return isUseFormRequest;
	}

	public void setUseFormRequest(boolean isUseFormRequest) {
		this.isUseFormRequest = isUseFormRequest;
	}

}
