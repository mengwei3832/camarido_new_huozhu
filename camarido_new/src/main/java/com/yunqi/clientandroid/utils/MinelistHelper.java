package com.yunqi.clientandroid.utils;

import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.MineListItem;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 个人中心列表数据配置类
 * @date 15/11/24
 */
public class MinelistHelper {
	/**
	 * item类型
	 */
	public static final int TYPE_BACKGROUND = 0;
	public static final int TYPE_MY_WALLET = 1;
	public static final int TYPE_MY_VEHICLE = 2;
	public static final int TYPE_MY_PROMOTION = 3;
	public static final int TYPE_HELP = 4;
	public static final int TYPE_LOGOUT = 5;
	public static final int TYPE_ATTENTION = 6;
	public static final int TYPE_ISREAL = 7;
	public static final int TYPE_ISDRIVER = 8;
	public static final int TYPE_TASK = 9;
	public static final int TYPE_RECOMMENDED = 10;

	/**
	 * item名字
	 */
	public static final String NAME_MY_WALLET = "我的钱包";
	public static final String NAME_MY_VEHICLE = "我的车辆";
	public static final String NAME_MY_PROMOTION = "我的推广";
	public static final String NAME_HELP = "帮助";
	public static final String NAME_LOGOUT = "退出";
	public static final String NAME_ATTENTION = "我的关注";
	public static final String NAME_ISREAL = "实名认证";
	public static final String NAME_TASK = "任务奖励";
	public static final String NAME_RECOMMENDED = "推荐奖励";
	public static final String NAME_ISDRIVER = "司机认证";

	/**
	 * 要用的 list
	 */
	private static List<MineListItem> mMineList = new ArrayList<MineListItem>();

	// static {
	// mMineList.add(new MineListItem(TYPE_MY_WALLET, R.drawable.mine_wallet,
	// NAME_MY_WALLET));
	// mMineList.add(new MineListItem(TYPE_MY_VEHICLE, R.drawable.mine_car,
	// NAME_MY_VEHICLE));
	// mMineList.add(new MineListItem(TYPE_ISREAL, R.drawable.mine_isreal,
	// NAME_ISREAL));
	// mMineList.add(new MineListItem(TYPE_ISDRIVER, R.drawable.mine_isdriver,
	// NAME_ISDRIVER));
	// mMineList.add(new MineListItem(TYPE_BACKGROUND, 0, ""));
	// mMineList.add(new MineListItem(TYPE_ATTENTION, R.drawable.mine_attention,
	// NAME_ATTENTION));
	// // TODO--mMineList.add(new MineListItem(TYPE_MY_PROMOTION,
	// // R.drawable.mine_promotion,NAME_MY_PROMOTION));
	// mMineList.add(new MineListItem(TYPE_HELP, R.drawable.mine_help,
	// NAME_HELP));
	// mMineList.add(new MineListItem(TYPE_LOGOUT, 0, NAME_LOGOUT));
	// }

	/**
	 * 动态获取list
	 * 
	 * @param statusReal
	 * @return
	 */
	public static List<MineListItem> getMineList(String statusReal,
			String statusDriver) {
		mMineList.clear();
		mMineList.add(new MineListItem(TYPE_MY_WALLET, R.drawable.mine_wallet,
				NAME_MY_WALLET));
		mMineList.add(new MineListItem(TYPE_MY_VEHICLE, R.drawable.mine_car,
				NAME_MY_VEHICLE));
		mMineList.add(new MineListItem(TYPE_ISREAL, R.drawable.mine_isreal,
				NAME_ISREAL, statusReal));
		mMineList.add(new MineListItem(TYPE_TASK, R.drawable.mine_task,
				NAME_TASK));
		mMineList.add(new MineListItem(TYPE_RECOMMENDED,
				R.drawable.mine_recommended, NAME_RECOMMENDED));
		// mMineList.add(new MineListItem(TYPE_ISDRIVER,
		// R.drawable.mine_isdriver,
		// NAME_ISDRIVER, statusDriver));
		mMineList.add(new MineListItem(TYPE_BACKGROUND, 0, ""));
		mMineList.add(new MineListItem(TYPE_ATTENTION, R.drawable.mine_notice,
				NAME_ATTENTION));
		// mMineList.add(new MineListItem(TYPE_MY_PROMOTION,
		// R.drawable.mine_promotion, NAME_MY_PROMOTION));
		mMineList.add(new MineListItem(TYPE_HELP, R.drawable.mine_help,
				NAME_HELP));
		mMineList.add(new MineListItem(TYPE_LOGOUT, 0, NAME_LOGOUT));
		return mMineList;
	}
}
