package com.yunqi.clientandroid.employer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 发包方状态选择SP
 */
public class SpManager {

	private static final String SP_NAME = "statuschoice";
	private static SpManager mSpManager;

	private SharedPreferences mShare;

	public SharedPreferences getShare() {
		return mShare;
	}

	private SpManager(Context context) {
		mShare = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	public static synchronized SpManager instance(Context context) {
		if (mSpManager == null)
			synchronized (SpManager.class) {
				if (null == mSpManager) {
					mSpManager = new SpManager(context);
				}
			}
		return mSpManager;
	}

	/**
	 * 保存状态选择类型
	 * 
	 * @param statusType
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setStatusType(int statusType) {
		mShare.edit().putInt("statusType", statusType).commit();
	}

	/**
	 * 获取状态选择类型
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getStatusType() {
		return mShare.getInt("statusType", -1);
	}

	/**
	 * @Description:class 保存包的ID
	 * @Title:setPackageId
	 * @param packageId
	 * @return:void
	 * @throws
	 * @Create: 2016-6-12 下午4:25:18
	 * @Author : mengwei
	 */
	public void setPackageId(String packageId) {
		mShare.edit().putString("packageId", packageId).commit();
	}

	/**
	 * @Description:class 获取包的ID
	 * @Title:getPackageId
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016-6-12 下午4:25:23
	 * @Author : mengwei
	 */
	public String getPackageId() {
		return mShare.getString("packageId", "");
	}

	/**
	 * 保存省份集合
	 * @param province 省份集合转的字符串
	 */
	public void setProvince(String province){
		mShare.edit().putString("province",province).commit();
	}

	/**
	 * 获取省份集合
	 * @return 省份集合转的字符串
	 */
	public String getProvince(){
		return mShare.getString("province","");
	}

	/**
	 * 保存市集合
	 * @param provinceId 选中的省份Id
	 * @param city 市集合转的字符串
	 */
	public void setCity(String provinceId, String city){
		mShare.edit().putString(provinceId,city).commit();
	}

	/**
	 * 获取市集合
	 * @param provinceId 选中的省份Id
	 * @return 市集合转的字符串
	 */
	public String getCity(String provinceId){
		return mShare.getString(provinceId,"");
	}
	/**
	 * 保存县集合
	 * @param cityId 选中的省份Id
	 * @param country 市集合转的字符串
	 */
	public void setCountry(String cityId, String country){
		mShare.edit().putString(cityId,country).commit();
	}

	/**
	 * 获取县集合
	 * @param cityId 选中的省份Id
	 * @return 市集合转的字符串
	 */
	public String getCountry(String cityId){
		return mShare.getString(cityId,"");
	}

	/**
	 * 保存区集合
	 * @param cityId 选中的市Id
	 * @param district 区集合转的字符串
	 */
	public void setDistrict(String cityId, String district){
		mShare.edit().putString(cityId, district).commit();
	}

	/**
	 * 获取区集合
	 * @param cityId 选中的市Id
	 * @return 区集合转的字符串
	 */
	public String getDistrict(String cityId){
		return mShare.getString(cityId,"");
	}



	/**
	 * 清空所有条件
	 */
	public void clearUp() {
		setStatusType(-1);
		setPackageId("");
	}

}
