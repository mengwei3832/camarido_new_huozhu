package com.yunqi.clientandroid.entity;

/**
 * 
 * @Description:货主轮播图信息
 * @ClassName: EmployerBannerInfo
 * @author: chengtao
 * @date: Jul 14, 2016 3:28:23 PM
 * 
 */
public class BannerInfo {
	public String ImageUrl;
	public String ArticleUrl;

	public BannerInfo(String imageUrl, String articleUrl) {
		super();
		ImageUrl = imageUrl;
		ArticleUrl = articleUrl;
	}
}
