package com.yunqi.clientandroid.utils;

import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.employer.entity.HomeFragmentZixun;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreManager {

	private static PreManager preManager;

	private SharedPreferences mShare;

	public SharedPreferences getShare() {
		return mShare;
	}

	private PreManager(Context context) {
		mShare = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static synchronized PreManager instance(Context context) {
		if (preManager == null)
			preManager = new PreManager(context);
		return preManager;
	}

	/**
	 * @param firstStart
	 * @throws
	 * @Title:setFirstStart
	 * @Description:设置程序是否第一次启动
	 * @return:void
	 * @Create: 2014-3-14 上午10:44:27
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public void setFirstStart(boolean firstStart) {
		mShare.edit().putBoolean("first_start_key", firstStart).commit();
	}

	/**
	 * @return
	 * @throws
	 * @Title:getFirstStart
	 * @Description:获取程序是否第一次启动
	 * @return:boolean
	 * @Create: 2014-3-14 上午10:44:33
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public boolean getFirstStart() {
		return mShare.getBoolean("first_start_key", true);
	}

	/**
	 * 保存登录token
	 * 
	 * @param token
	 * @throws
	 * @Description:
	 * @Title:setToken
	 * @return:void
	 * @Create: 2015年11月22日 下午3:35:12
	 * @Author : zhm
	 */
	public void setToken(String token) {
		mShare.edit().putString("tokeb_key", token).commit();
	}

	/**
	 * 获取保存到本地的token
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getToken
	 * @return:String
	 * @Create: 2015年11月22日 下午3:35:29
	 * @Author : zhm
	 */
	public String getToken() {
		return mShare.getString("tokeb_key", null);
	}

	/**
	 * 保存token过期时间
	 * 
	 * @param token
	 * @throws
	 * @Description:
	 * @Title:setTokenExpires
	 * @return:void
	 * @Create: 2015年11月22日 下午3:36:00
	 * @Author : zhm
	 */
	public void setTokenExpires(long token) {
		mShare.edit().putLong("tokeb_expires_key", token).commit();
	}

	/**
	 * 删除token过期时间
	 * 
	 * @throws
	 * @Description:
	 * @Title:setTokenExpires
	 * @return:void
	 * @Create: 2015年11月22日 下午3:36:00
	 * @Author : zhm
	 */
	public void removeTokenExpires() {
		mShare.edit().remove("tokeb_expires_key").commit();
	}

	/**
	 * token过期时间
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getTokenExpires
	 * @return:long
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhm
	 */
	public long getTokenExpires() {
		return mShare.getLong("tokeb_expires_key", 0);
	}

	/**
	 * 是否登录状态
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getTokenExpires
	 * @return:boolean
	 * @Create: 2015年12月05日
	 * @Author : zhangwb
	 */
	public boolean isLogin() {
		long currentTime = System.currentTimeMillis() / 1000;
		return currentTime < mShare.getLong("tokeb_expires_key", 0);
	}

	/**
	 * 保存排序标识
	 * 
	 * @param sortType
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setOrderSortType(int sortType) {
		mShare.edit().putInt("order_sort_type", sortType).commit();
	}

	/**
	 * 获取排序标识
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhm
	 */
	public int getOrderSortType() {
		return mShare.getInt("order_sort_type", 1);
	}

	/**
	 * 保存纬度
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setLatitude(String latitude) {
		mShare.edit().putString("latitude", latitude).commit();
	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getLatitude() {
		return mShare.getString("latitude", "");
	}

	/**
	 * 保存经度
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setLongitude(String longitude) {
		mShare.edit().putString("longitude", longitude).commit();
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getLongitude() {
		return mShare.getString("longitude", "");
	}

	/**
	 * 保存uid
	 * 
	 * @param userId
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setUserId(String userId) {
		mShare.edit().putString("uid", userId).commit();
	}

	/**
	 * 获取uid
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getUserId() {
		return mShare.getString("uid", "0");
	}

	/**
	 * 保存消息气泡数
	 * 
	 * @param msgBubble
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setMsgBubble(int msgBubble) {
		mShare.edit().putInt("msgBubble", msgBubble).commit();
	}

	/**
	 * 获取消息气泡数
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getMsgBubble() {
		return mShare.getInt("msgBubble", 0);
	}

	/**
	 * 保存关注气泡数
	 * 
	 * @param attentionBubble
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setAttentionBubble(int attentionBubble) {
		mShare.edit().putInt("attentionBubble", attentionBubble).commit();
	}

	/**
	 * 获取关注气泡数
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getAttentionBubble() {
		return mShare.getInt("attentionBubble", 0);
	}

	/**
	 * 保存活动气泡数
	 * 
	 * @param activeBubble
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setActiveBubble(int activeBubble) {
		mShare.edit().putInt("activeBubble", activeBubble).commit();
	}

	/**
	 * 获取活动气泡数
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public int getActiveBubble() {
		return mShare.getInt("activeBubble", 0);
	}

	/**
	 * 保存头像
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setAvatar(String avatar) {
		mShare.edit().putString("avatar", avatar).commit();
	}

	/**
	 * 获取头像
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String getAvatar() {
		return mShare.getString("avatar", "");
	}

	/**
	 * @Description:class 保存企业名字简称
	 * @Title:setCompanyName
	 * @param companyName
	 * @return:void
	 * @throws
	 * @Create: 2016-6-6 下午4:17:27
	 * @Author : mengwei
	 */
	public void setCompanyName(String companyName) {
		L.e("----jinlai----companyName---------"+companyName);
		mShare.edit().putString("companyName", companyName).commit();
	}

	/**
	 * @Description:class 获取企业名字简称
	 * @Title:getCompanyName
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016-6-6 下午4:17:39
	 * @Author : mengwei
	 */
	public String getCompanyName() {
		return mShare.getString("companyName", "");
	}

	/**
	 * 保存Banners
	 * 
	 * @throws
	 * @Description:
	 * @return:void
	 * @Create: 2015年11月22日
	 * @Author : zhangwb
	 */
	public void setBanner(String banners) {
		mShare.edit().putString("banners", banners).commit();
	}

	/**
	 * 获取Banners
	 * 
	 * @return
	 * @throws
	 * @Description:
	 * @return:int
	 * @Create: 2015年11月22日 下午3:36:08
	 * @Author : zhangwb
	 */
	public String[] getBanner() {
		return mShare.getString("banners", "").split(",");
	}

	/**
	 * 
	 * @Description:保存banner对应文章地址
	 * @Title:setBannerArticalUrls
	 * @param url
	 * @return:void
	 * @throws
	 * @Create: Jul 14, 2016 2:25:46 PM
	 * @Author : chengtao
	 */
	public void setBannerArticalUrls(String url) {
		mShare.edit().putString("bannerArticalUrls", url).commit();
	}

	/**
	 * 
	 * @Description:获取banner对应文章地址
	 * @Title:getBannerArticalUrls
	 * @return
	 * @return:String
	 * @throws
	 * @Create: Jul 14, 2016 2:26:32 PM
	 * @Author : chengtao
	 */
	public String[] getBannerArticalUrls() {
		return mShare.getString("bannerArticalUrls", "").split(",");
	}

	/**
	 * 
	 * @Description:保存发包方首页banner信息
	 * @Title:setBannerInfo
	 * @param imageUrl
	 * @param articalUrl
	 * @return:void
	 * @throws
	 * @Create: Jul 27, 2016 4:03:46 PM
	 * @Author : chengtao
	 */
	public void setEmployerBannerInfo(String imageUrl, String articalUrl) {
		if (StringUtils.isStrNotNull(imageUrl)
				&& StringUtils.isStrNotNull(articalUrl)) {
			setBanner(imageUrl);
			setBannerArticalUrls(articalUrl);
			setBannerCache(true);
			Log.e("TAG", "setBannerCache(true)");
		} else {
			setBannerCache(false);
			Log.e("TAG", "setBannerCache(false)");
		}
	}

	/**
	 * 
	 * @Description:设置货主首页banner信息
	 * @Title:setCarrBannerInfo
	 * @param imageUrl
	 * @return:void
	 * @throws
	 * @Create: Jul 27, 2016 4:09:06 PM
	 * @Author : chengtao
	 */
	public void setCarrBannerInfo(String imageUrl) {
		if (StringUtils.isStrNotNull(imageUrl)) {
			setBanner(imageUrl);
			setBannerCache(true);
			Log.e("TAG", "setBannerCache(true)");
		} else {
			setBannerCache(false);
			Log.e("TAG", "setBannerCache(false)");
		}
	}

	/**
	 * 
	 * @Description:设置首页轮播缓存
	 * @Title:setBannerCache
	 * @return:void
	 * @throws
	 * @Create: Jul 27, 2016 3:03:06 PM
	 * @Author : chengtao
	 */
	public void setBannerCache(boolean boo) {
		if (boo) {
			Log.e("TAG", "bannerCache" + "-----" + 1);
			mShare.edit().putInt("bannerCache", 1).commit();
		} else {
			Log.e("TAG", "bannerCache" + "-----" + 0);
			mShare.edit().putInt("bannerCache", 0).commit();
		}
	}

	/**
	 * 
	 * @Description:发包方首页轮播缓存是否存在
	 * @Title:isBannerCacheExist
	 * @return
	 * @return:boolean
	 * @throws
	 * @Create: Jul 27, 2016 3:03:57 PM
	 * @Author : chengtao
	 */
	public boolean isBannerCacheExist() {
		Log.e("TAG",
				"isBannerCacheExist-----" + mShare.getInt("bannerCache", 0)
						+ "");
		switch (mShare.getInt("bannerCache", 0)) {
		case 1:
			return true;
		case 0:
		default:
			return false;
		}
	}

	// ------------发包方首页资讯-----------------//
	/**
	 * 
	 * @Description:保存首页资讯
	 * @Title:setEmployerZiXun
	 * @param ziXunLists
	 * @return:void
	 * @throws
	 * @Create: Jul 27, 2016 3:34:17 PM
	 * @Author : chengtao
	 */
	public void setEmployerZiXun(List<HomeFragmentZixun> ziXunLists) {
		if (ziXunLists != null) {
			StringBuilder ziXunId = new StringBuilder();// 活动信息ID
			StringBuilder ziXunTitle = new StringBuilder();// 活动信息标题
			int ziXunSize = ziXunLists.size();

			for (int i = 0; i < ziXunSize; i++) {
				ziXunId.append(i == (ziXunSize - 1) ? ziXunLists.get(i).Id + ""
						: ziXunLists.get(i).Id + ",");
				ziXunTitle
						.append(i == (ziXunSize - 1) ? ziXunLists.get(i).MessageTitle
								: ziXunLists.get(i).MessageTitle + ",");
			}

			mShare.edit().putString("ziXunId", ziXunId.toString()).commit();
			mShare.edit().putString("ziXunTitle", ziXunTitle.toString())
					.commit();
			setEmployerZiXunCache(true);
			Log.e("TAG", "setEmployerZiXunCache(true)");
		} else {
			setEmployerZiXunCache(false);
			Log.e("TAG", "setEmployerZiXunCache(false)");
		}
	}

	/**
	 * 
	 * @Description:获取首页资讯缓存
	 * @Title:getEmployerZiXun
	 * @return
	 * @return:List<HomeFragmentZixun>
	 * @throws
	 * @Create: Jul 27, 2016 3:35:10 PM
	 * @Author : chengtao
	 */
	public List<HomeFragmentZixun> getEmployerZiXun() {
		List<HomeFragmentZixun> ziXunLists = null;
		String ziXunId[] = mShare.getString("ziXunId", "").split(",");
		String ziXunTitle[] = mShare.getString("ziXunTitle", "").split(",");
		if (ziXunId.length != 0 && ziXunTitle.length != 0
				&& ziXunId.length == ziXunTitle.length
				&& !ziXunId[0].equals("") && !ziXunTitle[0].equals("")) {
			ziXunLists = new ArrayList<HomeFragmentZixun>();
			for (int i = 0; i < ziXunTitle.length; i++) {
				HomeFragmentZixun zixun = new HomeFragmentZixun();
				zixun.Id = Integer.parseInt(ziXunId[i]);
				zixun.MessageTitle = ziXunTitle[i];
				ziXunLists.add(zixun);
			}
		}
		return ziXunLists;
	}

	/**
	 * 
	 * @Description:设置发包方首页资讯缓存
	 * @Title:setEmployerZiXunCache
	 * @return:void
	 * @throws
	 * @Create: Jul 27, 2016 3:37:47 PM
	 * @Author : chengtao
	 */
	public void setEmployerZiXunCache(boolean boo) {
		if (boo) {
			Log.e("TAG", "ziXunCache" + "-----" + 1);
			mShare.edit().putInt("ziXunCache", 1).commit();
		} else {
			Log.e("TAG", "ziXunCache" + "-----" + 0);
			mShare.edit().putInt("ziXunCache", 0).commit();
		}

	}

	/**
	 * 
	 * @Description:发包方首页资讯是否存在
	 * @Title:isEmployerZiXunCacheExist
	 * @return
	 * @return:boolean
	 * @throws
	 * @Create: Jul 27, 2016 3:39:23 PM
	 * @Author : chengtao
	 */
	public boolean isEmployerZiXunCacheExist() {
		Log.e("TAG",
				"isEmployerZiXunCacheExist----"
						+ mShare.getInt("ziXunCache", 0) + "");
		switch (mShare.getInt("ziXunCache", 0)) {
		case 1:
			return true;
		case 0:
		default:
			return false;
		}
	}

	/**
	 * 保存资讯请求的MD5值
	 * @param md5
	 */
	public void setMessageRed(String md5){
		mShare.edit().putString("md5",md5).commit();
	}

	/**
	 * 获取资讯请求的MD5值
	 * @return
	 */
	public String getMessageRed(){
		return mShare.getString("md5","");
	}

	/**
	 * 发现页面资讯小红点显示赋值
	 * @param isShow
	 */
	public void setShowRed(boolean isShow){
		mShare.edit().putBoolean("isShow",isShow).commit();
	}

	/**
	 * 获取发现页面资讯小红点是否显示
	 * @return
	 */
	public boolean getShowRed(){
		return mShare.getBoolean("isShow",true);
	}

	/**
	 * 保存首页最新资讯的内容
	 * @param SXProvinceName
	 * @param NMGProvinceName
	 * @param SHXProvinceName
	 * @param SXHisCount
	 * @param NMGHisCount
	 * @param SHXHisCount
	 * @param HighwayIndexUrl
	 */
	public void setHomeCrash(String SXProvinceName,String NMGProvinceName,String SHXProvinceName,
							 String SXHisCount,String NMGHisCount,String SHXHisCount,String HighwayIndexUrl){
		mShare.edit().putString("SXProvinceName",SXProvinceName).commit();
		mShare.edit().putString("NMGProvinceName",NMGProvinceName).commit();
		mShare.edit().putString("SHXProvinceName",SHXProvinceName).commit();
		mShare.edit().putString("SXHisCount",SXHisCount).commit();
		mShare.edit().putString("NMGHisCount",NMGHisCount).commit();
		mShare.edit().putString("SHXHisCount",SHXHisCount).commit();
		mShare.edit().putString("HighwayIndexUrl",HighwayIndexUrl).commit();
	}

	/**
	 * 获取首页最新资讯保存的数组
	 * @return
	 */
	public String[] getHomeCrash(){
		String[] strs = null;
		String mSXProvinceName = mShare.getString("SXProvinceName","");
		String mNMGProvinceName = mShare.getString("NMGProvinceName","");
		String mSHXProvinceName = mShare.getString("SHXProvinceName","");
		String mSXHisCount = mShare.getString("SXHisCount","");
		String mNMGHisCount = mShare.getString("NMGHisCount","");
		String mSHXHisCount = mShare.getString("SHXHisCount","");

		/** 判断是否为空 */
		if (!StringUtils.isStrNotNull(mSXProvinceName)){
			mSXProvinceName = "山西";
			mSXHisCount = "0";
		}
		if (!StringUtils.isStrNotNull(mNMGProvinceName)){
			mNMGProvinceName = "内蒙古";
			mNMGHisCount = "0";
		}
		if (!StringUtils.isStrNotNull(mSHXProvinceName)){
			mSHXProvinceName = "陕西";
			mSHXHisCount = "0";
		}

		strs = new String[]{
				mSXProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mSXHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条",
				mNMGProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mNMGHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条",
				mSHXProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mSHXHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条"
		};
		return strs;
	}

	// ------------发包方首页资讯-----------------//
	public void clearHomeCache() {
		setBanner("");
		setBannerArticalUrls("");
		setEmployerZiXun(null);
		setBannerCache(false);
		setEmployerZiXunCache(false);
		setAvatar("");
		setCompanyName("");
	}
}
