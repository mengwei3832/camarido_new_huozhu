package com.yunqi.clientandroid.utils;

import java.util.Calendar;

import android.widget.TextView;

public class TimeDefaultUtils {
	/**
	 * 
	 * @Description:初始化默认时间
	 * @Title:setDefaultTime
	 * @param start
	 * @param end
	 * @return:void
	 * @throws
	 * @Create: 2016年5月19日 下午6:25:13
	 * @Author : chengtao
	 */
	public static void setDefaultTime(TextView start, TextView end) {
		start.setText(getCurrentYear() + "-" + getCurrentMonth() + "-"
				+ getCurrenttDay() + "");
		switch (getCurrentMonth()) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if (getCurrenttDay() > 28) {
				end.setText(getCurrentYear() + "-" + (getCurrentMonth() + 1)
						+ "-" + ((getCurrenttDay() + 3) % 31) + "");
			} else {
				end.setText(getCurrentYear() + "-" + getCurrentMonth() + "-"
						+ (getCurrenttDay() + 3) + "");
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (getCurrenttDay() > 27) {
				end.setText(getCurrentYear() + "-" + (getCurrentMonth() + 1)
						+ "-" + ((getCurrenttDay() + 3) % 30) + "");
			} else {
				end.setText(getCurrentYear() + "-" + getCurrentMonth() + "-"
						+ (getCurrenttDay() + 3) + "");
			}
			break;
		case 2:
			if (isLeapYear(getCurrentYear())) {
				if (getCurrenttDay() > 26) {
					end.setText(getCurrentYear() + "-"
							+ (getCurrentMonth() + 1) + "-"
							+ ((getCurrenttDay() + 3) % 29) + "");
				} else {
					end.setText(getCurrentYear() + "-" + getCurrentMonth()
							+ "-" + (getCurrenttDay() + 3) + "");
				}
			} else {
				if (getCurrenttDay() > 25) {
					end.setText(getCurrentYear() + "-"
							+ (getCurrentMonth() + 1) + "-"
							+ ((getCurrenttDay() + 3) % 28) + "");
				} else {
					end.setText(getCurrentYear() + "-" + getCurrentMonth()
							+ "-" + (getCurrenttDay() + 3) + "");
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:获取当前年份
	 * @Title:getCurrentYear
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:21:08
	 * @Author : chengtao
	 */
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @Description:获取当前月份
	 * @Title:getCurrentMonth
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:21:39
	 * @Author : chengtao
	 */
	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * @Description:获取当前日期
	 * @Title:getCurrenttDay
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:22:00
	 * @Author : chengtao
	 */
	public static int getCurrenttDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 
	 * @Description:判断是否为闰年
	 * @Title:calcualateDay
	 * @param cyear
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午8:22:43
	 * @Author : chengtao
	 */
	private static boolean isLeapYear(int cyear) {
		if (cyear % 4 == 0 && cyear % 100 != 0) {
			return true;
		} else {
			return false;
		}
	}
}
