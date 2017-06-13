package com.yunqi.clientandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 筛选sp
 */
public class FilterManager {

	private static final String SP_NAME = "filter";
	private static FilterManager mFilterManager;

	private SharedPreferences mShare;

	public SharedPreferences getShare() {
		return mShare;
	}

	private FilterManager(Context context) {
		mShare = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	public static synchronized FilterManager instance(Context context) {
		if (mFilterManager == null)
			mFilterManager = new FilterManager(context);
		return mFilterManager;
	}

	/**
	 * 保存起点省份
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setStartProvince(String startProvince) {
		mShare.edit().putString("startProvince", startProvince).commit();
	}

	/**
	 * 获取起点省份
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getStartProvince() {
		return mShare.getString("startProvince", "");
	}

	/**
	 * 保存起点城市
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setStartCity(String startCity) {
		mShare.edit().putString("startCity", startCity).commit();
	}

	/**
	 * 获取起点城市
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getStartCity() {
		return mShare.getString("startCity", "");
	}

	/**
	 * @Description:class 保存省份
	 * @Title:setProvince
	 * @param province
	 * @return:void
	 * @throws
	 * @Create: 2016-6-8 上午10:05:47
	 * @Author : mengwei
	 */
	public void setProvince(String province) {
		mShare.edit().putString("province", province).commit();
	}

	/**
	 * @Description:class 获取省份
	 * @Title:getProvince
	 * @return:String
	 * @throws
	 * @Create: 2016-6-8 上午10:07:24
	 * @Author : mengwei
	 */
	public String getProvince() {
		return mShare.getString("province", "");
	}

	/**
	 * @Description:class 保存市
	 * @Title:setCity
	 * @param city
	 * @return:void
	 * @throws
	 * @Create: 2016-6-8 上午10:10:13
	 * @Author : mengwei
	 */
	public void setCity(String city) {
		mShare.edit().putString("city", city).commit();
	}

	/**
	 * @Description:class 获取市
	 * @Title:getCity
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-8 上午10:10:21
	 * @Author : mengwei
	 */
	public String getCity() {
		return mShare.getString("city", "");
	}

	/**
	 * @Description:class 保存区或县
	 * @Title:setAreas
	 * @param areas
	 * @return:void
	 * @throws
	 * @Create: 2016-6-8 上午9:56:49
	 * @Author : mengwei
	 */
	public void setAreas(String areas) {
		mShare.edit().putString("areas", areas).commit();
	}

	/**
	 * @Description:class 获取区或县
	 * @Title:getAreas
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-8 上午9:59:45
	 * @Author : chengtao
	 */
	public String getAreas() {
		return mShare.getString("areas", "");
	}

	/**
	 * 
	 * @Description:添加详细地址
	 * @Title:setAddressDetail
	 * @param addr
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 上午11:25:26
	 * @Author : chengtao
	 */
	public void setAddressDetail(String addr) {
		mShare.edit().putString("addressDetail", addr).commit();
	}

	/**
	 * 
	 * @Description:获取常用地址
	 * @Title:getAddressDetail
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016年6月28日 上午11:26:55
	 * @Author : chengtao
	 */
	public String getAddressDetail() {
		return mShare.getString("addressDetail", "");
	}

	/**
	 * 
	 * @Description:添加公司别名
	 * @Title:setCompanyCustomName
	 * @param name
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 上午11:25:26
	 * @Author : chengtao
	 */
	public void setCompanyCustomName(String name) {
		mShare.edit().putString("companyCustomName", name).commit();
	}

	/**
	 * 
	 * @Description:获取公司别名
	 * @Title:getCompanyCustomName
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016年6月28日 上午11:26:55
	 * @Author : chengtao
	 */
	public String getCompanyCustomName() {
		return mShare.getString("companyCustomName", "");
	}

	/**
	 * 保存终点省份
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setEndProvince(String endProvince) {
		mShare.edit().putString("endProvince", endProvince).commit();
	}

	/**
	 * 获取终点省份
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getEndProvince() {
		return mShare.getString("endProvince", "");
	}

	/**
	 * 保存终点城市
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setEndCity(String endCity) {
		mShare.edit().putString("endCity", endCity).commit();
	}

	/**
	 * 获取终点城市
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getEndCity() {
		return mShare.getString("endCity", "");
	}

	/**
	 * 保存起始价格
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setStartPrice(long startPrice) {
		mShare.edit().putLong("startPrice", startPrice).commit();
	}

	/**
	 * 获取终点价格
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public long getStartPrice() {
		return mShare.getLong("startPrice", 0);
	}

	/**
	 * 保存最大价格
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setEndPrice(long endPrice) {
		mShare.edit().putLong("endPrice", endPrice).commit();
	}

	/**
	 * 获取最大价格
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public long getEndPrice() {
		return mShare.getLong("endPrice", 0);
	}

	/**
	 * 保存抢单类型
	 * 
	 * @param orderType
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setOrderType(int orderType) {
		mShare.edit().putInt("orderType", orderType).commit();
	}

	/**
	 * 获取抢单
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getOrderType() {
		return mShare.getInt("orderType", -1);
	}

	/**
	 * 保存货品类型
	 * 
	 * @param catoryType
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setCatoryType(int catoryType) {
		mShare.edit().putInt("catoryType", catoryType).commit();
	}

	/**
	 * 获取货品类型
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getCatoryType() {
		return mShare.getInt("catoryType", 0);
	}

	/**
	 * @Description:保存用户选择的身份
	 * @Title:setChooseIdentity
	 * @param mChooseIdentity
	 * @return:void
	 * @throws
	 * @Create: 2016-6-16 下午3:08:48
	 * @Author : mengwei
	 */
	public void setChooseIdentity(String mChooseIdentity) {
		mShare.edit().putString("chooseIdentity", mChooseIdentity).commit();
	}

	/**
	 * @Description:获取用户选择的身份
	 * @Title:getChooseIdentity
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-16 下午3:08:59
	 * @Author : mengwei
	 */
	public String getChooseIdentity() {
		return mShare.getString("chooseIdentity", "");
	}

	/**
	 * @Description:保存省的ID
	 * @Title:setProvinceId
	 * @param provinceId
	 * @return:void
	 * @throws
	 * @Create: 2016-6-18 下午3:36:48
	 * @Author : chengtao
	 */
	public void setProvinceId(int provinceId) {
		Log.e("TAG", "----------要保存的省ID--------------" + provinceId);
		mShare.edit().putInt("provinceId", provinceId).commit();
	}

	/**
	 * @Description:取出省的ID
	 * @Title:getProvinceId
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-18 下午3:36:55
	 * @Author : chengtao
	 */
	public int getProvinceId() {
		return mShare.getInt("provinceId", 0);
	}

	/**
	 * @Description:保存市的ID
	 * @Title:setCityId
	 * @param cityId
	 * @return:void
	 * @throws
	 * @Create: 2016-6-18 下午3:37:01
	 * @Author : chengtao
	 */
	public void setCityId(int cityId) {
		Log.e("TAG", "----------要保存的市ID--------------" + cityId);
		mShare.edit().putInt("cityId", cityId).commit();
	}

	/**
	 * @Description:取出市的ID
	 * @Title:getCityId
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-18 下午3:37:15
	 * @Author : chengtao
	 */
	public int getCityId() {
		return mShare.getInt("cityId", 0);
	}

	/**
	 * @Description:保存区或县ID
	 * @Title:setAreasId
	 * @param areasId
	 * @return:void
	 * @throws
	 * @Create: 2016-6-18 下午3:37:22
	 * @Author : chengtao
	 */
	public void setAreasId(int areasId) {
		Log.e("TAG", "----------要保存的区ID--------------" + areasId);
		mShare.edit().putInt("areasId", areasId).commit();
	}

	/**
	 * @Description:取出区或县ID
	 * @Title:getAreasId
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-18 下午3:37:30
	 * @Author : chengtao
	 */
	public int getAreasId() {
		return mShare.getInt("areasId", 0);
	}

	/**
	 * @Description:判断值是否赋空
	 * @Title:setEtEmpty
	 * @param empty
	 * @return:void
	 * @throws
	 * @Create: 2016-7-4 下午3:05:03
	 * @Author : mengwei
	 */
	public void setEtEmpty(boolean empty) {
		mShare.edit().putBoolean("empty", empty).commit();
	}

	/**
	 * @Description:判断值是否赋空
	 * @Title:getEtEmpty
	 * @return
	 * @return:boolean
	 * @throws
	 * @Create: 2016-7-4 下午3:05:44
	 * @Author : chengtao
	 */
	public boolean getEtEmpty() {
		return mShare.getBoolean("empty", true);
	}

	/**
	 * 装卸货地址标题判断赋值
	 * @param isChoose
	 */
	public void setZhuangXieAddress(boolean isChoose){
		mShare.edit().putBoolean("isChoose",isChoose).commit();
	}

	/**
	 * 获取装卸货地址标题判断
	 * @return
	 */
	public boolean getZhuangXieAddress(){
		return mShare.getBoolean("isChoose",true);
	}

	/**
	 * 清空所有条件
	 */
	public void clearSp() {
		Log.i("TAG", "------------------------");
		setStartProvince("");
		setProvince("");
		setProvinceId(0);
		setStartCity("");
		setCity("");
		setCityId(0);
		setAreas("");
		setAreasId(0);
		setAddressDetail("");
		setCompanyCustomName("");
		setEndProvince("");
		setEndCity("");
		setStartPrice(0);
		setEndPrice(0);
		setOrderType(-1);
		setCatoryType(0);
		setEtEmpty(false);
	}
}
