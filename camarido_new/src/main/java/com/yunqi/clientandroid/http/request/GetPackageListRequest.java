package com.yunqi.clientandroid.http.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.entity.FocusonRoute;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 获取所有路线点进去的包列表
 * @date 15/11/27
 */
public class GetPackageListRequest extends IRequest {

	public GetPackageListRequest(Context context, int PageIndex, int PageSize,
			String StartCity, String EndCity) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", PageSize);
			Pagenation.put("PageIndex", PageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("PackageBeginCity", StartCity);
			mParams.put("PackageEndCity", EndCity);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 首页和订单fragment请求参数
	 * 
	 * @param context
	 * @param pageIndex
	 * @param pageSize
	 */
	public GetPackageListRequest(Context context, int pageIndex, int pageSize,
			int orderByType, int packageBeginProvince, int packageBeginCity,
			int packageEndProvince, int packageEndCity, int packageType,
			int categoryId, long beginPrice, long endPrice) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("OrderByType", orderByType);
			if (packageBeginProvince != 0) {
				mParams.put("PackageBeginProvince", packageBeginProvince);
			}
			if (packageBeginCity != 0) {
				mParams.put("PackageBeginCity", packageBeginCity);
			}
			if (packageEndProvince != 0) {
				mParams.put("PackageEndProvince", packageEndProvince);
			}
			if (packageEndCity != 0) {
				mParams.put("PackageEndCity", packageEndCity);
			}

			if (packageType != -1) {
				mParams.put("PackageType", packageType);
			}
			if (categoryId != 0) {
				mParams.put("CategoryId", categoryId);
			}
			if (beginPrice != 0) {
				mParams.put("BeginPrice", beginPrice);
			}

			if (endPrice != 0) {
				mParams.put("EndPrice", endPrice);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 排序接口
	 * 
	 * @param context
	 * @param pageIndex
	 * @param pageSize
	 */
	public GetPackageListRequest(Context context, int pageIndex, int pageSize,
			int orderByType) {
		super(context);

		// 传参
		JSONObject Pagenation = new JSONObject();
		try {
			Pagenation.put("PageSize", pageSize);
			Pagenation.put("PageIndex", pageIndex);
			mParams.put("Pagenation", Pagenation);
			mParams.put("OrderByType", orderByType);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getUrl() {
		return getHost() + "api/Package/GetPackageList";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, FocusonRoute>>() {
		}.getType();
	}
}
