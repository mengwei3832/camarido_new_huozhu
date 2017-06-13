package com.yunqi.clientandroid.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yunqi.clientandroid.employer.entity.TimeCollection;
import com.yunqi.clientandroid.employer.util.ComparatorUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class StringUtils {

	// 未配置 正则表达，默认为通过
	public static boolean checkRegexValied(String strValue, String strRegex) {
		Pattern pattern = Pattern.compile(strRegex);
		Matcher matcher = pattern.matcher(strValue);
		return matcher.matches();
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		// Date time = toDate(sdate);
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date(Long.parseLong(sdate) * 1000);

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2) {
			ftime = days + "天前";
		}
		// else if (days > 2 && days <= 10) {
		// ftime = days + "天前";
		// }
		// else if (days > 10) {
		// ftime = dateFormater2.get().format(time);
		// }
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return matchPattern("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", email);
	}

	/**
	 * 判断是不是一个合法的手机号码 ^((\+86)|(86))?(1)\d{10}$
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return false;
		}
		return matchPattern("^1\\d{10}$", phone);
	}

	public static boolean matchPattern(String strRegex, String strValue) {
		Pattern pattern = Pattern.compile(strRegex);
		Matcher matcher = pattern.matcher(strValue);
		return matcher.matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	private static DecimalFormat dfs = null;

	/**
	 * 滚动数字用的
	 * 
	 * @param pattern
	 * @return
	 */
	public static DecimalFormat format(String pattern) {
		if (dfs == null) {
			dfs = new DecimalFormat();
		}
		dfs.setRoundingMode(RoundingMode.FLOOR);
		dfs.applyPattern(pattern);
		return dfs;
	}

	/**
	 * 按规则转换时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 
	 * @Description:按规则转换时间(MM.dd)
	 * @Title:formatSimpleDate
	 * @param date
	 * @return
	 * @return:String
	 * @throws @Create:
	 *             2016年6月29日 下午4:04:32
	 * @Author : chengtao
	 */
	public static String formatSimpleDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM.dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 
	 * @Description:按规则转换时间(yyyy.MM.dd)
	 * @Title:formatSimpleDate
	 * @param date
	 * @return
	 * @return:String
	 * @throws @Create:
	 *             2016年6月29日 下午4:04:32
	 * @Author : chengtao
	 */
	public static String formatChengQixian(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 按规则转换运单详情的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTicket(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 按规则转换运单详情的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatdiff(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 
	 * @Description:企业明细时间转换
	 * @Title:formatMingXi
	 * @param date
	 * @return
	 * @return:String
	 * @throws @Create:
	 *             2016年7月8日 下午3:27:04
	 * @Author : chengtao
	 */
	public static String formatMingXi(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * @Description:计算时间差
	 * @Title:formatTimeDifference
	 * @param date
	 * @return
	 * @return:String
	 * @throws @Create:
	 *             2016-6-30 下午7:02:50
	 * @Author : mengwei
	 */
	public static String formatTimeDifference(String date, String time) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String difference = null;
		try {
			Date d1 = df.parse(date);
			// Date d2 = new Date(System.currentTimeMillis());//获取当前时间
			Date d2 = df.parse(time);
			TimeZone zone = TimeZone.getTimeZone("GTM");
			L.v("TAG", d1.getTime() + "");
			Long targetTime = d2.getTime() - zone.getRawOffset();
			long diff = targetTime - d1.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

			if (days != 0) {
				difference = days + "天前报价";
			} else if (hours != 0) {
				difference = hours + "小时前报价";
			} else if (minutes != 0) {
				difference = minutes + "分钟前报价";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return difference;
	}

	/**
	 * 按规则转换待执行条目的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatPerform(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 按规则转换待执行条目的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDianType(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 按规则转换分配的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatCanReceive(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 按规则转换订单过程的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatModify(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	/**
	 * 消息列表时间转换规则
	 * 
	 * @param date
	 * @return
	 */
	public static String getMsgDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm aaa");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(new Date(Long.parseLong(date) * 1000));
	}

	public static boolean isIntentSafe(Activity activity, Intent intent) {
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		return activities.size() > 0;
	}

	/**
	 * 获取存的城市信息
	 * 
	 * @param orderCity
	 * @return
	 */
	public static String[] getOrderCity(String orderCity) {
		String[] cityArray = orderCity.split("_");
		if (!orderCity.contains("_")) {
			cityArray = new String[] { cityArray[0], "0" };
		}
		return cityArray;
	}

	// 将小写字母转换为大写
	public static String swapCase(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}

		char[] buffer = str.toCharArray();

		for (int i = 0; i < buffer.length; i++) {
			char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			} else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			} else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}

	/**
	 * 
	 * @Description:将字符串时间转化成long类型(时间戳)
	 * @Title:StringDateToDateString
	 * @param s
	 * @return
	 * @return:String
	 * @throws @Create:
	 *             2016年6月18日 下午7:24:28
	 * @Author : chengtao
	 */
	public static String StringDateToDateLong(String s) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		Date d;
		try {
			d = sdf.parse(s);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * 将String的时间字符串转化为long类型
	 * 
	 * @Title:getDateType
	 * @param str
	 * @return:long
	 * @Create: 2016年11月17日 下午4:42:10
	 */
	public static long getDateType(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		long dateLong = 0;
		try {
			date = sdf.parse(str);
			dateLong = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateLong;
	}

	/**
	 * 将String的时间字符串转化为long类型
	 * @param str "yyyy-MM-dd HH:mm" 格式日期
	 * @return
	 */
	public static long getDateTypeLong(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		long dateLong = 0;
		try {
			date = sdf.parse(str);
			dateLong = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateLong;
	}

	/**
	 * 获取昨天的日期
	 */
	public static String getYesterDay(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
			yesterday = sdf.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 获取昨天的日期
	 */
	public static String getYesterDayText(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, 0); // 设置为前一天
			yesterday = sdf1.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 获取"MM月dd日"转化"yyyy-MM-dd"
	 */
	public static String getzhuanhuaDayText(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf1.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, 0); // 设置为前一天
			yesterday = sdf.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 将返回数据"yyyy-MM-dd HH:mm:ss"转化为"yyyy-MM-dd"
	 */
	public static String getFanHuiBiao(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, 0); //
			yesterday = sdf1.format(calendar.getTime());//
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 获取前天的日期
	 */
	public static String getQianDay(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, -2); // 设置为前一天
			yesterday = sdf.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 获取前天的日期
	 */
	public static String getQianDayText(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, 0); // 设置为前一天
			yesterday = sdf1.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 获取明天的日期
	 */
	public static String getMingDay(String str) {
		String yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();// 获取日历实例
			calendar.setTime(sdf.parse(str));
			calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为前一天
			yesterday = sdf.format(calendar.getTime());// 获得前一天
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return yesterday;
	}

	/**
	 * 
	 * @Description:判断字符串是否为空
	 * @Title:isStrNotNull
	 * @param s
	 * @return
	 * @return:boolean
	 * @throws @Create:
	 *             2016年6月28日 下午8:20:42
	 * @Author : chengtao
	 */
	public static boolean isStrNotNull(String s) {
		if (s != null && !TextUtils.isEmpty(s)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存两位小数
	 */
	public static String saveTwoNumber(String str) {
		double mNum = Double.valueOf(str);
		String number = String.format("%.2f", mNum);
		return number;
	}

	/**
	 * 将三位小数转化为带千分号的数据
	 */
	public static String sanToQianFenHao(Double str) {
		int mSan = (int) (str * 1000);
		String mQianFenHao = mSan + "‰";
		return mQianFenHao;
	}

	/**
	 * 将集合中的重复数据去掉
	 * 
	 * @Title:getChongFu
	 * @param list
	 * @return:ArrayList<TimeCollection>
	 * @Create: 2016-11-14 下午5:02:38
	 */
	public static ArrayList<TimeCollection> getChongFu(ArrayList<TimeCollection> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if ((list.get(j).EffectiveTime).equals(list.get(i).EffectiveTime)) {
					list.remove(j);
				}
			}
		}
		// Set set = new LinkedHashSet<TimeCollection>();
		// set.addAll(list);
		// list.clear();
		// list.addAll(set);
		return list;
	}

	/**
	 * 给集合中的数据排序
	 * 
	 * @Title:getPaiXu
	 * @param list
	 * @return:ArrayList<TimeCollection>
	 * @Create: 2016-11-14 下午5:20:34
	 */
	public static ArrayList<TimeCollection> getPaiXu(ArrayList<TimeCollection> list) {
		ComparatorUser comparatorUser = new ComparatorUser();
		Collections.sort(list, comparatorUser);
		return list;
	}

	public static boolean getComparToDate(String time1, String time2) throws ParseException {
		// 如果想比较日期则写成"yyyy-MM-dd"就可以了
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 将字符串形式的时间转化为Date类型的时间
		Date a = sdf.parse(time1);
		Date b = sdf.parse(time2);
		// Date类的一个方法，如果a早于b返回true，否则返回false
		if (a.before(b))
			return true;
		else
			return false;
	}

	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static Long getCurrentTime(){
		String mCurrentstr = String.valueOf(System.currentTimeMillis());
		long mCurrent = Long.valueOf(mCurrentstr.substring(0,10));
		return mCurrent;
	}

}
